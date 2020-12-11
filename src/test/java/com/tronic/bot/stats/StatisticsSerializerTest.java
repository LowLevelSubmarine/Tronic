package com.tronic.bot.stats;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

import java.io.Serializable;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class StatisticsSerializerTest  {
    @Test
    public void testSerializer() {
        StatisticsSerializer s = new StatisticsSerializer();
        CommandStatisticsElement c = new CommandStatisticsElement("help","lol",true);
        System.out.println(s.serialize(new CommandStatisticsElement("help","lol",true)));
        CommandStatisticsElement e = (CommandStatisticsElement) s.deserializeLine(s.serialize(new CommandStatisticsElement("h;elp","lo;l",true)), CommandStatisticsElement.class);
        assertNotNull(e);
    }

}
