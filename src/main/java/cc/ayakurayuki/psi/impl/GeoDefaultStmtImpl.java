// This is a generated file. Not intended for manual editing.
package cc.ayakurayuki.psi.impl;

import cc.ayakurayuki.psi.GeoDefaultStmt;
import cc.ayakurayuki.psi.ValueStmt;
import cc.ayakurayuki.psi.Visitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeoDefaultStmtImpl extends ASTWrapperPsiElement implements GeoDefaultStmt {

    public GeoDefaultStmtImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull Visitor visitor) {
        visitor.visitGeoDefaultStmt(this);
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
    public ValueStmt getValueStmt() {
        return findChildByClass(ValueStmt.class);
    }

}
