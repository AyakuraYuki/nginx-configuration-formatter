// This is a generated file. Not intended for manual editing.
package cc.ayakurayuki.psi.impl;

import cc.ayakurayuki.psi.BlockStmt;
import cc.ayakurayuki.psi.LocationDirectiveStmt;
import cc.ayakurayuki.psi.LocationModifierStmt;
import cc.ayakurayuki.psi.LocationPathStmt;
import cc.ayakurayuki.psi.LocationStmt;
import cc.ayakurayuki.psi.Visitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocationDirectiveStmtImpl extends ASTWrapperPsiElement implements LocationDirectiveStmt {

    public LocationDirectiveStmtImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull Visitor visitor) {
        visitor.visitLocationDirectiveStmt(this);
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
    @Nullable
    public LocationModifierStmt getLocationModifierStmt() {
        return findChildByClass(LocationModifierStmt.class);
    }

    @Override
    @Nullable
    public LocationPathStmt getLocationPathStmt() {
        return findChildByClass(LocationPathStmt.class);
    }

    @Override
    @NotNull
    public LocationStmt getLocationStmt() {
        return findNotNullChildByClass(LocationStmt.class);
    }

}
