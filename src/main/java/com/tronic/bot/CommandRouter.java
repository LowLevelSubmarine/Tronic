package com.tronic.bot;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.Arguments;
import com.tronic.arguments.LiteralArgument;
import com.tronic.arguments.TextArgument;
import com.tronic.bot.io.Logger;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandRouter {

    public void routeCommand(String command, MessageReceivedEvent event) {
        try {
            Arguments arguments = new Arguments(command);
            arguments.splitParse(new LiteralArgument("say")).throwException();
            String text = arguments.splitParse(new TextArgument()).getOrThrowException();
            event.getChannel().sendMessage(new TronicMessage(text).b()).queue();
            event.getMessage().addReaction(Emoji.THUMBSUP.getUtf8()).queue();
        } catch (InvalidArgumentException e) {
            Logger.log(this, "Invalid command: " + command);
        }
    }

}
