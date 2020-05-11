package com.tronic.bot.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.Random;

public class Track {

    private final long id = new Random().nextLong();
    private final AudioTrack audioTrack;
    private final String title;
    private final String artist;
    private final String uri;

    public Track(AudioTrack audioTrack, String title, String artist, String uri) {
        this.audioTrack = audioTrack;
        this.title = title;
        this.artist = artist;
        this.uri = uri;
    }

    public Track(AudioTrack audioTrack) {
        this.audioTrack = audioTrack;
        this.title = audioTrack.getInfo().title;
        this.artist = audioTrack.getInfo().author;
        this.uri = audioTrack.getInfo().uri;
    }

    public AudioTrack getAudioTrack() {
        return this.audioTrack;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getUri() {
        return this.uri;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Track) {
            Track other = (Track) obj;
            return other.id == this.id && other.audioTrack.equals(this.audioTrack);
        }
        return false;
    }
}
