package com.sstimap.highlighter;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SSTImapAnnotator implements Annotator {

    private static final String PREFIX = "SSTIMAP:";
    private static final int PREFIX_LEN = PREFIX.length();

    private static final class HighlightRange {
        final int start, end;
        final TextAttributesKey key;
        HighlightRange(int start, int end, TextAttributesKey key) {
            this.start = start; this.end = end; this.key = key;
        }
    }

    private static final class Template {
        final int start, end;
        final List<HighlightRange> parts;
        Template(int start, int end, List<HighlightRange> parts) {
            this.start = start; this.end = end; this.parts = parts;
        }
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element.getFirstChild() != null) return;

        String text = element.getText();
        if (text.isEmpty()) return;

        char first = text.charAt(0);
        if (first != '"' && first != '\'' && first != '`') return;
        if (text.indexOf(PREFIX) < 0) return;

        int elementStart = element.getTextRange().getStartOffset();
        int elementEnd   = element.getTextRange().getEndOffset();

        for (Template t : parseTemplates(text)) {
            int absStart = elementStart + t.start;
            int absEnd   = elementStart + t.end;
            if (absStart < elementStart || absEnd > elementEnd || absStart >= absEnd) continue;

            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                  .range(TextRange.create(absStart, absEnd))
                  .textAttributes(SSTImapColors.TEMPLATE_BACKGROUND)
                  .create();

            for (HighlightRange r : t.parts) {
                int s = elementStart + r.start;
                int e = elementStart + r.end;
                if (s < absStart || e > absEnd || s >= e) continue;
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                      .range(TextRange.create(s, e))
                      .textAttributes(r.key)
                      .create();
            }
        }
    }

    private static List<Template> parseTemplates(String text) {
        List<Template> out = new ArrayList<>();
        int searchFrom = 0;

        while (true) {
            int start = text.indexOf(PREFIX, searchFrom);
            if (start < 0) break;

            List<HighlightRange> parts = new ArrayList<>();
            int pos = start + PREFIX_LEN;

            int varStart = pos;
            pos = skipIdentifier(text, pos);
            if (pos == varStart) { searchFrom = start + 1; continue; }

            parts.add(new HighlightRange(start, start + PREFIX_LEN, SSTImapColors.TEMPLATE_STRUCTURE));
            parts.add(new HighlightRange(varStart, pos, SSTImapColors.TEMPLATE_VARIABLE));

            boolean valid = true;
            while (pos < text.length() && text.charAt(pos) == ':') {
                parts.add(new HighlightRange(pos, pos + 1, SSTImapColors.TEMPLATE_STRUCTURE)); // ':'
                pos++;

                int nameStart = pos;
                pos = skipIdentifier(text, pos);
                if (pos == nameStart) { valid = false; break; }
                parts.add(new HighlightRange(nameStart, pos, SSTImapColors.TEMPLATE_FILTER));

                while (pos < text.length() && text.charAt(pos) == ',') {
                    parts.add(new HighlightRange(pos, pos + 1, SSTImapColors.TEMPLATE_STRUCTURE)); // ','
                    pos++;

                    while (pos < text.length() && Character.isWhitespace(text.charAt(pos))) pos++;
                    int argStart = pos;
                    while (pos < text.length()) {
                        char c = text.charAt(pos);
                        if (c == ',' || c == ':' || c == ';') break;
                        pos++;
                    }
                    int argEnd = pos;
                    while (argEnd > argStart && Character.isWhitespace(text.charAt(argEnd - 1))) argEnd--;
                    if (argEnd > argStart) {
                        parts.add(new HighlightRange(argStart, argEnd, SSTImapColors.TEMPLATE_ARGUMENT));
                    }
                }
            }
            if (!valid) { searchFrom = start + 1; continue; }

            if (pos >= text.length() || text.charAt(pos) != ';') { searchFrom = start + 1; continue; }
            parts.add(new HighlightRange(pos, pos + 1, SSTImapColors.TEMPLATE_STRUCTURE)); // ';'

            out.add(new Template(start, pos + 1, parts));
            searchFrom = pos + 1;
        }
        return out;
    }

    private static int skipIdentifier(String text, int pos) {
        while (pos < text.length()) {
            char c = text.charAt(pos);
            if (!Character.isLetterOrDigit(c) && c != '_') break;
            pos++;
        }
        return pos;
    }
}