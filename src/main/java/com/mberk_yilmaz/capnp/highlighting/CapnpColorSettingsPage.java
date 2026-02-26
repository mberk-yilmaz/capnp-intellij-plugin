package com.mberk_yilmaz.capnp.highlighting;

import com.mberk_yilmaz.capnp.CapnpFileType;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import java.util.Map;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CapnpColorSettingsPage implements ColorSettingsPage {
  private static final AttributesDescriptor[] DESCRIPTORS =
      new AttributesDescriptor[] {
          new AttributesDescriptor("Keyword", CapnpSyntaxHighlighter.KEYWORD),
          new AttributesDescriptor("Type / Class name",
                                   CapnpSyntaxHighlighter.TYPE_CLASS),
          new AttributesDescriptor("String", CapnpSyntaxHighlighter.STRING),
          new AttributesDescriptor("Number", CapnpSyntaxHighlighter.NUMBER),
          new AttributesDescriptor("Comment", CapnpSyntaxHighlighter.COMMENT),
      };

  @Nullable
  @Override
  public Icon getIcon() {
    return CapnpFileType.INSTANCE.getIcon();
  }

  @NotNull
  @Override
  public SyntaxHighlighter getHighlighter() {
    return new CapnpSyntaxHighlighter();
  }

  @NotNull
  @Override
  public String getDemoText() {
    return "# Cap'n Proto Example\n"
        + "@0x1234567890abcdef;\n"
        + "\n"
        + "struct Person {\n"
        + "  name @0 :Text;\n"
        + "  id @1 :UInt32;\n"
        + "  email @2 :Text;\n"
        + "}\n";
  }

  @Nullable
  @Override
  public Map<String, TextAttributesKey>
  getAdditionalHighlightingTagToDescriptorMap() {
    return null;
  }

  @NotNull
  @Override
  public AttributesDescriptor[] getAttributeDescriptors() {
    return DESCRIPTORS;
  }

  @NotNull
  @Override
  public ColorDescriptor[] getColorDescriptors() {
    return ColorDescriptor.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Cap'n Proto";
  }
}

