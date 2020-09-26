package com.tronic.bot.commands.administration;

import com.tronic.bot.commands.*;

public class SandboxCommand implements Command {

    @Override
    public String invoke() {
        return "sandbox";
    }

    @Override
    public Permission getPermission() {
        return Permission.HOST;
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


    }





    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Sandbox","dbg tool","sandbox");
    }

}

