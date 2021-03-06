package com.tronic.bot.commands;

import com.tronic.bot.core.Core;
import com.tronic.bot.core.Tronic;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public enum Permission {

    HOST(3), CO_HOST(2), ADMIN(1), NONE(0);

    private final int level;

    Permission(int level) {
        this.level = level;
    }

    @SuppressWarnings("DuplicateCondition")
    public boolean isValid (MessageReceivedEvent event, Core tronic) {
        boolean isCoHost =tronic.getStorage().getStatic().isCoHoster(event.getAuthor());
        boolean isHost = event.getAuthor().equals(tronic.getJDA().getUserById(tronic.getHostToken()));
        Member author = event.getGuild().getMember(event.getAuthor());
        boolean isAdmin = author != null && author.hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR);
        if (this.level == HOST.level) {
            return isHost;
        } else if (this.level == CO_HOST.level) {
            return isCoHost ||isHost;
        } else if (this.level == ADMIN.level) {
            return isCoHost || isAdmin;
        } else if (this.level ==NONE.level){
            return true;
        } else {

            return false;
        }
    }
    public static Permission getUserPermission(User user,Guild guild, Core core) {
        boolean isCoHost =core.getStorage().getStatic().isCoHoster(user);
        boolean isHost = user.equals(core.getJDA().getUserById(core.getHostToken()));
        Member author = guild.getMember(user);
        boolean isAdmin = author != null && author.hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR);
        if (isHost) {
            return HOST;
        } else if (isCoHost) {
            return CO_HOST;
        } else if (isAdmin) {
            return ADMIN;
        } else {
            return NONE;
        }
    }

    public int getLevel() {
        return this.level;
    }

    public void calcLevel(User user, Guild guild) {
        if (user == null) {
            return;
        }
    }

}
