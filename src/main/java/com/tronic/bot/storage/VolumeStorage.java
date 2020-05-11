package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;
import com.tronic.bot.core.Core;

import java.util.ArrayList;
import java.util.List;

public class VolumeStorage extends StorageElement {
    StaticStorage staticStorage;
    VolumeStorage(Shelf shelf, StaticStorage staticStorage) {
        super(shelf);
        this.staticStorage = staticStorage;
    }
    public int getCalculatedValue() {
        return calculateArithm()*this.staticStorage.getBotVolume();
    }

    public int getRawValue() {
        return calculateArithm();
    }

    public int getBigCalculatedValue() {
        return getCalculatedValue() *100;
    }

    public void storeValue(int value) {
        ArrayList<Integer> volumes = getArray();
        volumes.add(value);
        super.set("volumes",volumes);
    }

    private ArrayList<Integer> getArray() {
        List<Object> list = super.getList("volumes",Integer.class);
        ArrayList<Integer> volumes = new ArrayList<>();
        if (list!=null) {
            for (Object inte :list) {
                volumes.add((Integer) inte);
            }
        }
        return  volumes;
    }
    private int calculateArithm() {
        int arithtemp=0;
        for (int i=0;i<getArray().size();i++) {
            arithtemp = arithtemp + getArray().get(0);
        }
        return arithtemp/ getArray().size();
    }
}

