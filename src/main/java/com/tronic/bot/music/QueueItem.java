package com.tronic.bot.music;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Collection;

public class QueueItem {

    private final Member target;
    private final String title;
    private final QueueList<Track> tracks;
    private final MessageBuilder messageBuilder = new MessageBuilder();
    private Player player;
    private Message message;

    public QueueItem(Member target, Track track) {
        this.tracks = new QueueList<>(track);
        this.target = target;
        this.title = null;
    }

    public QueueItem(Member target, Collection<Track> tracks, String title) {
        this.tracks = new QueueList<>(tracks);
        this.target = target;
        this.title = title;
    }

    public Member getTarget() {
        return this.target;
    }

    public void initialize(Player player) {
        this.player = player;
        player.addEventListener(new EventListener());
        sendMessage(this.messageBuilder.buildQueueMessage(this.tracks.get(0).getTitle(), this.tracks.get(0).getUri()));
    }

    public String getDisplayName() {
        return this.tracks.get(0).getTitle();
    }

    public boolean hasNext() {
        return this.tracks.hasNext();
    }

    public Track getCurrent() {
        return this.tracks.getCurrent();
    }

    public Track next() {
        return this.tracks.next();
    }

    private static class EventListener implements Player.EventListener {

        @Override
        public void onTrackChanged(Track newTrack, Track oldTrack, Player player) {

        }

        @Override
        public void onStateChanged(boolean isPlaying, Player player) {

        }

    }

    public synchronized void sendMessage(MessageEmbed embed) {
        if (this.message == null) {
            this.message = getChannel().sendMessage(embed).complete();
        } else {
            this.message.editMessage(embed).complete();
        }
    }

    private TextChannel getChannel() {
        return this.player.getGuild().getDefaultChannel();
    }

}
