// This is a generated file. Not intended for manual editing.
package cc.ayakurayuki.psi;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DirectiveStmt extends WithPathElement {

    @Nullable
    GeoDirectiveStmt getGeoDirectiveStmt();

    @Nullable
    IfDirectiveStmt getIfDirectiveStmt();

    @Nullable
    LocationDirectiveStmt getLocationDirectiveStmt();

    @Nullable
    LuaDirectiveStmt getLuaDirectiveStmt();

    @Nullable
    MapDirectiveStmt getMapDirectiveStmt();

    @Nullable
    RegularDirectiveStmt getRegularDirectiveStmt();

    @NotNull
    List<String> getPath();

}
