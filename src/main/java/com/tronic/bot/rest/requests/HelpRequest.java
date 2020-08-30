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
                if (this.core.getJwtStore().isJWTValid(token)) {
                    Claims claims = this.core.getJwtStore().getClaims(token);
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
        LinkedList<HelpJsonElement> administrationCommand = new LinkedList<>();
        LinkedList<HelpJsonElement> infoCommand = new LinkedList<>();
        LinkedList<HelpJsonElement> funCommand = new LinkedList<>();
        LinkedList<HelpJsonElement> settingsCommand = new LinkedList<>();
        LinkedList<HelpJsonElement> musicCommand = new LinkedList<>();
        LinkedList<Command> commands = this.core.getCommandHandler().getCommands();
        for (Command c:commands) {
            if (c.getType() == CommandType.ADMINISTRATION) {
                if (testPem(permission,c)) administrationCommand.add(new HelpJsonElement(c));
            } else if (c.getType() == CommandType.INFO) {
                if (testPem(permission,c)) infoCommand.add(new HelpJsonElement(c));
            } else if (c.getType() == CommandType.FUN) {
                if (testPem(permission,c)) funCommand.add(new HelpJsonElement(c));
            } else if (c.getType() == CommandType.SETTINGS) {
                if (testPem(permission,c)) settingsCommand.add(new HelpJsonElement(c));
            } else if (c.getType() == CommandType.MUSIC) {
                if (testPem(permission,c)) musicCommand.add(new HelpJsonElement(c));
            }
        }
        HashMap<String,LinkedList<HelpJsonElement>> all = new HashMap<>();
        all.put("administration",administrationCommand);
        all.put("info",infoCommand);
        all.put("fun",funCommand);
        all.put("settings",settingsCommand);
        all.put("music",musicCommand);
        return all;
    }

    private boolean testPem(Permission pem, Command c) {
        if (c.getPermission() == Permission.NONE) {
            return true;
        } else if (c.getPermission()==Permission.ADMIN && pem == Permission.ADMIN) {
            return true;
        } else if (c.getPermission()==Permission.CO_HOST && pem == Permission.CO_HOST ||c.getPermission()==Permission.CO_HOST && pem == Permission.HOST) {
            return true;
        } else if (c.getPermission() == Permission.HOST && pem == Permission.HOST) {
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
