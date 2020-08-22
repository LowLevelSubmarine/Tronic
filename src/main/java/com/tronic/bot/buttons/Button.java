package com.tronic.bot.buttons;

import com.tronic.bot.statics.Emoji;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

public class Button {

    private final Emoji emoji;
    private final PressListener pressListener;
    private final Validator validator;

    public Button(Emoji emoji, PressListener pressListener) {
        this(emoji, pressListener, null);
    }

    public Button(Emoji emoji, PressListener pressListener, Validator validator) {
        this.emoji = emoji;
        this.pressListener = pressListener;
        this.validator = validator;
    }

    public void onPress(GenericMessageReactionEvent event) {
        if (this.validator == null || this.validator.validate(event)) {
            this.pressListener.onPress();
        }
    }

    public Emoji getEmoji() {
        return this.emoji;
    }

    public Validator getValidator() {
        return this.validator;
    }

    public interface PressListener {
        void onPress();
    }

    public interface Validator {
        boolean validate(GenericMessageReactionEvent event);
    }

}
