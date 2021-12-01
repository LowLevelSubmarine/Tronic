package com.tronic.bot.interaction_commands;

import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.music.playing.QueueItem;
import com.tronic.bot.music.playing.SingleQueueItem;
import com.tronic.bot.tools.JDAUtils;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.Component;

import java.util.LinkedList;
import java.util.List;

public class SearchICommand implements ICommand {

    private final OptionData OPTION_SEARCH_QUERY =
            new OptionData(OptionType.STRING, "search_query", "A song name, an artist or a video title");

    @Override
    public String getName() {
        return "search";
    }

    @Override
    public String getDescription() {
        return "Choose from 5 song search results";
    }

    @Override
    public CommandData getCommandData(CommandData prototype) {
        return prototype.addOptions(OPTION_SEARCH_QUERY);
    }

    private ICommandInfo info;

    @Override
    public void run(ICommandInfo info) {
        this.info = info;
        String query = info.getEvent().getOption(OPTION_SEARCH_QUERY.getName()).getAsString();
        List<SingleQueueItem> queueItems = info.getTrackProvider().fromMultiSearch(query, 5);
        StringBuilder optionsString = new StringBuilder();
        LinkedList<Component> components = new LinkedList<>();
        for (int i = 0; i < queueItems.size(); i++) {
            String itemName = queueItems.get(i).getName();
            optionsString.append(JDAUtils.getEmoji(i + 1).getUtf8()).append(" ").append(itemName).append("\n");
            components.add(info.getIButtonManager().generateButton(
                    ButtonStyle.SECONDARY,
                    queueItems.get(i),
                    this::onSelection,
                    JDAUtils.getEmoji(i + 1)));
        }
        info.getEvent().replyEmbeds(new TronicMessage(
                "Search Results for: " + query,
                optionsString.toString()
        ).b()).addActionRow(components).complete();
    }

    private void onSelection(ButtonClickEvent event, Object o) {
        if (o instanceof QueueItem) {
            this.info.getPlayer().addToQueue((QueueItem) o, this.info.getEvent().getMember());
        }
    }

}
