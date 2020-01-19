package com.tronic.bot.commands.administration;

import com.tronic.bot.commands.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SandboxCommand implements Command {
    @Override
    public String invoke() {
        return "sandbox";
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
        return CommandType.ADMINISTRATION;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        info.getCore().getStorage().getStatic().addCoHoster(info.getAuthor());
    }

    public ByteArrayOutputStream convertTextToGraphic() {
        Object[][] data = {
                {"Hari", 23, 78.23, true},
                {"James", 23, 47.64, false},
                {"Sally", 22, 84.81, true}
        };

        Object[][] datta = {
                {"FLO", 23, 78.23, true},
                {"JOHANNA", 23, 47.64, false},
                {"JULI", 22, 84.81, true}
        };
        Object[] columns = {"Name", "Age", "GPA", "Pass"};
        JTable table = new JTable(data, columns);
        JTable tablee = new JTable(datta, columns);
        JTableHeader h = table.getTableHeader();
        GridLayout grid = new GridLayout(3,1);
        grid.setColumns(0);
        JPanel p = new JPanel(grid);
        p.add(h);
        p.add(table);
        p.add(tablee);
        JScrollPane scroll = new JScrollPane(p);


        // JTable must have been added to a TLC in order to render
        // correctly - go figure.
        JFrame f = new JFrame("Never shown");
        f.setContentPane(scroll);
        f.pack();
        f.setVisible(true);

        Dimension dH = h.getSize();
        Dimension dT = table.getSize();
        int x = (int) scroll.getWidth();
        int y = (int)scroll.getHeight();

        scroll.setDoubleBuffered(false);

        BufferedImage bi = new BufferedImage(
                (int)x,
                (int)y,
                BufferedImage.TYPE_INT_RGB
        );

        Graphics g = bi.createGraphics();
        scroll.paint(g);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufImg, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        info.getEvent().getChannel().sendFile(baos.toByteArray(), "hey.png").queue();
    }

    private String getHTML(Tronic tronic) {
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

    private String getPerformedCommasndsALL(Tronic tronic) {
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

