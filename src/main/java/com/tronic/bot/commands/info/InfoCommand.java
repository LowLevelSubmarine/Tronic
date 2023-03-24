package com.tronic.bot.commands.info;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.GitHub;
import com.tronic.bot.statics.Info;
import com.tronic.bot.tools.Markdown;

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
        String inviteUrl = info.getJDA().getInviteUrl(net.dv8tion.jda.api.Permission.ADMINISTRATOR);
        TronicMessage tm = new TronicMessage("Info","Tronic is currently running on version " + Info.VERSION + ".\n" +
                "You can " + Markdown.uri("invite Tronic here", inviteUrl) + ".\n" +
                "View the " + Markdown.uri("official Tronic GitHub repo", GitHub.REPO_URL) + ".");
        info.getChannel().sendMessageEmbeds(tm.b()).queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Info","Shows some Bot info,","info");
    }
}
