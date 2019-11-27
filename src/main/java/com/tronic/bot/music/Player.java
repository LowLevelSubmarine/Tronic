package com.tronic.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.LinkedList;

public class Player {

    private final AudioPlayer player;
    private final VoiceChannel voiceChannel;
    private final LinkedList<QueueItem> queue = new LinkedList<>();

    Player(AudioPlayer player, VoiceChannel voiceChannel) {
        this.player = player;
        this.voiceChannel = voiceChannel;
        voiceChannel.getGuild().getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
        this.player.addListener(this::onEvent);
    }

    public boolean isIdle() {
        return this.player.getPlayingTrack() == null;
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
            ensureConnection();
            QueueItem queueItem = this.queue.getFirst();
            this.player.playTrack(queueItem.pop());
        }
    }

    public void ensureConnection() {
        if (!this.voiceChannel.getGuild().getAudioManager().isConnected()) {
            this.voiceChannel.getGuild().getAudioManager().openAudioConnection(this.voiceChannel);
        }
    }

}
