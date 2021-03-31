package com.tronic.bot.commands.settings;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.SingleArgument;
import com.tronic.bot.buttons.Button;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.MessageChanger;

public class SetPrefixCommand implements Command {

    private MessageChanger messageChanger;
    private CommandInfo info;
    private String oldPrefix;
    private String newPrefix;

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
            this.newPrefix = info.getArguments().parse(new SingleArgument()).getOrThrowException();
            this.oldPrefix = info.getGuildStorage(info.getGuild()).getPrefix();
            this.info = info;
            Button declineButton = new Button(Emoji.X, this::onDecline);
            Button acceptButton = new Button(Emoji.WHITE_CHECK_MARK, this::onAccept);
            this.messageChanger = new MessageChanger(info.getCore(), info.getChannel());
            this.messageChanger.change(new TronicMessage(
                    "PREFIX",
                    "Would you like to change this guilds prefix from '" + this.oldPrefix + "' to '" + this.newPrefix + "'?"
            ).b(), declineButton, acceptButton);
        } catch (InvalidArgumentException e) {
            throw new InvalidCommandArgumentsException();
        }
    }

    private void onAccept() {
        this.info.getGuildStorage(this.info.getGuild()).setPrefix(newPrefix);
        this.messageChanger.change(new TronicMessage(
                "PREFIX",
                "Changed prefix from \'" + this.oldPrefix + "\' to \'" + this.newPrefix + "\'"
        ).b());
    }

    private void onDecline() {
        this.messageChanger.delete();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Prefix","Set the prefix of the bot in a guilds","setprefix <prefix>");
    }

}
