package com.tronic.bot.listeners;

import com.tronic.bot.core.Core;
import com.tronic.bot.smartbot.SmartBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;

public class CommandListener extends Listener {

    public CommandListener(Core core) {
        super(core);
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String prefix = getCore().getStorage().getGuild(event.getGuild()).getPrefix();
        String messageContent = event.getMessage().getContentRaw();
        if (event.getMessage().isMentioned(event.getJDA().getSelfUser())) {
            new SmartBot(event);
        }
        if (messageContent.startsWith(prefix)) {
            String string = messageContent.substring(prefix.length());
            getCore().getCommandHandler().handle(string, event);
        }
    }

}
