package com.tronic.bot.music.sources.track_provider;

import com.tronic.bot.music.playing.QueueItem;

public interface UrlTrackProvider {

    QueueItem fromUrl(String url);

}
