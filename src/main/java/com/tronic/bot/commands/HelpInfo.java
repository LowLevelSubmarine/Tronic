package com.tronic.bot.commands;

public class HelpInfo {

    private final String shortDescription;
    private final String title;
    private final String syntax;

    public HelpInfo(String title, String shortDescription, String syntax) {
        this.shortDescription = shortDescription;
        this.syntax  = syntax;
        this.title = title;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getTitle() {
        return title;
    }

    public String getSyntax() {
        return syntax;
    }
}
