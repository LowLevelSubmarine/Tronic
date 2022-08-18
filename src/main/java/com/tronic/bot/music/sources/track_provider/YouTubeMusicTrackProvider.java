package com.tronic.bot.music.sources.track_provider;

import com.lowlevelsubmarine.ytma.core.YTMA;
import com.lowlevelsubmarine.ytma.entity.Song;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.tronic.bot.music.playing.SingleQueueItem;
import com.tronic.bot.music.sources.Track;
import com.tronic.bot.tools.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class YouTubeMusicTrackProvider implements SearchTrackProvider {


    private final YTMA ytma = new YTMA();
    private final YoutubeAudioSourceManager audioSourceManager = new YoutubeAudioSourceManager();

    @Override
    public SingleQueueItem fromSingleSearch(String query) {
        var results = this.ytma.songPager(query).getResults();
        if (!results.isEmpty()) {
            Song song = this.ytma.songPager(query).getResults().get(0);
            if (song != null) {
                Track track = new YouTubeMusicTrack(song);
                return new SingleQueueItem(track);
            }
        }
        return null;
    }

    @Override
    public List<SingleQueueItem> fromMultiSearch(String query, int maxResults) {
        List<Song> songs = this.ytma.songPager(query).getResults();
        songs = CollectionUtils.trim(songs, maxResults);
        return songs.stream().map((song) -> new SingleQueueItem(new YouTubeMusicTrack(song))).collect(Collectors.toList());
    }

    public class YouTubeMusicTrack implements Track {

        private final String displayName;
        private final String url;
        private final YoutubeAudioTrack youtubeAudioTrack;

        private YouTubeMusicTrack(Song song) {
            this.displayName = Track.defaultDisplayName(song.getAuthor(), song.getTitle());
            this.url = "https://www.youtube.com/watch?v=" + song.getId();
            AudioTrackInfo audioTrackInfo = new AudioTrackInfo(
                    song.getTitle(),
                    song.getAuthor(),
                    song.getDuration(),
                    song.getId(),
                    false,
                    url
            );
            this.youtubeAudioTrack = new YoutubeAudioTrack(audioTrackInfo, YouTubeMusicTrackProvider.this.audioSourceManager);
        }

        private YouTubeMusicTrack(String displayName, String url, YoutubeAudioTrack youtubeAudioTrack) {
            this.displayName = displayName;
            this.url = url;
            this.youtubeAudioTrack = youtubeAudioTrack;
        }

        @Override
        public String getDisplayName() {
            return this.displayName;
        }

        @Override
        public String getUrl() {
            return this.url;
        }

        @Override
        public AudioTrack getAudioTrack() {
            return this.youtubeAudioTrack;
        }

        @Override
        public Track copy() {
            return new YouTubeMusicTrack(
                    this.displayName,
                    this.url,
                    (YoutubeAudioTrack) this.youtubeAudioTrack.makeClone() //TODO: Check for ClassCastException
            );
        }

    }

}
