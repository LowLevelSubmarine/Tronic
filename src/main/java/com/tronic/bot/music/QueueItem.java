package com.tronic.bot.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.Markdown;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Collection;
import java.util.LinkedList;

public class QueueItem {

    private final LinkedList<Track> tracks;
    private final String name;
    private final State state = State.QUEUED;
    private final Member owner;
    private Message message;

    public QueueItem(Collection<Track> tracks, String name, Member owner) {
        this.tracks = new LinkedList<>(tracks);
        this.name = name;
        this.owner = owner;
        updateMessage();
    }

    public QueueItem(Track track, Member owner) {
        this.tracks = new LinkedList<>();
        this.tracks.add(track);
        this.name = null;
        this.owner = owner;
        updateMessage();
    }

    public AudioTrack pop() {
        updateMessage();
        return this.tracks.pop().getAudioTrack();
    }

    public String getName() {
        return this.name;
    }

    public boolean isEmpty() {
        return this.tracks.isEmpty();
    }

    public boolean isSingle() {
        return this.name == null;
    }

    public State getState() {
        return this.state;
    }

    public Member getOwner() {
        return this.owner;
    }

    private void updateMessage() {
        MessageEmbed embed;
        if (!this.tracks.isEmpty()) {
            embed = createMessage(this.tracks.getFirst().getTitle(), this.tracks.getFirst().getUri(), this.state);
        } else {
            embed = new TronicMessage("Work in progress!").b();
        }
        if (this.message == null) {
            this.message = getMusicChannel().sendMessage(embed).complete();
        } else {
            this.message.editMessage(embed).queue();
        }
    }

    private MessageEmbed createMessage(String text, String uri, State state) {
        return new TronicMessage(getEmojiByState(state).getUtf8() + " " + Markdown.uri(text, uri)).b();
    }

    private TextChannel getMusicChannel() {
        return this.owner.getGuild().getDefaultChannel();
    }

    private Emoji getEmojiByState(State state) {
        switch (state) {
            case QUEUED:
                return Emoji.ARROW_HEADING_DOWN;
            case PAUSED:
                return Emoji.PAUSE_BUTTON;
            case PLAYING:
                return Emoji.ARROW_FORWARD;
            case PLAYED:
                return Emoji.STOP_BUTTON;
            case SKIPPED:
                return Emoji.FAST_FORWARD;
            case DELETION: default:
                return Emoji.X;
        }
    }

    public enum State {
        QUEUED, PAUSED, PLAYING, PLAYED, SKIPPED, DELETION,
    }

}
