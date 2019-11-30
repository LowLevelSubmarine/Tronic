package com.tronic.bot.commands;

import com.tronic.arguments.Arguments;
import com.tronic.bot.Tronic;
import com.tronic.bot.buttons.Button;
import com.tronic.bot.music.Player;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.storage.GuildStorage;
import com.tronic.bot.storage.StaticStorage;
import com.tronic.bot.storage.Storage;
import com.tronic.bot.storage.UserStorage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandInfo {

    private final Tronic tronic;
    private final Arguments arguments;
    private final MessageReceivedEvent event;

    public CommandInfo(Tronic tronic, Arguments arguments, MessageReceivedEvent event) {
        this.tronic = tronic;
        this.arguments = arguments;
        this.event = event;
    }

    public void createButton(Message message, Emoji emoji, Button.PressListener listener) {
        new Button(this.tronic, message, emoji, listener);
    }

    public Tronic getTronic() {
        return this.tronic;
    }

    public Arguments getArguments() {
        return this.arguments;
    }

    public JDA getJDA() {
        return this.tronic.getJDA();
    }

    public StaticStorage getStaticStorage() {
        return this.tronic.getStorage().getStatic();
    }

    public GuildStorage getGuildStorage(Guild guild) {
        return this.tronic.getStorage().getGuild(guild);
    }

    public UserStorage getUserStorage(User user) {
        return this.tronic.getStorage().getUser(user);
    }

    public Guild getGuild() throws IllegalStateException {
        return this.getEvent().getGuild();
    }

    public MessageChannel getChannel() {
        return this.event.getChannel();
    }

    public Player getPlayer() {
        return this.tronic.getPlayerManager().getPlayer(this.event.getGuild());
    }

    public User getAuthor() {
        return this.event.getAuthor();
    }

    public MessageReceivedEvent getEvent() {
        return this.event;
    }

}
