package com.tronic.bot.music;

import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.Markdown;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class MessageBuilder {

    public MessageEmbed buildQueueMessage(String title, String uri) {
        return buildMessage(Emoji.ARROW_HEADING_DOWN, title, uri);
    }

    public MessageEmbed buildMessage(Emoji emoji, String title, String uri) {
        return new TronicMessage(emoji.getUtf8() + "  | " + Markdown.uri(title, uri)).b();
    }

}
