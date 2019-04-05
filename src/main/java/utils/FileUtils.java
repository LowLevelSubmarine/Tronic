package utils;

import java.io.File;

public class FileUtils {

    public static File createChild(File folder, String name) {
        return new File(folder.getAbsolutePath() + File.separatorChar + name);
    }

}
