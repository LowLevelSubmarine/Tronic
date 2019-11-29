package com.tronic.bot.storage;

import com.toddway.shelf.Clock;
import com.toddway.shelf.FileStorage;
import com.toddway.shelf.KotlinxSerializer;
import com.toddway.shelf.Shelf;
import com.tronic.bot.Tronic;
import net.tetraowl.watcher.toolbox.JavaTools;

import java.io.File;

public abstract class StorageElement {

    Shelf shelf;

    StorageElement(Shelf shelf) {
        this.shelf = shelf;
    }

    void saveElement() {
        
    }

}
