package com.mberk_yilmaz.capnp.lexer;

import com.intellij.lexer.FlexAdapter;

public class CapnpLexerAdapter extends FlexAdapter {
  public CapnpLexerAdapter() { super(new CapnpLexer(null)); }
}

