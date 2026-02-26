package com.mberk_yilmaz.capnp.psi;

import com.mberk_yilmaz.capnp.CapnpLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


public class CapnpTokenType extends IElementType {
  public CapnpTokenType(@NotNull @NonNls String debugName) {
    super(debugName, CapnpLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "CapnpTokenType." + super.toString();
  }
}

