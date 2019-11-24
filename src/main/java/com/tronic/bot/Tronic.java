package com.tronic.bot;

import com.tronic.bot.commands.CommandHandler;
import com.tronic.bot.commands.administration.SpeedtestCommand;
import com.tronic.bot.commands.fun.DiceCommand;
import com.tronic.bot.commands.fun.SayCommand;
import com.tronic.bot.commands.info.PingCommand;
import com.tronic.bot.listeners.ButtonListener;
import com.tronic.bot.listeners.CommandListener;
import com.tronic.bot.listeners.MessageLoggerListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Tronic {

    private final Listeners listeners = new Listeners();
    private final JDA jda;
    private final CommandHandler commandHandler = new CommandHandler(this);

    public Tronic(String token) throws LoginException {
        this.jda = buildJDA(token);
        try {
            this.jda.awaitReady();
            addCommands();
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

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
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

    private void addCommands() {
        this.commandHandler.addCommand(new DiceCommand());
        this.commandHandler.addCommand(new SayCommand());
        this.commandHandler.addCommand(new PingCommand());
        this.commandHandler.addCommand(new SpeedtestCommand());
    }

    public class Listeners {

        public final ButtonListener button = new ButtonListener(Tronic.this);
        public final CommandListener command = new CommandListener(Tronic.this);
        public final MessageLoggerListener messageLogger = new MessageLoggerListener(Tronic.this);

        public void addAll(JDABuilder builder) {
            builder.addEventListeners(this.button);
            builder.addEventListeners(this.command);
            builder.addEventListeners(this.messageLogger);
        }

    }

}
