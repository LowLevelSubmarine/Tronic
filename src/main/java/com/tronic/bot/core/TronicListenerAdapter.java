package com.tronic.bot.core;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class TronicListenerAdapter extends ListenerAdapter {

    abstract void onBootUp(Core core);

}
