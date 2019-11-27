package com.tronic.bot.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.tronic.bot.io.TronicMessage;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.LinkedList;

public class QueueItem {

    private final LinkedList<AudioTrack> queue = new LinkedList<>();

    public QueueItem() {

    }

    public MessageEmbed display() {
        return new TronicMessage().b();
    }

    public void skip() {
        this.queue.poll();
    }

    public void clear() {
        this.queue.clear();
    }

}
