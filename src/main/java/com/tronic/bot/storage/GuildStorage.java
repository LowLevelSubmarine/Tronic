package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;

import java.io.File;

public class GuildStorage extends StorageElement {
    private static final String DEFAULT_PREFIX="!";
    GuildStorage(Shelf shelf) {
        super(shelf);
    }

    public String getPrefix() {
        String prefix = (String) super.get("prefix",String.class);
        return  prefix!=null ? prefix : DEFAULT_PREFIX ;
    }

    public void setPrefix(String prefix) {
        super.set("prefix",prefix);
    }
}
