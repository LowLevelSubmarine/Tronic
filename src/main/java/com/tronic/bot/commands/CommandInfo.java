package com.tronic.bot.commands;

import com.tronic.arguments.Arguments;
import com.tronic.bot.buttons.ButtonHandler;
import com.tronic.bot.core.Core;
import com.tronic.bot.music.MusicManager;
import com.tronic.bot.music.playing.Player;
import com.tronic.bot.storage.GuildStorage;
import com.tronic.bot.storage.StaticStorage;
import com.tronic.bot.storage.UserStorage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandInfo {

    private final Core core;
    private final Arguments arguments;
    private final MessageReceivedEvent event;

    public CommandInfo(Core core, Arguments arguments, MessageReceivedEvent event) {
        this.core = core;
        this.arguments = arguments;
        this.event = event;
    }

    public Core getCore() {
        return this.core;
    }

    public Arguments getArguments() {
        return this.arguments;
    }

    public JDA getJDA() {
        return this.core.getJDA();
    }

    public StaticStorage getStaticStorage() {
        return this.core.getStorage().getStatic();
    }

    public GuildStorage getGuildStorage() {
        return getGuildStorage(this.getGuild());
    }

    public GuildStorage getGuildStorage(Guild guild) {
        return this.core.getStorage().getGuild(guild);
    }

    public UserStorage getUserStorage() {
        return getUserStorage(this.getAuthor());
    }

    public UserStorage getUserStorage(User user) {
        return this.core.getStorage().getUser(user);
    }

    public Guild getGuild() throws IllegalStateException {
        return this.getEvent().getGuild();
    }

    public Member getMember() throws IllegalStateException {
        return this.getEvent().getMember();
    }

    public MessageChannelUnion getChannel() {
        return this.event.getChannel();
    }

    public MusicManager getMusicManger() {
        return this.core.getMusicManager();
    }

    public Player getPlayer() {
        return this.core.getMusicManager().getPlayer(this.event.getGuild());
    }

    public ButtonHandler getButtonHandler() {
        return this.core.getButtonHandler();
    }

    public User getAuthor() {
        return this.event.getAuthor();
    }

    public MessageReceivedEvent getEvent() {
        return this.event;
    }

    public boolean isGuildContext() {
        return this.getGuild() != null;
    }

}
