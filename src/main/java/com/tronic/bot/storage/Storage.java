package com.tronic.bot.storage;

import com.toddway.shelf.Clock;
import com.toddway.shelf.FileStorage;
import com.toddway.shelf.KotlinxSerializer;
import com.toddway.shelf.Shelf;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.tetraowl.watcher.toolbox.JavaTools;

import java.io.File;

public class Storage {

    private static final String STRING_FILE_NAME_STATIC = "static";
    private static final String STRING_FILE_PREFIX_GUILD = "guild-";
    private static final String STRING_FILE_PREFIX_USER = "user-";

    private final Shelf shelf;

    public Storage() {
        File root = new File(JavaTools.getJarUrl(Storage.class));
        FileStorage fileStorage = new FileStorage(root);
        KotlinxSerializer kotlinxSerializer = new KotlinxSerializer();
        Clock clock = new Clock();
        this.shelf = new Shelf(fileStorage, kotlinxSerializer, clock);
    }

    public StaticStorage getStatic() {
        return new StaticStorage(this.shelf.item(STRING_FILE_NAME_STATIC).getShelf());
    }

    public GuildStorage getGuild(Guild guild) {
        return new GuildStorage(this.shelf.item(getGuildKey(guild)).getShelf());
    }

    public UserStorage getUser(User user) {
        return new UserStorage(this.shelf.item(getUserKey(user)).getShelf());
    }

    private String getGuildKey(Guild guild) {
        return STRING_FILE_PREFIX_GUILD + guild.getId();
    }

    private String getUserKey(User user) {
        return STRING_FILE_PREFIX_USER + user.getId();
    }

}
