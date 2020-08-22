package com.tronic.bot.music.sources;

import com.lowlevelsubmarine.ytml.library.Song;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

public class YouTubeTrackProvider {

    private final YoutubeAudioSourceManager audioSourceManager = new YoutubeAudioSourceManager();

    public YouTubeTrack getTrack(String displayName, Song song) {
        String url = "https://www.youtube.com/watch?v=" + song.getId();
        AudioTrackInfo audioTrackInfo = new AudioTrackInfo(song.getName(),
                song.getArtists(),
                song.getDuration(),
                song.getId(),
                false,
                url);
        YoutubeAudioTrack youtubeAudioTrack = new YoutubeAudioTrack(audioTrackInfo, this.audioSourceManager);
        return new YouTubeTrack(displayName, url, youtubeAudioTrack);
    }

    public static class YouTubeTrack implements Track {

        private final String displayName;
        private final String url;
        private final YoutubeAudioTrack youtubeAudioTrack;

        private YouTubeTrack(String displayName, String url, YoutubeAudioTrack youtubeAudioTrack) {
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
        public AudioTrack getAudio() {
            return this.youtubeAudioTrack;
        }

    }

}
