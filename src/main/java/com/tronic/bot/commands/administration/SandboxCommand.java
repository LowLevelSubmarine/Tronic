package com.tronic.bot.commands.administration;

import com.toddway.shelf.Shelf;
import com.tronic.bot.commands.*;
import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

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
        info.getTronic().getStorage().getStatic().setCoHoster(info.getAuthor());
        LinkedList<User> hosters = info.getTronic().getStorage().getStatic().getCoHosters();
        for (User use : hosters) {
            info.getEvent().getChannel().sendMessage(use.getName()).queue();
        }
    }

    public ByteArrayOutputStream convertTextToGraphic() {
        Object[][] data = {
                {"Hari", new Integer(23), new Double(78.23), new Boolean(true)},
                {"James", new Integer(23), new Double(47.64), new Boolean(false)},
                {"Sally", new Integer(22), new Double(84.81), new Boolean(true)}
        };

        Object[][] datta = {
                {"FLO", new Integer(23), new Double(78.23), new Boolean(true)},
                {"JOHANNA", new Integer(23), new Double(47.64), new Boolean(false)},
                {"JULI", new Integer(22), new Double(84.81), new Boolean(true)}
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
            ImageIO.write(bi,"png",baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos;
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("","","");
    }
}
