package com.tronic.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.tronic.bot.core.Core;
import com.tronic.bot.music.playing.Player;
import com.tronic.bot.music.playing.PlaylistQueueItem;
import com.tronic.bot.music.playing.QueueItem;
import com.tronic.bot.music.playing.SingleQueueItem;
import com.tronic.bot.music.sources.Track;
import com.tronic.bot.music.sources.TrackProvider;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.LinkedList;

public class MusicManager {

    private final Core core;
    private final TrackProvider trackProvider;
    private final HashMap<Guild, Player> players = new HashMap<>();
    private final AudioPlayerManager manager = createManager();

    public MusicManager(Core core) {
        this.core = core;
        this.trackProvider = new TrackProvider();
    }

    public TrackProvider getTrackProvider() {
        return this.trackProvider;
    }

    public Player getPlayer(Guild guild) {
        if (this.players.containsKey(guild)) {
            return this.players.get(guild);
        } else {
            return createPlayer(guild);
        }
    }

    public void loadQueueItem(String url, QueueItemResultListener listener, Member owner) {
        this.manager.loadItem(url, new QueueItemLoader(listener, owner));
    }

    private Player createPlayer(Guild guild) {
        Player player = new Player(this.core, guild, this.manager.createPlayer());
        this.players.put(guild, player);
        return player;
    }

    private static final AudioPlayerManager createManager() {
        AudioPlayerManager manager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(manager);
        return manager;
    }

    private static class QueueItemLoader implements AudioLoadResultHandler {

        private final QueueItemResultListener queueItemResultListener;
        private final Member owner;

        public QueueItemLoader(QueueItemResultListener queueItemResultListener, Member owner) {
            this.queueItemResultListener = queueItemResultListener;
            this.owner = owner;
        }

        @Override
        public void trackLoaded(AudioTrack track) {
            this.queueItemResultListener.onResult(new SingleQueueItem(new SimpleTrack(track), this.owner));
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            LinkedList<Track> tracks = new LinkedList<>();
            for (AudioTrack track : playlist.getTracks()) {
                tracks.add(new SimpleTrack(track));
            }
            this.queueItemResultListener.onResult(new PlaylistQueueItem(playlist.getName(), this.owner, tracks));
        }

        @Override
        public void noMatches() {
            this.queueItemResultListener.onError();
        }

        @Override
        public void loadFailed(FriendlyException exception) {
            this.queueItemResultListener.onError();
        }

        private static class SimpleTrack implements Track {

            private final AudioTrack audioTrack;

            public SimpleTrack(AudioTrack audioTrack) {
                this.audioTrack = audioTrack;
            }

            @Override
            public String getDisplayName() {
                return this.audioTrack.getInfo().title;
            }

            @Override
            public String getUrl() {
                return this.audioTrack.getInfo().uri;
            }

            @Override
            public AudioTrack getAudio() {
                return this.audioTrack;
            }

        }

    }

    public interface QueueItemResultListener {
        void onResult(QueueItem queueItem);
        void onError();
    }

}
