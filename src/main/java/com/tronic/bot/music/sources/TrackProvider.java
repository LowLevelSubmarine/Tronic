package com.tronic.bot.music.sources;

import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.library.Song;

import java.util.LinkedList;
import java.util.List;

public class TrackProvider {

    private final YTML ytml = new YTML();
    private final YouTubeTrackProvider youTubeTrackProvider = new YouTubeTrackProvider();

    public TrackProvider() {
        this.ytml.fetchKey();
    }

    public YouTubeTrackProvider.YouTubeTrack magnetSearch(String query) {
        Song result = ytml.search(query).complete().getSongs().get().get(0);
        String displayName = result.getArtists() + " / " + result.getName();
        return this.youTubeTrackProvider.getTrack(displayName, result);
    }

    public LinkedList<YouTubeTrackProvider.YouTubeTrack> listSearch(String query) {
        List<Song> results = ytml.search(query).complete().getSongs().parse().complete();
        LinkedList<YouTubeTrackProvider.YouTubeTrack> youTubeTracks = new LinkedList<>();
        for (Song result : results) {
            String displayName = result.getArtists() + " / " + result.getName();
            youTubeTracks.add(this.youTubeTrackProvider.getTrack(displayName, result));
        }
        return youTubeTracks;
    }

}
