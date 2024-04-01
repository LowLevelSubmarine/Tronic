package com.tronic.bot.core;

import com.tronic.logger.receiver.Receiver;

import java.util.Collection;

public interface ConfigProvider {

    String getToken();
    boolean getDebugMode();
    String getHost();
    String getSpotifyClientId();
    String getSpotifyClientSecret();
    Collection<Receiver> getLogReceivers();
    boolean getActivateApi();
    String getSentryDsn();
}
