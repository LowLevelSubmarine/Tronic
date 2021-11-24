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

    private CachedTrack(String displayName, String url, AudioTrack audioTrack) {
        this.displayName = displayName;
        this.url = url;
        this.audioTrack = audioTrack;
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

    @Override
    public Track copy() {
        return new CachedTrack(
                this.displayName,
                this.url,
                this.audioTrack.makeClone()
        );
    }

}
