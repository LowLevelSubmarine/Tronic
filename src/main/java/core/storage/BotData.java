package core.storage;

import core.storage.serialized.UserSerialized;
import net.dv8tion.jda.core.entities.User;
import statics.ProjectConfig;

import java.util.LinkedList;

public class BotData implements Storable {

    private transient Storage storage;
    private transient String id;

    private String game;
    private LinkedList<String> botAdmins = new LinkedList<>();

    public String getGame() {
        if (game != null) {
            return game;
        }
        return ProjectConfig.DEFAULT_GAME;
    }
    public void setGame(String game) {
        this.game = game;
        save();
    }

    public boolean isBotAdmin(User user) {
        return this.botAdmins.contains(user.getId());
    }
    public void addBotAdmin(User user) {
        this.botAdmins.add(user.getId());
    }
    public void removeBotAdmin(User user) {
        this.botAdmins.remove(user.getId());
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void populate(Storage storage, String id) {
        this.storage = storage;
        this.id = id;
    }

    private void save() {
        this.storage.saveBot(this);
    }

}
