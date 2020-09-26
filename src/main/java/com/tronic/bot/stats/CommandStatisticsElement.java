package com.tronic.bot.stats;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

import java.io.Serializable;

public class CommandStatisticsElement implements Serializable {
    public String command;
    private long date;
    private String userId;
    private boolean isAutocompleted;


    public CommandStatisticsElement () {}
    public CommandStatisticsElement(String arguments, String userId, boolean isAutocompleted) {
        this.command = arguments;
        this.date = System.currentTimeMillis();
        this.userId = userId;
        this.isAutocompleted = isAutocompleted;

    }

    public String getCommands() {
        return command;
    }

    public long getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }
    public User getUser(JDA jda) {
        return jda.getUserById(this.userId);
    }

    public boolean isAutocompleted() {
        return isAutocompleted;
    }


}
