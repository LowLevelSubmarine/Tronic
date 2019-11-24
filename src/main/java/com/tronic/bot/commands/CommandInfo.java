package com.tronic.bot.commands;

import com.tronic.arguments.Arguments;
import com.tronic.bot.Tronic;
import com.tronic.bot.buttons.Button;
import com.tronic.bot.statics.Emoji;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
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

    public MessageReceivedEvent getEvent() {
        return this.event;
    }

}
