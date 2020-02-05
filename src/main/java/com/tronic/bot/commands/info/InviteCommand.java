package com.tronic.bot.commands.info;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;

public class InviteCommand implements Command {
    @Override
    public String invoke() {
        return "invite";
    }

    @Override
    public Permission getPermission() {
        return Permission.NONE;
    }

    @Override
    public boolean silent() {
        return true;
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        info.getChannel().sendMessage(new TronicMessage("Invite link: "+info.getJDA().getInviteUrl(net.dv8tion.jda.api.Permission.ADMINISTRATOR)).b()).queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Invite Command","post an invite link to the command","invite");
    }
}
