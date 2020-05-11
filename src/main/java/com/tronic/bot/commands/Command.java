package com.tronic.bot.commands;

public interface Command {

    String invoke();
    Permission getPermission();
    boolean silent();
    CommandType getType();
    void run(CommandInfo info) throws InvalidCommandArgumentsException;
    HelpInfo getHelpInfo();

}
