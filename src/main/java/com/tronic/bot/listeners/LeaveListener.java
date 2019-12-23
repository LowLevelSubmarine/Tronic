package com.tronic.bot.listeners;

import com.tronic.bot.Tronic;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;

import javax.annotation.Nonnull;

public class LeaveListener extends Listener {
    public LeaveListener(Tronic tronic) {
        super(tronic);
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);
        this.getTronic().getHyperchannelManager().onUserLeaves(event);
    }
}
