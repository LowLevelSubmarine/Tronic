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
            info.getCore().getPlayerManager().loadQueueItem(this.query, info.getEvent().getMember(), this::onResults);
        } catch (InvalidArgumentException e) {
            throw new InvalidCommandArgumentsException();
        }
    }

    private void onResults(PlayerManager.QueueItemLoadResult queueItemLoadResult) {
        List<QueueItem> searchResults = queueItemLoadResult.getSearchResults();
        if (searchResults.size() > 5) {
            searchResults = searchResults.subList(0, 5);
        }
        StringBuilder options = new StringBuilder();
        for (int i = 0; i < searchResults.size(); i++) {
            String displayName = searchResults.get(i).getDisplayName();
            options.append(JDAUtils.getEmoji(i + 1).getUtf8()).append(" ").append(displayName).append("\n");
        }
        this.message = info.getChannel().sendMessage(new TronicMessage(
                "Search Results for: " + this.query,
                options.toString()
        ).b()).complete();
        Player player = info.getPlayer();
        for (int i = 0; i < searchResults.size(); i++) {
            Emoji emoji = JDAUtils.getEmoji(i + 1);
            SelectListener listener = new SelectListener(searchResults.get(i), player);
            this.info.getCore().getButtonManager().register(new Button(message, emoji, listener));
        }
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Search","Shows different results for a song","search");
    }

    private class SelectListener implements Button.PressListener {

        private final QueueItem option;
        private final Player player;

        public SelectListener(QueueItem option, Player player) {
            this.option = option;
            this.player = player;
        }

        @Override
        public void onPressed(Button button) {
            this.player.addToQueue(this.option);
            SearchCommand.this.message.delete().queue();
        }

    }

}
