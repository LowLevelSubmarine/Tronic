package com.tronic.bot.tools;

public class Markdown {

    public static String uri(String text, String uri) {
        return "[" + text + "](" + uri + ")";
    }
    public static String codeblock(String text) { return "`" + text + "`"; }

}
