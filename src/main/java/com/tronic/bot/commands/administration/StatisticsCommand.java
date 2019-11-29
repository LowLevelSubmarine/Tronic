package com.tronic.bot.commands.administration;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.StatisticsTool;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
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
        List<Guild> guilds = info.getJDA().getGuilds();
        ArrayList<StatisticsTool.TableObject> t = new ArrayList<>();
        JPanel p = getGuilds(guilds);
        info.getEvent().getAuthor().openPrivateChannel().queue((channel)->{
            channel.sendFile(StatisticsTool.jpanelToBAOS(p).toByteArray(),"statistics.png").queue();
        });
    }

    @Override
    public HelpInfo getHelpInfo() {
        return null;
    }


    private JPanel getGuilds(List<Guild> guildList) {
        GridLayout grid = new GridLayout(guildList.size(),1);
        JPanel p = new JPanel(grid);
        Object[][] rows = new Object[3][];
        for ( int i=0;i<guildList.size();i++) {
            Guild guild = guildList.get(i);
            rows[i] = new Object[]{guild.getName(), guild.getId(), guild.getMembers().size(), guild.getOwner().getUser().getName()};
        }
        String[] columns = {"Name", "Id", "Members", "Owner"};
        JTextArea t = new JTextArea("Tronic runs on the following Guilds:");
        JTable table = new JTable(rows,columns);
        p.add(t);
        p.add(table.getTableHeader());
        p.add(table);
        return p;
    }


}
