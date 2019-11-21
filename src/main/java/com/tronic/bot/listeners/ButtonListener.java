package com.tronic.bot.listeners;

import com.tronic.bot.buttons.Button;
import com.tronic.bot.statics.Emoji;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class ButtonListener extends ListenerAdapter {

    private final HashMap<ButtonKey, Button> buttons = new HashMap<>();

    @Override
    public void onGenericMessageReaction(@Nonnull GenericMessageReactionEvent event) {
        if (buttons.containsKey(event.getMessageId())) {
            this.buttons.remove(event.getMessageId()).onPressed();
        }
    }

    public void register(Button button) {
        this.buttons.put(new ButtonKey(button), button);
    }

    private class ButtonKey {

        private final String messageId;
        private final Emoji emoji;

        public ButtonKey(Button button) {
            this(button.getMessageId(), button.getEmoji());
        }

        public ButtonKey(String messageId, Emoji emoji) {
            this.messageId = messageId;
            this.emoji = emoji;
        }

        public String getMessageId() {
            return this.messageId;
        }

        public Emoji getEmoji() {
            return this.emoji;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ButtonKey) {
                ButtonKey other = (ButtonKey) obj;
                return other.messageId.equals(this.messageId) && other.emoji == this.emoji;
            }
            return false;
        }

    }

}
