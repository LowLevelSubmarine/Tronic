package com.tronic.bot.commands.administration;

import com.lowlevelsubmarine.subsconsole.graphs.ListGraphRenderer;
import com.lowlevelsubmarine.subsconsole.graphs.Vertex;
import com.tronic.bot.commands.*;
import com.tronic.bot.stats.CommandStatisticsElement;
import com.tronic.bot.stats.StatisticsGraphRenderer;
import com.tronic.bot.stats.StatisticsHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;


public class StatisticsCommand implements Command {

    @Override
    public String invoke() {
        return "statistics";
    }

    @Override
    public Permission getPermission() {
        return Permission.CO_HOST;
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
        HashMap<Long,ArrayList<CommandStatisticsElement>> a = StatisticsHandler.getStatisticsForUsers();
        HashMap<String, Integer> commandAmount = new HashMap<>();
        for (Long l:a.keySet()) {
            for (CommandStatisticsElement cs: a.get(l)) {
                commandAmount.put(cs.getCommand(),commandAmount.getOrDefault(cs.getCommand(),0)+1);
            }
        }
        int maxValue = commandAmount.values().stream().max(Comparator.comparingInt(Integer::intValue)).get();
        StatisticsGraphRenderer<Integer> lg = new StatisticsGraphRenderer<>(40,maxValue+1);
        for (String s:commandAmount.keySet()) {
            lg.addVertex(new Vertex<>(s,commandAmount.get(s)));
        }
        info.getChannel().sendFile(lg.render().getBytes(),"Statistics.txt").submit();
    }

    public String getDDMMYYY(Long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = new Date(timestamp);
        return format.format(date);
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("statistics","Shows statistics","statistics");
    }

}
