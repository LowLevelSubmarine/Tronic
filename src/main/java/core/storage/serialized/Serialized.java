package core.storage.serialized;

import java.io.Serializable;

public interface Serialized<O> extends Serializable {
    O get();
    void set(O o);
}
