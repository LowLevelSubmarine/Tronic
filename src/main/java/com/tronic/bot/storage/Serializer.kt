package com.tronic.bot.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken
import com.toddway.shelf.Shelf;
import kotlin.reflect.KClass;

class Serializer(private val gson:Gson = Gson()): Shelf.Serializer {

    override fun <T : Any> fromType(value: T): String {
        val dbg = this.gson.toJson(value)
        return dbg;
    }

    override fun <T : Any> toType(string: String, klass: KClass<T>): T {
        val dbg =  this.gson.fromJson<T>(string,klass::class.java)
        return dbg
    }

    override fun <T : Any> toTypeList(string: String, klass: KClass<T>): List<T> {
        val typeOfT = TypeToken.getParameterized(List::class.java, klass::class.java).type;
        return this.gson.fromJson(string,typeOfT);
    }

}
