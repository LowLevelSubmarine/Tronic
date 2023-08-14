package com.tronic.bot.commands.info;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.tools.Markdown;

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
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        info.getChannel().sendMessageEmbeds(new TronicMessage(
                Markdown.uri("Here's the invite link", createInviteUrl(info))
        ).b()).queue();
    }

    private String createInviteUrl(CommandInfo info) {
        return info.getJDA().getInviteUrl(net.dv8tion.jda.api.Permission.ADMINISTRATOR);
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Invite Tronic","Posts a invite link so you can add Tronic to your own server","invite");
    }

}
