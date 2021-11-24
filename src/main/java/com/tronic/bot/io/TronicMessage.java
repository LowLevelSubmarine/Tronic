package com.tronic.bot.io;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class TronicMessage {

    private static final Color COLOR = new Color(143, 41, 158);
    private final EmbedBuilder builder = new EmbedBuilder();

    public TronicMessage(String text) {
        setDefaults();
        this.builder.setDescription(text);
    }

    public TronicMessage(String title, String text) {
        setDefaults();
        this.builder.setTitle(title);
        if (text != null) this.builder.setDescription(text);
    }

    public TronicMessage addField(String name, String value, boolean inline) {
        this.builder.addField(name, value, inline);
        return this;
    }

    public TronicMessage addBlankField(boolean inline) {
        this.builder.addBlankField(inline);
        return this;
    }

    private void setDefaults() {
        this.builder.setColor(COLOR);
    }

    public TronicMessage setImage(String url) {
        this.builder.setImage(url);
        return this;
    }

    public TronicMessage setFooter(String footer) {
        this.builder.setFooter(footer);
        return this;
    }

    public MessageEmbed build() {
        return this.builder.build();
    }

    public MessageEmbed b() {
        return this.builder.build();
    }

}
