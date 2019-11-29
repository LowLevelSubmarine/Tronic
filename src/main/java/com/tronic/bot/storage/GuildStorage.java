package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;

import java.io.File;

public class GuildStorage extends StorageElement {
    GuildStorage(Shelf shelf) {
        super(shelf);
    }

    public String getPrefix() {
        return (String) super.get("prefix",String.class);
    }

    public void setPrefix(String prefix) {
        super.set("prefix",prefix);
    }
}
