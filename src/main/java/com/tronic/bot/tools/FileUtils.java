package com.tronic.bot.tools;

import java.io.File;

public class FileUtils {

    public static File getFileByPath(String... names) {
        String finalName = null;
        for (String name : names) {
            if (finalName == null) {
                finalName = name;
            } else {
                finalName += File.pathSeparator + name;
            }
        }
        return new File(finalName);
    }

}
