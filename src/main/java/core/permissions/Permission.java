package core.permissions;

import core.Core;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

public enum Permission {

    NONE(0), GUILD_ADMIN(1), BOT_ADMIN(2), HOST(3);

    private final int level;

    Permission(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }

    public boolean hasPermission(User user, Core core) {
        return validate(user, null, core);
    }

    public boolean hasPermission(Member member, Core core) {
        return validate(member.getUser(), member.getGuild(), core);
    }

    private boolean validate(User user, Guild guild, Core core) {
        return calcLevel(user, guild, core) >= this.getLevel();
    }

    private int calcLevel(User user, Guild guild, Core core) {
        if (this.isBotHost(user, core)) {
            return HOST.getLevel();
        } else if (this.isBotAdmin(user, core)) {
            return BOT_ADMIN.getLevel();
        } else if (this.isGuildAdmin(user, guild)) {
            return GUILD_ADMIN.getLevel();
        } else {
            return NONE.getLevel();
        }
    }

    private boolean isGuildAdmin(User user, Guild guild) {
        return user != null && guild != null
                && guild.getMember(user).getPermissions().contains(net.dv8tion.jda.core.Permission.ADMINISTRATOR);
    }

    private boolean isBotAdmin(User user, Core core) {
        return user != null && core != null
                && core.getStorage().getBot().isBotAdmin(user);
    }

    private boolean isBotHost(User user, Core core) {
        return user != null && core != null
                && core.getConfig().getHostId().equals(user.getId());
    }

}
