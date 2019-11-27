package com.tronic.bot.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public enum Permission {

    HOST(3), CO_HOST(2), ADMIN(1), NONE(0);

    private final int level;

    Permission(int level) {
        this.level = level;
    }

    public boolean isValid() {
        return true;
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
