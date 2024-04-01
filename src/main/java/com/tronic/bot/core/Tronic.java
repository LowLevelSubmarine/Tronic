package com.tronic.bot.core;

import com.tronic.logger.Loggy;
import io.sentry.Sentry;

import javax.security.auth.login.LoginException;

public class Tronic {

    private final ConfigProvider configProvider;
    private Core core;
    private final Updater updater = new Updater(this);

    public Tronic(ConfigProvider configProvider) {
        Sentry.init(options -> {
            options.setDsn((configProvider.getSentryDsn() != null)? configProvider.getSentryDsn(): "");
            // Set tracesSampleRate to 1.0 to capture 100% of transactions for performance monitoring.
            // We recommend adjusting this value in production.
            options.setTracesSampleRate(1.0);
        });
        Loggy.quickStart();
        if (configProvider.getLogReceivers() != null) {
            Loggy.addReceivers(configProvider.getLogReceivers());
        }
        this.configProvider = configProvider;
        start();
    }

    public Updater getUpdater() {
        return this.updater;
    }

    public void restart() {
        shutdown(true, false);
        start();
    }

    public void shutdown() {
        shutdown(false, true);
    }

    public void shutdown(boolean restart, boolean exit) {
        this.core.prepareShutdown(restart);
        this.core = null;
        if (exit) System.exit(0);
    }

    private void start() {
        try {
            this.core = new Core(this);
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ConfigProvider getConfigProvider() {
        return this.configProvider;
    }

}
