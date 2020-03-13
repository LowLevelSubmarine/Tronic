package com.tronic.bot.tools;
import com.lowagie.text.Document;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;


import javax.swing.text.html.HTMLDocument;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class HTMLTable {
    public static String HTMLTableElement(LinkedList<LinkedList<String>> columns, LinkedList<String> rows){
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1|0*'>");
        sb.append("<thead>");
        sb.append("<tr>");
        for (String s :rows) {
            sb.append("<th>").append(s).append("</th>");
        }
        sb.append("</tr>").append("</thead>");
        for(LinkedList<String> ll :columns) {
            sb.append("<tr>");
            for (String string:ll) {
                sb.append("<th>").append(string).append("</th>");
            }
            sb.append("</tr>");
        }
        sb.append("<tbody>");
        sb.append("</tbody>").append("</table>");
        return sb.toString();
    }


    /*StringBuilder sb = new StringBuilder();
        sb.append("<style>table,\n" +
                "th,\n" +
                "td {\n" +
                "\tborder: 1px solid black;\n" +
                "\tborder-collapse: separate;\n" +
                "}</style>");
        sb.append("<table>");
        sb.append("<thead>");
        sb.append("<tr>");
        for (String s :rows) {
            sb.append("<th>").append(s).append("</th>");
        }
        sb.append("</tr>").append("</thead>");
        for(LinkedList<String> ll :columns) {
            sb.append("<tr>");
            for (String string:ll) {
                sb.append("<th>").append(string).append("</th>");
            }
            sb.append("</tr>");
        }
        sb.append("<tbody>");
        sb.append("</tbody>").append("</table>");
        return sb.toString();*/
}
