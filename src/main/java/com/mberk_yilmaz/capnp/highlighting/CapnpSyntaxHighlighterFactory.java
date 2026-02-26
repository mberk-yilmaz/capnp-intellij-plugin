package com.mberk_yilmaz.capnp.highlighting;

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CapnpSyntaxHighlighterFactory extends SyntaxHighlighterFactory {
  @NotNull
  @Override
  public SyntaxHighlighter
  getSyntaxHighlighter(@Nullable Project project,
                       @Nullable VirtualFile virtualFile) {
    return new CapnpSyntaxHighlighter(); // Will be created next
  }
}

