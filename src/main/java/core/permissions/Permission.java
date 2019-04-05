package core.permissions;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

public enum Permission {

    NONE(0), GUILD_ADMIN(1), BOT_ADMIN(2), HOST(3);

    private final int level;

    Permission(int level) {
        this.level = level;
    }

    public boolean hasPermission(User user) {
        return true;
    }

    public boolean hasPermission(Member member) {
        return true;
    }

}
