package com.tronic.bot.commands.administration;

import com.tronic.bot.commands.*;

public class ShutdownCommand implements Command {

    @Override
    public String invoke() {
        return "shutdown";
    }

    @Override
    public Permission getPermission() {
        return Permission.MASTER;
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

    }

    @Override
    public HelpInfo getHelpInfo() {
        return null;
    }
}
