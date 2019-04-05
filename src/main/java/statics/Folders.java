package statics;

import utils.FileUtils;

import java.io.File;

public class Folders {

    public static File DATA = new File("data");
    public static final File DATA_BOT = DATA;
    public static final File DATA_GUILD = FileUtils.createChild(DATA, "guilds");
    public static final File DATA_USER = FileUtils.createChild(DATA, "user");

}
