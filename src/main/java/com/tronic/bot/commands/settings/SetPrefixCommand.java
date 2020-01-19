package com.tronic.bot.commands.settings;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.SingleArgument;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.storage.GuildStorage;

public class SetPrefixCommand implements Command {

    @Override
    public String invoke() {
        return "setprefix";
    }

    @Override
    public Permission getPermission() {
        return Permission.ADMIN;
    }

    @Override
    public boolean silent() {
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.SETTINGS;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        try {
            String newPrefix = info.getArguments().parse(new SingleArgument()).getOrThrowException();
            GuildStorage guildStorage = info.getGuildStorage(info.getGuild());
            String oldPrefix = guildStorage.getPrefix();
            guildStorage.setPrefix(newPrefix);
            info.getChannel().sendMessage(
                    new TronicMessage(
                            "PREFIX",
                            "Changed prefix from \'" + oldPrefix + "\' to \'" + newPrefix + "\'"
                    ).b()).queue();
        } catch (InvalidArgumentException e) {
            throw new InvalidCommandArgumentsException();
        }
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Prefix","Set the prefix of the bot in a guils","setprefix");
    }

}
