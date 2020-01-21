package com.tronic.bot;

import com.google.gson.Gson;
import com.tronic.bot.core.ConfigProvider;
import com.tronic.bot.core.Tronic;
import com.tronic.bot.tools.BotConfig;
import net.tetraowl.watcher.toolbox.JavaTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.*;

public class ReleaseConfig implements ConfigProvider {

    public static void main(String[] args) {
        new Tronic(new ReleaseConfig());
    }
    final static File CONF_FILE = new File(JavaTools.getJarUrl(ReleaseConfig.class)+"/config.json");
    final static String DEFAULT_TOKEN = "Bot Token";
    final static String DEFAULT_HOST_ID = "HostId";
    Gson gson = new Gson();
    public ReleaseConfig() {
        LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        File file = new File(JavaTools.getJarUrl(Tronic.class)+"/log4j2.xml");
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
        File file;
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
        File file;
        try {
            FileReader fr = new FileReader(CONF_FILE);
            BotConfig bc = gson.fromJson(fr,BotConfig.class);
            return bc.getHost();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
