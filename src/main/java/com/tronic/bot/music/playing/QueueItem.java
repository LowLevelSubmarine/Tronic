package com.tronic.bot.music.playing;

import com.tronic.bot.music.sources.Track;

public interface QueueItem {

    String getId();
    String getName();
    String getUrl();
    QueueItem copy();
    boolean isMultiTrack();
    Track getCurrentTrack();
    Track getNextPossibleTrack();
    boolean hasNextTrack();
    Track nextTrack();

}
