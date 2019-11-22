package com.tronic.bot.commands.administration;

import com.tronic.bot.commands.*;

public class ShutdownCommand implements Command {

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
    public void parse(CommandInfo info) throws InvalidCommandArgumentsException {

    }

    @Override
    public void run(CommandInfo info) {

    }

    @Override
    public HelpInfo getHelpInfo() {
        return null;
    }

}
