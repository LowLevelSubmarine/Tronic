package com.tronic.bot.music.sources.track_provider;

import com.tronic.bot.music.playing.SingleQueueItem;

import java.util.List;

public interface SearchTrackProvider {

    SingleQueueItem fromSingleSearch(String query);
    List<SingleQueueItem> fromMultiSearch(String query, int MaxResults);

}
