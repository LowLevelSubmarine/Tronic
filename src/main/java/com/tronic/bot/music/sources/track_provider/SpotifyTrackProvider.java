package com.tronic.bot.music.sources.track_provider;

import com.tronic.bot.core.Core;
import com.tronic.bot.music.playing.PlaylistQueueItem;
import com.tronic.bot.music.playing.QueueItem;
import com.tronic.bot.music.sources.Track;
import com.tronic.bot.tools.BatchProcessor;
import com.tronic.logger.Loggy;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpotifyTrackProvider implements UrlTrackProvider {

    private static final Pattern PATTERN_SPOTIFY_TRACK_URL = Pattern.compile("http[s]?:\\/\\/[^.]*\\.spotify\\.com\\/track\\/([a-zA-Z0-9]+)[^ ]*");
    private static final Pattern PATTERN_SPOTIFY_PLAYLIST_URL = Pattern.compile("http[s]?:\\/\\/[^.]*\\.spotify\\.com\\/playlist\\/([a-zA-Z0-9]+)[^ ]*");
    private final Core core;
    private SpotifyApi api;

    public SpotifyTrackProvider(Core core) {
        this.core = core;
        Loggy.logI("Connecting to Spotify Api ...");
        try {
            SpotifyApi api;
            api = new SpotifyApi.Builder()
                    .setClientId(core.getConfig().getSpotifyClientId())
                    .setClientSecret(core.getConfig().getSpotifyClientSecret())
                    .build();
            Loggy.logI("Connected to Spotify Api");
            this.api = api;
            updateApiToken();
        } catch (IOException e) {
            Loggy.logW("Could not connect to Spotify Api.\nCheck internet connection");
        } catch (SpotifyWebApiException e) {
            Loggy.logW("Could not connect to Spotify Api.\nCheck the client credentials");
        } catch (ParseException e) {
            Loggy.logW("Could not connect to Spotify Api.\nLook for Tronic updates");
        }
    }

    @Override
    public QueueItem fromUrl(String url) {
        Matcher trackMatcher = PATTERN_SPOTIFY_TRACK_URL.matcher(url);
        if (trackMatcher.matches()) {
            String id = trackMatcher.group(1);
            return queueItemFromTrackId(id);
        }
        Matcher playlistMatcher = PATTERN_SPOTIFY_PLAYLIST_URL.matcher(url);
        if (playlistMatcher.matches()) {
            String id = playlistMatcher.group(1);
            return queueItemFromPlaylistId(id, 20);
        }
        return null;
    }

    private QueueItem queueItemFromSpotifyTrack(se.michaelthelin.spotify.model_objects.specification.Track spotifyTrack) {
        return this.core.getMusicManager().getTrackProvider().getYouTubeMusicTP()
                .fromSingleSearch(spotifyTrack.getArtists()[0].getName() + " - " + spotifyTrack.getName());
    }

    private QueueItem queueItemFromTrackId(String id) {
        try {
            return queueItemFromSpotifyTrack(executeRequest(api.getTrack(id).build()));
        } catch (Exception e) {
            Loggy.logD("Failed creating queue item from spotify track id " + id + " " + e);
            return null;
        }
    }

    private QueueItem queueItemFromPlaylistId(String id, int maxTracks) {
        try {
            Playlist playlist = executeRequest(this.api.getPlaylist(id).build());
            Paging<PlaylistTrack> paging = executeRequest(this.api.getPlaylistsItems(id).limit(100).build());
            List<PlaylistTrack> playlistTracks = new LinkedList<>();
            while (paging != null) {
                playlistTracks.addAll(Arrays.asList(paging.getItems()));
                if (paging.getNext() != null) {
                    paging = executeRequest(this.api.getPlaylistsItems(id).limit(100).offset(paging.getOffset() + 100).build());
                } else {
                    paging = null;
                }
            }
            if (playlistTracks.size() > maxTracks) {
                Collections.shuffle(playlistTracks);
                playlistTracks = playlistTracks.subList(0, maxTracks);
            }
            BatchProcessor<PlaylistTrack, Track> playlistTrackProcessor = new BatchProcessor<>(playlistTracks, (PlaylistTrack from) -> {
                QueueItem queueItem = queueItemFromTrackId(from.getTrack().getId());
                if (queueItem != null) return queueItem.nextTrack();
                else return null;
            }, true);
            return new PlaylistQueueItem(playlist.getName(), "https://open.spotify.com/playlist/" + playlist.getId(), playlistTrackProcessor.process());
        } catch (Exception e) {
            Loggy.logD("Failed creating queue item from spotify playlist id " + id + "\n" + e);
            return null;
        }
    }

    private <T> T executeRequest(AbstractDataRequest<T> request) throws IOException, SpotifyWebApiException, ParseException {
        try {
            return request.execute();
        } catch (UnauthorizedException e) {
            Loggy.logD(e + " Executing access token refresh ...");
            try {
                Thread.sleep(500); // wait a second, as it seems like the token refresh doesn't have an immidiate effect
            } catch (InterruptedException ex) {
                Loggy.logW(ex);
            }
            updateApiToken();
            return request.execute();
        }
    }

    private void updateApiToken() throws IOException, SpotifyWebApiException, ParseException {
        try {
            Loggy.logD("Updating access token");
            this.api.setAccessToken(api.clientCredentials().build().execute().getAccessToken());
            Loggy.logD("Successfully updated access token");
        } catch (Exception e) {
            Loggy.logW("Something went wrong while updating the Api Token: " + e + " This can cause further issues with spotify dependent features.");
            throw e;
        }
    }

}
