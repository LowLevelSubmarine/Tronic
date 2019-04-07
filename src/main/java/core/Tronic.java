package core;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import core.config.TronicConfig;
import core.listeners.MessageReceivedListener;
import core.music.GuildPlayerManager;
import core.storage.Storage;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Tronic {

    private final JDA jda;
    private final Storage storage = new Storage();
    private final ListenerAdapter[] allJDAListeners = {
        new MessageReceivedListener(this)
    };
    private final TronicConfig config;
    private final GuildPlayerManager playerManager;

    public Tronic(String token, TronicConfig config) throws LoginException, InterruptedException {
        this.config = config;
        this.playerManager = new GuildPlayerManager();
        JDABuilder builder = new JDABuilder();
        builder.setToken(token);
        builder.setEnableShutdownHook(false);
        builder.setAutoReconnect(true);
        builder.setAudioEnabled(true);
        builder.setAudioSendFactory(new NativeAudioSendFactory());
        builder.addEventListener(this.allJDAListeners);
        builder.setGame(Game.playing(this.storage.getBot().getGame()));
        jda = builder.build();
        jda.awaitReady();
        System.out.println("Successfully logged in as: " + jda.getSelfUser().getName());
    }

    public void shutdown() {
        this.jda.getPresence().setStatus(OnlineStatus.INVISIBLE);
        this.jda.shutdown();
    }

    public void setGame(String game) {
        this.jda.getPresence().setGame(Game.playing(game));
    }

    public Storage getStorage() {
        return this.storage;
    }

    public TronicConfig getConfig() {
        return this.config;
    }

    public GuildPlayerManager getPlayerManager() {
        return this.playerManager;
    }

}
