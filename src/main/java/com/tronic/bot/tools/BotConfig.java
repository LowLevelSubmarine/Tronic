package com.tronic.bot.tools;

public class BotConfig {

    private String token;
    private String host;
    private boolean activateApi = false;

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

    public boolean getActivateApi() {
        return activateApi;
    }

}
