package core.storage;

public class GuildData implements Storable {

    private transient Storage storage;
    private transient String id;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void populate(Storage storage, String id) {
        this.storage = storage;
        this.id = id;
    }

}
