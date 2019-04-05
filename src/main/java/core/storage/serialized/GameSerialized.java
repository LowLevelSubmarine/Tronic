package core.storage.serialized;

import net.dv8tion.jda.core.entities.Game;

public class GameSerialized implements Serialized<Game> {

    private int typeKey;
    private String name;
    private String url;

    @Override
    public Game get() {
        return Game.of(Game.GameType.fromKey(typeKey), name, url);
    }

    @Override
    public void set(Game o) {
        this.typeKey = o.getType().getKey();
        this.name = o.getName();
        this.url = o.getUrl();
    }

}
