package com.mberk_yilmaz.capnp.psi;

import com.mberk_yilmaz.capnp.CapnpLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


public class CapnpElementType extends IElementType {
  public CapnpElementType(@NotNull @NonNls String debugName) {
    super(debugName, CapnpLanguage.INSTANCE);
  }
}

