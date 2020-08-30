package com.tronic.bot.commands.music;

import com.tronic.bot.commands.*;
import com.tronic.bot.music.playing.Player;

public class PauseCommand implements Command {

    @Override
    public String invoke() {
        return "pause";
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
        Player player = info.getPlayer();
        player.setPaused(!player.isPaused());
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Pause","Pause the music","pause");
    }

}
