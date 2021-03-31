package com.tronic.bot.commands;

import com.tronic.bot.statics.Emoji;

public enum CommandType {

    MUSIC("Music", Emoji.MUSICAL_NOTE),
    INFO("Info",Emoji.INFORMATION_SOURCE),
    FUN("Fun",Emoji.GAME_DICE),
    SETTINGS("Settings",Emoji.GEAR),
    ADMINISTRATION("Administration",Emoji.ROBOT);

    private final String displayName;
    private final Emoji emoji;

    CommandType(String displayName, Emoji emoji) {
        this.displayName = displayName;
        this.emoji = emoji;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Emoji getEmoji() {
        return emoji;
    }
}
