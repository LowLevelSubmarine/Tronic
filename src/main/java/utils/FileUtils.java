package utils;

import java.io.File;

public class FileUtils {

    public static File createChild(File folder, String name) {
        folder.mkdirs();
        return new File(folder.getPath() + File.pathSeparator + name);
    }

}
