package com.tronic.bot.listeners;

import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;

import javax.annotation.Nonnull;

public class ReadyListener extends Listener {

    public ReadyListener(Core core) {
        super(core);
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            new Thread(() -> guild.getAudioManager().closeAudioConnection()).start();
        }
    }
}
