package com.tronic.bot.commands;

import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public enum Permission {

    HOST(3), CO_HOST(2), ADMIN(1), NONE(0), INTERN(Integer.MAX_VALUE);

    private final int level;

    Permission(int level) {
        this.level = level;
    }

    public boolean isValid(MessageReceivedEvent event, Core core) {
        return isValid(event.getAuthor(), event.getGuild(), core);
    }

    public boolean isValid(User user, Guild guild, Core core) {
        return this.getLevel() <= getPermission(user, guild, core).getLevel();
    }

    public static Permission getPermission(User user, Guild guild, Core core) {
        boolean isCoHost = core.getStorage().getStatic().isCoHoster(user);
        boolean isHost = user.getId().equals(core.getHostToken());
        Member member = guild.getMember(user);
        boolean isAdmin = member != null && member.hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR);
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

}
