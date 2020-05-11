package com.tronic.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.LinkedList;

public class PlayerManager {

    private final Core core;
    private final HashMap<Guild, Player> players = new HashMap<>();
    private final AudioPlayerManager manager = createManager();

    public PlayerManager(Core core) {
        this.core = core;
    }

    public Player getPlayer(Guild guild) {
        if (this.players.containsKey(guild)) {
            return this.players.get(guild);
        } else {
            return createPlayer(guild);
        }
    }

    public void loadQueueItem(String identifier, Member owner, QueueItemLoadListener listener) {
        new QueueItemLoader(identifier, owner, listener);
    }

    private Player createPlayer(Guild guild) {
        Player player = new Player(guild, this.manager.createPlayer());
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

    public class QueueItemLoadResult {

        private final LinkedList<AudioTrack> tracks;
        private final boolean isSearchResult;
        private final boolean isPlaylist;
        private final String name;
        private final Member owner;

        public QueueItemLoadResult(AudioPlaylist playlist, Member owner) {
            this.tracks = new LinkedList<>(playlist.getTracks());
            this.isSearchResult = playlist.isSearchResult();
            this.isPlaylist = !this.isSearchResult;
            this.name = playlist.getName();
            this.owner = owner;
        }

        public QueueItemLoadResult(AudioTrack track, Member owner) {
            this.tracks = new LinkedList<>();
            this.tracks.add(track);
            this.isSearchResult = false;
            this.isPlaylist = false;
            this.name = null;
            this.owner = owner;
        }

        public boolean isSearchResult() {
            return this.isSearchResult;
        }

        public boolean isSingle() {
            return !isSearchResult() && !this.isPlaylist;
        }

        public boolean isPlaylist() {
            return !isSearchResult() && this.isPlaylist;
        }

        public QueueItem get() {
            if (!isPlaylist()) {
                return new QueueItem(this.owner, new Track(this.tracks.getFirst()));
            } else {
                LinkedList<Track> tracks = new LinkedList<>();
                for (AudioTrack track : this.tracks) {
                    tracks.add(new Track(track));
                }
                return new QueueItem(this.owner, tracks, this.name);
            }
        }

        public LinkedList<QueueItem> getSearchResults() {
            LinkedList<QueueItem> queueItems = new LinkedList<>();
            for (AudioTrack track : this.tracks) {
                queueItems.add(new QueueItem(this.owner, new Track(track)));
            }
            return queueItems;
        }

    }

    private class QueueItemLoader implements AudioLoadResultHandler {

        private final QueueItemLoadListener listener;
        private final Member owner;

        public QueueItemLoader(String identifier, Member owner, QueueItemLoadListener listener) {
            this.listener = listener;
            this.owner = owner;
            PlayerManager.this.manager.loadItem(identifier, this);
        }

        @Override
        public void trackLoaded(AudioTrack track) {
            this.listener.onLoaded(new QueueItemLoadResult(track, this.owner));
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            this.listener.onLoaded(new QueueItemLoadResult(playlist, this.owner));
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
