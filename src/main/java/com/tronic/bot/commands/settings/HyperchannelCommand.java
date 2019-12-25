package com.tronic.bot.commands.settings;

import com.tronic.arguments.BooleanArgument;
import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.TextArgument;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;

public class HyperchannelCommand implements Command {
    @Override
    public String invoke() {
        return "sethyperchannel";
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
            boolean state = info.getArguments().parse(new BooleanArgument()).getOrThrowException();
            info.getTronic().getStorage().getGuild(info.getEvent().getGuild()).setHyperchannelState(state);
            if (state) info.getChannel().sendMessage(new TronicMessage("Activate Hyperchannel!").b()).queue();
            if (!state) info.getChannel().sendMessage(new TronicMessage("Deactivate Hyperchannel!").b()).queue();
            info.getTronic().getHyperchannelManager().refreshHyper(info.getGuild());

        } catch (InvalidArgumentException e) {
            info.getChannel().sendMessage(new TronicMessage("Please use true or false as an option!").b()).queue();
        }
    }
    @Override
    public HelpInfo getHelpInfo() {
        return null;
    }
}
