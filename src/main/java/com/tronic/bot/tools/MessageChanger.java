package com.tronic.bot.tools;

import com.tronic.bot.buttons.Button;
import com.tronic.bot.buttons.ButtonHandler;
import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class MessageChanger implements Runnable {

    private final Core core;
    private final MessageChannel textChannel;
    private boolean running;
    private Message message;
    private MessageChangeAction nextMessageChangeAction;

    public MessageChanger(Core core, MessageChannel messageChannel) {
        this.core = core;
        this.textChannel = messageChannel;
    }

    public void change(MessageEmbed embed, Button... buttons) {
        change(embed, false, buttons);
    }

    public void change(MessageEmbed embed, boolean threadBlocking, Button... buttons) {
        this.nextMessageChangeAction = new MessageChangeAction(embed, buttons, this.core.getButtonHandler());
        Thread thread = new Thread(this);
        if (threadBlocking) thread.run();
        else thread.start();
    }

    public void delete() {
        this.message.delete().queue();
    }

    public void run() {
        if (!this.running && this.nextMessageChangeAction != null) {
            this.running = true;
            MessageChangeAction messageChangeAction = this.nextMessageChangeAction;
            this.nextMessageChangeAction = null;
            if (this.message == null) {
                this.message = messageChangeAction.changeMessage(this.textChannel);
            } else {
                this.message = messageChangeAction.changeMessage(this.message);
            }
            this.running = false;
            run();
        }
    }

    private static class MessageChangeAction {

        private final MessageEmbed embed;
        private final Button[] buttons;
        private final ButtonHandler buttonHandler;

        public MessageChangeAction(MessageEmbed embed, Button[] buttons, ButtonHandler buttonHandler) {
            this.embed = embed;
            this.buttons = buttons;
            this.buttonHandler = buttonHandler;
        }

        public Message changeMessage(MessageChannel channel) {
            Message message = channel.sendMessageEmbeds(this.embed).complete();
            for (Button button : this.buttons) {
                this.buttonHandler.register(button, message).complete();
            }
            return message;
        }

        public Message changeMessage(Message message) {
            message.editMessageEmbeds(this.embed).complete();
            message.clearReactions().complete();
            for (Button button : this.buttons) {
                this.buttonHandler.register(button, message).complete();
            }
            return message;
        }

    }

}