package com.tronic.bot;

import com.google.gson.JsonParseException;
import com.tronic.bot.core.ConfigProvider;
import com.tronic.bot.core.Tronic;
import com.tronic.bot.statics.Files;
import com.tronic.bot.tools.GsonUtils;
import com.tronic.logger.Level;
import com.tronic.logger.receiver.FileReceiver;
import com.tronic.logger.receiver.Receiver;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;

public class ReleaseConfig implements ConfigProvider {

    private static final File CONF_FILE = new File(Files.ROOT_FOLDER,"config.json");
    private static final File LOG_FILE = new File(Files.ROOT_FOLDER, "tronic.log");

    private class DTO implements Serializable {
        DiscordAuthentication discordAuthentication = new DiscordAuthentication();
        private class DiscordAuthentication {
            String token = "Discord Bot Token";
        }
        SpotifyAuthentication spotifyAuthentication = new SpotifyAuthentication();
        private class SpotifyAuthentication {
            String clientId = "Spotify Client Id";
            String clientSecret = "Spotify Client Secret";
        }
        String hostId = "The User Id of Tronics hoster";
        boolean debug = false;
        boolean activateApi = false;
    }

    private static class ConfigException extends RuntimeException {}

    public static void main(String[] args) {
        try {
            new Tronic(new ReleaseConfig());
        } catch (ConfigException e) {
            System.out.println("Stopped Tronic bootup due to config file problems");
        }
    }

    private final DTO dto;

    public ReleaseConfig() throws ConfigException {
        try {
            System.out.println(CONF_FILE.getAbsolutePath());
            this.dto = GsonUtils.INSTANCE.fromJson(new FileReader(CONF_FILE), DTO.class);
        } catch (FileNotFoundException e) {
            String fileName = CONF_FILE.getName();
            try {
                FileWriter writer = new FileWriter(CONF_FILE);
                GsonUtils.PRETTY_PRINT_INSTANCE.toJson(new DTO(), writer);
                writer.close();
                System.out.println("Before running Tronic, you need to enter your Discord Authentication values inside the " + fileName + ".");
            } catch (IOException f) {
                System.out.println("Something went wrong while creating " + fileName + "! Check the file system permissions.");
            }
            throw new ConfigException();
        } catch (JsonParseException e) {
            String fileName = CONF_FILE.getName();
            System.out.println("Something went wrong while loading " + fileName + ".\n" +
                    "To generate a new " + fileName + ", just rename or delete the current one.");
            throw new ConfigException();
        }
    }

    @Override
    public String getToken() {
        return this.dto.discordAuthentication.token;
    }

    @Override
    public boolean getDebugMode() {
        return this.dto.debug;
    }

    @Override
    public String getHost() {
        return this.dto.hostId;
    }

    @Override
    public String getSpotifyClientId() {
        return this.dto.spotifyAuthentication.clientId;
    }

    @Override
    public String getSpotifyClientSecret() {
        return this.dto.spotifyAuthentication.clientSecret;
    }

    @Override
    public Collection<Receiver> getLogReceivers() {
        Collection<Receiver> receivers = new LinkedList<>();
        receivers.add(new FileReceiver(Level.TRACE, LOG_FILE));
        return receivers;
    }

    @Override
    public boolean getActivateApi() {
        return this.dto.activateApi;
    }

}
