package com.tronic.bot.music;

import net.dv8tion.jda.api.entities.Guild;

public class Volume {

    private final Guild guild;

    public Volume(Guild guild) {
        this.guild = guild;
    }

    //Calculates the volume to a float.
    //1F is the normalized value
    public float calcLevel() {
        return 1;
    }


}
