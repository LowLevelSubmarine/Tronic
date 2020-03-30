package com.tronic.bot.commands.music;

import com.tronic.bot.commands.*;

public class StopCommand implements Command {

    @Override
    public String invoke() {
        return "stop";
    }

    @Override
    public Permission getPermission() {
        return Permission.NONE;
    }

    @Override
    public boolean silent() {
        return true;
    }

    @Override
    public CommandType getType() {
        return CommandType.MUSIC;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        info.getPlayer().stop();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return null;
    }

}
