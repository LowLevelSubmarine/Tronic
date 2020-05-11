package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;
import com.tronic.bot.statics.Presets;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StaticStorage extends StorageElement {

    public StaticStorage(Shelf shelf) {
        super(shelf);
    }
    private void setCoHosters(LinkedList<String> hosters) {
        super.set("cohoster",hosters);
    }

    public void addCoHoster(User hoster) {
        LinkedList<String> ll = new LinkedList<>();
        List<Object> list = super.getList("cohoster",String.class);
        if (list!=null) {
            for (Object user :list) {
                ll.add((String) user);
            }
        }
        ll.add(hoster.getId());
        super.set("cohoster",ll);
    }

    public void removeCoHoster(User hoster) {
        LinkedList<String> ll = new LinkedList<>();
        List<Object> list = super.getList("cohoster",String.class);
        if (list!=null) {
            for (Object user :list) {
                String id = (String) user;
                if (!id.equals(hoster.getId())) {
                    ll.add(id);
                }
            }
            setCoHosters(ll);
        }
    }

    public boolean isCoHoster(User hoster) {
        LinkedList<String> ll = new LinkedList<>();
        List<Object> list = super.getList("cohoster",String.class);
        if (list!=null) {
            for (Object user :list) {
                String id = (String) user;
                if (id.equals(hoster.getId())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public void setBotVolume (int vol){
        super.set("volume",vol);
    }

    public int getBotVolume() {
        Integer vol = (Integer) super.get("volume",Integer.class);
        return vol == null ? Presets.VOLUME: vol;
    }


}
