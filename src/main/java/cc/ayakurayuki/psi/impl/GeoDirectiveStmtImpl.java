// This is a generated file. Not intended for manual editing.
package cc.ayakurayuki.psi.impl;

import cc.ayakurayuki.psi.GeoBlockStmt;
import cc.ayakurayuki.psi.GeoDirectiveStmt;
import cc.ayakurayuki.psi.GeoStmt;
import cc.ayakurayuki.psi.VariableStmt;
import cc.ayakurayuki.psi.Visitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeoDirectiveStmtImpl extends ASTWrapperPsiElement implements GeoDirectiveStmt {

    public GeoDirectiveStmtImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull Visitor visitor) {
        visitor.visitGeoDirectiveStmt(this);
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
    public GeoBlockStmt getGeoBlockStmt() {
        return findChildByClass(GeoBlockStmt.class);
    }

    @Override
    @NotNull
    public GeoStmt getGeoStmt() {
        return findNotNullChildByClass(GeoStmt.class);
    }

    @Override
    @NotNull
    public List<VariableStmt> getVariableStmtList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, VariableStmt.class);
    }

}
