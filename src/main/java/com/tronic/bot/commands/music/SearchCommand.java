package com.tronic.bot.commands.music;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.TextArgument;
import com.tronic.bot.buttons.Button;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.music.Player;
import com.tronic.bot.music.PlayerManager;
import com.tronic.bot.music.QueueItem;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.JDAUtils;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class SearchCommand implements Command {

    private CommandInfo info;
    private String query;
    private Message message;

    @Override
    public String invoke() {
        return "search";
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
        this.info = info;
        try {
            this.query = "ytsearch:" + info.getArguments().parse(new TextArgument()).getOrThrowException();
        } catch (InvalidArgumentException e) {
            throw new InvalidCommandArgumentsException();
        }
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Search","Shows different results for a song","search");
    }

}
