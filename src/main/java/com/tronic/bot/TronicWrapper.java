package com.tronic.bot;

import javax.security.auth.login.LoginException;

public class TronicWrapper {

    private Tronic tronic;

    public static void main(String[] args) {
        new TronicWrapper();
    }

    private TronicWrapper() {
        start();
    }

    public void restart() {
        shutdown();
        start();
    }

    private void start() {
        try {
            this.tronic = new Tronic("");
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private void shutdown() {
        this.tronic.shutdown();
    }

}
