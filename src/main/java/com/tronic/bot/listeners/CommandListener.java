package com.tronic.bot.listeners;

import com.tronic.bot.Tronic;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;

public class CommandListener extends Listener {

    public CommandListener(Tronic tronic) {
        super(tronic);
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String prefix = "#";
        String messageContent = event.getMessage().getContentRaw();
        if (messageContent.startsWith(prefix)) {
            String string = messageContent.substring(prefix.length());
            getTronic().getCommandHandler().handle(string, event);
        }
    }

}
