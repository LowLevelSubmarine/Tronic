package com.tronic.bot.listeners;

import com.tronic.bot.Tronic;
import com.tronic.bot.io.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class MessageLoggerListener extends Listener {

    public MessageLoggerListener(Tronic tronic) {
        super(tronic);
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (!event.getAuthor().equals(event.getJDA().getSelfUser())) {
            String authorTag = event.getAuthor().getAsTag();
            String content = event.getMessage().getContentRaw();
            Logger.log(this, authorTag + ": \"" + content + "\"");
        }
    }

}
