package com.tronic.bot.core;

public interface ConfigProvider {

    String getToken();
    boolean getDebugMode();
    String getHost();
    boolean getActivateApi();
    boolean isOriginal();

}
