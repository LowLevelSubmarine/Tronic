package core.music;

import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;

public class GuildPlayerManager {

    private HashMap<Guild, GuildPlayer> guildPlayer = new HashMap<>();

    public GuildPlayer getPlayer(Guild guild) {
        GuildPlayer player = this.guildPlayer.get(guild);
        if (player == null) {
            player = createPlayer(guild);
        }
        return player;
    }

    private GuildPlayer createPlayer(Guild guild) {
        GuildPlayer player = new GuildPlayer(guild);
        this.guildPlayer.put(guild, player);
        return player;
    }



}
