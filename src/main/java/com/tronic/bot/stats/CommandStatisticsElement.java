package com.tronic.bot.stats;

import java.io.Serializable;

public class CommandStatisticsElement implements Serializable {
    public String text;
    private long date;
    private String userId;
    private boolean isAutocompleted;
    private String command;

    public CommandStatisticsElement () {}
    public CommandStatisticsElement(String text,String command, String userId, boolean isAutocompleted) {
        this.text = text;
        this.command = command;
        this.date = System.currentTimeMillis();
        this.userId = userId;
        this.isAutocompleted = isAutocompleted;

    }

    public String getText() {
        return text;
    }

    public String getCommand() {
        return command;
    }

    public long getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isAutocompleted() {
        return isAutocompleted;
    }


}
