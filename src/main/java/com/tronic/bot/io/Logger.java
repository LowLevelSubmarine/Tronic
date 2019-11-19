package com.tronic.bot.io;

public class Logger {

    public static void log(Object caller, String message) {
        System.out.println("[" + caller.getClass().getSimpleName() + "] " + message);
    }

}
