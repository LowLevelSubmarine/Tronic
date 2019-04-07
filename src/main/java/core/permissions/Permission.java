package core.permissions;

import core.Tronic;
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

    public boolean hasPermission(User user, Tronic tronic) {
        return validate(user, null, tronic);
    }

    public boolean hasPermission(Member member, Tronic tronic) {
        return validate(member.getUser(), member.getGuild(), tronic);
    }

    private boolean validate(User user, Guild guild, Tronic tronic) {
        return calcLevel(user, guild, tronic) >= this.getLevel();
    }

    private int calcLevel(User user, Guild guild, Tronic tronic) {
        if (this.isBotHost(user, tronic)) {
            return HOST.getLevel();
        } else if (this.isBotAdmin(user, tronic)) {
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

    private boolean isBotAdmin(User user, Tronic tronic) {
        return user != null && tronic != null
                && tronic.getStorage().getBot().isBotAdmin(user);
    }

    private boolean isBotHost(User user, Tronic tronic) {
        return user != null && tronic != null
                && tronic.getConfig().getHostId().equals(user.getId());
    }

}
