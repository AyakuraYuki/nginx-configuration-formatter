// This is a generated file. Not intended for manual editing.
package cc.ayakurayuki.psi.impl;

import cc.ayakurayuki.psi.BlockStmt;
import cc.ayakurayuki.psi.NameStmt;
import cc.ayakurayuki.psi.RegularDirectiveStmt;
import cc.ayakurayuki.psi.ValueStmt;
import cc.ayakurayuki.psi.Visitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegularDirectiveStmtImpl extends ASTWrapperPsiElement implements RegularDirectiveStmt {

    public RegularDirectiveStmtImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull Visitor visitor) {
        visitor.visitRegularDirectiveStmt(this);
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
    public BlockStmt getBlockStmt() {
        return findChildByClass(BlockStmt.class);
    }

    @Override
    @NotNull
    public NameStmt getNameStmt() {
        return findNotNullChildByClass(NameStmt.class);
    }

    @Override
    @NotNull
    public List<ValueStmt> getValueStmtList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, ValueStmt.class);
    }

}
