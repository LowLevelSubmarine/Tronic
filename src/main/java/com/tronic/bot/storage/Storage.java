package com.tronic.bot.storage;

import com.tronic.bot.tools.FileUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.tetraowl.watcher.toolbox.JavaTools;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Storage {

    private static final Pattern MATCHER_GUILD_ID = Pattern.compile("[0-9]{18}");
    private static final Pattern MATCHER_USER_ID = Pattern.compile("[0-9]{18}");
    private static final String STRING_STATIC_FILE_NAME = "STATIC.obj";

    private final File root;
    private final HashMap<String, GuildStorage> guildMap = new HashMap<>();
    private final HashMap<String, UserStorage> userMap = new HashMap<>();
    private final StaticStorage staticStorage;

    public Storage() {
        this.root = new File(JavaTools.getJarUrl(Storage.class));
        this.staticStorage = new StaticStorage(FileUtils.getFileByPath(this.root.getAbsolutePath(), STRING_STATIC_FILE_NAME);
    }

    public StaticStorage getStatic() {
        return this.staticStorage;
    }

    public GuildStorage getGuild(Guild guild) {
        return null;
    }

    public UserStorage getUser(User user) {
        return null;
    }

}
