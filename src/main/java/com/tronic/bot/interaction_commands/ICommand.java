package com.tronic.bot.interaction_commands;

import com.tronic.bot.core.Core;
import com.tronic.bot.music.MusicManager;
import com.tronic.bot.music.playing.Player;
import com.tronic.bot.music.sources.TrackProvider;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface ICommand {

    String getName();
    String getDescription();
    CommandData getCommandData(CommandData prototype);
    void run(ICommandInfo i);

    class ICommandInfo {

        private final Core core;
        private final SlashCommandEvent event;

        public ICommandInfo(SlashCommandEvent event, Core core) {
            this.event = event;
            this.core = core;
        }

        public TrackProvider getTrackProvider() {
            return this.core.getMusicManager().getTrackProvider();
        }

        public Player getPlayer() {
            return this.core.getMusicManager().getPlayer(this.event.getGuild());
        }

        public SlashCommandEvent getEvent() {
            return this.event;
        }

        public Core getCore() {
            return this.core;
        }

    }

}
