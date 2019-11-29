package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StaticStorage extends StorageElement {

    public StaticStorage(Shelf shelf) {
        super(shelf);
    }
    public void setCoHosters(LinkedList<User> hosters) {
        super.set("cohoster",hosters);
    }

    public void setCoHoster(User hoster) {
        LinkedList<User> ll = getCoHosters();
        ll.add(hoster);
        super.set("cohoster",ll);
    }

    public LinkedList<User> getCoHosters() {
        List<Object> list = super.getList("cohoster",User.class);
        LinkedList<User> ll = new LinkedList<>();
        if (list!= null) {
            for (Object user : list) {
                ll.add((User) user);
            }
        }
        return ll;
    }

}
