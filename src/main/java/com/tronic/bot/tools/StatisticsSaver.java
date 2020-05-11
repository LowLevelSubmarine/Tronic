package com.tronic.bot.tools;

import com.tronic.bot.commands.Command;
import kotlin.reflect.KClass;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

import java.lang.annotation.Annotation;
import java.util.Date;

public class StatisticsSaver {
    public static class StatisticElement implements java.io.Serializable {
        private final String arguments;
        private final Date date;
        private final String userId;
        private final boolean isAutocompleted;

        public StatisticElement(String arguments, Date date, String userId, boolean isAutocompleted) {
            this.arguments = arguments;
            this.date = date;
            this.userId = userId;
            this.isAutocompleted = isAutocompleted;

        }

        public String getArguments() {
            return arguments;
        }

        public Date getDate() {
            return date;
        }

        public String getUserId() {
            return userId;
        }
        public User getUser(JDA jda) {
            return jda.getUserById(this.userId);
        }

        public boolean isAutocompleted() {
            return isAutocompleted;
        }


    }
}
