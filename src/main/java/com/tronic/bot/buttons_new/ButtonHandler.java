package com.tronic.bot.buttons_new;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.HashMap;
import java.util.Objects;

public class ButtonHandler {

    private final HashMap<ButtonKey, Button> buttons = new HashMap<>();

    public void handle(GenericMessageReactionEvent event) {
        ButtonKey key = new ButtonKey(event);
        if (this.buttons.containsKey(key)) {
            this.buttons.get(key).onPress(event);
        }
    }

    public RestAction<Void> register(Button button, Message message) {
        register(button, message.getId());
        return message.addReaction(button.getEmoji().getUtf8());
    }

    public void register(Button button, String messageId) {
        this.buttons.put(new ButtonKey(button, messageId), button);
    }

    private static class ButtonKey {

        private final String emoji;
        private final String messageId;

        public ButtonKey(Button button, String messageId) {
            this.emoji = button.getEmoji().getUtf8();
            this.messageId = messageId;
        }

        public ButtonKey(GenericMessageReactionEvent event) {
            this.emoji = event.getReactionEmote().getEmoji();
            this.messageId = event.getMessageId();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ButtonKey) {
                ButtonKey other = (ButtonKey) obj;
                return this.emoji.equals(other.emoji) && this.messageId.equals(other.messageId);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.emoji, this.messageId);
        }

    }

}
