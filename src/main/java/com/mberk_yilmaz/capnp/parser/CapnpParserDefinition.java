package com.mberk_yilmaz.capnp.parser;

import com.mberk_yilmaz.capnp.CapnpLanguage;
import com.mberk_yilmaz.capnp.lexer.CapnpLexerAdapter;
import com.mberk_yilmaz.capnp.psi.CapnpFile;
import com.mberk_yilmaz.capnp.psi.CapnpTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;


public class CapnpParserDefinition implements ParserDefinition {
  public static final IFileElementType FILE =
      new IFileElementType(CapnpLanguage.INSTANCE);

  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    return new CapnpLexerAdapter();
  }

  @NotNull
  @Override
  public TokenSet getCommentTokens() {
    return TokenSet.create(CapnpTypes.LINE_COMMENT);
  }

  @NotNull
  @Override
  public TokenSet getStringLiteralElements() {
    return TokenSet.create(CapnpTypes.STRING_LITERAL);
  }

  @NotNull
  @Override
  public PsiParser createParser(Project project) {
    return new CapnProtoParser();
  }

  @NotNull
  @Override
  public IFileElementType getFileNodeType() {
    return FILE;
  }

  @NotNull
  @Override
  public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    return new CapnpFile(viewProvider);
  }

  @NotNull
  @Override
  public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left,
                                                           ASTNode right) {
    return SpaceRequirements.MAY;
  }

  @NotNull
  @Override
  public PsiElement createElement(ASTNode node) {
    return CapnpTypes.Factory.createElement(node);
  }
}

