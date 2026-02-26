# Cap'n Proto Support — IntelliJ Plugin

[![Build](https://github.com/mberk-yilmaz/capnp-intellij-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/mberk-yilmaz/capnp-intellij-plugin/actions/workflows/build.yml)
[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/mberk-yilmaz/capnp-intellij-plugin/releases)

Complete [Cap'n Proto](https://capnproto.org/) schema language support for IntelliJ-based IDEs.

## ✨ Features

- **🎨 Syntax Highlighting** — Full syntax coloring for structs, enums, interfaces, annotations, and keywords
- **💡 Smart Code Completion** — Auto-completion for keywords, types, primitives, and annotation suggestions (`$required`, `$maxLength`, `$minValue`, etc.)
- **🔍 Navigation** — Go to Definition for custom types and references (Ctrl+Click)
- **📝 Code Formatting** — Automatic formatting and indentation (Ctrl+Alt+L)
- **⚠️ Error Detection** — Real-time validation with helpful error messages
- **🏷️ Namespace Support** — Full support for namespace declarations with `::` syntax
- **🆔 ID Generation** — Right-click context menu to generate unique file IDs
- **📊 Structure View** — Quick navigation with outline view
- **🔗 Brace Matching** — Smart bracket and brace matching

### Advanced Features

- Generic types and parameterized types
- Union and group definitions
- Interface definitions with RPC methods
- Import and using statements
- Const definitions with complex values
- Field annotations with smart parameter completion

## 🚀 Installation

### From JetBrains Marketplace (Coming Soon)
Search for **"Cap'n Proto Support"** in `Settings → Plugins → Marketplace`.

### Manual Installation
1. Download the latest `.zip` from [Releases](https://github.com/mberk-yilmaz/capnp-intellij-plugin/releases)
2. In IntelliJ/CLion: `Settings → Plugins → ⚙️ → Install Plugin from Disk`
3. Select the downloaded ZIP file
4. Restart IDE

## 🛠️ Building from Source

```bash
git clone https://github.com/mberk-yilmaz/capnp-intellij-plugin.git
cd capnp-intellij-plugin

# Build plugin
./gradlew buildPlugin
# Output: build/distributions/capnp-intellij-plugin-1.0.0.zip

# Or run in development mode
./gradlew runIde
```

## 📋 Supported IDEs

- IntelliJ IDEA (Community & Ultimate)
- CLion
- PyCharm
- WebStorm
- All JetBrains IDEs (2024.3+)

## 📝 Example

```capnp
@0xdbb9ad1f14bf0b36;  # Generated with right-click → Generate Cap'n Proto ID

namespace blog.schema;

struct User {
  id @0 :UInt64 $required(true);
  username @1 :Text $maxLength(50) $required(true);
  email @2 :Text $maxLength(255) $required(true);
  bio @3 :Text $maxLength(1000);
}
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

[MIT](LICENSE)
