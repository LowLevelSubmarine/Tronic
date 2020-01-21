package com.tronic.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.LinkedList;

public class Player {

    private final AudioPlayer player;
    private final AudioManager manager;
    private final QueueList<QueueItem> queue = new QueueList<>();
    private boolean paused = false;
    private LinkedList<EventListener> eventListeners = new LinkedList<>();

    public Player(Guild guild, AudioPlayer player) {
        this.player = player;
        this.manager = guild.getAudioManager();
        this.manager.setSendingHandler(new AudioPlayerSendHandler(this.player));
        player.addListener(this::onPlayerEvent);
    }

    public Guild getGuild() {
        return this.manager.getGuild();
    }

    public void addEventListener(EventListener eventListener) {
        this.eventListeners.add(eventListener);
    }

    public void removeEventListener(EventListener eventListener) {
        this.eventListeners.remove(eventListener);
    }

    public void addToQueue(QueueItem queueItem) {
        this.queue.add(queueItem);
        queueItem.initialize(this);
        if (isStopped()) {
            playNextTrack();
        }
    }

    public void removeFromQueue(QueueItem queueItem) {
        this.queue.remove(queueItem);
    }

    public void destroy() {
        this.player.destroy();
    }

    public boolean isPaused() {
        return this.player.isPaused();
    }

    public boolean isStopped() {
        return this.player.getPlayingTrack() == null;
    }

    public void setPaused(boolean paused) {
        this.player.setPaused(paused);
        sendStateChange();
    }

    public void skipSoft() {
        playNextTrack();
    }

    public void skipHard() {
        this.queue.removeCurrent();
        playNextTrack();
    }

    private boolean playNextTrack() {
        Track oldTrack = getCurrentTrack();
        Track newTrack = getNextTrack();
        if (newTrack != null) {
            this.player.playTrack(newTrack.getAudioTrack());
            ensureConnection(this.queue.getCurrent().getTarget());
            sendTrackChange(newTrack, oldTrack);
            return true;
        } else {
            this.player.stopTrack();
            return false;
        }
    }

    private Track getCurrentTrack() {
        if (this.queue.isPosValid() && this.queue.getCurrent().isPosValid()) {
            return this.queue.getCurrent().getCurrent();
        }
        return null;
    }

    private Track getNextTrack() {
        if (!this.queue.isIdle() && this.queue.getCurrent().hasNext()) {
            return this.queue.getCurrent().next();
        } else if (this.queue.hasNext() && this.queue.getNext().hasNext()) {
            return this.queue.next().next();
        } else {
            return null;
        }
    }

    private void onPlayerEvent(AudioEvent audioEvent) {
        if (this.paused != player.isPaused()) {
            this.paused = player.isPaused();
        }
        if (this.player.getPlayingTrack() == null) {
            if (!playNextTrack()) {
                this.manager.closeAudioConnection();
            }
        }
    }

    private boolean ensureConnection(Member target) {
        if (!this.manager.isConnected()) {
            GuildVoiceState voiceState = target.getVoiceState();
            if (voiceState != null && voiceState.inVoiceChannel()) {
                this.manager.openAudioConnection(voiceState.getChannel());
                return true;
            }
        }
        return false;
    }

    private void sendTrackChange(Track newTrack, Track oldTrack) {
        for (EventListener eventListener : this.eventListeners) {
            eventListener.onTrackChanged(newTrack, oldTrack, this);
        }
    }

    private void sendStateChange() {
        for (EventListener eventListener : this.eventListeners) {
            eventListener.onStateChanged(isPaused(), this);
        }
    }

    public interface EventListener {
        void onTrackChanged(Track newTrack, Track oldTrack, Player player);
        void onStateChanged(boolean isPaused, Player player);
    }

}
