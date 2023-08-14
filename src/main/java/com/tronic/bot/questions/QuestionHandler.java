package com.tronic.bot.questions;

import com.lowlevelsubmarine.google_answers_api.AskRequest;
import com.lowlevelsubmarine.google_answers_api.GoogleAnswersApi;
import com.tronic.bot.core.Core;
import com.tronic.bot.core.Tronic;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuestionHandler {

    private final Core core;
    private final GoogleAnswersApi googleAnswersApi = new GoogleAnswersApi();
    private final Logger logger = LogManager.getLogger(Tronic.class);
    private final String REGEX_MENTION = "<(.*?)>";

    public QuestionHandler(Core core) {
        this.core = core;
    }

    public void fire(MessageReceivedEvent event) {
        String raw = event.getMessage().getContentRaw();
        String question = raw.replaceAll(REGEX_MENTION, "");
        new QuestionHandler.AnswerThread(event.getChannel(), question);
    }

    private class AnswerThread {

        private final MessageChannelUnion channel;

        public AnswerThread(MessageChannelUnion channel, String question) {
            this.channel = channel;
            QuestionHandler.this.googleAnswersApi.ask(question).queue(this::onComplete, this::onException);
        }

        private void onComplete(AskRequest askRequest) {
            this.channel.sendMessageEmbeds(new TronicMessage(askRequest.getAnswer()).b()).queue();
        }

        private void onException(Exception e) {
            this.channel.sendMessageEmbeds(new TronicMessage("Im sorry, I don't understand this question " + Emoji.CONFUSED.getUtf8()).b()).queue();
        }


    }

}
