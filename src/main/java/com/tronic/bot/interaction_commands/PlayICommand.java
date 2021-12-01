package com.tronic.bot.interaction_commands;

import com.tronic.bot.music.playing.QueueItem;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PlayICommand implements ICommand {

    private static final OptionData OPTION_SEARCH_QUERY =
            new OptionData(OptionType.STRING, "search_query", "A song name, an artist or a video title", true);

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Plays a song";
    }

    @Override
    public CommandData getCommandData(CommandData prototype) {
        return prototype.addOptions(OPTION_SEARCH_QUERY);
    }

    @Override
    public void run(ICommandInfo i) {
        i.getEvent().getHook().deleteOriginal().queue();
        String query = i.getEvent().getOption(OPTION_SEARCH_QUERY.getName()).getAsString();
        QueueItem queueItem = i.getTrackProvider().fromSingleSearch(query);
        Member member = i.getEvent().getMember();
        i.getPlayer().addToQueue(queueItem, member);
    }

}
