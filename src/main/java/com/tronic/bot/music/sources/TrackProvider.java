package com.tronic.bot.music.sources;

import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.YTMLBuilder;
import com.lowlevelsubmarine.ytml.library.fields.SongFields;
import com.lowlevelsubmarine.ytml.library.interfaces.Content;
import com.lowlevelsubmarine.ytml.library.interfaces.Search;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TrackProvider {

    private final YTML ytml = new YTMLBuilder().build().complete();
    private final YouTubeTrackProvider youTubeTrackProvider = new YouTubeTrackProvider();


    public YouTubeTrackProvider.YouTubeTrack magnetSearch(String query) {
        Content<SongFields> result = ytml.search(query).complete().getSongSection().getItems().get(0);
        String displayName = result.getArtist() + " / " + result.getTitle();
        return this.youTubeTrackProvider.getTrack(displayName, result);
    }

    public LinkedList<YouTubeTrackProvider.YouTubeTrack> listSearch(String query) {
        Search search = ytml.search(query).complete();
        try {
            search.getSongSection().fetchMore();
        } catch(IOException e) {
            System.err.println("Exception in fetch more.");
            e.printStackTrace();
        }
        List<Content<SongFields>> results = search.getSongSection().getItems();
        LinkedList<YouTubeTrackProvider.YouTubeTrack> youTubeTracks = new LinkedList<>();
        for (Content<SongFields> result : results) {
            String displayName = result.getArtist() + " / " + result.getTitle();
            youTubeTracks.add(this.youTubeTrackProvider.getTrack(displayName, result));
        }
        return youTubeTracks;
    }

}
