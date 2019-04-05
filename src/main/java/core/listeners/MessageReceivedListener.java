package core.listeners;

import core.Core;
import core.command_system.CmdHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageReceivedListener extends ListenerAdapter {

    private final CmdHandler handler;

    public MessageReceivedListener(Core core) {
        this.handler = new CmdHandler(core);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        this.handler.handle(event);
    }

}
