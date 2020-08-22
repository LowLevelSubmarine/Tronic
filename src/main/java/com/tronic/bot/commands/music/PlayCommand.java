package com.tronic.bot.commands.music;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.TextArgument;
import com.tronic.bot.commands.*;
import com.tronic.bot.music.MusicManager;
import com.tronic.bot.music.playing.QueueItem;
import com.tronic.bot.music.playing.SingleQueueItem;

public class PlayCommand implements Command, MusicManager.QueueItemResultListener {

    private CommandInfo info;

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
        this.info = info;
        try {
            String query = info.getArguments().parse(new TextArgument()).getOrThrowException();
            if (!query.startsWith("https://")) {
                QueueItem queueItem = new SingleQueueItem(info.getMusicManger().getTrackProvider().magnetSearch(query), info.getMember());
                this.info.getPlayer().addToQueue(queueItem);
            } else {
                info.getCore().getMusicManager().loadQueueItem(query, this, info.getMember());
            }
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
        return new HelpInfo("Play","plays music","play <song>");
    }

    @Override
    public void onResult(QueueItem queueItem) {
        this.info.getPlayer().addToQueue(queueItem);
    }

    @Override
    public void onError() {

    }

}
