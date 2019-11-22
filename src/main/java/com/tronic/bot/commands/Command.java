package com.tronic.bot.commands;

public interface Command {

    Permission getPermission();
    boolean silent();
    CommandType getType();
    void parse(CommandInfo info) throws InvalidCommandArgumentsException;
    void run(CommandInfo info);
    HelpInfo getHelpInfo();

}
