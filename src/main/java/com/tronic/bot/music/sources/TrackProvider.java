package com.tronic.bot.music.sources;

import com.lowlevelsubmarine.ytma.core.YTMA;
import com.lowlevelsubmarine.ytma.entity.Song;
import com.lowlevelsubmarine.ytma.request.MediaTypeSearchRequest;

import java.util.LinkedList;

public class TrackProvider {

    private final YTMA ytma = new YTMA();
    private final YouTubeTrackProvider youTubeTrackProvider = new YouTubeTrackProvider();


    public YouTubeTrackProvider.YouTubeTrack magnetSearch(String query) {
        Song result = this.ytma.search(query).getSongs().get(0);
        String displayName = result.getAuthor() + " / " + result.getTitle();
        return this.youTubeTrackProvider.getTrack(displayName, result);
    }

    public LinkedList<YouTubeTrackProvider.YouTubeTrack> listSearch(String query) {
        MediaTypeSearchRequest<Song> searchRequest = this.ytma.search(query).getSongPager();
        searchRequest.fetchNext();
        LinkedList<YouTubeTrackProvider.YouTubeTrack> youTubeTracks = new LinkedList<>();
        for (Song song : searchRequest.getResults()) {
            String displayName = song.getAuthor() + " / " + song.getTitle();
            youTubeTracks.add(this.youTubeTrackProvider.getTrack(displayName, song));
        }
        return youTubeTracks;
    }

}
