package com.tronic.bot.listeners;

import com.tronic.bot.io.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class MessageLoggerListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        Logger.log(this, event.getMessage().getContentDisplay());
    }

}
