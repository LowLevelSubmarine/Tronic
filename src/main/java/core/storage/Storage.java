package core.storage;

import com.toddway.shelf.Shelf;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import statics.Folders;

import java.io.File;
import java.util.LinkedList;

public class Storage {

    private final String BOT_DATA_KEY = "bot_data";

    private final Type<BotData> botData = new Type<>(Folders.DATA_BOT, BotData.class);
    private final Type<GuildData> guildData = new Type<>(Folders.DATA_GUILD, GuildData.class);
    private final Type<UserData> userData = new Type<>(Folders.DATA_USER, UserData.class);

    public BotData getBot() {
        BotData botData = this.botData.get(BOT_DATA_KEY);
        if (botData == null) {
            botData = new BotData();
            botData.populate(this, BOT_DATA_KEY);
        }
        return botData;
    }

    public GuildData getGuild(Guild guild) {
        GuildData guildData = this.guildData.get(guild.getId());
        if (guildData == null) {
            guildData = new GuildData();
            guildData.populate(this, guild.getId());
        }
        return guildData;
    }

    public UserData getUser(User user) {
        UserData userData = this.userData.get(user.getId());
        if (userData == null) {
            userData = new UserData();
            userData.populate(this, user.getId());
        }
        return userData;
    }

    void saveBot(BotData botData) {
        this.botData.save(botData.getId());
    }

    void saveGuild(GuildData guildData) {
        this.guildData.save(guildData.getId());
    }

    void saveUser(UserData userData) {
        this.userData.save(userData.getId());
    }

    class Type<T extends Storable> {
        private final LinkedList<T> cache = new LinkedList<>();
        private final Shelf shelf;
        public Type(File root, Class<T> clazz) {
            this.shelf = new Shelf(root);
            for (String key : this.shelf.keys("")) {
                T item = this.shelf.item(key).get(clazz);
                item.populate(Storage.this, key);
                this.cache.add(item);
            }
        }
        public T get(String id) {
            for (T item : cache) {
                if (item.getId().equals(id)) {
                    return item;
                }
            }
            return null;

        }
        public void save(String id) {
            T item = get(id);
            if (item != null) {
                this.shelf.item(id).put(item);
            }
        }
    }

}
