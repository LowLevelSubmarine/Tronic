package com.tronic.bot.buttons;

import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class ButtonHandler {

    private final Core core;
    private final HashMap<ButtonKey, Button> buttons = new HashMap<>();

    public ButtonHandler(Core core) {
        this.core = core;
    }

    public void handle(GenericMessageReactionEvent event) {
        ButtonKey key = new ButtonKey(event);
        if (this.buttons.containsKey(key)) {
            this.buttons.get(key).onPressed(event);
        }
    }

    public void register(Button button) {
        this.buttons.put(new ButtonKey(button), button);
    }

    public void unregister(Button button) {
        this.buttons.remove(new ButtonKey(button));
    }

    public void unregisterAll(Message message) {
        Set<ButtonKey> keys = this.buttons.keySet();
        for (ButtonKey key : keys) {
            if (key.messageId.equals(message.getId())) {
                this.buttons.remove(key);
            }
        }
    }

    private static class ButtonKey {

        private final String messageId;
        private final String emoji;

        public ButtonKey(Button button) {
            this(button.getMessageId(), button.getEmoji().getUtf8());
        }

        public ButtonKey(GenericMessageReactionEvent event) {
            this(event.getMessageId(), event.getReactionEmote().getEmoji());
        }

        public ButtonKey(String messageId, String emoji) {
            this.messageId = messageId;
            this.emoji = emoji;
        }

        public String getMessageId() {
            return this.messageId;
        }

        public String getEmojie() {
            return this.emoji;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ButtonKey) {
                ButtonKey other = (ButtonKey) obj;
                return this.messageId.equals(other.messageId) && this.emoji.equals(other.emoji);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.messageId, this.emoji);
        }

    }

}
