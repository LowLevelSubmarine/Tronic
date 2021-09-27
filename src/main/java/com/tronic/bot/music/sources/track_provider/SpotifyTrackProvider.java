package com.tronic.bot.music.sources.track_provider;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.tronic.bot.core.Core;
import com.tronic.bot.music.playing.PlaylistQueueItem;
import com.tronic.bot.music.playing.QueueItem;
import com.tronic.bot.music.sources.Track;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpotifyTrackProvider implements UrlTrackProvider {

    private static final Pattern PATTERN_SPOTIFY_TRACK_URL = Pattern.compile("http[s]?:\\/\\/[^.]*\\.spotify\\.com\\/track\\/([a-zA-Z0-9]+)[^ ]*");
    private static final Pattern PATTERN_SPOTIFY_PLAYLIST_URL = Pattern.compile("http[s]?:\\/\\/[^.]*\\.spotify\\.com\\/playlist\\/([a-zA-Z0-9]+)[^ ]*");
    private static final Logger LOGGER = Logger.getLogger(SpotifyTrackProvider.class.getName());
    private final Core core;
    private SpotifyApi api;

    public SpotifyTrackProvider(Core core) {
        this.core = core;
        LOGGER.log(Level.INFO, "Connecting to Spotify Api ...");
        try {
            SpotifyApi api;
            api = new SpotifyApi.Builder()
                    .setClientId(core.getConfig().getSpotifyClientId())
                    .setClientSecret(core.getConfig().getSpotifyClientSecret())
                    .build();
            api.setAccessToken(api.clientCredentials().build().execute().getAccessToken());
            LOGGER.log(Level.INFO, "Connected to Spotify Api");
            this.api = api;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not connect to Spotify Api.\nCheck internet connection");
        } catch (SpotifyWebApiException e) {
            LOGGER.log(Level.WARNING, "Could not connect to Spotify Api.\nCheck the client credentials");
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Could not connect to Spotify Api.\nLook for Tronic updates");
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

    private QueueItem queueItemFromSpotifyTrack(com.wrapper.spotify.model_objects.specification.Track spotifyTrack) {
        return this.core.getMusicManager().getTrackProvider().getYouTubeMusicTP()
                .fromSingleSearch(spotifyTrack.getArtists()[0].getName() + " - " + spotifyTrack.getName());
    }

    private QueueItem queueItemFromTrackId(String id) {
        try {
            return queueItemFromSpotifyTrack(api.getTrack(id).build().execute());
        } catch (Exception e) {
            LOGGER.info("Failed creating queue item from spotify track id " + id + "\n" + e);
            return null;
        }
    }

    private QueueItem queueItemFromPlaylistId(String id, int maxTracks) {
        try {
            Playlist playlist = this.api.getPlaylist(id).build().execute();
            Paging<PlaylistTrack> paging = this.api.getPlaylistsItems(id).limit(100).build().execute();
            List<PlaylistTrack> playlistTracks = new LinkedList<>();
            while (paging != null) {
                playlistTracks.addAll(Arrays.asList(paging.getItems()));
                if (paging.getNext() != null) {
                    paging = this.api.getPlaylistsItems(id).limit(100).offset(paging.getOffset() + 100).build().execute();
                } else {
                    paging = null;
                }
            }
            if (playlistTracks.size() > maxTracks) {
                Collections.shuffle(playlistTracks);
                playlistTracks = playlistTracks.subList(0, maxTracks);
            }
            LinkedList<Track> tracks = new LinkedList<>();
            for (PlaylistTrack playlistTrack : playlistTracks) {
                QueueItem queueItem = queueItemFromTrackId(playlistTrack.getTrack().getId());
                if (queueItem != null) tracks.add(queueItem.nextTrack());
            }
            return new PlaylistQueueItem(playlist.getName(), "https://open.spotify.com/playlist/" + playlist.getId(), tracks);
        } catch (Exception e) {
            LOGGER.info("Failed creating queue item from spotify playlist id " + id + "\n" + e);
            return null;
        }
    }

}
