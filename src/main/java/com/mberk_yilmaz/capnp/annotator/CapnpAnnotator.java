package com.mberk_yilmaz.capnp.annotator;

import com.mberk_yilmaz.capnp.highlighting.CapnpSyntaxHighlighter;
import com.mberk_yilmaz.capnp.psi.CapnpTypes;
import com.mberk_yilmaz.capnp.psi.impl.*;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class CapnpAnnotator implements Annotator {

  @Override
  public void annotate(@NotNull PsiElement element,
                       @NotNull AnnotationHolder holder) {

    // ── LEAF-LEVEL approach ──────────────────────────────────────────────────
    // Intercept every IDENTIFIER token and check its parent to decide the
    // color. This is more reliable than checking composite parent PSI nodes.
    IElementType tokenType = element.getNode().getElementType();

    if (tokenType == CapnpTypes.IDENTIFIER) {
      PsiElement parent = element.getParent();
      if (parent == null)
        return;
      IElementType parentType = parent.getNode().getElementType();

      // Case A: This IDENTIFIER is a type USAGE (inside customType node)
      // e.g.  t @0 :Test  →  color "Test"
      if (parentType == CapnpTypes.CUSTOM_TYPE) {
        applyTypeColor(element, holder);
        return;
      }

      // Case B: This IDENTIFIER is a struct/enum/interface DEFINITION name
      // e.g.  struct Test2 { }  →  keep "Test2" as plain text, but warn if
      // lowercase
      if (parent instanceof CapnpStructDefImpl ||
          parent instanceof CapnpEnumDefImpl ||
          parent instanceof CapnpInterfaceDefImpl) {
        String name = element.getText();
        if (name != null && !name.isEmpty() &&
            !Character.isUpperCase(name.charAt(0))) {
          holder
              .newAnnotation(
                  HighlightSeverity.WARNING,
                  parent instanceof CapnpStructDefImpl
                      ? "Struct names should start with an uppercase letter"
                  : parent instanceof CapnpEnumDefImpl
                      ? "Enum names should start with an uppercase letter"
                      : "Interface names should start with an uppercase letter")
              .range(element)
              .create();
        }
        return;
      }
    }

    // ── COMPOSITE-LEVEL checks (for structural errors) ───────────────────────

    // Struct: missing { or }
    if (element instanceof CapnpStructDefImpl) {
      PsiElement id = ((CapnpStructDefImpl)element).getIdentifier();
      checkMissingToken(element, id, CapnpTypes.LBRACE, "{", holder);
      checkMissingToken(element, id, CapnpTypes.RBRACE, "}", holder);
    }

    // Enum: missing { or }
    else if (element instanceof CapnpEnumDefImpl) {
      PsiElement id = ((CapnpEnumDefImpl)element).getIdentifier();
      checkMissingToken(element, id, CapnpTypes.LBRACE, "{", holder);
      checkMissingToken(element, id, CapnpTypes.RBRACE, "}", holder);
    }

    // Interface: missing { or }
    else if (element instanceof CapnpInterfaceDefImpl) {
      PsiElement id = ((CapnpInterfaceDefImpl)element).getIdentifier();
      checkMissingToken(element, id, CapnpTypes.LBRACE, "{", holder);
      checkMissingToken(element, id, CapnpTypes.RBRACE, "}", holder);
    }

    // Statements that need a trailing semicolon
    // Note: Only check statements at the statement level, not nested occurrences
    else if (element instanceof CapnpFieldDefImpl ||
             element instanceof CapnpConstDefImpl ||
             element instanceof CapnpAnnotationDefImpl ||
             element instanceof CapnpUsingDefImpl ||
             element instanceof CapnpImportDeclImpl ||
             element instanceof CapnpEnumContentImpl ||
             element instanceof CapnpMethodDefImpl) {

      PsiElement id = null;
      if (element instanceof CapnpFieldDefImpl)
        id = ((CapnpFieldDefImpl)element).getIdentifier();
      else if (element instanceof CapnpConstDefImpl)
        id = ((CapnpConstDefImpl)element).getIdentifier();
      else if (element instanceof CapnpUsingDefImpl)
        id = ((CapnpUsingDefImpl)element).getIdentifier();
      else if (element instanceof CapnpEnumContentImpl)
        id = ((CapnpEnumContentImpl)element).getIdentifier();
      else if (element instanceof CapnpMethodDefImpl)
        id = ((CapnpMethodDefImpl)element).getIdentifier();

      checkMissingToken(element, id, CapnpTypes.SEMICOLON, ";", holder);
    }
    
    // For annotation app statements (standalone @file-level annotations with $)
    else if (element instanceof CapnpAnnotationAppStmtImpl) {
      checkMissingToken(element, null, CapnpTypes.SEMICOLON, ";", holder);
    }
  }

  // ── Helpers
  // ─────────────────────────────────────────────────────────────────

  private void applyTypeColor(@NotNull PsiElement element,
                              @NotNull AnnotationHolder holder) {
    // Use enforcedTextAttributes with an explicit bright color so it's always
    // visible regardless of IDE color scheme. This bypasses COLOR_SCHEME
    // lookups.
    com.intellij.openapi.editor.markup.TextAttributes attrs =
        new com.intellij.openapi.editor.markup.TextAttributes(
            new java.awt.Color(
                0, 180,
                180), // Teal/Cyan — very visible in both light and dark themes
            null, null, null, java.awt.Font.BOLD);
    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
        .range(element.getTextRange())
        .enforcedTextAttributes(attrs)
        .create();
  }

  private void checkMissingToken(PsiElement element, PsiElement identifier,
                                 IElementType tokenType, String tokenChar,
                                 AnnotationHolder holder) {
    boolean hasToken = false;
    
    // First try: direct children
    PsiElement child = element.getFirstChild();
    while (child != null) {
      if (child.getNode().getElementType() == tokenType) {
        hasToken = true;
        break;
      }
      child = child.getNextSibling();
    }
    
    // Second try: if not found and looking for SEMICOLON, check the last token
    // (semicolons are often at the end and might be in a different position)
    if (!hasToken && tokenType == CapnpTypes.SEMICOLON) {
      PsiElement lastChild = element.getLastChild();
      if (lastChild != null && lastChild.getNode().getElementType() == tokenType) {
        hasToken = true;
      }
    }

    if (!hasToken) {
      TextRange errorRange;
      if (identifier != null) {
        errorRange = identifier.getTextRange();
      } else {
        TextRange r = element.getTextRange();
        errorRange =
            new TextRange(r.getStartOffset(),
                          Math.min(r.getEndOffset(), r.getStartOffset() + 20));
      }
      holder
          .newAnnotation(HighlightSeverity.ERROR, "Missing '" + tokenChar + "'")
          .range(errorRange)
          .create();
    }
  }
}

