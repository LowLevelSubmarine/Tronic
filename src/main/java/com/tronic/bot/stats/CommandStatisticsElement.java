package com.tronic.bot.stats;

import java.io.Serializable;

public class CommandStatisticsElement implements Serializable {
    public String text;
    private long date;
    private int isAutocompleted;
    private String command;

    public CommandStatisticsElement () {}
    public CommandStatisticsElement(String text,String command, boolean isAutocompleted) {
        this.text = text;
        this.command = command;
        this.date = System.currentTimeMillis();
        this.isAutocompleted = isAutocompleted?1:0;

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


    public boolean isAutocompleted() {
        return isAutocompleted == 1;
    }


}
