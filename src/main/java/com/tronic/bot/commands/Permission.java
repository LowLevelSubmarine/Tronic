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

    @SuppressWarnings("DuplicateCondition")
    public boolean isValid (MessageReceivedEvent event, Core tronic) {
        User u = tronic.getJDA().getUserById(tronic.getHostToken());
        boolean isCoHost =tronic.getStorage().getStatic().isCoHoster(event.getAuthor());
        boolean isHost = event.getAuthor().equals(u);
        boolean isAdmin = event.getGuild().getMember(event.getAuthor()).hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR);
        System.out.println(this.level);
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

    public int getLevel() {
        return this.level;
    }

    public void calcLevel(User user, Guild guild) {
        if (user == null) {
            return;
        }
    }

}
