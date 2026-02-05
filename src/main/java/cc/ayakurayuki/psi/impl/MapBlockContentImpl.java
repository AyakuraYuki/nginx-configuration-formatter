// This is a generated file. Not intended for manual editing.
package cc.ayakurayuki.psi.impl;

import cc.ayakurayuki.psi.MapBlockContent;
import cc.ayakurayuki.psi.MapDefaultStmt;
import cc.ayakurayuki.psi.MapHostnamesStmt;
import cc.ayakurayuki.psi.MapIncludeStmt;
import cc.ayakurayuki.psi.MapValueStmt;
import cc.ayakurayuki.psi.MapVolatileStmt;
import cc.ayakurayuki.psi.Visitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MapBlockContentImpl extends ASTWrapperPsiElement implements MapBlockContent {

    public MapBlockContentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull Visitor visitor) {
        visitor.visitMapBlockContent(this);
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
    public MapDefaultStmt getMapDefaultStmt() {
        return findChildByClass(MapDefaultStmt.class);
    }

    @Override
    @Nullable
    public MapHostnamesStmt getMapHostnamesStmt() {
        return findChildByClass(MapHostnamesStmt.class);
    }

    @Override
    @Nullable
    public MapIncludeStmt getMapIncludeStmt() {
        return findChildByClass(MapIncludeStmt.class);
    }

    @Override
    @Nullable
    public MapValueStmt getMapValueStmt() {
        return findChildByClass(MapValueStmt.class);
    }

    @Override
    @Nullable
    public MapVolatileStmt getMapVolatileStmt() {
        return findChildByClass(MapVolatileStmt.class);
    }

}
