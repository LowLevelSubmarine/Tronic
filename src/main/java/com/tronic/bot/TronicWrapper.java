package com.tronic.bot;

import javax.security.auth.login.LoginException;

public class TronicWrapper {

    private static Tronic tronic;

    public static void main(String[] args) {
        try {
            tronic = new Tronic("");
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static void restart() {
        try {
            tronic.shutdown();
            tronic = new Tronic("");
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

}
