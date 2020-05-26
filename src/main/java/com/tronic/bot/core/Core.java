package com.tronic.bot.core;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import com.tronic.bot.buttons.ButtonHandler;
import com.tronic.bot.commands.CommandHandler;
import com.tronic.bot.commands.administration.*;
import com.tronic.bot.commands.fun.DiceCommand;
import com.tronic.bot.commands.fun.SayCommand;
import com.tronic.bot.commands.info.*;
import com.tronic.bot.commands.music.PauseCommand;
import com.tronic.bot.commands.music.PlayCommand;
import com.tronic.bot.commands.music.SearchCommand;
import com.tronic.bot.commands.music.SkipCommand;
import com.tronic.bot.commands.settings.*;
import com.tronic.bot.hyperchannel.HyperchannelManager;
import com.tronic.bot.listeners.*;
import com.tronic.bot.music.PlayerManager;
import com.tronic.bot.music_new.MusicManager;
import com.tronic.bot.statics.Presets;
import com.tronic.bot.storage.Storage;
import com.tronic.bot.tools.ColorisedSout;
import com.tronic.updater.Updater;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.tetraowl.watcher.toolbox.JavaTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;

public class Core {

    private final Tronic tronic;
    private final Listeners listeners = new Listeners();
    private final JDA jda;
    private final Storage storage = new Storage();
    private final CommandHandler commandHandler = new CommandHandler(this);
    private final ButtonHandler buttonHandler = new ButtonHandler(this);
    private final com.tronic.bot.buttons_new.ButtonHandler newButtonHandler = new com.tronic.bot.buttons_new.ButtonHandler();
    private final PlayerManager playerManager = new PlayerManager(this);
    private final MusicManager musicManager = new MusicManager(this);
    private final String hostToken;
    Logger logger = LogManager.getLogger(Tronic.class);
    private HyperchannelManager hyperchannelManager;


    public Core(Tronic tronic) throws LoginException {
        this.tronic = tronic;
        this.hostToken = tronic.getConfigProvider().getHost();
        this.jda = buildJDA(tronic.getConfigProvider().getToken());
        try {
            this.jda.awaitReady();
            addCommands();
            hyperchannelManager = new HyperchannelManager(this);
            Updater.initialJson();
            Updater.initialError();
            this.jda.getPresence().setActivity(Activity.playing(Presets.PREFIX +"help"));
            System.out.println(ColorisedSout.ANSI_GREEN+"Bot started!"+ColorisedSout.ANSI_RESET);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void restart() {
        this.tronic.restart();
    }

    public boolean getDebugMode() {
        return this.tronic.getConfigProvider().getDebugMode();
    }

    public String getHostToken() {
        return hostToken;
    }

    public JDA getJDA() {
        return this.jda;
    }

    public void shutdown() {
        this.jda.shutdown();
        System.out.println(ColorisedSout.ANSI_GREEN+"Bot shutdowned!"+ColorisedSout.ANSI_RESET);
        System.exit(0);
    }

    public Storage getStorage() {
        return this.storage;
    }

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public ButtonHandler getButtonManager() {
        return this.buttonHandler;
    }

    public com.tronic.bot.buttons_new.ButtonHandler getNewButtonHandler() {
        return this.newButtonHandler;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public MusicManager getMusicManager() {
        return this.musicManager;
    }

    public Listeners getListeners() {
        return this.listeners;
    }

    public HyperchannelManager getHyperchannelManager() {
        return hyperchannelManager;
    }

    private JDA buildJDA(String token) throws LoginException {
        JDABuilder builder = new JDABuilder();
        builder.setToken(token);
        builder.setAudioSendFactory(new NativeAudioSendFactory());
        this.listeners.addAll(builder);
        return builder.build();
    }

    private void addCommands() {
        //Administration
        this.commandHandler.addCommand(new BroadcastCommand());
        this.commandHandler.addCommand(new RangeReportCommand());
        this.commandHandler.addCommand(new RestartCommand());
        this.commandHandler.addCommand(new ShutdownCommand());
        this.commandHandler.addCommand(new SpeedtestCommand());
        this.commandHandler.addCommand(new UpdateCommand());
        this.commandHandler.addCommand(new SetCoHosterCommand());
        //Fun
        this.commandHandler.addCommand(new DiceCommand());
        this.commandHandler.addCommand(new SayCommand());
        //Info
        this.commandHandler.addCommand(new PingCommand());
        this.commandHandler.addCommand(new StatisticsCommand());
        this.commandHandler.addCommand(new UptimeCommand());
        this.commandHandler.addCommand(new InfoCommand());
        this.commandHandler.addCommand(new NerdInfoCommands());
        this.commandHandler.addCommand(new HelpCommand());
        this.commandHandler.addCommand(new InviteCommand());
        this.commandHandler.addCommand(new WhoAmICommand());
        //Music
        this.commandHandler.addCommand(new PauseCommand());
        this.commandHandler.addCommand(new PlayCommand());
        this.commandHandler.addCommand(new SearchCommand());
        this.commandHandler.addCommand(new SkipCommand());
        //Settings
        this.commandHandler.addCommand(new SetPrefixCommand());
        this.commandHandler.addCommand(new HyperchannelCommand());
        this.commandHandler.addCommand(new BotVolumeCommand());
        this.commandHandler.addCommand(new RemoveCoHosterCommand());
        //Debugging
        this.commandHandler.addCommand(new SandboxCommand());
    }

    public class Listeners {

        public final ButtonListener button = new ButtonListener(Core.this);
        public final CommandListener command = new CommandListener(Core.this);
        public final MessageLoggerListener messageLogger = new MessageLoggerListener(Core.this);
        public final ReadyListener startup = new ReadyListener(Core.this);

        public void addAll(JDABuilder builder) {
            builder.addEventListeners(this.button);
            builder.addEventListeners(this.command);
            builder.addEventListeners(new ExperimentStartupListener(Core.this));
            builder.addEventListeners(this.messageLogger);
            builder.addEventListeners(this.startup);
            builder.addEventListeners(new JoinListener(Core.this));
            builder.addEventListeners(new LeaveListener(Core.this));
            builder.addEventListeners(new MoveListener(Core.this));
        }

    }

}
