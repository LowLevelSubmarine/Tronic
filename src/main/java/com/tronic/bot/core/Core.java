package com.tronic.bot.core;

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
import com.tronic.bot.interaction_commands.ICommandManager;
import com.tronic.bot.listeners.*;
import com.tronic.bot.music.MusicManager;
import com.tronic.bot.questions.QuestionHandler;
import com.tronic.bot.statics.Presets;
import com.tronic.bot.storage.Storage;
import com.tronic.bot.tools.ColorisedSout;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.LinkedList;

public class Core {

    private final Tronic tronic;
    private final LinkedList<ShutdownHook> shutdownHooks = new LinkedList<>();
    private final LinkedList<BootUpHook> bootUpHooks = new LinkedList<>();
    private final Listeners listeners = new Listeners();
    private final JDA jda;
    private final Storage storage = new Storage();
    private final ICommandManager iCommandManager = new ICommandManager(this);
    private final CommandHandler commandHandler = new CommandHandler(this);
    private final ButtonHandler buttonHandler = new ButtonHandler();
    private final MusicManager musicManager = new MusicManager(this);
    private final QuestionHandler questionHandler = new QuestionHandler(this);
    private final HyperchannelManager hyperchannelManager;
    private final String hostToken;


    public Core(Tronic tronic) throws LoginException, InterruptedException {
        this.tronic = tronic;
        this.hostToken = tronic.getConfigProvider().getHost();
        this.jda = buildJDA(tronic.getConfigProvider().getToken());
        this.jda.awaitReady();
        addCommands();
        hyperchannelManager = new HyperchannelManager(this);
        this.jda.getPresence().setActivity(Activity.playing(Presets.PREFIX +"help"));
        System.out.println(ColorisedSout.ANSI_GREEN+"Bot started!" + ColorisedSout.ANSI_RESET);
        bootUpHooks.forEach(BootUpHook::onBootUp);
    }

    public void addBootUpHook(BootUpHook hook) {
        this.bootUpHooks.add(hook);
    }

    public void removeBootUpHooks(BootUpHook hook) {
        this.bootUpHooks.remove(hook);
    }

    public void addShutdownHook(ShutdownHook hook) {
        this.shutdownHooks.add(hook);
    }

    public void removeShutdownHook(ShutdownHook hook) {
        this.shutdownHooks.remove(hook);
    }

    public void restartTronic() {
        this.tronic.restart();
    }

    public void shutdownTronic() {
        this.tronic.shutdown();
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

    public void prepareShutdown(boolean restart) {
        for (ShutdownHook hook : this.shutdownHooks) {
            hook.onShutdown(restart);
        }
        this.jda.shutdown();
        System.out.println(ColorisedSout.ANSI_GREEN + "Bot shutdown successfully!" + ColorisedSout.ANSI_RESET);
    }

    public Storage getStorage() {
        return this.storage;
    }

    public ICommandManager getICommandManager() {
        return this.iCommandManager;
    }

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public ButtonHandler getButtonHandler() {
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

    public Updater getTronicUpdater() {
        return this.tronic.getUpdater();
    }

    private JDA buildJDA(String token) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        this.listeners.attachToBuilder(builder);
        return builder.build();
    }

    private void addCommands() {
        //Administration
        this.commandHandler.addCommand(new BroadcastCommand());
        this.commandHandler.addCommand(new CoHosterCommand());
        this.commandHandler.addCommand(new RangeReportCommand());
        this.commandHandler.addCommand(new RestartCommand());
        this.commandHandler.addCommand(new ServerInfoCommand());
        this.commandHandler.addCommand(new ShortcutCommand());
        this.commandHandler.addCommand(new ShutdownCommand());
        this.commandHandler.addCommand(new SpeedTestCommand());
        this.commandHandler.addCommand(new UpdateCommand());
        //Fun
        this.commandHandler.addCommand(new DiceCommand());
        this.commandHandler.addCommand(new SayCommand());
        //Info
        this.commandHandler.addCommand(new PingCommand());
        this.commandHandler.addCommand(new StatisticsCommand());
        this.commandHandler.addCommand(new UptimeCommand());
        this.commandHandler.addCommand(new InfoCommand());
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

        public void attachToBuilder(JDABuilder builder) {
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

    public interface ShutdownHook {
        void onShutdown(boolean restart);
    }

    public interface BootUpHook {
        void onBootUp();
    }

}
