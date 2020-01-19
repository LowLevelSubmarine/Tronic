package com.tronic.bot.core;

import javax.security.auth.login.LoginException;

public class Tronic {

    private final ConfigProvider configProvider;
    private Core core;

    public Tronic(ConfigProvider configProvider) {
        this.configProvider = configProvider;
        start();
    }

    public void restart() {
        shutdown();
        start();
    }

    private void start() {
        try {
            this.core = new Core(this.configProvider);
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private void shutdown() {
        this.core.shutdown();
    }

}
