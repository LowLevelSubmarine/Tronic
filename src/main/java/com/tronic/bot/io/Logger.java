package com.tronic.bot.io;

import org.apache.logging.log4j.LogManager;

public class Logger {

    public static void log(Object caller, String message) {
        org.apache.logging.log4j.Logger logger = LogManager.getLogger(caller.getClass());
        logger.info(message);
    }

    public static void log(Object caller, String message, Exception e) {
        log(caller, message + " (Caused by: " + e.getMessage() + ")");
    }

}
