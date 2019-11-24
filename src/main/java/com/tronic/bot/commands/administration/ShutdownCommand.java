package com.tronic.bot.commands.administration;

import com.tronic.bot.buttons.Button;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.Logger;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class ShutdownCommand implements Command {

    private Message deleteMessage;

    @Override
    public String invoke() {
        return "shutdown";
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
        MessageEmbed embed = new TronicMessage("Do you really want to shut me down?").b();
        this.deleteMessage = info.getEvent().getChannel().sendMessage(embed).complete();
        Logger.log(this, Emoji.WHITE_CHECK_MARK.getUtf8());
        info.createButton(this.deleteMessage, Emoji.WHITE_CHECK_MARK, this::onConfirm);
        info.createButton(this.deleteMessage, Emoji.X, this::onDiscard);
    }

    private void onConfirm(Button button) {
        this.deleteMessage.delete().queue();
    }

    private void onDiscard(Button button) {
        this.deleteMessage.delete().queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return null;
    }

}
