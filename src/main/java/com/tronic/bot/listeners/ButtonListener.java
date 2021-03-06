package com.tronic.bot.listeners;

import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ButtonListener extends Listener  {

    public ButtonListener(Core core) {
        super(core);
    }

    @Override
    public void onGenericMessageReaction(@Nonnull GenericMessageReactionEvent event) {
        if (!Objects.requireNonNull(event.getUser()).isBot()) {
            getCore().getButtonManager().handle(event);
        }
    }

}
