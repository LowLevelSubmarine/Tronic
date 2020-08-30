package com.tronic.bot.rest.requests;

import com.google.gson.Gson;
import com.tronic.bot.core.Core;
import io.jsonwebtoken.Claims;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;

public class UserRequest implements Route {
    private Core core;

    public UserRequest(Core core) {
        this.core=core;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String jwt = request.queryParams("token");
        try {
            if (this.core.getRestServer().getJwtStore().isJWTValid(jwt)) {
                Claims claims = this.core.getRestServer().getJwtStore().getClaims(jwt);
                User user = this.core.getJDA().getUserById((Long) claims.get("user"));
                RestUser ru = new RestUser(user.getName(), user.getId(), user.getAvatarUrl());
                response.header("Content-Type","application/json");
                return new Gson().toJson(ru);

            }
        } catch (Exception e) {
            response.status(400);
        }
        return "";
    }

    public class RestUser {
        public String name;
        public String id;
        public String image;

        public RestUser(String name, String id, String image) {
            this.name = name;
            this.id = id;
            this.image = image;
        }
    }
}
