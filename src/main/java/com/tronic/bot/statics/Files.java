package com.tronic.bot.statics;

import java.io.File;

public class Files {

    public static final File ROOT_FOLDER = new File(Files.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParentFile();

}
