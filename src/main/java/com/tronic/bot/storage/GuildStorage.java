package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;
import com.tronic.bot.shortcuts.ShortcutElement;
import com.tronic.bot.statics.Presets;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class GuildStorage extends StorageElement {

    GuildStorage(Shelf shelf) {
        super(shelf);
    }

    //MUTED BROADCAST
    public void setBroadcastMuted(Boolean broadcastMuted) {
        super.set("broadcast_muted", broadcastMuted);
    }
    public boolean getBroadcastMuted() {
        Boolean broadcastMuted = (Boolean) super.get("broadcast_muted", Boolean.class);
        return broadcastMuted != null? broadcastMuted : false;
    }

    //PREFIX
    public String getPrefix() {
        String prefix = (String) super.get("prefix",String.class);
        return  prefix!=null ? prefix : Presets.PREFIX;
    }
    public void setPrefix(String prefix) {
        super.set("prefix",prefix);
    }

    //HYPERCHANNELL
    public String getNewChannel() {
        return (String) super.get("hyperchannel", String.class);
    }
    public void setNewChannel(String channel) {
        super.set("hyperchannel", channel);
    }
    public String getHyperName() {
        String name = (String) super.get("hypername",String.class);
        return  name != null ? name : Presets.NEW_CHANNEL;
    }
    public void setHyperName(String hyperName) {
        super.set("hypername",hyperName);
    }
    public boolean getHyperchannelState() {
        try {
            boolean state = (boolean) super.get("hyperstate",boolean.class);
            return state;
        } catch (NullPointerException e) {
            return false;
        }
    }
    public void setHyperchannelState(boolean state) {
        super.set("hyperstate", state);
    }

    public LinkedList<String> getResiduallHyper() {
        LinkedList<String> ll = new LinkedList<>();
        List<Object> list = super.getList("residualhyper",String.class);
        if (list!=null) {
            for (Object hyperId :list) {
                ll.add((String) hyperId);
            }
        }
        return ll;
    }

    public void setResidualHyper(LinkedList<String> residualHyper) {
        super.set("residualhyper",residualHyper);
    }

    public Category getHyperCategory(Guild guild) {
        String id =  (String) super.get("hypercategory",String.class);
        return (id == null || id.equals(""))? null : guild.getCategoryById(id);
    }

    public void setHyperCategory(Category category) {
        if (category!= null) {
            super.set("hypercategory",category.getId());
        } else {
            super.set("hypercategory","");
        }
    }

    // SHORTCUTS
    public LinkedList<ShortcutElement> getShortcuts() {
        LinkedList<ShortcutElement> ll = new LinkedList<>();
        List<Object> list = super.getList("shortcuts",ShortcutElement.class);
        if (list!=null) {
            for (Object shortcut :list) {
                ll.add((ShortcutElement) shortcut);
            }
        }
        return ll;
    }
    public void addShortcut(ShortcutElement shortcut) throws ObjectExistsException{
        LinkedList<ShortcutElement> shortcuts = getShortcuts();
        if (containsShortcutName(shortcuts,shortcut.getName())) {
            throw new ObjectExistsException();
        } else {
            shortcuts.add(shortcut);
            setShortcuts(shortcuts);
        }
    }

    public void removeShortcut(String name) throws NoSuchElementException {
        LinkedList<ShortcutElement> shortcuts = getShortcuts();
        if (!containsShortcutName(shortcuts,name)) {
            throw new NoSuchElementException();
        } else {
            LinkedList<ShortcutElement> l = new LinkedList<>();
            for (ShortcutElement shortcut : shortcuts) {
                if (!shortcut.getName().equals(name)) l.add(shortcut);
            }
            setShortcuts(l);
        }
    }

    private void setShortcuts(List<ShortcutElement> s) {
        super.set("shortcuts", s);
    }
    private boolean containsShortcutName(List<ShortcutElement> shortcuts, String name) {
        boolean in = false;
        for (ShortcutElement s: shortcuts) {
            if (s.getName().equals(name)) {
                in = true;
                break;
            }
        }
        return  in;
    }

    public static class ObjectExistsException extends Exception {}
}
