# Cap'n Proto Support — IntelliJ Plugin

An IntelliJ IDEA plugin that provides first-class support for editing [Cap'n Proto](https://capnproto.org/) (`.capnp`) schema files.

## Features

- **Syntax Highlighting** — Keywords, primitive types, strings, comments, and custom type references all have distinct colors
- **Error Highlighting** — Missing semicolons (`;`) and unclosed braces (`{`, `}`) are flagged inline
- **Code Completion** — Keyword and primitive type suggestions as you type
- **Code Formatting** — Proper indentation and spacing (`Ctrl+Alt+L`)
- **Brace Matching** — Matching `{}`, `()`, `[]` pairs are highlighted
- **Go to Definition** — Ctrl+Click on a custom type to jump to its `struct`/`enum`/`interface` declaration

## Supported Languages / IDEs

- IntelliJ IDEA Community & Ultimate (2023.3 – 2024.3)
- Other JetBrains IDEs built on the IntelliJ Platform

## Installation

### From JetBrains Marketplace
Search for **"Cap'n Proto Support"** in `Settings → Plugins → Marketplace`.

### Manual
Download the latest `.zip` from [Releases](../../releases) and install via `Settings → Plugins → ⚙ → Install Plugin from Disk`.

## Building from Source

```bash
git clone https://github.com/mberk-yilmaz/capnp-intellij-plugin.git
cd capnp-intellij-plugin
./gradlew buildPlugin
# Output: build/distributions/capnp-plugin-*.zip
```

## License

[MIT](LICENSE)
