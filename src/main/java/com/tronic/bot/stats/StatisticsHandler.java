package com.tronic.bot.stats;

import com.tronic.bot.statics.Files;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Nullable;
import org.jsoup.SerializationException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class StatisticsHandler {

    private final static String BASEUSERDIR = new File(Files.ROOT_FOLDER, "data/users/").getAbsolutePath();

    public static void  storeCommandStatistics(CommandStatisticsElement element, User user) {
        StatisticsSerializer serializer = new StatisticsSerializer();
        String serialized = serializer.serialize(element);
        if (serialized == null) {
            new SerializationException().printStackTrace();
            return;
        }
        try {
            File file = new File(BASEUSERDIR+"/"+user.getIdLong()+"/statistics");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileWriter fw = new FileWriter(file,true);
            fw.write(serialized);
            fw.write("\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public static <T> Object getFirst(Class<T> clazz,Long user) {
        StatisticsSerializer serializer = new StatisticsSerializer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(BASEUSERDIR+"/"+user+"/statistics"));
            Object o = serializer.deserializeLine(reader.readLine(),clazz);
            while (o== null) {
                o = serializer.deserializeLine(reader.readLine(),clazz);
            }
            reader.close();
            return o;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static <T>ArrayList<Object> getAll(Class<T> clazz,Long user) {
        StatisticsSerializer serializer = new StatisticsSerializer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(BASEUSERDIR+"/"+user+"/statistics"));
            String line = reader.readLine();
            ArrayList<Object> all = new ArrayList<>();
            while (line!= null) {
                if (serializer.deserializeLine(line,clazz)!=null) {
                    all.add(serializer.deserializeLine(line,clazz));
                }
                line = reader.readLine();
            }
            reader.close();
            return all;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static public HashMap<Long,ArrayList<CommandStatisticsElement>> getStatisticsForUsers() {
        HashMap<Long,ArrayList<CommandStatisticsElement>> map = new HashMap<>();
        File dir = new File(BASEUSERDIR);
        File[] files = dir.listFiles();
        for (File file: files) {
            if (file.isDirectory() && new File(file,"statistics").exists()) {
                ArrayList<Object> o = getAll(CommandStatisticsElement.class,Long.parseLong(file.getName()));
                ArrayList<CommandStatisticsElement> a = new ArrayList<>();
                for (Object c: o) {
                    a.add((CommandStatisticsElement) c);
                }
                map.put(Long.parseLong(file.getName()),a);
            }
        }
        return map;
    }
}
