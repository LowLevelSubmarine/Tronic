package com.tronic.bot.listeners;

import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

import java.util.Objects;

public class ButtonListener extends Listener  {

    public ButtonListener(Core core) {
        super(core);
    }

    @Override
    public void onGenericMessageReaction(GenericMessageReactionEvent event) {
        if (!Objects.requireNonNull(event.getUser()).isBot()) {
            getCore().getButtonHandler().handle(event);
        }
    }
}
