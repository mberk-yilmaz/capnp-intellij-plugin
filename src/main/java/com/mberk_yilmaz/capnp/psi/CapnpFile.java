package com.mberk_yilmaz.capnp.psi;

import com.mberk_yilmaz.capnp.CapnpFileType;
import com.mberk_yilmaz.capnp.CapnpLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;


public class CapnpFile extends PsiFileBase {
  public CapnpFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, CapnpLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return CapnpFileType.INSTANCE;
  }

  @Override
  public String toString() {
    return "Cap'n Proto File";
  }
}

