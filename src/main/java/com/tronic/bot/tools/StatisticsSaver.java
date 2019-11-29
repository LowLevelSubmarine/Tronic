package com.tronic.bot.tools;

import com.toddway.shelf.Shelf;
import com.tronic.bot.Tronic;
import net.dv8tion.jda.api.entities.User;
import net.tetraowl.watcher.toolbox.JavaTools;

import java.io.File;
import java.util.Date;

public class StatisticsSaver {
    public static void saveCommand(StatisticElement statisticElement) {

    }

    public static class StatisticElement{
        private final String arguments;
        private final Date date;
        private final User user;
        private final boolean isAutocompleted;

        public StatisticElement(String arguments, Date date, User user, boolean isAutocompleted) {
            this.arguments = arguments;
            this.date = date;
            this.user = user;
            this.isAutocompleted = isAutocompleted;
        }

        public String getArguments() {
            return arguments;
        }

        public Date getDate() {
            return date;
        }

        public User getUser() {
            return user;
        }

        public boolean isAutocompleted() {
            return isAutocompleted;
        }
    }
}
