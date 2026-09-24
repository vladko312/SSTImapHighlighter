package com.sstimap.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SSTImapColors implements ColorSettingsPage {

    public static final TextAttributesKey TEMPLATE_STRUCTURE =
        TextAttributesKey.createTextAttributesKey("SSTIMAP.STRUCTURE",
            new TextAttributes(
                new JBColor(new Color(0xA07800), new Color(0xD4B94A)),
                null, null, null, Font.BOLD));

    public static final TextAttributesKey TEMPLATE_VARIABLE =
        TextAttributesKey.createTextAttributesKey("SSTIMAP.VARIABLE",
            new TextAttributes(
                new JBColor(new Color(0x1F6FBF), new Color(0x6CB6FF)),
                null, null, null, Font.PLAIN));

    public static final TextAttributesKey TEMPLATE_FILTER =
        TextAttributesKey.createTextAttributesKey("SSTIMAP.FILTER",
            new TextAttributes(
                new JBColor(new Color(0x9B2C87), new Color(0xE48FDB)),
                null, null, null, Font.BOLD));

    public static final TextAttributesKey TEMPLATE_ARGUMENT =
        TextAttributesKey.createTextAttributesKey("SSTIMAP.ARGUMENT",
            new TextAttributes(
                new JBColor(new Color(0x2E7D32), new Color(0x7CD98A)),
                null, null, null, Font.PLAIN));

    public static final TextAttributesKey TEMPLATE_BACKGROUND =
        TextAttributesKey.createTextAttributesKey("SSTIMAP.BACKGROUND",
            new TextAttributes(
                null,
                new JBColor(new Color(0xF4F0D8), new Color(0x2E2A18)),
                null, null, Font.PLAIN));

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
        new AttributesDescriptor("Structure", TEMPLATE_STRUCTURE),
        new AttributesDescriptor("Variable", TEMPLATE_VARIABLE),
        new AttributesDescriptor("Filters", TEMPLATE_FILTER),
        new AttributesDescriptor("Filter args", TEMPLATE_ARGUMENT),
        new AttributesDescriptor("Background", TEMPLATE_BACKGROUND),
    };

    @Override public @Nullable Icon getIcon() { return null; }
    @Override public @Nullable com.intellij.openapi.fileTypes.SyntaxHighlighter getHighlighter() { return null; }

    @Override
    public @NotNull String getDemoText() {
        return "file_exists = 'require(\"fs\").existsSync(\"SSTIMAP:path;\")'\n" +
               "prefix = \"SSTIMAP:closure;%}\"\n" +
               "header = '{{SSTIMAP:header:get,0;+SSTIMAP:header:get,1;}}'\n" +
               "payload = '''<!--#exec cmd=\"`echo 'SSTIMAP:code:b64;'|base64 -d`\" -->'''\n" +
               "test = \"SSTIMAP:variable:filter1:filter2,arg1,arg2:filter3;\"";
    }

    @Override public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() { return null; }
    @Override public @NotNull AttributesDescriptor[] getAttributeDescriptors() { return DESCRIPTORS; }
    @Override public @NotNull ColorDescriptor[] getColorDescriptors() { return ColorDescriptor.EMPTY_ARRAY; }
    @Override public @NotNull String getDisplayName() { return "SSTImap Plugins"; }
}