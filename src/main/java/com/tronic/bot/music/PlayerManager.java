package com.tronic.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

public class PlayerManager {

    private final HashMap<Guild, Player> players = new HashMap<>();
    private final AudioPlayerManager manager = createManager();

    public Player getPlayer(Guild guild) {
        if (this.players.containsKey(guild)) {
            return this.players.get(guild);
        } else {
            return createPlayer(guild);
        }
    }

    public void loadTrack() {

    }

    private Player createPlayer(Guild guild) {
        Player player = new Player(this.manager.createPlayer());
        this.players.put(guild, player);
        return player;
    }

    private static final AudioPlayerManager createManager() {
        AudioPlayerManager manager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(manager);
        return manager;
    }

}
