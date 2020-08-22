package com.tronic.bot.music.playing;

import com.tronic.bot.music.sources.Track;
import net.dv8tion.jda.api.entities.Member;

import java.util.Collection;
import java.util.Random;

public class PlaylistQueueItem implements QueueItem {

    private final String name;
    private final String id = "playlist #" + new Random().nextInt();
    private final QueueList<Track> tracks;
    private final Member owner;

    public PlaylistQueueItem(String name, Member owner, Collection<Track> tracks) {
        this.name = name;
        this.owner = owner;
        this.tracks = new QueueList<>(tracks);
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
        return this.tracks.get(0).getUrl();
    }

    @Override
    public Member getOwner() {
        return this.owner;
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
    public boolean hasNextTrack() {
        return this.tracks.hasNext();
    }

    @Override
    public Track nextTrack() {
        return this.tracks.next();
    }

}
