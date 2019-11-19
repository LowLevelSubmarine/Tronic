package com.tronic.bot;

import com.tronic.bot.listeners.CommandListener;
import com.tronic.bot.listeners.MessageLoggerListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Tronic {

    private final JDA jda;

    public Tronic(String token) throws LoginException {
        this.jda = buildJDA(token);
        try {
            this.jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {

    }

    private JDA buildJDA(String token) throws LoginException {
        JDABuilder builder = new JDABuilder();
        builder.setToken(token);
        addListeners(builder);
        return builder.build();
    }

    private void addListeners(JDABuilder builder) {
        builder.addEventListeners(new MessageLoggerListener());
        builder.addEventListeners(new CommandListener());
    }

}
