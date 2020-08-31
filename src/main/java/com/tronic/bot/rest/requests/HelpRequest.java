package com.tronic.bot.rest.requests;

import com.google.gson.Gson;
import com.tronic.bot.commands.Command;
import com.tronic.bot.commands.CommandType;
import com.tronic.bot.commands.Permission;
import com.tronic.bot.core.Core;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.security.SignatureException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.User;
import spark.*;

import java.util.HashMap;
import java.util.LinkedList;

public class HelpRequest implements Route {

    private Core core;
    public HelpRequest(Core core) {
        this.core = core;
    }

    @Override
    public Object handle(Request request, Response response) {
        String token = request.queryParams("token");
        Gson gson = new Gson();
        try {
            if (token != null) {
                if (this.core.getRestServer().getJwtStore().isJWTValid(token)) {
                    Claims claims = this.core.getRestServer().getJwtStore().getClaims(token);
                    User user = User.fromId((Long) claims.get("user"));
                    Guild guild = this.core.getJDA().getGuildById((String) claims.get("guild"));
                    response.type("application/json");
                    return gson.toJson(getHelp(Permission.getUserPermission(user, guild, this.core)));
                }
            }
            response.type("application/json");
            return gson.toJson(getHelp(Permission.ADMIN));
        } catch (NullPointerException | DeserializationException | UnsupportedJwtException e) {
            response.status(400);
        } catch (SignatureException f) {
            response.status(403);
        }
       return "";
    }


    private HashMap<String,LinkedList<HelpJsonElement>> getHelp(Permission permission) {
        HashMap<String,LinkedList<HelpJsonElement>> all = new HashMap<>();
        LinkedList<Command> commands = this.core.getCommandHandler().getCommands();
        for (Command command:commands) {
            if (testPem(permission,command)) {
                LinkedList<HelpJsonElement> tmpList = all.getOrDefault(command.getType().name().toLowerCase(),new LinkedList<>());
                tmpList.add(new HelpJsonElement(command));
                all.put(command.getType().name().toLowerCase(),tmpList);
            }
        }
        return all;
    }

    private boolean testPem(Permission pem, Command c) {
        Permission dbg = c.getPermission();
        if (c.getPermission() == Permission.NONE) {
            return true;
        } else if (c.getPermission()==Permission.ADMIN && pem == Permission.ADMIN) {
            return true;
        } else if (c.getPermission()==Permission.CO_HOST && pem == Permission.CO_HOST ||c.getPermission()==Permission.CO_HOST && pem == Permission.HOST) {
            return true;
        } else if (pem == Permission.HOST) {
            return true;
        } else {
            return false;
        }
    }

    protected class HelpJsonElement {
        public String title;
        public String description;
        public String syntax;

        public HelpJsonElement(String title, String description, String syntax) {
            this.title = title;
            this.description = description;
            this.syntax = syntax;
        }

        public HelpJsonElement(Command command) {
            this.title = command.getHelpInfo().getTitle();
            this.description = command.getHelpInfo().getShortDescription();
            this.syntax = command.getHelpInfo().getSyntax();
        }
    }
}
