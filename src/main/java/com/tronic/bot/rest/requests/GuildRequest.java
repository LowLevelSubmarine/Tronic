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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GuildRequest implements Route {

    private final Core core;

    public GuildRequest(Core core) {
        this.core = core;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String jwt = request.queryParams("token");
        try {
            if (this.core.getRestServer().getJwtStore().isJWTValid(jwt)) {
                Claims claims = this.core.getRestServer().getJwtStore().getClaims(jwt);
                Guild guild = this.core.getJDA().getGuildById((String) claims.get("guild"));
                ArrayList<String> members = new ArrayList<>();
                for(Member user:guild.getMembers()) {
                    members.add(user.getId());
                }
                RestGuild rg = new RestGuild(guild.getName(),guild.getId(),members,this.core.getStorage().getGuild(guild).getPrefix(),guild.getIconUrl());
                response.header("Content-Type","application/json");
                return new Gson().toJson(rg);

            }
        } catch (Exception e) {
            response.status(400);
        }
        return "";
    }

    public class RestGuild {
        public String guildName;
        public String guildId;
        public List<String> members;
        public String prefix;
        public String icon;

        public RestGuild(String guildName, String guildId, List<String> members, String prefix,String icon) {
            this.guildName = guildName;
            this.guildId = guildId;
            this.members = members;
            this.prefix = prefix;
            this.icon = icon;
        }
    }
}
