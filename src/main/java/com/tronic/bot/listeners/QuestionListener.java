package com.tronic.bot.listeners;

import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;

public class QuestionListener extends Listener {

    public QuestionListener(Core core) {
        super(core);
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getMessage().isMentioned(event.getJDA().getSelfUser())) {
            getCore().getQuestionHandler().fire(event);
        }
    }

}
