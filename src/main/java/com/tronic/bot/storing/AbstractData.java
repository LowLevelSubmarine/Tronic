package com.tronic.bot.storing;

import com.toddway.shelf.Shelf;

import java.io.File;

public abstract class AbstractData {

    private final Shelf shelf;

    public AbstractData(File file) {
        this.shelf = new Shelf()
    }

}
