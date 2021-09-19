package com.tronic.bot;

import com.google.gson.Gson;
import com.lowlevelsubmarine.envelope.util.FileBrowser;
import com.tronic.bot.core.ConfigProvider;
import com.tronic.bot.core.Tronic;
import com.tronic.bot.statics.Files;
import com.tronic.bot.tools.BotConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.*;

public class ReleaseConfig implements ConfigProvider {

    private static final File CONF_FILE = new File(Files.ROOT_FOLDER,"config.json");
    private static final String DEFAULT_TOKEN = "Bot Token";
    private static final String DEFAULT_HOST_ID = "HostId";

    public static void main(String[] args) {
        new Tronic(new ReleaseConfig());
    }

    private final Gson gson = new Gson();

    public ReleaseConfig() {
        LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        File file = new File(FileBrowser.getJARFile().getFile().getAbsolutePath(), "log4j2.xml");
        context.setConfigLocation(file.toURI());
        try {
            FileReader fr = new FileReader(CONF_FILE);
            BotConfig bc = gson.fromJson(fr,BotConfig.class);
            if (bc.getHost().equals(DEFAULT_HOST_ID)||bc.getToken().equals(DEFAULT_TOKEN)) {
                System.out.println("Please fill out the config.json file!");
                System.exit(0);
            }
        } catch (FileNotFoundException e) {
            BotConfig bc = new BotConfig();
            bc.setToken(DEFAULT_TOKEN);
            bc.setHost(DEFAULT_HOST_ID);
            try {
                FileWriter fw = new FileWriter(CONF_FILE);
                gson.toJson(bc,fw);
                fw.close();
                System.out.println("Please fill out the config.json file!");
                System.exit(0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public String getToken() {
        try {
            FileReader fr = new FileReader(CONF_FILE);
            BotConfig bc = gson.fromJson(fr,BotConfig.class);
            return bc.getToken();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean getDebugMode() {
        return false;
    }

    @Override
    public String getHost() {
        try {
            FileReader fr = new FileReader(CONF_FILE);
            BotConfig bc = gson.fromJson(fr, BotConfig.class);
            return bc.getHost();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getSpotifyClientId() {
        try {
            FileReader fr = new FileReader(CONF_FILE);
            BotConfig bc = gson.fromJson(fr, BotConfig.class);
            return bc.getSpotifyClientId();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getSpotifyClientSecret() {
        try {
            FileReader fr = new FileReader(CONF_FILE);
            BotConfig bc = gson.fromJson(fr, BotConfig.class);
            return bc.getSpotifyClientSecret();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean getActivateApi() {
        try {
            FileReader fr = new FileReader(CONF_FILE);
            BotConfig bc = gson.fromJson(fr,BotConfig.class);
            return bc.getActivateApi();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
