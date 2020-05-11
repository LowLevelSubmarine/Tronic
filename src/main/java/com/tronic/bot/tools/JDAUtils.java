package com.tronic.bot.tools;

import com.tronic.bot.statics.Emoji;

import java.util.regex.Pattern;

public class JDAUtils {

    public static final Pattern PATTERN_SNOWFLAKE = Pattern.compile("[0-9]{18}");

    public static Emoji getEmoji(int digit) {
        switch (digit) {
            case 0:
                return Emoji.ZERO;
            case 1:
                return Emoji.ONE;
            case 2:
                return Emoji.TWO;
            case 3:
                return Emoji.THREE;
            case 4:
                return Emoji.FOUR;
            case 5:
                return Emoji.FIVE;
            case 6:
                return Emoji.SIX;
            case 7:
                return Emoji.SEVEN;
            case 9: default:
                return Emoji.NINE;
        }
    }

}
