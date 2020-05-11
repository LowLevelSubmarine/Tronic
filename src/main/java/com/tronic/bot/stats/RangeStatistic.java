package com.tronic.bot.stats;

import net.dv8tion.jda.api.JDA;

public class RangeStatistic {

    private final JDA jda;

    public RangeStatistic(JDA jda) {
        this.jda = jda;
    }

    public long calc() {
        return jda.getUserCache().size();
    }

}
