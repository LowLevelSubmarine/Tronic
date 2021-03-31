package com.tronic.bot.commands.administration;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.TextArgument;
import com.tronic.bot.buttons.Button;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.LinkedList;

public class BroadcastCommand implements Command {

    private CommandInfo info;
    private MessageEmbed draft;
    private Message message;

    @Override
    public String invoke() {
        return "broadcast";
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
        this.info = info;
        try {
            String text = info.getArguments().parse(new TextArgument()).getOrThrowException();
            this.draft = new TronicMessage(Emoji.WARNING.getUtf8() + "  " + info.getAuthor().getAsTag(), text).b();
            this.message = info.getChannel().sendMessage(this.draft).complete();
            Button confirmButton = new Button(Emoji.WHITE_CHECK_MARK, this::onConfirm);
            Button discardButton = new Button(Emoji.X, this::onDiscard);
            info.getButtonHandler().register(confirmButton, this.message).queue();
            info.getButtonHandler().register(discardButton, this.message).queue();
        } catch (InvalidArgumentException e) {
            throw new InvalidCommandArgumentsException();
        }
    }

    private void onConfirm() {
        this.message.clearReactions().queue();
        broadcast();
    }

    private void onDiscard() {
        this.message.delete().queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Broadcast a Message","Sends a message to all guilds","broadcast <message>");
    }

    private void broadcast() {
        LinkedList<Guild> guilds = new LinkedList<>(this.info.getJDA().getGuilds());
        guilds.remove(this.info.getGuild());
        for (Guild guild : guilds) {
            TextChannel channel = guild.getDefaultChannel();
            if (channel != null) {
                guild.getDefaultChannel().sendMessage(this.draft).queue();
            }
        }
    }

}
