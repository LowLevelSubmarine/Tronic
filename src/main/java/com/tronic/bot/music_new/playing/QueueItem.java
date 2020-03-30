package com.tronic.bot.music_new.playing;

import com.tronic.bot.music_new.sources.Track;
import net.dv8tion.jda.api.entities.Member;

public interface QueueItem {

    String getId();
    String getName();
    String getUrl();
    Member getOwner();
    boolean isMultiTrack();
    Track getCurrentTrack();
    boolean hasNextTrack();
    Track nextTrack();

}
