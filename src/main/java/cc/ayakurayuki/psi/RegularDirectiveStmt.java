// This is a generated file. Not intended for manual editing.
package cc.ayakurayuki.psi;

import com.intellij.psi.PsiElement;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RegularDirectiveStmt extends PsiElement {

    @Nullable
    BlockStmt getBlockStmt();

    @NotNull
    NameStmt getNameStmt();

    @NotNull
    List<ValueStmt> getValueStmtList();

}
