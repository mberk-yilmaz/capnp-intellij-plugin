package com.mberk_yilmaz.capnp.reference;

import com.mberk_yilmaz.capnp.psi.CapnpCustomType;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class CapnpReferenceContributor extends PsiReferenceContributor {
  @Override
  public void
  registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
    registrar.registerReferenceProvider(
        PlatformPatterns.psiElement(CapnpCustomType.class),
        new PsiReferenceProvider() {
          @NotNull
          @Override
          public PsiReference[] getReferencesByElement(
              @NotNull PsiElement element, @NotNull ProcessingContext context) {
            String text = element.getText();
            if (text != null && text.length() > 0) {
              // CustomType usually contains an IDENTIFIER leaf node
              PsiElement identifier = element.getFirstChild();
              if (identifier != null &&
                  identifier.getNode().getElementType() ==
                      com.mberk_yilmaz.capnp.psi.CapnpTypes.IDENTIFIER) {
                return new PsiReference[] {new CapnpReference(
                    element, identifier.getTextRangeInParent())};
              }
              return new PsiReference[] {new CapnpReference(
                  element,
                  new com.intellij.openapi.util.TextRange(0, text.length()))};
            }
            return PsiReference.EMPTY_ARRAY;
          }
        });
  }
}

