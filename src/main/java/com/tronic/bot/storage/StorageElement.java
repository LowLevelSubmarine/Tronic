package com.tronic.bot.storage;

import com.toddway.shelf.Clock;
import com.toddway.shelf.FileStorage;
import com.toddway.shelf.KotlinxSerializer;
import com.toddway.shelf.Shelf;
import com.tronic.bot.Tronic;
import jdk.internal.reflect.Reflection;
import kotlin.jvm.JvmClassMappingKt;
import net.tetraowl.watcher.toolbox.JavaTools;

import java.io.File;
import java.io.Serializable;

public abstract class StorageElement {

    Shelf shelf;

    StorageElement(Shelf shelf) {
        this.shelf = shelf;
    }

    void store(String item,Serializable value) {
        this.shelf.item(item).put(value);
    }
     Object get(String item,Serializable value) {
        Shelf.Item itemm = this.shelf.item(item);
        return itemm.get(JvmClassMappingKt.getKotlinClass(value.getClass()));
    }

}
