package com.tronic.bot.music;

import com.tronic.bot.buttons.Button;
import com.tronic.bot.statics.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Collection;

public class QueueItem {

    private final Member target;
    private final String title;
    private final QueueList<Track> tracks;
    private final EventListener eventListener = new EventListener();
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
        player.addEventListener(this.eventListener);
        sendMessageAsync(MessageBuilder.buildQueueMessage(this.tracks.get(0).getTitle(), this.tracks.get(0).getUri()));
    }

    public void destroy() {
        this.player.removeEventListener(this.eventListener);
    }

    public String getDisplayName() {
        return this.tracks.get(0).getTitle();
    }

    public boolean hasNext() {
        return this.tracks.hasNext();
    }

    public boolean isPosValid() {
        return this.tracks.isPosValid();
    }

    public Track getCurrent() {
        return this.tracks.getCurrent();
    }

    public Track next() {
        return this.tracks.next();
    }

    private class EventListener implements Player.EventListener {

        @Override
        public void onTrackChanged(Track newTrack, Track oldTrack, Player player) {
            if (QueueItem.this.tracks.contains(newTrack)) {
                sendActiveMessageAsync();
            } else if (QueueItem.this.tracks.contains(oldTrack)) {
                sendSkippedMessageAsync();
            }
        }

        @Override
        public void onStateChanged(boolean isPaused, Player player) {
            sendActiveMessageAsync();
        }

    }

    private void sendQueuedMessageAsync() {
        Track track = getCurrent();
        sendMessageAsync(MessageBuilder.buildQueueMessage(track.getTitle(), track.getUri()));
    }

    private void sendActiveMessageAsync() {
        Track track = getCurrent();
        if (player.isPaused()) {
            sendMessageAsync(MessageBuilder.buildPauseMessage(track.getTitle(), track.getUri()));
        } else {
            sendMessageAsync(MessageBuilder.buildPlayingMessage(track.getTitle(), track.getUri()));
        }
    }

    private void sendSkippedMessageAsync() {
        Track track = getCurrent();
        sendMessageAsync(MessageBuilder.buildSkippedMessage(track.getTitle(), track.getUri()));
    }

    private void sendDequeuedMessageAsync() {
        Track track = getCurrent();
        sendMessageAsync(MessageBuilder.buildDequeuedMessage(track.getTitle(), track.getUri()));
    }

    private void sendMessageAsync(MessageEmbed embed) {
        new Thread(() -> this.sendMessage(embed)).start();
    }

    private synchronized void sendMessage(MessageEmbed embed) {
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
