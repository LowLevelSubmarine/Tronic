package com.tronic.bot.stats;

import jdk.internal.jline.internal.Nullable;
import net.dv8tion.jda.api.entities.User;
import net.tetraowl.watcher.toolbox.JavaTools;
import org.apache.commons.lang.SerializationException;

import java.io.*;
import java.util.ArrayList;

public class StatisticsHandler {
    private final static String BASEUSERDIR = JavaTools.getJarUrl(StatisticsHandler.class)+"/data/users/";
    public static void  storeCommandStatistics(CommandStatisticsElement element, User user) {
     StatisticsSerializer serializer = new StatisticsSerializer();
     String serialized = serializer.serialize(element);
     if (serialized == null) {
         new SerializationException().printStackTrace();
         return;
     }
     try {
         File file = new File(BASEUSERDIR+user.getIdLong()+"/statistics");
         if (file.getParentFile().exists()) {
             file.getParentFile().mkdirs();
         }
         FileWriter fw = new FileWriter(file,true);
         fw.write(serialized);
         fw.write("\n");
         fw.close();
     } catch (IOException e) {
         //e.printStackTrace();
     }
    }

    @Nullable
    public static <T> Object getFirst(Class<T> clazz,User user) {
        StatisticsSerializer serializer = new StatisticsSerializer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(BASEUSERDIR+user.getIdLong()+"/statistics"));
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
    public static <T>ArrayList<Object> getAll(Class<T> clazz,User user) {
        StatisticsSerializer serializer = new StatisticsSerializer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(BASEUSERDIR+user.getIdLong()+"/statistics"));
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
}
