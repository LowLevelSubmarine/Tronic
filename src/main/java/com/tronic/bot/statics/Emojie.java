package com.tronic.bot.statics;

public enum Emojie {

    GRINNING,
    SMILEY,
    SMILE,
    GRIN,
    LAUGHING,
    SWEAT_SMILE,
    JOY;
    //TODO: Generate automatically

    private final String raw;

    Emojie() {
        this.raw = this.name().toLowerCase();
    }

    Emojie(String raw) {
        this.raw = raw;
    }

    public String getRaw() {
        return this.raw;
    }

}
