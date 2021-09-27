package com.tronic.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.tronic.bot.core.Core;
import com.tronic.bot.music.playing.Player;
import com.tronic.bot.music.sources.TrackProvider;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

public class MusicManager {

    private final Core core;
    private final HashMap<Guild, Player> players = new HashMap<>();
    private final AudioPlayerManager manager = createManager();
    private final TrackProvider trackProvider;

    public MusicManager(Core core) {
        this.core = core;
        this.trackProvider = new TrackProvider(core, this.manager);
        core.addShutdownHook(this::onCoreShutdown);
    }

    private void onCoreShutdown(boolean restart) {
        for (Player player : this.players.values()) {
            player.stop();
        }
    }

    public Player getPlayer(Guild guild) {
        if (this.players.containsKey(guild)) {
            return this.players.get(guild);
        } else {
            return createPlayer(guild);
        }
    }

    public TrackProvider getTrackProvider() {
        return this.trackProvider;
    }

    private Player createPlayer(Guild guild) {
        Player player = new Player(this.core, guild, this.manager.createPlayer());
        this.players.put(guild, player);
        return player;
    }

    private static AudioPlayerManager createManager() {
        AudioPlayerManager manager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(manager);
        return manager;
    }

}
