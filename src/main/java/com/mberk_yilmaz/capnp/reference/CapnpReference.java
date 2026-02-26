package com.mberk_yilmaz.capnp.reference;

import com.mberk_yilmaz.capnp.psi.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CapnpReference
    extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
  private final String key;

  public CapnpReference(@NotNull PsiElement element, TextRange textRange) {
    super(element, textRange);
    key = element.getText().substring(textRange.getStartOffset(),
                                      textRange.getEndOffset());
  }

  @NotNull
  @Override
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    Project project = myElement.getProject();
    List<ResolveResult> results = new ArrayList<>();

    // Very basic resolution: scan the current file for
    // struct/enum/interface/const names
    PsiFile file = myElement.getContainingFile();
    if (file instanceof CapnpFile) {
      findDeclarations(file, key, results);
    }

    return results.toArray(new ResolveResult[0]);
  }

  private void findDeclarations(PsiElement element, String name,
                                List<ResolveResult> results) {
    if (element instanceof CapnpStructDef) {
      PsiElement id = ((CapnpStructDef)element).getIdentifier();
      if (id != null && name.equals(id.getText())) {
        results.add(new PsiElementResolveResult(id));
      }
    } else if (element instanceof CapnpEnumDef) {
      PsiElement id = ((CapnpEnumDef)element).getIdentifier();
      if (id != null && name.equals(id.getText())) {
        results.add(new PsiElementResolveResult(id));
      }
    } else if (element instanceof CapnpInterfaceDef) {
      PsiElement id = ((CapnpInterfaceDef)element).getIdentifier();
      if (id != null && name.equals(id.getText())) {
        results.add(new PsiElementResolveResult(id));
      }
    } else if (element instanceof CapnpConstDef) {
      PsiElement id = ((CapnpConstDef)element).getIdentifier();
      if (id != null && name.equals(id.getText())) {
        results.add(new PsiElementResolveResult(id));
      }
    }

    for (PsiElement child : element.getChildren()) {
      findDeclarations(child, name, results);
    }
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    ResolveResult[] resolveResults = multiResolve(false);
    return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
  }

  @NotNull
  @Override
  public Object[] getVariants() {
    return new Object[0];
  }
}

