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
        this.builder.setDescription(text);
    }

    public void addField(String name, String value, boolean inline) {
        this.builder.addField(name, value, inline);
    }

    public void addBlankField(boolean inline) {
        this.builder.addBlankField(inline);
    }

    private void setDefaults() {
        this.builder.setColor(COLOR);
    }

    public MessageEmbed build() {
        return this.builder.build();
    }

}
