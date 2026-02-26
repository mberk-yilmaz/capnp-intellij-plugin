package com.mberk_yilmaz.capnp;

import com.intellij.openapi.fileTypes.LanguageFileType;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class CapnpFileType extends LanguageFileType {
  public static final CapnpFileType INSTANCE = new CapnpFileType();

  private CapnpFileType() { super(CapnpLanguage.INSTANCE); }

  @NotNull
  @Override
  public String getName() {
    return "Cap'n Proto File";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Cap'n Proto schema file";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "capnp";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return CapnpIcons.FILE;
  }
}

