package com.tronic.bot.listeners;

import com.tronic.bot.Tronic;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;

import javax.annotation.Nonnull;

public class DeleteListener extends Listener {
    public DeleteListener(Tronic tronic) {
        super(tronic);
    }

    @Override
    public void onVoiceChannelDelete(@Nonnull VoiceChannelDeleteEvent event) {
        super.onVoiceChannelDelete(event);
        this.getTronic().getHyperchannelManager().onChannelDelete(event);
    }
}
