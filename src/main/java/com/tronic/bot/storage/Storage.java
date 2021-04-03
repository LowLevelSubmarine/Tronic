package com.tronic.bot.storage;

import com.toddway.shelf.Clock;
import com.toddway.shelf.FileStorage;
import com.toddway.shelf.Shelf;
import com.tronic.bot.statics.Files;
import com.tronic.bot.tools.FileUtils;
import com.tronic.bot.tools.JDAUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.util.HashMap;

public class Storage {

    private static final File DATA_FILE_ROOT = FileUtils.getFile(Files.ROOT_FOLDER, "data");
    private static final File FILE_STATIC = FileUtils.getFile(DATA_FILE_ROOT, "static");
    private static final File FILE_GUILDS = FileUtils.getFile(DATA_FILE_ROOT, "guilds");
    private static final File FILE_USERS = FileUtils.getFile(DATA_FILE_ROOT, "users");
    private static final File FILE_VOLUME = FileUtils.getFile(DATA_FILE_ROOT, "volume");

    private final StaticStorage staticStorage;
    private final HashMap<String, GuildStorage> guildStorages;
    private final HashMap<String, UserStorage> userStorages;
    private final HashMap<String, VolumeStorage> volumeStorage;


    public Storage() {
        this.staticStorage = new StaticStorage(createShelf(FILE_STATIC));
        this.guildStorages = loadGuildStorages();
        this.userStorages = loadUserStorages();
        this.volumeStorage = loadVolumeStorages();
    }

    public StaticStorage getStatic() {
        return this.staticStorage;
    }

    public GuildStorage getGuild(Guild guild) {
        if (this.guildStorages.containsKey(guild.getId())) {
            return this.guildStorages.get(guild.getId());
        } else {
            return new GuildStorage(createShelf(getGuildStorageFile(guild)));
        }
    }

    public UserStorage getUser(User user) {
        if (this.userStorages.containsKey(user.getId())) {
            return this.userStorages.get(user.getId());
        } else {
            return new UserStorage(createShelf(getUserStorageFile(user)));
        }
    }

    public VolumeStorage getVolume(String videoId) {
        if (this.volumeStorage.containsKey(videoId)) {
            return this.volumeStorage.get(videoId);
        } else {
            return new VolumeStorage(createShelf(getVolumeStorageFile(videoId)),this.staticStorage);
        }
    }

    private HashMap<String, GuildStorage> loadGuildStorages() {
        HashMap<String, GuildStorage> guildStorages = new HashMap<>();
        for (File file : getSnowflakeFiles(FILE_GUILDS)) {
            guildStorages.put(file.getName(), new GuildStorage(createShelf(file)));
        }
        return guildStorages;
    }

    private HashMap<String, VolumeStorage> loadVolumeStorages() {
        HashMap<String, VolumeStorage> volumeStorage = new HashMap<>();
        for (File file : getSnowflakeFiles(FILE_VOLUME)) {
            volumeStorage.put(file.getName(), new VolumeStorage(createShelf(file),this.staticStorage));
        }
        return volumeStorage;
    }

    private HashMap<String, UserStorage> loadUserStorages() {
        HashMap<String, UserStorage> userStorages = new HashMap<>();
        for (File file : getSnowflakeFiles(FILE_USERS)) {
            userStorages.put(file.getName(), new UserStorage(createShelf(file)));
        }
        return userStorages;
    }

    private File[] getSnowflakeFiles(File file) {
        return FileUtils.getFiles(file, JDAUtils.PATTERN_SNOWFLAKE);
    }

    private File getGuildStorageFile(Guild guild) {
        return FileUtils.getFile(FILE_GUILDS, guild.getId());
    }

    private File getUserStorageFile(User user) {
        return FileUtils.getFile(FILE_USERS, user.getId());
    }

    private File getVolumeStorageFile(String videoId) {
        return FileUtils.getFile(FILE_VOLUME, videoId);
    }

    private Shelf createShelf(File file) {
        file.mkdirs();
        FileStorage fileStorage = new FileStorage(file);
         Serializer serializer = new Serializer();
        Clock clock = new Clock();
        return new Shelf(fileStorage, serializer, clock);
    }

}
