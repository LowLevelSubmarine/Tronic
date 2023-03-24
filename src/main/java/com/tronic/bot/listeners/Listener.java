package com.tronic.bot.listeners;

import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class Listener extends ListenerAdapter {
    private final Core core;

    public Listener(Core core) {
        this.core = core;
    }

    protected Core getCore() {
        return this.core;
    }
}
