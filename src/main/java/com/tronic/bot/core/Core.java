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
import com.tronic.bot.music.MusicManager;
import com.tronic.bot.rest.JWTStore;
import com.tronic.bot.rest.RestServer;
import com.tronic.bot.questions.QuestionHandler;
import com.tronic.bot.statics.Presets;
import com.tronic.bot.storage.Storage;
import com.tronic.bot.tools.ColorisedSout;
import com.tronic.updater.Updater;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

public class Core {

    private final Tronic tronic;
    private final Listeners listeners = new Listeners();
    private final JDA jda;
    private final Storage storage = new Storage();
    private final CommandHandler commandHandler = new CommandHandler(this);
    private final ButtonHandler buttonHandler = new ButtonHandler();
    private final MusicManager musicManager = new MusicManager(this);
    private final QuestionHandler questionHandler = new QuestionHandler(this);
    private final String hostToken;
    private RestServer restServer;
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
            if (tronic.getConfigProvider().getActivateApi()) {
                this.restServer = new RestServer(this);
            }
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

    public ConfigProvider getConfig() {
        return this.tronic.getConfigProvider();
    }

    public void shutdown() {
        this.jda.shutdown();
        System.out.println(ColorisedSout.ANSI_GREEN+"Bot shutdowned!"+ColorisedSout.ANSI_RESET);
        System.exit(0);
    }

    public RestServer getRestServer() {
        return this.restServer;
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

    public com.tronic.bot.buttons.ButtonHandler getButtonHandler() {
        return this.buttonHandler;
    }

    public MusicManager getMusicManager() {
        return this.musicManager;
    }

    public QuestionHandler getQuestionHandler() {
        return this.questionHandler;
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
        public final ExperimentStartupListener experimentStartup = new ExperimentStartupListener(Core.this);
        public final JoinListener join = new JoinListener(Core.this);
        public final LeaveListener leave = new LeaveListener(Core.this);
        public final MessageLoggerListener messageLogger = new MessageLoggerListener(Core.this);
        public final MoveListener move = new MoveListener(Core.this);
        public final ReadyListener ready = new ReadyListener(Core.this);
        public final QuestionListener question = new QuestionListener(Core.this);

        public void addAll(JDABuilder builder) {
            builder.addEventListeners(this.button);
            builder.addEventListeners(this.command);
            builder.addEventListeners(this.experimentStartup);
            builder.addEventListeners(this.join);
            builder.addEventListeners(this.leave);
            builder.addEventListeners(this.messageLogger);
            builder.addEventListeners(this.move);
            builder.addEventListeners(this.ready);
            builder.addEventListeners(this.question);
        }

    }

}
