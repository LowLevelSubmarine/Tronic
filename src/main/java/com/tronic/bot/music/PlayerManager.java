package com.tronic.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.HashMap;
import java.util.LinkedList;

public class PlayerManager {

    private final HashMap<Guild, Player> players = new HashMap<>();
    private final AudioPlayerManager manager = createManager();

    public Player getPlayer(Guild guild, VoiceChannel voiceChannel) {
        if (this.players.containsKey(guild)) {
            return this.players.get(guild);
        } else {
            return createPlayer(guild, voiceChannel);
        }
    }

    public void loadQueueItem(String identifier, QueueItemLoadListener listener) {
        new QueueItemLoader(identifier, listener);
    }

    private Player createPlayer(Guild guild, VoiceChannel voiceChannel) {
        Player player = new Player(this.manager.createPlayer(), voiceChannel);
        this.players.put(guild, player);
        return player;
    }

    private static final AudioPlayerManager createManager() {
        AudioPlayerManager manager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(manager);
        return manager;
    }

    public interface QueueItemLoadListener {
        void onLoaded(QueueItemLoadResult queueItemLoadResult);
    }

    public static class QueueItemLoadResult {

        private final LinkedList<AudioTrack> tracks;
        private final boolean isSearchResult;
        private final String name;

        public QueueItemLoadResult(AudioPlaylist playlist) {
            this.tracks = new LinkedList<>(playlist.getTracks());
            this.isSearchResult = playlist.isSearchResult();
            this.name = playlist.getName();
        }

        public QueueItemLoadResult(AudioTrack track) {
            this.tracks = new LinkedList<>();
            this.tracks.add(track);
            this.isSearchResult = false;
            this.name = null;
        }

        public boolean isSearchResult() {
            return this.isSearchResult;
        }

        public boolean isSingle() {
            return !isSearchResult && this.tracks.size() == 1;
        }

        public boolean isPlaylist() {
            return !isSearchResult() && !isSingle();
        }

        public QueueItem get() {
            if (isSingle()) {
                return new QueueItem(new Track(this.tracks.getFirst()));
            } else {
                LinkedList<Track> tracks = new LinkedList<>();
                for (AudioTrack track : this.tracks) {
                    tracks.add(new Track(track));
                }
                return new QueueItem(tracks, this.name);
            }
        }

    }

    private class QueueItemLoader implements AudioLoadResultHandler {

        private final QueueItemLoadListener listener;

        public QueueItemLoader(String identifier, QueueItemLoadListener listener) {
            this.listener = listener;
            PlayerManager.this.manager.loadItem(identifier, this);
        }

        @Override
        public void trackLoaded(AudioTrack track) {
            this.listener.onLoaded(new QueueItemLoadResult(track));
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            this.listener.onLoaded(new QueueItemLoadResult(playlist));
        }

        @Override
        public void noMatches() {
            this.listener.onLoaded(null);
        }

        @Override
        public void loadFailed(FriendlyException exception) {
            this.listener.onLoaded(null);
        }

    }

}
