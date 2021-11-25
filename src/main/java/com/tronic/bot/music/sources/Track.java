package com.tronic.bot.music.sources;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public interface Track {

    String getDisplayName();
    String getUrl();
    AudioTrack getAudioTrack();
    Track copy();

    static String defaultDisplayName(String author, String title) {
        return author + " / " + title;
    }

}
