package core.storage.serialized;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;

import java.io.Serializable;

public class GuildSerialized implements Serializable {

    private String guildId;

    public Guild get(JDA jda) {
        return jda.getGuildById(guildId);
    }

}
