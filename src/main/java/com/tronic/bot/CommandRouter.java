package com.tronic.bot;

import com.tronic.arguments.ArgumentParseException;
import com.tronic.arguments.Arguments;
import com.tronic.arguments.LiteralArgument;
import com.tronic.arguments.TextArgument;
import com.tronic.bot.io.Logger;
import com.tronic.bot.io.TronicMessage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandRouter {

    public void routeCommand(String command, MessageReceivedEvent event) {
        try {
            Arguments arguments = new Arguments(command);
            arguments.parseAndSplit(new LiteralArgument("say"));
            String text = arguments.parseAndSplit(new TextArgument());
            event.getChannel().sendMessage(new TronicMessage(text).build()).queue();
        } catch (ArgumentParseException e) {
            Logger.log(this, "Invalid command: " + command);
        }
    }

}
