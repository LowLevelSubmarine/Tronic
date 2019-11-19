package com.tronic.bot.listeners;

import com.tronic.bot.CommandRouter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class CommandListener extends ListenerAdapter {

    private CommandRouter router = new CommandRouter();

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String prefix = "#";
        String messageContent = event.getMessage().getContentRaw();
        if (messageContent.startsWith(prefix)) {
            this.router.routeCommand(messageContent.substring(prefix.length()), event);
        }
    }

}
