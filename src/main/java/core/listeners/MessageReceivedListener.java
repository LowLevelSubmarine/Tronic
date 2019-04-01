package core.listeners;

import core.command_system.CmdHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageReceivedListener extends ListenerAdapter {

    private final CmdHandler handler = new CmdHandler();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        this.handler.handle(event);
    }

}
