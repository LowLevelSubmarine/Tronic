package core.storage;

import java.io.Serializable;

public interface Storable extends Serializable {

    String getId();
    void populate(Storage storage, String id);

}
