package com.tronic.bot.commands;

import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.checkerframework.checker.units.qual.A;

public enum Permission {

    HOST(3), CO_HOST(2), ADMIN(1), NONE(0);

    private final int level;

    Permission(int level) {
        this.level = level;
    }

    public boolean isValid (MessageReceivedEvent event, Core tronic) {
        boolean isCoHost =tronic.getStorage().getStatic().isCoHoster(event.getAuthor());
        boolean isAdmin = event.getGuild().getMember(event.getAuthor()).hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR);
        System.out.println(this.level);
        if (this.level == CO_HOST.level) {
            return isCoHost;
        } else if (this.level == ADMIN.level) {
            if (isCoHost||isAdmin) {
                return true;
            } else {
                return false;
            }
        } else if (this.level ==NONE.level){
            return true;
        } else {

            return false;
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
