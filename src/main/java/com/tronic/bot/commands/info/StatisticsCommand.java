package com.tronic.bot.commands.info;


import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.stats.CommandStatisticsElement;
import com.tronic.bot.stats.StatisticsHandler;
import net.dv8tion.jda.api.utils.AttachmentOption;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class StatisticsCommand implements Command {

    @Override
    public String invoke() {
        return "statistics";
    }

    @Override
    public Permission getPermission() {
        //TODO: Should be CO_HOST but Set to Intern to Lock Command
        return Permission.HOST;

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
        String message ="";
        HashMap<Long,ArrayList<CommandStatisticsElement>> a = StatisticsHandler.getStatisticsForUsers();
        for (Long l:a.keySet()) {
            message += "Commands for User: "+info.getJDA().getUserById(l).getName()+"("+l+"):\n";
            ArrayList<CommandStatisticsElement> ac = a.get(l);
            for (CommandStatisticsElement c:ac) {
                message+="\ttext:"+c.text+" command:"+c.getCommand()+" isAutocompleted:"+c.isAutocompleted()+" date:"+getDDMMYYY(c.getDate())+"\n";
            }
        }
        info.getChannel().sendFile(message.getBytes(),"Statistics.txt").complete();
    }

    public String getDDMMYYY(Long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY HH:mm");
        Date date = new Date(timestamp);
        return format.format(date);
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("statistics","Shows statistics","statistics");
    }

}
