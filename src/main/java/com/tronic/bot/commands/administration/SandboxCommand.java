package com.tronic.bot.commands.administration;

import com.tronic.bot.commands.*;
import com.tronic.bot.core.Core;
import com.tronic.bot.stats.RangeStatistic;
import com.tronic.bot.tools.HTMLTable;
import com.tronic.bot.tools.StatisticsSaver;
import gui.ava.html.Html2Image;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class SandboxCommand implements Command {

    @Override
    public String invoke() {
        return "sandbox";
    }

    @Override
    public Permission getPermission() {
        return Permission.HOST;
    }

    @Override
    public boolean silent() {
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.ADMINISTRATION;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        Html2Image html2Image = new Html2Image();
        html2Image.getParser().loadHtml(getHTML(info.getCore()));
        BufferedImage bufIm = html2Image.getImageRenderer().getBufferedImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write( bufIm, "png", baos );
            baos.flush();
            info.getAuthor().openPrivateChannel().complete().sendFile(baos.toByteArray(),"statistics.png").queue();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private String getHTML(Core tronic) {
        RangeStatistic rs = new RangeStatistic(tronic.getJDA());
        String range = String.valueOf(rs.calc());
        int commands =0;
        LinkedList<LinkedList<String>> o = new LinkedList<>();
        LinkedList<String> p = new LinkedList<>();
        p.add("User");
        p.add("Commands");
        for (User user : tronic.getJDA().getUsers()) {
            LinkedList<String> content = new LinkedList<>();
            content.add(user.getName());
            try {
                String argument = tronic.getStorage().getUser(user).getStatistics().getLast().getArguments();
                content.add(argument);
            } catch (NoSuchElementException e) {}
            o.add(content);
            System.out.println();
        }
        String avatar = tronic.getJDA().getSelfUser().getAvatarUrl();


        String html = "<img src='"+avatar+"'></img><p>Tronic</p>" +getUserRange(tronic.getJDA())+getPerformedCommasndsALL(tronic)+ HTMLTable.HTMLTableElement(o, p);
        return html;
    }

    private String getUserRange(JDA jda) {
        RangeStatistic range = new RangeStatistic(jda);
        long userRange =range.calc();
        return "<table border='1|0'><tr><th>Ranged Users</th><th>"+userRange+"</th></tr></table>";
    }

    private String getPerformedCommasndsALL(Core tronic) {
        int all = 0;
        for (User user:tronic.getJDA().getUsers()) {
            LinkedList<StatisticsSaver.StatisticElement> commands = tronic.getStorage().getUser(user).getStatistics();
            for (StatisticsSaver.StatisticElement statEl : commands) {
                try {
                   if (!statEl.getArguments().equals("")) all++;
                } catch (Exception e) {}
            }

        }
        return "<table border='1|0'><tr><th>Performed Commands</th><th>"+all+"</th></tr></html>";

    }




    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Sandbox","dbg tool","sandbox");
    }

}

