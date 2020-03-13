package com.tronic.bot.commands.music;

import com.tronic.bot.commands.*;

public class VolumeCommand implements Command {
    @Override
    public String invoke() {
        return "volume";
    }

    @Override
    public Permission getPermission() {
        return Permission.NONE;
    }

    @Override
    public boolean silent() {
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.MUSIC;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {

    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Volume","set the Volume for a song","volume 1-200");
    }
}
