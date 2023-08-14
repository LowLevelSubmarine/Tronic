package com.tronic.bot.commands.info;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;

public class WhoAmICommand implements Command {

    @Override
    public String invoke() {
        return "whoami";
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
        info.getChannel().sendMessageEmbeds(new TronicMessage(info.getAuthor().getAsMention()+" you have the role "+Permission.getPermission(info.getAuthor(),info.getGuild(),info.getCore())).b()).queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("WhoAmI","Tells you your (highest) Permission Role","whoami");
    }

}
