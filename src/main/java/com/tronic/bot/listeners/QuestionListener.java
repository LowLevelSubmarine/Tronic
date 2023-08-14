package com.tronic.bot.listeners;

import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class QuestionListener extends Listener {

    public QuestionListener(Core core) {
        super(core);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getMentions().isMentioned(event.getJDA().getSelfUser())) {
            getCore().getQuestionHandler().fire(event);
        }
    }

}
