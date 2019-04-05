package core.storage;

import statics.ProjectConfig;

public class BotData implements Storable {

    private transient Storage storage;
    private transient String id;

    private String game;

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
