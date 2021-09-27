package com.tronic.bot.core;

public interface ConfigProvider {

    String getToken();
    boolean getDebugMode();
    String getHost();
    String getSpotifyClientId();
    String getSpotifyClientSecret();
    boolean getActivateApi();

}
