package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;
import kotlin.jvm.JvmClassMappingKt;

import java.io.Serializable;
import java.util.List;

public abstract class StorageElement {

    Shelf shelf;

    StorageElement(Shelf shelf) {
        this.shelf = shelf;
    }

    void set(String item,Object value) {

        this.shelf.item(item).put(value);
    }

     Object get(String item,Class value) {
        Shelf.Item itemm = this.shelf.item(item);
        return itemm.get(JvmClassMappingKt.getKotlinClass(value));
    }

    List<Object> getList(String item,Class classes) {
        return this.shelf.item(item).getList(JvmClassMappingKt.getKotlinClass(classes));
    }

}
