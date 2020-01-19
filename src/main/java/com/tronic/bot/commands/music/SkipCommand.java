package com.tronic.bot.commands.music;

import com.tronic.bot.commands.*;

public class SkipCommand implements Command {

    @Override
    public String invoke() {
        return "skip";
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
        info.getPlayer().skipHard();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("skip","skips running track","skip");
    }

}
