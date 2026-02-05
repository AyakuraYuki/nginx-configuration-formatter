// This is a generated file. Not intended for manual editing.
package cc.ayakurayuki.psi.impl;

import cc.ayakurayuki.psi.IfStmt;
import cc.ayakurayuki.psi.Visitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class IfStmtImpl extends ASTWrapperPsiElement implements IfStmt {

    public IfStmtImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull Visitor visitor) {
        visitor.visitIfStmt(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof Visitor) {
            accept((Visitor) visitor);
        } else {
            super.accept(visitor);
        }
    }

}
