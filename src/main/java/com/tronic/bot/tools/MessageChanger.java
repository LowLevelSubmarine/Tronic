package com.tronic.bot.tools;

import com.lowlevelsubmarine.ytml.actions.RestAction;
import com.tronic.bot.buttons_new.Button;
import com.tronic.bot.buttons_new.ButtonHandler;
import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.LinkedList;

public class MessageChanger implements Runnable {

    private final Core core;
    private final TextChannel textChannel;
    private boolean running;
    private Message message;
    private MessageChangeAction nextMessageChangeAction;

    public MessageChanger(Core core, TextChannel textChannel) {
        this.core = core;
        this.textChannel = textChannel;
    }

    public void change(MessageEmbed embed, Button... buttons) {
        this.nextMessageChangeAction = new MessageChangeAction(embed, buttons, this.core.getNewButtonHandler());
        new Thread(this).start();
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

        public Message changeMessage(TextChannel channel) {
            Message message = channel.sendMessage(this.embed).complete();
            for (Button button : this.buttons) {
                this.buttonHandler.register(button, message).complete();
            }
            return message;
        }

        public Message changeMessage(Message message) {
            message.editMessage(this.embed).complete();
            message.clearReactions().complete();
            for (Button button : this.buttons) {
                this.buttonHandler.register(button, message).complete();
            }
            return message;
        }

    }

}