package com.mberk_yilmaz.capnp.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.mberk_yilmaz.capnp.psi.CapnpTypes;
import com.intellij.psi.TokenType;

%%

%class CapnpLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF=\R
WHITE_SPACE=[\ \n\t\f]
LINE_COMMENT="#"[^\r\n]*

ID_VALUE=[0-9]+
HEX_STRING="0x"[0-9a-fA-F]+
FILE_ID="@""0x"[0-9a-fA-F]+";"
IDENTIFIER=[a-zA-Z_][a-zA-Z0-9_]*
STRING_LITERAL=\"([^\"\\]|\\.)*\"
FLOAT_LITERAL=-?(0|[1-9][0-9]*)(\.[0-9]+)?([eE][+-]?[0-9]+)?

%%

<YYINITIAL> {
  {WHITE_SPACE}        { return TokenType.WHITE_SPACE; }
  {CRLF}               { return TokenType.WHITE_SPACE; }
  {LINE_COMMENT}       { return CapnpTypes.LINE_COMMENT; }

  "{"                  { return CapnpTypes.LBRACE; }
  "}"                  { return CapnpTypes.RBRACE; }
  "("                  { return CapnpTypes.LPAREN; }
  ")"                  { return CapnpTypes.RPAREN; }
  "["                  { return CapnpTypes.LBRACKET; }
  "]"                  { return CapnpTypes.RBRACKET; }
  "::"                 { return CapnpTypes.DOUBLE_COLON; }
  ":"                  { return CapnpTypes.COLON; }
  ";"                  { return CapnpTypes.SEMICOLON; }
  "="                  { return CapnpTypes.EQUALS; }
  "@"                  { return CapnpTypes.AT; }
  ","                  { return CapnpTypes.COMMA; }
  "."                  { return CapnpTypes.DOT; }
  "->"                 { return CapnpTypes.ARROW; }
  "$"                  { return CapnpTypes.DOLLAR; }

  "struct"             { return CapnpTypes.STRUCT_KW; }
  "enum"               { return CapnpTypes.ENUM_KW; }
  "interface"          { return CapnpTypes.INTERFACE_KW; }
  "annotation"         { return CapnpTypes.ANNOTATION_KW; }
  "extends"            { return CapnpTypes.EXTENDS_KW; }
  "union"              { return CapnpTypes.UNION_KW; }
  "group"              { return CapnpTypes.GROUP_KW; }
  "const"              { return CapnpTypes.CONST_KW; }
  "using"              { return CapnpTypes.USING_KW; }
  "import"             { return CapnpTypes.IMPORT_KW; }
  "namespace"          { return CapnpTypes.NAMESPACE_KW; }

  "Void"|"Bool"|"Int8"|"Int16"|"Int32"|"Int64"|"UInt8"|"UInt16"|"UInt32"|"UInt64"|"Float32"|"Float64"|"Text"|"Data"|"AnyPointer" { return CapnpTypes.PRIMITIVE_TYPE; }

  {ID_VALUE}           { return CapnpTypes.ID_VALUE; }
  {FILE_ID}            { return CapnpTypes.FILE_ID_TOKEN; }
  {HEX_STRING}         { return CapnpTypes.HEX_STRING; }
  {STRING_LITERAL}     { return CapnpTypes.STRING_LITERAL; }
  {FLOAT_LITERAL}      { return CapnpTypes.FLOAT_LITERAL; }
  {IDENTIFIER}         { return CapnpTypes.IDENTIFIER; }

  [^]                  { return TokenType.BAD_CHARACTER; }
}
