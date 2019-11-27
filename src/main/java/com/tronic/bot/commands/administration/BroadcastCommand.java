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
            this.draft = new TronicMessage(Emoji.WARNING.getUtf8() + "  " + info.getAuthor().getAsTag() + ":", text).b();
            this.message = info.getChannel().sendMessage(this.draft).complete();
            this.info.createButton(message, Emoji.WHITE_CHECK_MARK, this::onConfirm);
            this.info.createButton(message, Emoji.X, this::onDiscard);
        } catch (InvalidArgumentException e) {
            throw new InvalidCommandArgumentsException();
        }
    }

    private void onConfirm(Button button) {
        this.message.clearReactions().queue();
        broadcast();
    }

    private void onDiscard(Button button) {
        this.message.delete().queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return null;
    }

    private void broadcast() {
        for (Guild guild : this.info.getJDA().getGuilds()) {
            if (!guild.equals(this.info.getGuild())) {
                TextChannel channel = guild.getDefaultChannel();
                if (channel != null) {
                    guild.getDefaultChannel().sendMessage(this.draft).queue();
                }
            }
        }
    }

}
