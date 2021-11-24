package com.tronic.bot.commands.music;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.TextArgument;
import com.tronic.bot.buttons.Button;
import com.tronic.bot.buttons.UserButtonValidator;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.music.playing.QueueItem;
import com.tronic.bot.music.playing.SingleQueueItem;
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
            //load results
            List<SingleQueueItem> tracks = info.getMusicManger().getTrackProvider().fromMultiSearch(query, 5);
            //generate message
            StringBuilder options = new StringBuilder();
            for (int i = 0; i < tracks.size(); i++) {
                String name = tracks.get(i).getName();
                options.append(JDAUtils.getEmoji(i + 1)).append(" ").append(name).append("\n");
            }
            this.message = info.getChannel().sendMessageEmbeds(new TronicMessage(
                    "Search Results for: " + query,
                    options.toString()
            ).b()).complete();
            //generate buttons
            for (int i = 0; i < tracks.size(); i++) {
                Emoji emoji = JDAUtils.getEmoji(i + 1);
                SelectListener listener = new SelectListener(tracks.get(i));
                Button selectionButton = new Button(emoji, listener, new UserButtonValidator(info));
                info.getButtonHandler().register(selectionButton, this.message).queue();
            }
            Button deleteButton = new Button(Emoji.X, this::cancel, new UserButtonValidator(info));
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

        private final QueueItem option;

        public SelectListener(QueueItem option) {
            this.option = option;
        }

        @Override
        public void onPress() {
            if (!SearchCommand.this.selected) {
                SearchCommand.this.selected = true;
                SearchCommand.this.info.getPlayer().addToQueue(this.option, SearchCommand.this.info.getMember());
                SearchCommand.this.message.delete().queue();
            }
        }

    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Search","Shows different results for a song search","search <song>");
    }

}
