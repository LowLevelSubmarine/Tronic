package com.tronic.bot.tools;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class StatisticsTool {
    public static JPanel createTableJPanel(ArrayList<TableObject> tarray) {
        GridLayout grid = new GridLayout(tarray.size()*2,1);
        JPanel p = new JPanel(grid);
        for (TableObject tObject:tarray) {
            JTable table = new JTable(tObject.rows,tObject.getTableHeader());
            JTableHeader tableHeader = table.getTableHeader();
            p.add(tableHeader);
            p.add(table);
        }
        return p;
    }


    public static ByteArrayOutputStream jpanelToBAOS(JPanel jPanel) {
        JScrollPane scroll = new JScrollPane(jPanel);
        scroll.setSize(scroll.getPreferredSize());


        // JTable must have been added to a TLC in order to render
        // correctly - go figure.
        JFrame f = new JFrame("Never shown");
        f.setContentPane(scroll);
        f.pack();
        //f.setVisible(true);

        int x = scroll.getWidth();
        int y = scroll.getHeight();

        scroll.setDoubleBuffered(false);

        BufferedImage bi = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);

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

    public static class TableObject {
        String[] tableHeader;
        Object[][] rows;
        public TableObject(String[] tableHeader,Object[][] rows) {
            this.rows = rows;
            this.tableHeader = tableHeader;
        }

        public String[] getTableHeader() {
            return tableHeader;
        }

        public Object[][] getRows() {
            return rows;
        }
    }
}
