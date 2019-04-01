package core;

import core.command_system.CmdHandler;
import core.listeners.MessageReceivedListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Core {

    private final JDA jda;

    private final ListenerAdapter[] allJDAListeners = {
        new MessageReceivedListener()
    };

    public Core(String token) throws LoginException, InterruptedException {
        JDABuilder builder = new JDABuilder();
        builder.setToken(token);
        builder.setEnableShutdownHook(true); //TODO: turn off
        builder.setAutoReconnect(true);
        builder.setAudioEnabled(true);
        builder.addEventListener(this.allJDAListeners);
        jda = builder.build();
        jda.awaitReady();
        System.out.println("Successfully logged in as: " + jda.getSelfUser().getName());
    }

    public JDA getJDA() {
        return this.jda;
    }

    public void shutdown() {
        this.jda.shutdown();

    }

}
