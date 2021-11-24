package com.tronic.bot.tools;

import java.util.List;

public class CollectionUtils {

    public static <T> List<T> trim(List<T> list, int maxSize) {
        if (list.size() <= maxSize) {
            return list;
        } else {
            return list.subList(0, maxSize);
        }
    }
}
