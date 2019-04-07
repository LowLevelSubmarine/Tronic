package core.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.core.entities.Guild;

public class GuildPlayer {

    private final Guild guild;
    private final AudioPlayer player;

    public GuildPlayer(Guild guild) {
        this.player = new DefaultAudioPlayer(new DefaultAudioPlayerManager());
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(this.player));
        this.guild = guild;
    }

    public void stop() {
        this.player.destroy();
    }

    public void setPaused(boolean paused) {
        this.player.setPaused(paused);
    }

}
