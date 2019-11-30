package com.tronic.bot.tools;

import net.tetraowl.watcher.toolbox.JavaTools;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

public class FileUtils {

    public static File getFile(File file, String... names) {
        return getFile(file.getAbsoluteFile(), names);
    }

    public static File getFile(String... names) {
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

    public static File[] getFiles(File root, Pattern filenamePattern) {
        return getFiles(root, new PatternFilenameFilter(filenamePattern));
    }

    public static File[] getFiles(File root, FilenameFilter filenameFilter) {
        try {
            return root.listFiles(filenameFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File[0];
    }

    public static File getJarFolder() {
        return new File(JavaTools.getJarUrl(FileUtils.class));
    }

    private static class PatternFilenameFilter implements FilenameFilter {

        private final Pattern pattern;

        public PatternFilenameFilter(Pattern pattern) {
            this.pattern = pattern;
        }

        @Override
        public boolean accept(File dir, String name) {
            return this.pattern.matcher(name).matches();
        }

    }

}
