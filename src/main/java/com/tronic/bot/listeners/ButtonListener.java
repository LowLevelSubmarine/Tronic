package com.tronic.bot.listeners;

import com.tronic.bot.Tronic;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

import javax.annotation.Nonnull;

public class ButtonListener extends Listener  {

    public ButtonListener(Tronic tronic) {
        super(tronic);
    }

    @Override
    public void onGenericMessageReaction(@Nonnull GenericMessageReactionEvent event) {
        if (!event.getUser().equals(event.getJDA().getSelfUser())) {
            getTronic().getButtonHandler().handle(event);
        }
    }

}
