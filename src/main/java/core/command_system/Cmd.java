package core.command_system;

import core.command_system.syntax.Syntax;
import core.permissions.Permission;

public interface Cmd {

    String invoke();
    CmdCategory category();
    boolean guildAccess();
    boolean privateAccess();
    Permission requiredPermission();
    Syntax syntax();
    CmdInstance createInstance();
    String info();
    String description();

}
