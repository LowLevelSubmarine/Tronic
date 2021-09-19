package com.tronic.bot.tools;

public class BotConfig {

    private String token;
    private String host;
    private String spotifyClientId;
    private String spotifyClientSecret;
    private boolean activateApi = false;
    private boolean isOriginal = false;

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

    public String getSpotifyClientId() {
        return this.spotifyClientId;
    }

    public String getSpotifyClientSecret() {
        return this.spotifyClientSecret;
    }

    public void setActivateApi(boolean activateApi) {
        this.activateApi = activateApi;
    }

    public boolean isOriginal() {
        return this.isOriginal;
    }

    public void setOriginal(boolean activateApi) {
        this.isOriginal = activateApi;
    }

}
