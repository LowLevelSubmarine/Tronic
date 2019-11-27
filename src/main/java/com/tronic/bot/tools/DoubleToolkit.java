package com.tronic.bot.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleToolkit {
    //Credits to stackoverflow-user: "Jonik"
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
