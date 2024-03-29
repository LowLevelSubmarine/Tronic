package com.tronic.bot.commands.settings;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.SingleArgument;
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
        return CommandType.SETTINGS;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        try {
            String option = info.getArguments().splitParse(new SingleArgument()).getOrThrowException();
            try {
                String value = info.getArguments().getString();
                switch (option) {
                    case "activate":
                        info.getCore().getStorage().getGuild(info.getEvent().getGuild()).setHyperchannelState((value.equals("true")));
                        if (value.equals("true")) info.getChannel().sendMessageEmbeds(new TronicMessage("Activate Hyperchannel!").b()).queue();
                        if (!value.equals("true")) info.getChannel().sendMessageEmbeds(new TronicMessage("Deactivate Hyperchannel!").b()).queue();
                        info.getCore().getHyperchannelManager().refreshHyper(info.getGuild());
                        break;
                    case "category":
                        info.getCore().getHyperchannelManager().setHyperCategory(value,info.getEvent().getGuild());
                        info.getChannel().sendMessageEmbeds(new TronicMessage("Change new HyperChannelCategory to "+value+" !").b()).queue();
                        break;
                    case "name":
                        info.getCore().getHyperchannelManager().hyperChannelRename(value,info.getEvent().getGuild());
                        info.getChannel().sendMessageEmbeds(new TronicMessage("Change new HyperChannel to "+value+" !").b()).queue();
                        break;
                }
            } catch (NullPointerException e) {
                switch (option) {
                    case "activate":
                        info.getChannel().sendMessageEmbeds(new TronicMessage("Please use true or false as an value!").b()).queue();
                        break;
                    case "category":
                        info.getCore().getHyperchannelManager().setHyperCategory(null,info.getEvent().getGuild());
                        info.getChannel().sendMessageEmbeds(new TronicMessage("HyperChannels are now not using a category (Not Recommended)!").b()).queue();
                        break;
                    case "name":
                        info.getChannel().sendMessageEmbeds(new TronicMessage("Please enter a name!").b()).queue();
                        break;
                }
            }
        } catch (InvalidArgumentException |NullPointerException e) {
            info.getChannel().sendMessageEmbeds(new TronicMessage("Please use activate|category|name as an option!").b()).queue();
        }
    }
    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Hyperchannel","Control Hyperchannel","sethyperchannel category/name/activate");
    }
}
