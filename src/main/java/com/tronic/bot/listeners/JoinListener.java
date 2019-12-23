package com.tronic.bot.listeners;

import com.tronic.bot.Tronic;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;

import javax.annotation.Nonnull;

public class JoinListener extends Listener {
    private Tronic tronic;
    public JoinListener(Tronic tronic) {
        super(tronic);
        this.tronic = tronic;
    }

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        super.onGuildVoiceJoin(event);
        this.tronic.getHyperchannelManager().onUserJoins(event);
    }


}
