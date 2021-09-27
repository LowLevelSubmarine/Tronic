package com.tronic.bot.music.sources.track_provider;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.tronic.bot.music.playing.PlaylistQueueItem;
import com.tronic.bot.music.playing.QueueItem;
import com.tronic.bot.music.playing.SingleQueueItem;
import com.tronic.bot.music.sources.Track;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LavaPlayerTrackProvider implements UrlTrackProvider {


    private static final Logger LOGGER = Logger.getLogger(LavaPlayerTrackProvider.class.getName());
    private final AudioPlayerManager audioPlayerManager;

    public LavaPlayerTrackProvider(AudioPlayerManager audioPlayerManager) {
        this.audioPlayerManager = audioPlayerManager;
    }

    @Override
    public QueueItem fromUrl(String url) {
        try {
            AudioLoadResultHandlerImpl resultHandler = new AudioLoadResultHandlerImpl();
            this.audioPlayerManager.loadItem(url, resultHandler).get();
            return resultHandler.getQueueItem();
        } catch (Exception e) {
            LOGGER.warning("Loading from url failed: " + e);
        }
        return null;
    }

    private final class AudioLoadResultHandlerImpl implements AudioLoadResultHandler {

        private QueueItem queueItem = null;
        private FriendlyException exception = null;

        public QueueItem getQueueItem() throws FriendlyException {
            if (exception != null) throw exception;
            return this.queueItem;
        }

        @Override
        public void trackLoaded(AudioTrack track) {
            this.queueItem = new SingleQueueItem(new LavaPlayerTrack(track));
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            List<Track> tracks = playlist.getTracks().stream().map(LavaPlayerTrack::new).collect(Collectors.toList());
            this.queueItem = new PlaylistQueueItem(playlist.getName(), "", tracks);
        }

        @Override
        public void noMatches() {}

        @Override
        public void loadFailed(FriendlyException exception) {
            this.exception = exception;
        }

    }

    private final class LavaPlayerTrack implements Track {

        AudioTrack audioTrack;
        AudioTrackInfo info;

        public LavaPlayerTrack(AudioTrack audioTrack) {
            this.audioTrack = audioTrack;
            this.info = audioTrack.getInfo();
        }

        @Override
        public String getDisplayName() {
            return Track.defaultDisplayName(this.info.author, this.info.title);
        }

        @Override
        public String getUrl() {
            return this.info.uri;
        }

        @Override
        public AudioTrack getAudioTrack() {
            return this.audioTrack;
        }

    }

}
