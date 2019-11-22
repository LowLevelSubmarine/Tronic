package com.tronic.bot;

import com.tronic.bot.listeners.ButtonListener;
import com.tronic.bot.listeners.CommandListener;
import com.tronic.bot.listeners.MessageLoggerListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Tronic {

    private final Listeners listeners = new Listeners();
    private final JDA jda;

    public Tronic(String token) throws LoginException {
        this.jda = buildJDA(token);
        try {
            this.jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public JDA getJDA() {
        return this.jda;
    }

    public void shutdown() {
        this.jda.shutdown();
    }

    public Listeners getListeners() {
        return this.listeners;
    }

    private JDA buildJDA(String token) throws LoginException {
        JDABuilder builder = new JDABuilder();
        builder.setToken(token);
        this.listeners.addAll(builder);
        return builder.build();
    }

    public static class Listeners {

        public final ButtonListener button = new ButtonListener();
        public final CommandListener command = new CommandListener();
        public final MessageLoggerListener messageLogger = new MessageLoggerListener();

        public void addAll(JDABuilder builder) {
            builder.addEventListeners(this.button);
            builder.addEventListeners(this.command);
            builder.addEventListeners(this.messageLogger);
        }

    }

}
