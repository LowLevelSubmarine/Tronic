package com.tronic.bot.commands;

public class HelpInfo {

    private final String shortDescription;

    HelpInfo(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

}
