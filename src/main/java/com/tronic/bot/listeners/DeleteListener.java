package com.tronic.bot.listeners;


import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;

import javax.annotation.Nonnull;

public class DeleteListener extends Listener {
    public DeleteListener(Core tronic) {
        super(tronic);
    }

    @Override
    public void onVoiceChannelDelete(@Nonnull VoiceChannelDeleteEvent event) {
        super.onVoiceChannelDelete(event);
        if (this.getCore().getHyperchannelManager()!=null) {
            this.getCore().getHyperchannelManager().onChannelDelete(event);
        }
    }
}
