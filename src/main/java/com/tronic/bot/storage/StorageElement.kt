package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;
import com.toddway.shelf.get
import kotlin.reflect.KClass


abstract class StorageElement(private val shelf: Shelf) {

    fun set(item: String,value: Any) {
        this.shelf.item(item).put(value);
    }

     fun <T: Any> get(item: String,value: KClass<T>): T? {
        val itemm:Shelf.Item = this.shelf.item(item);
        return itemm.get(value)
    }

    fun <T: Any> getList(item: String,classes: KClass<T>): List<T>? {
        return this.shelf.item(item).getList(classes)
    }

}
