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
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
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
        Loggy.logI("Connecting to Spotify api ...");
        try {
            SpotifyApi api;
            api = new SpotifyApi.Builder()
                    .setClientId(core.getConfig().getSpotifyClientId())
                    .setClientSecret(core.getConfig().getSpotifyClientSecret())
                    .build();
            this.api = api;
            updateApiToken();
            Loggy.logI("Connected to Spotify api");
        } catch (IOException e) {
            Loggy.logW("Could not connect to Spotify api. Check internet connection");
        } catch (SpotifyWebApiException e) {
            Loggy.logW("Could not connect to Spotify api. Check the client credentials");
        } catch (ParseException e) {
            Loggy.logW("Could not connect to Spotify api. Look for Tronic updates");
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
            return queueItemFromSpotifyTrack(executeRequest(api.getTrack(id)));
        } catch (Exception e) {
            Loggy.logD("Failed creating queue item from spotify track id " + id + " " + e);
            return null;
        }
    }

    private QueueItem queueItemFromPlaylistId(String id, int maxTracks) {
        try {
            Playlist playlist = executeRequest(this.api.getPlaylist(id));
            Paging<PlaylistTrack> paging = executeRequest(this.api.getPlaylistsItems(id).limit(100));
            List<PlaylistTrack> playlistTracks = new LinkedList<>();
            while (paging != null) {
                playlistTracks.addAll(Arrays.asList(paging.getItems()));
                if (paging.getNext() != null) {
                    paging = executeRequest(this.api.getPlaylistsItems(id).limit(100).offset(paging.getOffset() + 100));
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

    /**
     * Executes the given request while dealing with invalid token responses.
     * If executing the request fails due to an invalid access token,
     * the access token updates and the request executes for a second try.
     * @param builder The builder of the request
     * @param <T> The type of the builders response
     * @return The builders response
     * @throws IOException Is thrown when the reques fails
     * @throws SpotifyWebApiException Is thrown when the request fails
     * @throws ParseException Is thrown when the request fails
     */
    private synchronized <T> T executeRequest(AbstractDataRequest.Builder<T, ?> builder) throws IOException, SpotifyWebApiException, ParseException {
        try {
            return builder.build().execute();
        } catch (UnauthorizedException e) {
            Loggy.logD(e + " Executing access token refresh ...");
            updateApiToken();
            forceUpdateAccessToken(builder);
            return builder.build().execute();
        }
    }

    /**
     * Overwrites the access token of a request builder with the apis current one.
     * This is needed, as the library writes the access token to the builder
     * when it is beeing constructed, which makes it impossibile to resend
     * after the access token has been updated.
     * @param builder The builder to overwrite the access token of
     */
    private void forceUpdateAccessToken(AbstractDataRequest.Builder<?, ?> builder) {
        builder.setHeader("Authorization", "Bearer " + this.api.getAccessToken());
    }

    private void updateApiToken() throws IOException, SpotifyWebApiException, ParseException {
        try {
            Loggy.logD("Updating access token ...");
            ClientCredentials credentials = api.clientCredentials().build().execute();
            this.api.setAccessToken(credentials.getAccessToken());
            Loggy.logD("Successfully updated access token, valid for " + credentials.getExpiresIn() / 60 + " minutes.");
        } catch (Exception e) {
            Loggy.logW("Something went wrong while updating the access token: " + e + " This can cause further issues with spotify dependant features.");
            throw e;
        }
    }

}
