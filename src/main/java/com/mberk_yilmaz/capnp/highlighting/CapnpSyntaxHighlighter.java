package com.mberk_yilmaz.capnp.highlighting;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

import com.mberk_yilmaz.capnp.lexer.CapnpLexerAdapter;
import com.mberk_yilmaz.capnp.psi.CapnpTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class CapnpSyntaxHighlighter extends SyntaxHighlighterBase {
  public static final TextAttributesKey KEYWORD = createTextAttributesKey(
      "CAPNP_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
  public static final TextAttributesKey TYPE_CLASS = createTextAttributesKey(
      "CAPNP_TYPE", DefaultLanguageHighlighterColors.CLASS_NAME);
  public static final TextAttributesKey NUMBER = createTextAttributesKey(
      "CAPNP_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
  public static final TextAttributesKey STRING = createTextAttributesKey(
      "CAPNP_STRING", DefaultLanguageHighlighterColors.STRING);
  public static final TextAttributesKey COMMENT = createTextAttributesKey(
      "CAPNP_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);

  private static final TextAttributesKey[] KEYWORD_KEYS =
      new TextAttributesKey[] {KEYWORD};
  private static final TextAttributesKey[] TYPE_KEYS =
      new TextAttributesKey[] {TYPE_CLASS};
  private static final TextAttributesKey[] NUMBER_KEYS =
      new TextAttributesKey[] {NUMBER};
  private static final TextAttributesKey[] STRING_KEYS =
      new TextAttributesKey[] {STRING};
  private static final TextAttributesKey[] COMMENT_KEYS =
      new TextAttributesKey[] {COMMENT};
  private static final TextAttributesKey[] EMPTY_KEYS =
      new TextAttributesKey[0];

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new CapnpLexerAdapter();
  }

  @NotNull
  @Override
  public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
    if (tokenType.equals(CapnpTypes.STRUCT_KW) ||
        tokenType.equals(CapnpTypes.ENUM_KW) ||
        tokenType.equals(CapnpTypes.INTERFACE_KW) ||
        tokenType.equals(CapnpTypes.ANNOTATION_KW) ||
        tokenType.equals(CapnpTypes.EXTENDS_KW) ||
        tokenType.equals(CapnpTypes.UNION_KW) ||
        tokenType.equals(CapnpTypes.GROUP_KW) ||
        tokenType.equals(CapnpTypes.CONST_KW) ||
        tokenType.equals(CapnpTypes.USING_KW) ||
        tokenType.equals(CapnpTypes.IMPORT_KW) ||
        tokenType.equals(CapnpTypes.AT) ||
        tokenType.equals(CapnpTypes.PRIMITIVE_TYPE)) {
      return KEYWORD_KEYS;
    }
    if (tokenType.equals(CapnpTypes.CUSTOM_TYPE)) {
      return TYPE_KEYS;
    }
    if (tokenType.equals(CapnpTypes.ID_VALUE) ||
        tokenType.equals(CapnpTypes.HEX_STRING) ||
        tokenType.equals(CapnpTypes.FILE_ID_TOKEN) ||
        tokenType.equals(CapnpTypes.FLOAT_LITERAL)) {
      return NUMBER_KEYS;
    }
    if (tokenType.equals(CapnpTypes.STRING_LITERAL)) {
      return STRING_KEYS;
    }
    if (tokenType.equals(CapnpTypes.LINE_COMMENT)) {
      return COMMENT_KEYS;
    }
    return EMPTY_KEYS;
  }
}

