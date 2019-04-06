package core;

import core.config.TronicConfig;
import core.listeners.MessageReceivedListener;
import core.storage.Storage;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Core {

    private final JDA jda;
    private final Storage storage = new Storage();
    private final ListenerAdapter[] allJDAListeners = {
        new MessageReceivedListener(this)
    };
    private final TronicConfig config;

    public Core(String token, TronicConfig config) throws LoginException, InterruptedException {
        this.config = config;
        JDABuilder builder = new JDABuilder();
        builder.setToken(token);
        builder.setEnableShutdownHook(true); //TODO: turn off
        builder.setAutoReconnect(true);
        builder.setAudioEnabled(true);
        builder.addEventListener(this.allJDAListeners);
        builder.setGame(Game.playing(this.storage.getBot().getGame()));
        jda = builder.build();
        jda.awaitReady();
        System.out.println("Successfully logged in as: " + jda.getSelfUser().getName());
        onConnect();
    }

    public void shutdown() {
        onShutdown();
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

    private void onConnect() {

    }

    private void onShutdown() {

    }

}
