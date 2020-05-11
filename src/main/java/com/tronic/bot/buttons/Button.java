package com.tronic.bot.buttons;

import com.tronic.bot.statics.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

public class Button {

    private final String messageId;
    private final Emoji emoji;
    private final PressListener pressListener;
    private User userLimiter = null;

    public Button(Message message, Emoji emoji, PressListener pressListener) {
        this.messageId = message.getId();
        this.emoji = emoji;
        this.pressListener = pressListener;
        message.addReaction(emoji.getUtf8()).queue();
    }

    public Button limitTo(User user) {
        this.userLimiter = user;
        return this;
    }

    public void onPressed(GenericMessageReactionEvent e) {
        if (this.userLimiter == null || this.userLimiter.equals(e.getUser())) {
            this.pressListener.onPressed(this);
        }
    }

    public String getMessageId() {
        return this.messageId;
    }

    public Emoji getEmoji() {
        return this.emoji;
    }

    public interface PressListener {
        void onPressed(Button button);
    }

}
