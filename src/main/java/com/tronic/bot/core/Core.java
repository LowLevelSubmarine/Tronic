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
import com.tronic.bot.listeners.*;
import com.tronic.bot.music.MusicManager;
import com.tronic.bot.questions.QuestionHandler;
import com.tronic.bot.statics.Presets;
import com.tronic.bot.storage.Storage;
import com.tronic.bot.tools.JDAUtils;
import com.tronic.logger.Color;
import com.tronic.logger.Loggy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.LinkedList;

public class Core {
    private final Tronic tronic;
    private final LinkedList<ShutdownHook> shutdownHooks = new LinkedList<>();
    private final LinkedList<BootupHook> bootupHooks = new LinkedList<>();
    private final Listeners listeners = new Listeners();
    private final JDA jda;
    private final Storage storage = new Storage();
    private final CommandHandler commandHandler = new CommandHandler(this);
    private final ButtonHandler buttonHandler = new ButtonHandler();
    private final MusicManager musicManager;
    private final QuestionHandler questionHandler = new QuestionHandler(this);
    private final HyperchannelManager hyperchannelManager;
    private final String hostToken;

    public Core(Tronic tronic) throws LoginException, InterruptedException {
        Loggy.logI(Color.GREEN.tint("Tronic booting ..."));
        this.tronic = tronic;
        this.hostToken = tronic.getConfigProvider().getHost();
        this.jda = buildJDA(tronic.getConfigProvider().getToken());
        this.jda.awaitReady();
        Loggy.logI("Logged in as " + JDAUtils.getFullName(this.jda.getSelfUser()));
        addCommands();
        this.musicManager = new MusicManager(this);
        this.hyperchannelManager = new HyperchannelManager(this);
        this.jda.getPresence().setActivity(Activity.playing(Presets.PREFIX + "help"));
        bootupHooks.forEach(BootupHook::onBootup);
        Loggy.logI(Color.GREEN.tint("Tronic ready! Running version " + tronic.getUpdater().getCurrentVersion() + "."));
    }

    public void addShutdownHook(ShutdownHook hook) {
        this.shutdownHooks.add(hook);
    }

    public void removeShutdownHook(ShutdownHook hook) {
        this.shutdownHooks.remove(hook);
    }

    public void addBootupHook(BootupHook hook) {
        this.bootupHooks.add(hook);
    }

    public void removeBootupHook(BootupHook hook) {
        this.bootupHooks.remove(hook);
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
        Loggy.logI(Color.GREEN.tint("Tronic shutting down ..."));
        shutdownHooks.forEach((shutdownHook -> shutdownHook.onShutdown(restart)));
        this.jda.shutdown();
        Loggy.logI(Color.GREEN.tint("Tronic shutdown successfull!"));
    }

    public Storage getStorage() {
        return this.storage;
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
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
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
        this.commandHandler.addCommand(new BotVolumeCommand());
        this.commandHandler.addCommand(new HyperchannelCommand());
        this.commandHandler.addCommand(new SetBroadcastsCommand());
        this.commandHandler.addCommand(new SetPrefixCommand());
    }

    public class Listeners {

        public final ButtonListener button = new ButtonListener(Core.this);
        public final CommandListener command = new CommandListener(Core.this);
        public final MessageLoggerListener messageLogger = new MessageLoggerListener(Core.this);
        public final QuestionListener question = new QuestionListener(Core.this);
        public final QueueItemListener queueItem = new QueueItemListener(Core.this);
        public final ReadyListener ready = new ReadyListener(Core.this);
        public final VoiceUpdateListener voiceUpdate = new VoiceUpdateListener(Core.this);

        public void attachToBuilder(JDABuilder builder) {
            builder.addEventListeners(this.button);
            builder.addEventListeners(this.command);
            builder.addEventListeners(this.messageLogger);
            builder.addEventListeners(this.question);
            builder.addEventListeners(this.queueItem);
            builder.addEventListeners(this.ready);
            builder.addEventListeners(this.voiceUpdate);
        }

    }

    public interface ShutdownHook {
        void onShutdown(boolean restart);
    }

    public interface BootupHook {
        void onBootup();
    }

}
