package com.tronic.bot.commands.administration;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;

public class ShutdownCommand implements Command {

    @Override
    public String invoke() {
        return "shutdown";
    }

    @Override
    public Permission getPermission() {
        return Permission.HOST;
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
        info.getEvent().getChannel().sendMessage(new TronicMessage("LOL").b()).queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return null;
    }

}
