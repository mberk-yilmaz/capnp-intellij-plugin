package com.mberk_yilmaz.capnp.formatting;

import com.mberk_yilmaz.capnp.psi.CapnpTypes;
import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CapnpFormattingModelBuilder implements FormattingModelBuilder {
  @NotNull
  @Override
  public FormattingModel
  createModel(@NotNull FormattingContext formattingContext) {
    ASTNode rootNode = formattingContext.getNode();
    return FormattingModelProvider.createFormattingModelForPsiFile(
        formattingContext.getContainingFile(),
        new CapnpBlock(rootNode, null, null),
        formattingContext.getCodeStyleSettings());
  }

  private static class CapnpBlock extends AbstractBlock {
    protected CapnpBlock(@NotNull ASTNode node, @Nullable Wrap wrap,
                         @Nullable Alignment alignment) {
      super(node, wrap, alignment);
    }

    @Override
    protected List<Block> buildChildren() {
      List<Block> blocks = new ArrayList<>();
      ASTNode child = myNode.getFirstChildNode();
      while (child != null) {
        if (child.getElementType() != TokenType.WHITE_SPACE &&
            child.getTextRange().getLength() > 0) {
          blocks.add(new CapnpBlock(child, null, null));
        }
        child = child.getTreeNext();
      }
      return blocks;
    }

    @Nullable
    @Override
    public Indent getIndent() {
      IElementType type = myNode.getElementType();

      // Elements that should be one tab inside their parent bounds
      if (type == CapnpTypes.STRUCT_CONTENT ||
          type == CapnpTypes.ENUM_CONTENT ||
          type == CapnpTypes.INTERFACE_CONTENT ||
          type == CapnpTypes.UNION_CONTENT ||
          type == CapnpTypes.FIELD_DEF_NO_SEMI ||
          type == CapnpTypes.FIELD_ASSIGNMENT ||
          type == CapnpTypes.ANNOTATION_APP_ARG ||
          type == CapnpTypes.TYPE_PARAM) {
        return Indent.getNormalIndent();
      }
      return Indent.getNoneIndent();
    }

    @NotNull
    @Override
    public ChildAttributes getChildAttributes(int newChildIndex) {
      IElementType type = myNode.getElementType();

      // When user presses Enter inside these blocks, they should start with a
      // normal tab indent
      if (type == CapnpTypes.STRUCT_DEF || type == CapnpTypes.ENUM_DEF ||
          type == CapnpTypes.INTERFACE_DEF || type == CapnpTypes.GROUP_DEF ||
          type == CapnpTypes.UNION_DEF || type == CapnpTypes.STRUCT_LITERAL ||
          type == CapnpTypes.LIST_LITERAL) {

        return new ChildAttributes(Indent.getNormalIndent(), null);
      }

      // Everywhere else (like top of the file), indent is removed (starts at
      // line start)
      return new ChildAttributes(Indent.getNoneIndent(), null);
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
      if (child1 instanceof CapnpBlock && child2 instanceof CapnpBlock) {
        IElementType type1 = ((CapnpBlock)child1).myNode.getElementType();
        IElementType type2 = ((CapnpBlock)child2).myNode.getElementType();

        if (type2 == CapnpTypes.LBRACE) {
          return Spacing.createSpacing(1, 1, 0, false, 0);
        }
        if (type1 == CapnpTypes.AT && type2 == CapnpTypes.ID_VALUE) {
          return Spacing.createSpacing(0, 0, 0, false, 0);
        }
        if (type2 == CapnpTypes.AT) {
          return Spacing.createSpacing(1, 1, 0, false, 0);
        }
        if (type2 == CapnpTypes.COLON) {
          return Spacing.createSpacing(1, 1, 0, false, 0);
        }
        if (type1 == CapnpTypes.COLON) {
          return Spacing.createSpacing(0, 0, 0, false, 0);
        }
        if (type1 == CapnpTypes.EQUALS || type2 == CapnpTypes.EQUALS) {
          return Spacing.createSpacing(1, 1, 0, false, 0);
        }
        if (type1 == CapnpTypes.ARROW || type2 == CapnpTypes.ARROW) {
          return Spacing.createSpacing(1, 1, 0, false, 0);
        }

        // Force new lines around assignments inside struct literals
        if (myNode.getElementType() == CapnpTypes.STRUCT_LITERAL) {
          if (type1 == CapnpTypes.LPAREN || type2 == CapnpTypes.RPAREN ||
              type1 == CapnpTypes.COMMA) {
            return Spacing.createSpacing(0, 0, 1, false, 0);
          }
        }

        if (type1 == CapnpTypes.COMMA) {
          return Spacing.createSpacing(1, 1, 0, false, 0);
        }
      }
      return null;
    }

    @Override
    public boolean isLeaf() {
      return myNode.getFirstChildNode() == null;
    }
  }
}

