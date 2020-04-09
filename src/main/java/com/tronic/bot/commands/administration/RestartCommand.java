package com.tronic.bot.commands.administration;

import com.tronic.bot.commands.*;

public class RestartCommand implements Command {

    @Override
    public String invoke() {
        return "restart";
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
        return CommandType.ADMINISTRATION;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        info.getCore().restart();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Restart Tronic","Restart the bot","restart");
    }

}
