package com.tronic.bot.commands;

import com.tronic.arguments.Arguments;
import com.tronic.bot.core.Core;
import com.tronic.bot.buttons.Button;
import com.tronic.bot.music.Player;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.storage.GuildStorage;
import com.tronic.bot.storage.StaticStorage;
import com.tronic.bot.storage.UserStorage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
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

    public void createButton(Message message, Emoji emoji, Button.PressListener listener) {
        this.core.getButtonManager().register(new Button(message, emoji, listener));
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

    public GuildStorage getGuildStorage(Guild guild) {
        return this.core.getStorage().getGuild(guild);
    }

    public UserStorage getUserStorage(User user) {
        return this.core.getStorage().getUser(user);
    }

    public Guild getGuild() throws IllegalStateException {
        return this.getEvent().getGuild();
    }

    public MessageChannel getChannel() {
        return this.event.getChannel();
    }

    public Player getPlayer() {
        return this.core.getPlayerManager().getPlayer(this.event.getGuild());
    }

    public User getAuthor() {
        return this.event.getAuthor();
    }

    public MessageReceivedEvent getEvent() {
        return this.event;
    }

}
