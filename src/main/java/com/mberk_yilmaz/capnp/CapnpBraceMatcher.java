package com.mberk_yilmaz.capnp;

import com.mberk_yilmaz.capnp.psi.CapnpTypes;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class CapnpBraceMatcher implements PairedBraceMatcher {
  private static final BracePair[] PAIRS = new BracePair[] {
      new BracePair(CapnpTypes.LBRACE, CapnpTypes.RBRACE, true),
      new BracePair(CapnpTypes.LPAREN, CapnpTypes.RPAREN, false),
      new BracePair(CapnpTypes.LBRACKET, CapnpTypes.RBRACKET, false)};

  @NotNull
  @Override
  public BracePair[] getPairs() {
    return PAIRS;
  }

  @Override
  public boolean
  isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType,
                                  @Nullable IElementType contextType) {
    return true;
  }

  @Override
  public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
    return openingBraceOffset;
  }
}

