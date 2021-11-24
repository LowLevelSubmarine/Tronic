package com.tronic.bot.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    public static final Gson INSTANCE = new Gson();
    public static final Gson PRETTY_PRINT_INSTANCE = new GsonBuilder().setPrettyPrinting().create();

}
