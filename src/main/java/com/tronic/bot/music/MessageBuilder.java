package com.tronic.bot.music;

import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.Markdown;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class MessageBuilder {

    public static MessageEmbed buildDequeuedMessage(String title, String uri) {
        return buildMessage(Emoji.EJECT, title, uri);
    }

    public static MessageEmbed buildSkippedMessage(String title, String uri) {
        return buildMessage(Emoji.FAST_FORWARD, title, uri);
    }

    public static MessageEmbed buildPauseMessage(String title, String uri) {
        return buildMessage(Emoji.PAUSE_BUTTON, title, uri);
    }

    public static MessageEmbed buildPlayingMessage(String title, String uri) {
        return buildMessage(Emoji.ARROW_FORWARD, title, uri);
    }

    public static MessageEmbed buildQueueMessage(String title, String uri) {
        return buildMessage(Emoji.ARROW_HEADING_DOWN, title, uri);
    }

    private static MessageEmbed buildMessage(Emoji emoji, String title, String uri) {
        return new TronicMessage(emoji.getUtf8() + "  | " + Markdown.uri(title, uri)).b();
    }

}
