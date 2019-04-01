package core.command_system;

import core.permissions.Permission;

public interface Cmd {

    String invoke();
    boolean guildAccess();
    boolean privateAccess();
    Permission requiredPermission();
    CmdInstance createInstance();

}
