package com.tronic.bot.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toddway.shelf.Shelf;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Serializer implements Shelf.Serializer {
    private final Gson gson;

    public Serializer() {
        this.gson = new Gson();
    }

    @NotNull
    @Override
    public <T> String fromType(@NotNull T t) {
        return this.gson.toJson(t);
    }

    @NotNull
    @Override
    public <T> T toType(@NotNull String s, @NotNull KClass<T> kClass) {
        return this.gson.fromJson(s, JvmClassMappingKt.getJavaClass(kClass));
    }

    @NotNull
    @Override
    public <T> List<T> toTypeList(@NotNull String s, @NotNull KClass<T> kClass) {
        Class<T> cl = JvmClassMappingKt.getJavaClass(kClass);
        Type typeOfT = TypeToken.getParameterized(List.class, cl).getType();
        return this.gson.fromJson(s,typeOfT);
    }
}
