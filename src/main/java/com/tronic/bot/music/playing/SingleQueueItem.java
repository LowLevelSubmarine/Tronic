package com.tronic.bot.music.playing;

import com.tronic.bot.music.sources.Track;
import net.dv8tion.jda.api.entities.Member;

import java.util.Random;

public class SingleQueueItem implements QueueItem {

    private final String id;
    private final Track track;
    private final Member owner;
    private boolean played = false;

    public SingleQueueItem(Track track, Member owner) {
        this.track = track;
        this.owner = owner;
        this.id = track.getUrl() + new Random().nextInt();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return track.getDisplayName();
    }

    @Override
    public String getUrl() {
        return this.track.getUrl();
    }

    @Override
    public Member getOwner() {
        return this.owner;
    }

    @Override
    public boolean isMultiTrack() {
        return false;
    }

    @Override
    public Track getCurrentTrack() {
        return this.played? this.track : null;
    }

    @Override
    public boolean hasNextTrack() {
        return !this.played;
    }

    @Override
    public Track nextTrack() {
        if (this.played) {
            return null;
        } else {
            this.played = true;
            return this.track;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QueueItem) {
            QueueItem queueItem = (QueueItem) obj;
            return queueItem.getId().equals(getId());
        }
        return false;
    }
}
