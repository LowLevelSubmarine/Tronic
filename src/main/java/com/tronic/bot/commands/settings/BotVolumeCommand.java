package com.tronic.bot.commands.settings;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;

public class BotVolumeCommand implements Command {
    @Override
    public String invoke() {
        return "setbotvolume";
    }

    @Override
    public Permission getPermission() {
        return Permission.CO_HOST;
    }

    @Override
    public boolean silent() {
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.SETTINGS;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        Integer vol = Integer.parseInt(info.getArguments().getString());
        if (vol<=200 && vol>=0) {
            info.getCore().getStorage().getStatic().setBotVolume(Integer.parseInt(info.getArguments().getString())/100);
            info.getChannel().sendMessage(new TronicMessage("Bot Volume is set to "+info.getCore().getStorage().getStatic().getBotVolume()*100).b()).queue();
        } else {
            info.getChannel().sendMessage(new TronicMessage("Bot Volume out of range! Enter a value between 0 and 200").b()).queue();
        }

    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Bot Volume","Set general Bot Volume","setBotVolume <1-200>");
    }
}
