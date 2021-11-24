package com.tronic.bot.commands.music;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.TextArgument;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.music.playing.QueueItem;
import com.tronic.bot.statics.Emoji;

public class PlayCommand implements Command {

    @Override
    public String invoke() {
        return "play";
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
        try {
            String query = info.getArguments().parse(new TextArgument()).getOrThrowException();
            QueueItem queueItem;
            if (query.startsWith("https://")) {
                queueItem = info.getMusicManger().getTrackProvider().fromUrl(query);
                if (queueItem == null) info.getChannel().sendMessageEmbeds(new TronicMessage(
                        "There seems to be something wrong with your link! " + Emoji.CONFUSED).b()).queue();
            } else {
                queueItem = info.getMusicManger().getTrackProvider().fromSingleSearch(query);
                if (queueItem == null) info.getChannel().sendMessageEmbeds(new TronicMessage(
                        "Sorry, I couldn't find any song matching \"" + query + "\" " + Emoji.CONFUSED).b()).queue();
            }
            if (queueItem != null) info.getPlayer().addToQueue(queueItem, info.getMember());
        } catch (InvalidArgumentException e) {
            if (info.getPlayer().isPaused()) {
                info.getPlayer().setPaused(false);
            } else {
                throw new InvalidCommandArgumentsException();
            }
        }
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Play","Plays a given song","play <song/ Youtube url>");
    }

}
