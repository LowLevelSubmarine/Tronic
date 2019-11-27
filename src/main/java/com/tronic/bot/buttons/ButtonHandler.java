package com.tronic.bot.buttons;

import com.tronic.bot.Tronic;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

import java.util.HashMap;
import java.util.Objects;

public class ButtonHandler {

    private final Tronic tronic;
    private final HashMap<ButtonKey, Button> buttons = new HashMap<>();

    public ButtonHandler(Tronic tronic) {
        this.tronic = tronic;
    }

    public void handle(GenericMessageReactionEvent event) {
        ButtonKey key = new ButtonKey(event);
        if (this.buttons.containsKey(key)) {
            this.buttons.get(key).onPressed();
        }
    }

    public void addListener(Button button) {
        this.buttons.put(new ButtonKey(button), button);
    }

    public void removeListener(Button button) {
        this.buttons.remove(new ButtonKey(button));
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

    /*private static class ButtonKey {

        private final String messageId;
        private final byte[] emojiBytes;

        public ButtonKey(Button button) {
            this(button.getMessageId(), button.getEmoji().getUtf8().getBytes(Charsets.UTF_8));
        }

        public ButtonKey(GenericMessageReactionEvent event) {
            this(event.getMessageId(), event.getReactionEmote().getEmoji().getBytes(Charsets.UTF_8));
        }

        public ButtonKey(String messageId, byte[] emojiBytes) {
            this.messageId = messageId;
            this.emojiBytes = emojiBytes;
            Logger.log(this, Arrays.toString(emojiBytes));
        }

        public String getMessageId() {
            return this.messageId;
        }

        public byte[] getEmojieBytes() {
            return this.emojiBytes;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ButtonKey) {
                ButtonKey other = (ButtonKey) obj;
                return this.messageId.equals(other.messageId) && Arrays.equals(this.emojiBytes, other.emojiBytes);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.emojiBytes) * this.messageId.hashCode();
        }

    }*/

}
