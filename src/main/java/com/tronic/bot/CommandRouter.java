package com.tronic.bot;

import com.tronic.arguments.Argument;
import com.tronic.arguments.IntegerArgument;
import com.tronic.arguments.LiteralArgument;
import com.tronic.bot.io.Logger;
import com.tronic.bot.io.TronicMessage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandRouter {

    public void routeCommand(String command, MessageReceivedEvent event) {
        try {
            LiteralArgument<IntegerArgument> litArgument = new LiteralArgument<>("parse", new IntegerArgument(null));
            litArgument.parse(new Argument.Info(command, " "));
            Logger.log(this, "Parsed: " + litArgument.getArgument().getInteger());
            event.getChannel().sendMessage(new TronicMessage("Parsed: " + litArgument.getArgument().getInteger()).build()).queue();
        } catch (Argument.ParseException e) {
            e.printStackTrace();
        }
    }

}
