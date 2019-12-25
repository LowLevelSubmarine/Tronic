package com.tronic.bot.listeners;

import com.tronic.bot.Tronic;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;

import javax.annotation.Nonnull;

public class MoveListener extends Listener {
    public MoveListener(Tronic tronic) {
        super(tronic);
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        super.onGuildVoiceMove(event);
        this.getTronic().getHyperchannelManager().onUserMove(event);
    }
}
