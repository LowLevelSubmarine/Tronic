package com.tronic.bot.commands.info;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;

import java.util.HashMap;
import java.util.LinkedList;

public class HelpCommand implements Command {
    public static final String URL = "https://tronicbot.com/help?token=";

    @Override
    public String invoke() {
        return "help";
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
        return CommandType.INFO;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        HashMap<String,String> args = new HashMap<>();
        args.put("guild",info.getGuild().getId());
        if (info.getCore().getConfig().isOriginal()) {
            try {
                info.getAuthor().openPrivateChannel().queue((channel)-> channel.sendMessage(new TronicMessage("Help at "+URL+info.getCore().getRestServer().getJwtStore().createJWT(info.getAuthor().getIdLong(),args)).b()).queue());
            } catch (Exception e) {
                info.getChannel().sendMessage(new TronicMessage(info.getAuthor().getAsMention()+" Please allow directMessages from Tronic to you and try it again").b()).queue();
            }
        }
    }


    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Help","Returns this message","Help");
    }
}
