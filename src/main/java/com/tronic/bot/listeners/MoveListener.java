package com.tronic.bot.listeners;

import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;

import javax.annotation.Nonnull;

public class MoveListener extends Listener {
    public MoveListener(Core tronic) {
        super(tronic);
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        super.onGuildVoiceMove(event);
        this.getCore().getHyperchannelManager().onUserMove(event);
    }
}
