// This is a generated file. Not intended for manual editing.
package cc.ayakurayuki.psi.impl;

import cc.ayakurayuki.psi.ConcatenatedExpr;
import cc.ayakurayuki.psi.ConditionExpr;
import cc.ayakurayuki.psi.StringStmt;
import cc.ayakurayuki.psi.Visitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConditionExprImpl extends ASTWrapperPsiElement implements ConditionExpr {

    public ConditionExprImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull Visitor visitor) {
        visitor.visitConditionExpr(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof Visitor) {
            accept((Visitor) visitor);
        } else {
            super.accept(visitor);
        }
    }

    @Override
    @Nullable
    public ConcatenatedExpr getConcatenatedExpr() {
        return findChildByClass(ConcatenatedExpr.class);
    }

    @Override
    @Nullable
    public StringStmt getStringStmt() {
        return findChildByClass(StringStmt.class);
    }

}
