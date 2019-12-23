package com.tronic.bot.commands.settings;

import com.tronic.arguments.BooleanArgument;
import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.TextArgument;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;

public class HyperchannelNameCommand implements Command {
    @Override
    public String invoke() {
        return "sethyperchannelname";
    }

    @Override
    public Permission getPermission() {
        return Permission.ADMIN;
    }

    @Override
    public boolean silent() {
        return true;
    }

    @Override
    public CommandType getType() {
        return null;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        try {
            String name = info.getArguments().parse(new TextArgument()).getOrThrowException();
            info.getTronic().getHyperchannelManager().hyperChannelRename(name,info.getEvent().getGuild());
            info.getChannel().sendMessage(new TronicMessage("Change new HyperChannel to "+name+" !").b()).queue();
        } catch (InvalidArgumentException e) {
            info.getChannel().sendMessage(new TronicMessage("Add a name as an options").b()).queue();
        }
    }

    @Override
    public HelpInfo getHelpInfo() {
        return null;
    }
}
