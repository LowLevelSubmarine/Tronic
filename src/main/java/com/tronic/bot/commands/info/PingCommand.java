package com.tronic.bot.commands.info;

import com.tronic.arguments.LiteralArgument;
import com.tronic.bot.commands.*;

public class PingCommand implements Command {
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
