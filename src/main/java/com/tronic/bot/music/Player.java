package com.tronic.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.LinkedList;

public class Player {

    private final AudioPlayer player;
    private final AudioManager audioManager;
    private final LinkedList<QueueItem> queue = new LinkedList<>();

    Player(AudioPlayer player, Guild guild) {
        this.player = player;
        this.audioManager = guild.getAudioManager();
        this.player.addListener(this::onEvent);
        this.audioManager.setSendingHandler(new AudioPlayerSendHandler(player));
    }

    public boolean isPaused() {
        return this.player.isPaused();
    }

    public boolean isIdle() {
        return this.player.getPlayingTrack() == null;
    }

    public void skipHard() {
        this.queue.pop();
        loadNextTrack();
    }

    public void skipSoft() {
        loadNextTrack();
    }

    public void setPaused(boolean paused) {
        this.player.setPaused(paused);
    }

    public void queue(QueueItem queueItem) {
        this.queue.add(queueItem);
        if (isIdle()) {
            loadNextTrack();
        }
    }

    private void onEvent(AudioEvent event) {
        if (this.player.getPlayingTrack() == null) {
            loadNextTrack();
        }
    }

    private void loadNextTrack() {
        while (!this.queue.isEmpty() &&this.queue.getFirst().isEmpty()) {
            this.queue.pop();
        }
        if (!this.queue.isEmpty()) {
            QueueItem queueItem = this.queue.getFirst();
            ensureConnection(queueItem.getOwner());
            this.player.playTrack(queueItem.pop());
        }
    }

    private boolean ensureConnection(Member target) {
        if (!this.audioManager.isConnected()) {
            GuildVoiceState voiceState = target.getVoiceState();
            if (voiceState != null && voiceState.inVoiceChannel()) {
                this.audioManager.openAudioConnection(voiceState.getChannel());
                return true;
            }
        }
        return false;
    }

}
