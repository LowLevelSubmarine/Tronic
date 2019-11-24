package com.tronic.bot.buttons;

import com.tronic.bot.Tronic;
import com.tronic.bot.statics.Emoji;
import net.dv8tion.jda.api.entities.Message;

public class Button {

    private final String messageId;
    private final Emoji emoji;
    private final PressListener pressListener;

    public Button(Tronic tronic, Message message, Emoji emoji, PressListener pressListener) {
        this.messageId = message.getId();
        this.emoji = emoji;
        this.pressListener = pressListener;
        message.addReaction(emoji.getUtf8()).queue();
        tronic.getButtonHandler().addListener(this);
    }

    public void onPressed() {
        if (this.pressListener != null) {
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
