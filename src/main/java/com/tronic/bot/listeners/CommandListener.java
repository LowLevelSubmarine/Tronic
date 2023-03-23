package com.tronic.bot.listeners;

import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import jakarta.annotation.Nonnull;

public class CommandListener extends Listener {

    public CommandListener(Core core) {
        super(core);
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.isFromGuild()) {
            String prefix = getCore().getStorage().getGuild(event.getGuild()).getPrefix();
            String messageContent = event.getMessage().getContentRaw();
            if (messageContent.startsWith(prefix)) {
                String string = messageContent.substring(prefix.length());
                getCore().getCommandHandler().handle(string, event);
            }
        }
    }

}
