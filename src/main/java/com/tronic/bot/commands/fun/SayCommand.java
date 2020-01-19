package com.tronic.bot.commands.fun;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.TextArgument;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;

public class SayCommand implements Command {

    @Override
    public String invoke() {
        return "say";
    }

    @Override
    public Permission getPermission() {
        return Permission.NONE;
    }

    @Override
    public boolean silent() {
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.FUN;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        try {
            String text = info.getArguments().parse(new TextArgument()).getOrThrowException();
            info.getEvent().getChannel().sendMessage(new TronicMessage(text).b()).queue();
        } catch (InvalidArgumentException e) {
            throw new InvalidCommandArgumentsException();
        }
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Say Command","says a inserted text","say");
    }

}
