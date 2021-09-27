package com.tronic.bot.music.sources;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.tronic.bot.core.Core;
import com.tronic.bot.music.playing.QueueItem;
import com.tronic.bot.music.playing.SingleQueueItem;
import com.tronic.bot.music.sources.track_provider.*;

import java.util.LinkedList;
import java.util.List;

public class TrackProvider implements UrlTrackProvider, SearchTrackProvider {

    private final YouTubeMusicTrackProvider youTubeMusicTrackProvider;
    private final SpotifyTrackProvider spotifyTrackProvider;
    private final LavaPlayerTrackProvider lavaPlayerTrackProvider;
    private final List<UrlTrackProvider> urlTrackProviders = new LinkedList<>();

    public TrackProvider(Core core, AudioPlayerManager audioPlayerManager) {
        this.youTubeMusicTrackProvider = new YouTubeMusicTrackProvider();
        this.spotifyTrackProvider = new SpotifyTrackProvider(core);
        this.lavaPlayerTrackProvider = new LavaPlayerTrackProvider(audioPlayerManager);
        this.urlTrackProviders.add(this.spotifyTrackProvider);
        this.urlTrackProviders.add(lavaPlayerTrackProvider);
    }

    @Override
    public SingleQueueItem fromSingleSearch(String query) {
        return this.youTubeMusicTrackProvider.fromSingleSearch(query);
    }

    @Override
    public List<SingleQueueItem> fromMultiSearch(String query, int maxItems) {
        return this.youTubeMusicTrackProvider.fromMultiSearch(query, maxItems);
    }

    @Override
    public QueueItem fromUrl(String url) {
        QueueItem queueItem = null;
        for (int i = 0; queueItem == null && i < this.urlTrackProviders.size(); i++) {
            queueItem = this.urlTrackProviders.get(i).fromUrl(url);
        }
        return queueItem;
    }

    public YouTubeMusicTrackProvider getYouTubeMusicTP() {
        return this.youTubeMusicTrackProvider;
    }

    public SpotifyTrackProvider getSpotifyTP() {
        return this.spotifyTrackProvider;
    }

    public LavaPlayerTrackProvider getLavaPlayerTP() {
        return this.lavaPlayerTrackProvider;
    }

}
