package com.tronic.updater;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tronic.bot.Tronic;
import com.tronic.bot.tools.FileUtils;
import net.tetraowl.watcher.toolbox.JavaTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toolbox.os.OSTools;
import toolbox.process.OSnotDetectedException;
import updater.UpdaterSettings;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Updater {
    private String USER = "LowLevelSubmarine";
    private String REPO = "Tronic";
    Logger logger = LogManager.getLogger(Updater.class);
    public void getOnlineVersion(UpdaterSettings.onSucces onSucces,UpdaterSettings.onError onError) {
        UpdaterSettings setting = new UpdaterSettings(USER, REPO, onError,onSucces,true);
        new updater.Updater(setting);
    }

    public void doUpgrade(URL url) throws IOException {
        String ptJar = JavaTools.getJarUrl(Updater.class);
        long time = System.currentTimeMillis();
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(ptJar+"/Tronic_"+time+".jar");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        FileWriter fw = new FileWriter(new File(ptJar+"/tronic.json"));
        fw.write("[{\"procname\": \"java -jar "+ptJar+"/Tronic_"+time+"\", \"if\":\"\",\"else\":\"java -jar "+ptJar+"/Tronic_"+time+"\" }]");
        fw.close();
        int os = 7777;
        try {
             os = OSTools.getOS();
        } catch (OSnotDetectedException e) {
            logger.warn("OS not detected!");
        }
        if (os == OSTools.OS_WINDOWS) {
            try {
                selfDestructWindowsJARFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (os ==OSTools.OS_LINUX || os==OSTools.OS_MAC) {
            try {
                selfDestructLinuxFile();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);


    }

    private void selfDestructLinuxFile() throws URISyntaxException {
        File file = getCurrentJARFilePath();
        file.delete();
    }

    public static boolean isBigger(String version, String onversion) {
        String[] splitVers = version.split("\\.");
        String[] splitonVers = onversion.split("\\.");
        if (Integer.parseInt(splitonVers[0])<Integer.parseInt(splitVers[0])) {
            return false;
        } else if(Integer.parseInt(splitonVers[0])>Integer.parseInt(splitVers[0])) {
            return true;
        } else {
            if (Integer.parseInt(splitonVers[1])<Integer.parseInt(splitVers[1])) {
                return false;
            } else if(Integer.parseInt(splitonVers[1])>Integer.parseInt(splitVers[1])) {
                return true;
            } else {
                if (Integer.parseInt(splitonVers[2])<=Integer.parseInt(splitVers[2])) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    private static void selfDestructWindowsJARFile() throws Exception
    {
        String ptJar = JavaTools.getJarUrl(Updater.class);
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("cmd /c ping localhost -n 2 > nul && del \"" + getCurrentJARFilePath().toString() + "\"");
    }


    public static File getCurrentJARFilePath() throws URISyntaxException
    {
        return new File(Updater.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    }

}
