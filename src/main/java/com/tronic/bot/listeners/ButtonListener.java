package com.tronic.bot.listeners;

import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onGenericMessageReaction(@Nonnull GenericMessageReactionEvent event) {
        super.onGenericMessageReaction(event);
    }
}
