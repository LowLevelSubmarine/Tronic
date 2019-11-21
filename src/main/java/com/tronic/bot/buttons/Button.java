package com.tronic.bot.buttons;

import com.tronic.bot.Tronic;
import com.tronic.bot.statics.Emoji;

public class Button {

    private final String messageId;
    private final Emoji emoji;
    private final PressListener pressListener;

    public Button(Tronic tronic, String messageId, Emoji emoji, PressListener pressListener) {
        this.messageId = messageId;
        this.emoji = emoji;
        this.pressListener = pressListener;
        tronic.getListeners().button.register(this);
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
