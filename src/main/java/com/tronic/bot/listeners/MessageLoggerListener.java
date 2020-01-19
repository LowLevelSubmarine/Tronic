package com.tronic.bot.listeners;

import com.tronic.bot.core.Core;
import com.tronic.bot.io.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;

public class MessageLoggerListener extends Listener {

    public MessageLoggerListener(Core core) {
        super(core);
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
