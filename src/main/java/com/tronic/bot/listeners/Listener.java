package com.tronic.bot.listeners;

import com.tronic.bot.Tronic;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class Listener extends ListenerAdapter {

    private final Tronic tronic;

    public Listener(Tronic tronic) {
        this.tronic = tronic;
    }

    protected Tronic getTronic() {
        return this.tronic;
    }

}
