package com.tronic.bot.stats;


import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;


public class StatisticsSerializer  {
    public String serialize(Serializable s) {
        try {
            Field[] fields = s.getClass().getDeclaredFields();
            StringBuilder serialize = new StringBuilder();
            serialize.append(s.getClass().getSimpleName()).append(" ");
            for (Field f:fields) {
                f.setAccessible(true);
                if (f.getType() == String.class) {
                    serialize.append(f.getName()).append(":").append(f.get(s).toString()).append(";");
                } else {
                    serialize.append(f.getName()).append(":").append(f.get(s).toString()).append(";");
                }
            }
            return serialize.toString();
        }catch ( IllegalAccessException e) {
            e.printStackTrace();
            return "";
        }

    }

    @Nullable
    public <T> Object deserializeLine(String s, Class<T> c) {
        String className = s.substring(0,s.indexOf(" "));
        if (c.getSimpleName().equals(className)) {
            try {
                Object newClass = c.getConstructor().newInstance();
                ArrayList<String> fields = getFilteredFields(s.substring(s.indexOf(" ")+1));
                for (int i =0;i< fields.size();i++) {
                    String f = fields.get(i);
                    String[] parts = f.split(":");
                    Field declared = newClass.getClass().getDeclaredField(parts[0].replace(";",""));
                    boolean accesible = declared.isAccessible();
                    declared.setAccessible(true);
                    if (declared.getType() == long.class|| declared.getType() == Long.class) {
                        declared.set(newClass,Long.valueOf(parts[1]));
                    } else if (declared.getType() == Integer.class || declared.getType() == int.class) {
                        declared.set(newClass,Integer.valueOf(parts[1]));
                    }
                    else {
                        declared.set(newClass,declared.getType().cast(parts[1]));
                    }
                    declared.setAccessible(accesible);
                }
                return newClass;
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private ArrayList<String> getFilteredFields(String s) {
        ArrayList<String> fields = new ArrayList<>(Arrays.asList(s.split(";")));
        return fields;
    }
}
