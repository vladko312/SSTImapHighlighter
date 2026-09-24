# [SSTImap](https://github.com/vladko312/sstimap) Plugin Highlighter

[![Version 1.0](https://img.shields.io/badge/version-1.0-green.svg?logo=github)](https://github.com/vladko312/SSTImapHighlighter)
[![PyCharm 2026.2](https://img.shields.io/badge/PyCharm-2026.2-blue.svg?logo=jetbrains)](https://www.jetbrains.com/pycharm/)
[![License](https://img.shields.io/github/license/vladko312/SSTImapHighlighter?color=green&logo=gnu)](https://www.gnu.org/licenses/gpl-3.0.txt)
[![GitHub last commit](https://img.shields.io/github/last-commit/vladko312/SSTImapHighlighter?color=green&logo=github)](https://github.com/vladko312/SSTImapHighlighter/commits/)
[![Maintenance](https://img.shields.io/maintenance/yes/2027?logo=github)](https://github.com/vladko312/SSTImapHighlighter)

> This plugin is intended for the developers of third-party [SSTImap](https://github.com/vladko312/sstimap) plugins.

This plugin highlights SSTImap payload formatter syntax inside string literals.
All colors are configurable under `Settings → Editor → Color Scheme → SSTimap Plugins`
Malformed interpolations (missing terminator, variable, or filter name) are ignored.

## Payload interpolation syntax

```
SSTIMAP:variable:filter1:filter2,arg1,arg2:filter3;
```

## Build

```bash
./gradlew buildPlugin
```

Output: `build/distributions/`. Install via `Settings → Plugins → Install Plugin from Disk...`.
