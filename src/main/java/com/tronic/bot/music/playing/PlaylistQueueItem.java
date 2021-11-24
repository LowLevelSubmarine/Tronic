package com.tronic.bot.music.playing;

import com.tronic.bot.music.sources.CachedTrack;
import com.tronic.bot.music.sources.Track;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PlaylistQueueItem implements QueueItem {

    private final String name;
    private final String url;
    private final String id = "playlist #" + new Random().nextInt();
    private final QueueList<CachedTrack> tracks;

    public PlaylistQueueItem(String name, String url, List<Track> tracks) {
        this.name = name;
        this.url = url;
        List<CachedTrack> cachedTracks = tracks.stream().map(CachedTrack::new).collect(Collectors.toList());
        this.tracks = new QueueList<>(cachedTracks);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public boolean isMultiTrack() {
        return true;
    }

    @Override
    public Track getCurrentTrack() {
        return this.tracks.getCurrent();
    }

    @Override
    public Track getNextPossibleTrack() {
        try {
            return this.getCurrentTrack();
        } catch (IndexOutOfBoundsException e) {
            if (this.hasNextTrack()) {
                return this.tracks.getFirst();
            } else {
                return this.tracks.getLast();
            }
        }
    }

    @Override
    public boolean hasNextTrack() {
        return this.tracks.hasNext();
    }

    @Override
    public Track nextTrack() {
        return this.tracks.next();
    }

}
