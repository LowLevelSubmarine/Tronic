package com.tronic.bot.commands.music;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.TextArgument;
import com.tronic.bot.commands.*;
import com.tronic.bot.music.Player;
import com.tronic.bot.music.PlayerManager;

public class PlayCommand implements Command {

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
            String text = info.getArguments().parse(new TextArgument()).getOrThrowException();
            if (!text.contains("youtube.com")) {
                text = "ytsearch:" + text;
            }
            info.getTronic().getPlayerManager().loadQueueItem(text, info.getEvent().getMember(), this::onQueueItemLoaded);
        } catch (InvalidArgumentException e) {
            throw new InvalidCommandArgumentsException();
        }
    }

    private void onQueueItemLoaded(PlayerManager.QueueItemLoadResult queueItemLoadResult) {
        if (queueItemLoadResult != null) {
            Player player = this.info.getPlayer();
            if (player != null) {
                player.queue(queueItemLoadResult.get());
            }
        }
    }

    @Override
    public HelpInfo getHelpInfo() {
        return null;
    }

}
