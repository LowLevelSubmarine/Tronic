package com.tronic.bot.commands.administration;

import com.tronic.bot.buttons.Button;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.Logger;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class ShutdownCommand implements Command {

    private Message message;
    private CommandInfo info;

    @Override
    public String invoke() {
        return "shutdown";
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
    public void run(CommandInfo info) {
        this.info = info;
        MessageEmbed embed = new TronicMessage("Do you really want to shut me down?").b();
        this.message = info.getEvent().getChannel().sendMessageEmbeds(embed).complete();
        Logger.log(this, Emoji.WHITE_CHECK_MARK.getUtf8());
        Button confirmButton = new Button(Emoji.WHITE_CHECK_MARK, this::onConfirm);
        Button discardButton = new Button(Emoji.X, this::onDiscard);
        info.getButtonHandler().register(confirmButton, this.message).queue();
        info.getButtonHandler().register(discardButton, this.message).queue();
    }

    private void onConfirm() {
        this.message.delete().complete();
        this.info.getCore().shutdownTronic();
    }

    private void onDiscard() {
        this.message.delete().queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("shutdown","Shuts Tronic down","shutdown");
    }

}
