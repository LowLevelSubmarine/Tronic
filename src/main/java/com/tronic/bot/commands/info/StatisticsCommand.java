package com.tronic.bot.commands.info;


import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.stats.CommandStatisticsElement;
import com.tronic.bot.stats.StatisticsHandler;

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
        return Permission.INTERN;

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
            message += "Commands for User: "+l+":\n\n";
            ArrayList<CommandStatisticsElement> ac = a.get(l);
            for (CommandStatisticsElement c:ac) {
                message+="text:"+c.text+" command:"+c.getCommand()+" isAutocompleted:"+c.isAutocompleted()+" date:"+getDDMMYYY(c.getDate());
                info.getChannel().sendMessage(new TronicMessage(message).b()).complete();
                message = "";
            }
        }
    }

    public String getDDMMYYY(Long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY HH:mm");
        Date fechaNueva = new Date(timestamp);
        return format.format(fechaNueva);
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("statistics","Shows statistics","statistics");
    }

}
