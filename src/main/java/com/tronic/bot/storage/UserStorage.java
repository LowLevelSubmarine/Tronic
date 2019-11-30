package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;
import com.tronic.bot.tools.StatisticsSaver;
import com.tronic.bot.tools.StatisticsTool;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class UserStorage extends StorageElement {

    UserStorage(Shelf shelf) {
        super(shelf);
    }

    public void storeSatistic(StatisticsSaver.StatisticElement element) {
        LinkedList<StatisticsSaver.StatisticElement> ll =getStatistics();
        ll.add(element);
        super.set("statistics",ll);
    }

    public LinkedList<StatisticsSaver.StatisticElement> getStatistics () {
        LinkedList<StatisticsSaver.StatisticElement> ll = new LinkedList<>();
        List<Object> list = super.getList("statistics", StatisticsSaver.StatisticElement.class);
        if (list!=null) {
            for (Object user :list) {
                ll.add((StatisticsSaver.StatisticElement) user);
            }
        }
        return ll;
    }

}
