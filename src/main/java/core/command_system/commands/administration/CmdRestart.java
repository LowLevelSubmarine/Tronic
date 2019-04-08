package core.command_system.commands.administration;

import core.command_system.Cmd;
import core.command_system.CmdCategory;
import core.command_system.CmdInstance;
import core.command_system.CmdParser;
import core.command_system.syntax.Syntax;
import core.command_system.syntax.TextOption;
import core.permissions.Permission;
import main.Main;

public class CmdRestart implements Cmd {

    @Override
    public String invoke() {
        return "restart";
    }

    @Override
    public CmdCategory category() {
        return CmdCategory.ADMINISTRATION;
    }

    @Override
    public boolean guildAccess() {
        return true;
    }

    @Override
    public boolean privateAccess() {
        return true;
    }

    @Override
    public Permission requiredPermission() {
        return Permission.BOT_ADMIN;
    }

    @Override
    public Syntax syntax() {
        return new Syntax().add(new TextOption("reason"));
    }

    @Override
    public CmdInstance createInstance() {
        return new Instance();
    }

    private class Instance implements CmdInstance {
        @Override
        public void run(CmdParser parser) {
            Main.restart();
        }
    }

    @Override
    public String info() {
        return "Restarts me";
    }

    @Override
    public String description() {
        return "Restarts me and posts a reason for that into every guild im on";
    }
}
