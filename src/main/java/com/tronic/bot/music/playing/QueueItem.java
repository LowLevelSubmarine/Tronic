package com.tronic.bot.music.playing;

import com.tronic.bot.music.sources.Track;
import net.dv8tion.jda.api.entities.Member;

public interface QueueItem {

    String getId();
    String getName();
    String getUrl();
    boolean isMultiTrack();
    Track getCurrentTrack();
    boolean hasNextTrack();
    Track nextTrack();

}
