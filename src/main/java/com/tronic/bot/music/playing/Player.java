package com.tronic.bot.music.playing;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.IdentityHashMap;
import java.util.LinkedList;

public class Player extends AudioEventAdapter {

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

    public Core getCore() {
        return this.core;
    }

    public TextChannel getChannel() {
        return this.guild.getDefaultChannel();
    }

    public void addToQueue(QueueItem queueItem, Member owner) {
        this.queue.add(queueItem);
        new QueueMessage(queueItem, owner, this);
        ensurePlayback();
    }

    public void removeFromQueue(QueueItem queueItem) {
        this.queue.remove(queueItem);
        sendQueueItemRemoved(queueItem);
        ensurePlayback();
    }

    public void skip() {
        this.audioPlayer.stopTrack();
    }

    public void playlistSkip() {
        while (this.queue.getCurrent().hasNextTrack()) {
            this.queue.getCurrent().nextTrack();
        }
        playNextTrack(true);
    }

    public void stop() {
        while (this.queue.hasNext()) {
            QueueItem queueItem = this.queue.remove(this.queue.size());
            sendQueueItemRemoved(queueItem);
        }
        playNextTrack(true);
    }

    public boolean isPaused() {
        return this.audioPlayer.isPaused();
    }

    public void setPaused(boolean paused) {
        if (paused != isPaused()) {
            this.audioPlayer.setPaused(paused);
            this.sendStateChanged(paused);
        }
    }

    public QueueItem getCurrentQueueItem() {
        return this.queue.getCurrent();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack lastTrack, AudioTrackEndReason endReason) {
        playNextTrack(endReason == AudioTrackEndReason.STOPPED);
    }

    private boolean playNextTrack(boolean skip) {
        if (this.queue.isIdle() || !this.queue.getCurrent().hasNextTrack()) {
            if (this.queue.hasNext()) {
                this.queue.next();
            }
        }
        if (this.queue.getCurrent().hasNextTrack()) {
            this.audioPlayer.playTrack(this.queue.getCurrent().nextTrack().getAudioTrack());
            sendQueueItemChanged(this.queue.getPreviousNullable(), this.queue.getCurrent(), skip);
            QueueMessage message = sendRegisterSelf(this.queue.getCurrent());
            if (message != null) {
                ensureConnection(message.getOwner());
            }
            return true;
        } else {
            this.audioPlayer.stopTrack();
            sendQueueItemChanged(this.queue.getCurrent(), null, skip);
            this.guild.getAudioManager().closeAudioConnection();
            return false;
        }
    }

    private boolean isIdle() {
        return this.audioPlayer.getPlayingTrack() == null;
    }

    private void ensurePlayback() {
        if (isIdle()) {
            playNextTrack(false);
            if (isPaused()) {
                setPaused(false);
            }
        }
    }

    private boolean ensureConnection(Member target) {
        if (!this.guild.getAudioManager().isConnected()) {
            GuildVoiceState voiceState = target.getVoiceState();
            if (voiceState != null && voiceState.inVoiceChannel()) {
                this.guild.getAudioManager().openAudioConnection(voiceState.getChannel());
            }
            return true;
        }
        return false;
    }

    public void addEventListener(EventListener eventListener) {
        this.eventListeners.put(eventListener, eventListener);
    }

    public void removeEventListener(EventListener eventListener) {
        this.eventListeners.remove(eventListener);
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

    private QueueMessage sendRegisterSelf(QueueItem queueItem) {
        for (EventListener eventListener : new LinkedList<>(eventListeners.values())) {
            QueueMessage queueMessage = eventListener.registerSelf(queueItem);
            if (queueMessage != null) return queueMessage;
        }
        return null;
    }


    public interface EventListener {
        void onQueueItemRemoved(QueueItem queueItem, Player player);
        void onQueueItemChanged(QueueItem oldQueueItem, QueueItem newQueueItem, boolean skipped, Player player);
        void onVolumeChanged(float oldVolume, float newVolume, Player player);
        void onStateChanged(boolean paused, Player player);
        QueueMessage registerSelf(QueueItem queueItem);
    }

}
