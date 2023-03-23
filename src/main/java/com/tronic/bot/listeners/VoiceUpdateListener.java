package com.tronic.bot.listeners;

import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

public class VoiceUpdateListener extends Listener {
    public VoiceUpdateListener(Core core) {
        super(core);
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        this.getCore().getHyperchannelManager().onGuildVoiceUpdate(event);
    }
}
