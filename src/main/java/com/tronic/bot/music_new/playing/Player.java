package com.tronic.bot.music_new.playing;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.IdentityHashMap;
import java.util.LinkedList;

public class Player implements AudioEventListener {

    private final Core core;
    private final Guild guild;
    private final AudioPlayer audioPlayer;
    private final IdentityHashMap<EventListener, EventListener> eventListeners = new IdentityHashMap<>();
    private final QueueList<QueueItem> queue = new QueueList<>();

    public Player(Core core, Guild guild, AudioPlayer audioPlayer) {
        this.core = core;
        this.guild = guild;
        this.audioPlayer = audioPlayer;
        this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(this.audioPlayer));
        this.audioPlayer.addListener(this);
    }

    public void addToQueue(QueueItem queueItem) {
        this.queue.add(queueItem);
        new QueueMessage(queueItem, this);
        ensurePlayback();
    }

    public void removeFromQueue(QueueItem queueItem) {
        this.queue.remove(queueItem);
        sendQueueItemRemoved(queueItem);
        ensurePlayback();
    }

    public QueueItem getCurrentQueueItem() {
        return this.queue.getCurrent();
    }

    public boolean skip() {
        return playNext(true);
    }

    public boolean playlistSkip() {
        return playNext(true);
    }

    public void stop() {
        while (this.queue.hasNext()) {
            QueueItem queueItem = this.queue.remove(this.queue.size());
            sendQueueItemRemoved(queueItem);
            playNext(true);
        }
    }

    public void setPaused(boolean paused) {
        if (paused != isPaused()) {
            this.audioPlayer.setPaused(paused);
            this.sendStateChanged(paused);
        }
    }

    public boolean isPaused() {
        return this.audioPlayer.isPaused();
    }

    public void addEventListener(EventListener eventListener) {
        this.eventListeners.put(eventListener, eventListener);
    }

    public void removeEventListener(EventListener eventListener) {
        this.eventListeners.remove(eventListener);
    }

    @Override
    public void onEvent(AudioEvent event) {
        if (this.audioPlayer.getPlayingTrack() == null) {
            playNext(false);
        }
    }

    TextChannel getChannel() {
        return this.guild.getDefaultChannel();
    }

    Core getCore() {
        return this.core;
    }

    private void ensurePlayback() {
        if (isIdle()) {
            playNext(false);
            if (isPaused()) {
                setPaused(false);
            }
        }
    }

    private boolean isIdle() {
        return this.audioPlayer.getPlayingTrack() == null;
    }

    private boolean playNext(boolean skip) {
        if (this.queue.isIdle() || !this.queue.getCurrent().hasNextTrack()) {
            if (this.queue.hasNext()) {
                this.queue.next();
            }
        }
        if (this.queue.getCurrent().hasNextTrack()) {
            this.audioPlayer.playTrack(this.queue.getCurrent().nextTrack().getAudio());
            sendQueueItemChanged(this.queue.getPreviousNullable(), this.queue.getCurrent(), skip);
            ensureConnection(this.queue.getCurrent().getOwner());
            return true;
        } else {
            this.audioPlayer.removeListener(this);
            this.audioPlayer.stopTrack();
            this.audioPlayer.addListener(this);
            sendQueueItemChanged(this.queue.getCurrent(), null, skip);
            this.guild.getAudioManager().closeAudioConnection();
            return false;
        }
    }

    private boolean ensureConnection(Member target) {
        if (!this.guild.getAudioManager().isConnected()) {
            GuildVoiceState voiceState = target.getVoiceState();
            if (voiceState != null && voiceState.inVoiceChannel()) {
                this.guild.getAudioManager().openAudioConnection(voiceState.getChannel());
                return true;
            }
        }
        return false;
    }

    private void sendQueueItemRemoved(QueueItem queueItem) {
        for (EventListener eventListener : new LinkedList<>(eventListeners.values())) {
            eventListener.onQueueItemRemoved(queueItem, this);
        }
    }

    private void sendQueueItemChanged(QueueItem oldQueueItem, QueueItem newQueueItem, boolean skipped) {
        for (EventListener eventListener : new LinkedList<>(eventListeners.values())) {
            eventListener.onQueueItemChanged(oldQueueItem, newQueueItem, skipped, this);
        }
    }

    private void sendVolumeChanged(float oldVolume, float newVolume) {
        for (EventListener eventListener : new LinkedList<>(eventListeners.values())) {
            eventListener.onVolumeChanged(oldVolume, newVolume, this);
        }
    }

    private void sendStateChanged(boolean paused) {
        for (EventListener eventListener : new LinkedList<>(eventListeners.values())) {
            eventListener.onStateChanged(paused, this);
        }
    }

    public interface EventListener {
        void onQueueItemRemoved(QueueItem queueItem, Player player);
        void onQueueItemChanged(QueueItem oldQueueItem, QueueItem newQueueItem, boolean skipped, Player player);
        void onVolumeChanged(float oldVolume, float newVolume, Player player);
        void onStateChanged(boolean paused, Player player);
    }

}
