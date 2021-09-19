package com.tronic.bot.music.sources;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class CachedTrack implements Track {

    private final String displayName;
    private final String url;
    private final AudioTrack audioTrack;

    public CachedTrack(Track track) {
        this.displayName = track.getDisplayName();
        this.url = track.getUrl();
        this.audioTrack = track.getAudioTrack();
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public AudioTrack getAudioTrack() {
        return this.audioTrack;
    }

}
