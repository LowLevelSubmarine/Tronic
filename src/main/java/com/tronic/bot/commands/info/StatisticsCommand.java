package com.tronic.bot.commands.info;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.stats.CommandStatisticsElement;
import com.tronic.bot.stats.StatisticsHandler;
import com.tronic.bot.stats.StatisticsSerializer;
import com.tronic.bot.tools.StatisticsTool;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.tronic.bot.tools.StatisticsTool.createTableJPanel;

public class StatisticsCommand implements Command {
    @Override
    public String invoke() {
        return "statistics";
    }

    @Override
    public Permission getPermission() {
        return Permission.ADMIN;
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
        Object l = StatisticsHandler.getAll(CommandStatisticsElement.class,info.getAuthor());
        System.out.println(StatisticsHandler.getFirst(CommandStatisticsElement.class,info.getAuthor()));
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("statistics","Shows statistics","statistics");
    }

}
