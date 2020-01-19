package com.tronic.bot.io;

import org.apache.logging.log4j.LogManager;

import java.util.logging.Level;

public class Logger {

    public static void log(Object caller, String message) {
        org.apache.logging.log4j.Logger logger = LogManager.getLogger(caller.getClass());
        logger.info(message);
        //System.out.println("[" + caller.getClass().getSimpleName() + "] " + message);
    }

}
