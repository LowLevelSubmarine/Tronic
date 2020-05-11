package com.tronic.bot.tools;

import com.google.gson.annotations.SerializedName;

public class BotConfig {
    private String token;
    private String host;

    public String getToken() {
        return token;
    }

    public String getHost() {
        return host;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
