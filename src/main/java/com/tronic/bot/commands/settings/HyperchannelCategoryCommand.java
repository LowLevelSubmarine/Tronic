package com.tronic.bot.commands.settings;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.TextArgument;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import net.dv8tion.jda.api.entities.Category;

public class HyperchannelCategoryCommand implements Command {
    @Override
    public String invoke() {
        return "sethyperchannelcategory";
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
            info.getTronic().getHyperchannelManager().setHyperCategory(name,info.getEvent().getGuild());
            info.getChannel().sendMessage(new TronicMessage("Change new HyperChannelCategory to "+name+" !").b()).queue();
        } catch (InvalidArgumentException e) {
            info.getTronic().getHyperchannelManager().setHyperCategory(null,info.getEvent().getGuild());
            info.getChannel().sendMessage(new TronicMessage("HyperChannels are now not using a category (Not Recommended)!").b()).queue();
        }
    }

    @Override
    public HelpInfo getHelpInfo() {
        return null;
    }
}
