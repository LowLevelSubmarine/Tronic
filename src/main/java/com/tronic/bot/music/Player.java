package com.tronic.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;

import java.util.LinkedList;

public class Player {

    private final AudioPlayer player;
    private final LinkedList<Track> queue = new LinkedList<>();

    Player(AudioPlayer player) {
        this.player = player;
        this.player.addListener(this::onEvent);
    }

    public boolean isEmpty() {
        return this.player.getPlayingTrack() == null;
    }

    public void setPaused(boolean paused) {
        this.player.setPaused(paused);
    }

    private void onEvent(AudioEvent event) {
        if (isEmpty()) {
            //Play next track
        }
    }

}
