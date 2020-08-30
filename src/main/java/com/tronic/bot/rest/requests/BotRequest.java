package com.tronic.bot.rest.requests;

import com.google.gson.Gson;
import com.tronic.bot.core.Core;
import com.tronic.bot.statics.Info;
import com.tronic.bot.statics.Presets;
import spark.Request;
import spark.Response;
import spark.Route;

public class BotRequest implements Route {

    private final Core core;

    public BotRequest(Core core) {
        this.core = core;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.header("Content-Type","application/json");
        return new Gson().toJson(new RestBot(Info.VERSION, Presets.PREFIX));
    }

    public class RestBot {
        public String version;
        public String defaultPrefix;

        public RestBot(String version, String defaultPrefix) {
            this.version = version;
            this.defaultPrefix = defaultPrefix;
        }
    }
}
