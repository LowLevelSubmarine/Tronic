package com.tronic.bot.commands.music;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.TextArgument;
import com.tronic.bot.buttons.Button;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.music.playing.Player;
import com.tronic.bot.music.playing.SingleQueueItem;
import com.tronic.bot.music.sources.Track;
import com.tronic.bot.music.sources.YouTubeTrackProvider;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.JDAUtils;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class SearchCommand implements Command {

    private CommandInfo info;
    private Message message;
    private boolean selected = false;

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
            String query = info.getArguments().parse(new TextArgument()).getOrThrowException();
            List<YouTubeTrackProvider.YouTubeTrack> tracks = info.getMusicManger().getTrackProvider().listSearch(query);
            if (tracks.size() > 5) {
                tracks = tracks.subList(0, 5);
            }
            StringBuilder options = new StringBuilder();
            for (int i = 0; i < tracks.size(); i++) {
                String displayName = tracks.get(i).getDisplayName();
                options.append(JDAUtils.getEmoji(i + 1).getUtf8()).append(" ").append(displayName).append("\n");
            }
            this.message = info.getChannel().sendMessage(new TronicMessage(
                    "Search Results for: " + query,
                    options.toString()
            ).b()).complete();
            Player player = info.getPlayer();
            for (int i = 0; i < tracks.size(); i++) {
                Emoji emoji = JDAUtils.getEmoji(i + 1);
                SelectListener listener = new SelectListener(tracks.get(i), player);
                Button selectionButton = new Button(emoji, listener);
                info.getButtonHandler().register(selectionButton, this.message).queue();
            }
            Button deleteButton = new Button(Emoji.X, this::cancel);
            info.getButtonHandler().register(deleteButton, this.message).queue();
        } catch (InvalidArgumentException e) {
            throw new InvalidCommandArgumentsException();
        }
    }

    private void cancel() {
        this.selected = true;
        this.message.delete().queue();
    }

    private class SelectListener implements Button.PressListener {

        private final Track option;
        private final Player player;

        public SelectListener(Track option, Player player) {
            this.option = option;
            this.player = player;
        }

        @Override
        public void onPress() {
            if (!SearchCommand.this.selected) {
                SearchCommand.this.selected = true;
                this.player.addToQueue(new SingleQueueItem(this.option, SearchCommand.this.info.getMember()));
                SearchCommand.this.message.delete().queue();
            }
        }

    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Search","Shows different results for a song","search");
    }

}
