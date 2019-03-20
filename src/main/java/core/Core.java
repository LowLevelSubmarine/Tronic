package core;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class Core {

    private final JDA jda;

    public Core(String token) throws LoginException, InterruptedException {
        JDABuilder builder = new JDABuilder();
        builder.setToken(token);
        builder.setEnableShutdownHook(true); //TODO: turn off
        builder.setAutoReconnect(true);
        builder.setAudioEnabled(true);
        jda = builder.build();
        jda.awaitReady();
        System.out.println("Successfully logged in as: " + jda.getSelfUser().getName());
    }

    public void shutdown() {
        this.jda.shutdown();
    }

}
