package com.tronic.bot.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.Collection;
import java.util.LinkedList;

public class QueueItem {

    private final LinkedList<Track> tracks;
    private final String name;
    private final State state = State.QUEUED;

    public QueueItem(Collection<Track> tracks, String name) {
        this.tracks = new LinkedList<>(tracks);
        this.name = name;

    }

    public QueueItem(Track track) {
        this.tracks = new LinkedList<>();
        this.tracks.add(track);
        this.name = null;
    }

    public AudioTrack pop() {
        return this.tracks.pop().getAudioTrack();
    }

    public String getName() {
        return this.name;
    }

    public boolean isEmpty() {
        return this.tracks.isEmpty();
    }

    public boolean isSingle() {
        return this.name == null;
    }

    public State getState() {
        return this.state;
    }

    private void updateMessage() {

    }

    public enum State {
        QUEUED, PAUSED, PLAYING, PLAYED, SKIPPED,
    }

}
