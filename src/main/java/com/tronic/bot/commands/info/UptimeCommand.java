package com.tronic.bot.commands.info;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.TimeUnit;

public class UptimeCommand implements Command {

    @Override
    public String invoke() {
        return "uptime";
    }

    @Override
    public Permission getPermission() {
        return Permission.NONE;
    }

    @Override
    public boolean silent() {
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        info.getEvent().getChannel().sendMessageEmbeds(new TronicMessage("Uptime",uptimeMessage()).b()).queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Uptime","Shows the uptime of the bot","uptime");
    }

    private String uptimeMessage() {
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        long millisec = rb.getUptime();
        long days = TimeUnit.MILLISECONDS.toDays(millisec);
        long hours = TimeUnit.MILLISECONDS.toHours(millisec) - (days*24);
        long minute = TimeUnit.MILLISECONDS.toMinutes(millisec) - (TimeUnit.MILLISECONDS.toHours(millisec)*60);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisec) - (TimeUnit.MILLISECONDS.toMinutes(millisec) *60);
        StringBuilder builder = new StringBuilder();
        builder.append("Tronic runs now for ");
        if (days>0) {
            boolean b = days==1;
            builder.append(b ? days+" day": days+" days");
        }
        if (hours>0) {
            boolean b = hours==1;
            if (days>0) builder.append(minute==0?" and ":", ");
            builder.append(b ? hours+" hour": hours+" hours");
        }
        if (minute>0) {
            boolean b = minute==1;
            if (hours>0) builder.append(seconds==0?" and ":", ");
            builder.append(b ? minute+" minute": minute+" minutes");
        }
        if (seconds>0) {
            boolean b = seconds==1;
            builder.append(minute>0||hours>0||days>1?" and ":" ");
            builder.append(b ? seconds+" second": seconds+" seconds");
        }
        return builder.toString();
    }

}
