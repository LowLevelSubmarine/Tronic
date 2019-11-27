package com.tronic.bot.listeners;

import com.tronic.bot.Tronic;
import com.tronic.bot.stats.RangeStatistic;
import net.dv8tion.jda.api.events.ReadyEvent;

import javax.annotation.Nonnull;

public class ExperimentStartupListener extends Listener {

    public ExperimentStartupListener(Tronic tronic) {
        super(tronic);
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println(new RangeStatistic(event.getJDA()).calc());
    }
}
