package com.tronic.bot.shortcuts;

import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ShortcutResolver {
    private final Core core;

    public ShortcutResolver(Core c) {
        this.core = c;
    }
    public void resolveShortcut(String name, MessageReceivedEvent e) {
        ShortcutElement shortcut=null;
        for(ShortcutElement s:this.core.getStorage().getGuild(e.getGuild()).getShortcuts()) {
            if (s.getName().equals(name)) {
                shortcut=s;
                break;
            }
        }
        if (shortcut== null) return;
        

    }
}
