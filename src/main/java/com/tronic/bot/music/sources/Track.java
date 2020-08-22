package com.tronic.bot.music.sources;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public interface Track {

    String getDisplayName();
    String getUrl();
    AudioTrack getAudio();

}
