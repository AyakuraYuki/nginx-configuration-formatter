// This is a generated file. Not intended for manual editing.
package cc.ayakurayuki.psi;

import com.intellij.psi.PsiElement;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MapDirectiveStmt extends PsiElement {

    @Nullable
    MapBlockStmt getMapBlockStmt();

    @NotNull
    MapStmt getMapStmt();

    @NotNull
    List<ValueStmt> getValueStmtList();

}
