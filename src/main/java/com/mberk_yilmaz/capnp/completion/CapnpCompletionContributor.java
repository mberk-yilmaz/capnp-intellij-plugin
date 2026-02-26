package com.mberk_yilmaz.capnp.completion;

import com.mberk_yilmaz.capnp.CapnpLanguage;
import com.mberk_yilmaz.capnp.psi.CapnpTypes;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class CapnpCompletionContributor extends CompletionContributor {
  public CapnpCompletionContributor() {
    // Basic keyword and type completion
    extend(
        CompletionType.BASIC, 
        PlatformPatterns.psiElement().withLanguage(CapnpLanguage.INSTANCE),
        new CompletionProvider<>() {
          public void addCompletions(@NotNull CompletionParameters parameters,
                                     @NotNull ProcessingContext context,
                                     @NotNull CompletionResultSet resultSet) {
            
            PsiElement position = parameters.getPosition();
            String prefix = resultSet.getPrefixMatcher().getPrefix();
            
            // Check if we're after a $ sign (annotation context)
            PsiElement prevSibling = position.getPrevSibling();
            boolean isAfterDollar = false;
            if (prevSibling != null && prevSibling.getNode().getElementType() == CapnpTypes.DOLLAR) {
              isAfterDollar = true;
            } else if (prefix.startsWith("$")) {
              isAfterDollar = true;
            }
            
            // If after $, suggest annotations
            if (isAfterDollar) {
              addAnnotationCompletions(resultSet);
              return; // Don't suggest keywords in annotation context
            }
            
            // Regular keyword completions
            resultSet.addElement(LookupElementBuilder.create("struct"));
            resultSet.addElement(LookupElementBuilder.create("enum"));
            resultSet.addElement(LookupElementBuilder.create("interface"));
            resultSet.addElement(LookupElementBuilder.create("annotation"));
            resultSet.addElement(LookupElementBuilder.create("const"));
            resultSet.addElement(LookupElementBuilder.create("using"));
            resultSet.addElement(LookupElementBuilder.create("import"));
            resultSet.addElement(LookupElementBuilder.create("namespace"));
            resultSet.addElement(LookupElementBuilder.create("union"));
            resultSet.addElement(LookupElementBuilder.create("group"));

            // Primitive types
            String[] primitives = {"Void",   "Bool",   "Int8",      "Int16",
                                   "Int32",  "Int64",  "UInt8",     "UInt16",
                                   "UInt32", "UInt64", "Float32",   "Float64",
                                   "Text",   "Data",   "AnyPointer", "List"};
            for (String primitive : primitives) {
              resultSet.addElement(LookupElementBuilder.create(primitive));
            }
          }
        });
  }

  private void addAnnotationCompletions(@NotNull CompletionResultSet resultSet) {
    // Common Cap'n Proto annotations with descriptions and smart insert handlers
    // Note: We don't include $ in the completion text since user already typed it
    
    // Validation annotations
    resultSet.addElement(
        LookupElementBuilder.create("required(true)")
            .withPresentableText("required")
            .withTailText("(true)")
            .withTypeText("Validation")
            .bold()
    );
    
    resultSet.addElement(
        LookupElementBuilder.create("maxLength(255)")
            .withPresentableText("maxLength")
            .withTailText("(length)")
            .withTypeText("Validation")
            .withInsertHandler(new AnnotationWithParamInsertHandler("255"))
    );
    
    resultSet.addElement(
        LookupElementBuilder.create("minLength(0)")
            .withPresentableText("minLength")
            .withTailText("(length)")
            .withTypeText("Validation")
            .withInsertHandler(new AnnotationWithParamInsertHandler("0"))
    );
    
    resultSet.addElement(
        LookupElementBuilder.create("maxValue(100)")
            .withPresentableText("maxValue")
            .withTailText("(value)")
            .withTypeText("Validation")
            .withInsertHandler(new AnnotationWithParamInsertHandler("100"))
    );
    
    resultSet.addElement(
        LookupElementBuilder.create("minValue(0)")
            .withPresentableText("minValue")
            .withTailText("(value)")
            .withTypeText("Validation")
            .withInsertHandler(new AnnotationWithParamInsertHandler("0"))
    );
    
    // Metadata annotations
    resultSet.addElement(
        LookupElementBuilder.create("deprecated(\"Use alternative\")")
            .withPresentableText("deprecated")
            .withTailText("(message)")
            .withTypeText("Metadata")
            .withInsertHandler(new AnnotationWithParamInsertHandler("\"Use alternative\""))
    );
    
    resultSet.addElement(
        LookupElementBuilder.create("import(\"/path/to/file.capnp\")")
            .withPresentableText("import")
            .withTailText("(path)")
            .withTypeText("Import")
            .withInsertHandler(new AnnotationWithParamInsertHandler("\"/path/to/file.capnp\""))
    );
    
    // Documentation annotations
    resultSet.addElement(
        LookupElementBuilder.create("doc(\"Description\")")
            .withPresentableText("doc")
            .withTailText("(text)")
            .withTypeText("Documentation")
            .withInsertHandler(new AnnotationWithParamInsertHandler("\"Description\""))
    );
    
    // Custom annotations (user might define these)
    resultSet.addElement(
        LookupElementBuilder.create("defaultValue(0)")
            .withPresentableText("defaultValue")
            .withTailText("(value)")
            .withTypeText("Custom")
            .withInsertHandler(new AnnotationWithParamInsertHandler("0"))
    );
  }

  // Insert handler that selects the parameter value inside parentheses for easy editing
  private static class AnnotationWithParamInsertHandler implements InsertHandler<LookupElement> {
    private final String paramValue;

    public AnnotationWithParamInsertHandler(String paramValue) {
      this.paramValue = paramValue;
    }

    @Override
    public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
      // The completion has already inserted something like "maxLength(255)"
      // We need to find and select the value inside the parentheses
      int startOffset = context.getStartOffset();
      int tailOffset = context.getTailOffset();
      String insertedText = context.getDocument().getText().substring(startOffset, tailOffset);
      
      // Find the opening parenthesis
      int openParen = insertedText.indexOf('(');
      if (openParen != -1) {
        // Calculate the position of the parameter value
        int valueStart = startOffset + openParen + 1;
        int valueEnd = valueStart + paramValue.length();
        
        // Select the parameter value so user can immediately type to replace it
        context.getEditor().getCaretModel().moveToOffset(valueEnd);
        context.getEditor().getSelectionModel().setSelection(valueStart, valueEnd);
      }
    }
  }
}


