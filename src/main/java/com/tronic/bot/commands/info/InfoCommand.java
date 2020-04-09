package com.tronic.bot.commands.info;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;

public class InfoCommand implements Command {

    @Override
    public String invoke() {
        return "info";
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
        TronicMessage tm = new TronicMessage("Info","Tronic is a Discord Bot for Music and Hyperchannel");
        //TODO: Write a serious description Text
        tm.addField("Try Tronic/ Invide Tronic ",info.getJDA().getInviteUrl(net.dv8tion.jda.api.Permission.ADMINISTRATOR),false);
        tm.addField("GitHub Repository", "https://github.com/LowLevelSubmarine/Tronic",false);
        info.getChannel().sendMessage(tm.b()).queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Info","Shows some Bot info,","info");
    }
}
