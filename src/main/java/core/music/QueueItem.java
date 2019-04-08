package core.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.LinkedList;

public class QueueItem {

    public enum State {QUEUED, PLAYING, PAUSED, PLAYED, SKIPPED, DEQUEUED}

    private final LinkedList<AudioTrack> tracks;
    private final String playlistTitle;
    private State state = State.QUEUED;

    public QueueItem(AudioTrack track) {
        this.tracks = new LinkedList<>();
        this.tracks.add(track);
        this.playlistTitle = null;
        onStateUpdate();
    }

    public QueueItem(LinkedList<AudioTrack> tracks, String playlistTitle) {
        this.tracks = tracks;
        this.playlistTitle = playlistTitle;
    }

    public void setState(State state) {
        this.state = state;
        onStateUpdate();
    }

    public State getState() {
        return this.state;
    }

    public boolean isPlaylist() {
        return this.playlistTitle == null;
    }

    private void onStateUpdate() {
        if (this.state.equals(State.QUEUED)) {

        } else if (this.state.equals(State.PLAYING)) {

        } else if (this.state.equals(State.PAUSED)) {

        } else if (this.state.equals(State.PLAYED)) {

        } else if (this.state.equals(State.SKIPPED)) {

        } else if (this.state.equals(State.DEQUEUED)) {

        }
    }

}
