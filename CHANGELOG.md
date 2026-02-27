# Changelog

All notable changes to the Cap'n Proto IntelliJ Plugin will be documented in this file.

## [1.0.1] - 2026-02-27

### Changed
- **Moved to IntelliJ 2025.1+ only** - dropped support for 2024.3.2
- Updated minimum build number to 251 (IntelliJ IDEA 2025.1)

### Fixed
- Kotlin stdlib bundle compatibility
- Build searchable options processing

## [1.0.0] - 2026-02-26

### Added
-  Full syntax highlighting for Cap'n Proto schema files (.capnp)
-  Smart code completion for keywords, types, and primitives
-  Navigation: Go to Definition for custom types
-  Code formatting and indentation
-  Real-time error detection and validation
-  Complete namespace support with :: syntax
-  Annotation auto-completion ($required, $maxLength, $minValue, etc.)
-  Context menu action to generate unique file IDs
-  Structure view for quick navigation
-  Smart bracket and brace matching
-  File type icon for .capnp files

### Features
- Support for structs, enums, interfaces, annotations
- Generic types and parameterized types
- Union and group definitions
- Import and using statements
- Const definitions with complex values
- Field annotations with smart parameter completion

### Technical
- Grammar-Kit based parser
- JFlex lexer
- IntelliJ Platform SDK 2024.3+
- Java 17

---

## [Unreleased]

### Planned
- Live Templates for common Cap'n Proto patterns
- Quick fixes for common errors
- Refactoring support (rename, extract, etc.)
- Integration with Cap'n Proto compiler
- Schema validation against Cap'n Proto spec
