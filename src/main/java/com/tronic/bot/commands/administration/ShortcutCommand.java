package com.tronic.bot.commands.administration;

import com.tronic.bot.commands.*;

public class ShortcutCommand implements Command {
    @Override
    public String invoke() {
        return "shortcut";
    }

    @Override
    public Permission getPermission() {
        return Permission.ADMIN;
    }

    @Override
    public boolean silent() {
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.ADMINISTRATION;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        info.getCore().getCommandHandler().addCommand(new Command() {
            @Override
            public String invoke() {
                return null;
            }

            @Override
            public Permission getPermission() {
                return null;
            }

            @Override
            public boolean silent() {
                return false;
            }

            @Override
            public CommandType getType() {
                return null;
            }

            @Override
            public void run(CommandInfo info) throws InvalidCommandArgumentsException {
                info.getCore().getCommandHandler().
            }

            @Override
            public HelpInfo getHelpInfo() {
                return null;
            }
        });
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Shortcut","Manages shortcuts on server","create <shortcut>, delete <shortcut>,edit <shortcut>");
    }
}
