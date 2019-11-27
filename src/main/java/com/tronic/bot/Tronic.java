package com.tronic.bot;

import com.tronic.bot.buttons.ButtonHandler;
import com.tronic.bot.commands.CommandHandler;
import com.tronic.bot.commands.administration.BroadcastCommand;
import com.tronic.bot.commands.administration.ShutdownCommand;
import com.tronic.bot.commands.administration.SpeedtestCommand;
import com.tronic.bot.commands.fun.DiceCommand;
import com.tronic.bot.commands.fun.SayCommand;
import com.tronic.bot.commands.info.PingCommand;
import com.tronic.bot.commands.music.PauseCommand;
import com.tronic.bot.commands.music.PlayCommand;
import com.tronic.bot.commands.music.SkipCommand;
import com.tronic.bot.listeners.ButtonListener;
import com.tronic.bot.listeners.CommandListener;
import com.tronic.bot.listeners.MessageLoggerListener;
import com.tronic.bot.music.PlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Tronic {

    private final Listeners listeners = new Listeners();
    private final JDA jda;
    private final CommandHandler commandHandler = new CommandHandler(this);
    private final ButtonHandler buttonHandler = new ButtonHandler(this);
    private final PlayerManager playerManager = new PlayerManager();

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

    public ButtonHandler getButtonHandler() {
        return this.buttonHandler;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
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
        //Administration
        this.commandHandler.addCommand(new BroadcastCommand());
        this.commandHandler.addCommand(new ShutdownCommand());
        this.commandHandler.addCommand(new SpeedtestCommand());
        //Fun
        this.commandHandler.addCommand(new DiceCommand());
        this.commandHandler.addCommand(new SayCommand());
        //Info
        this.commandHandler.addCommand(new PingCommand());
        //Music
        this.commandHandler.addCommand(new PauseCommand());
        this.commandHandler.addCommand(new PlayCommand());
        this.commandHandler.addCommand(new SkipCommand());
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
