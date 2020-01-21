package com.tronic.bot.core;

import net.tetraowl.watcher.toolbox.JavaTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import javax.security.auth.login.LoginException;
import java.io.File;


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
