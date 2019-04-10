package core.command_system.commands.smartbot;

import core.command_system.Cmd;
import core.command_system.CmdCategory;
import core.command_system.CmdInstance;
import core.command_system.CmdParser;
import core.command_system.arguments.CmdArgument;
import core.command_system.syntax.Syntax;
import core.permissions.Permission;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.audio.AudioReceiveHandler;
import net.dv8tion.jda.core.audio.CombinedAudio;
import net.dv8tion.jda.core.audio.UserAudio;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import vocalCord.SilenceAudioSendHandler;
import vocalCord.SpeechCallback;
import vocalCord.SpeechReceiver;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;

public class CmdListen implements Cmd {
    @Override
    public String invoke() {
        return "listen";
    }

    @Override
    public CmdCategory category() {
        return CmdCategory.SMARTBOT;
    }

    @Override
    public boolean guildAccess() {
        return true;
    }

    @Override
    public boolean privateAccess() {
        return false;
    }

    @Override
    public Permission requiredPermission() {
        return Permission.NONE;
    }

    @Override
    public Syntax syntax() {
        return null;
    }

    @Override
    public CmdInstance createInstance() {
        return new testw();
    }

    @Override
    public String info() {
        return "I listen to your commands";
    }

    @Override
    public String description() {
        return "I hear commands that you say in a voice channel.";
    }

    private class testw implements CmdInstance {

        @Override
        public void run(CmdParser event) {
            VoiceChannel channel = event.getMember().getVoiceState().getChannel();
            AudioManager audio = event.getGuild().getAudioManager();
            audio.setSendingHandler(new SilenceAudioSendHandler());
            SpeechReceiver speechReceiver = new SpeechReceiver("hello", new SpeechCallback() {
                @Override
                public void commandReceived(String command) {
                    System.out.println("dasd");
                    event.getChannel().sendMessage("You said: "+command+". Is that right?").queue();
            }

                @Override
                public boolean botAwakeRequest(User... user) {
                    System.out.println("Bot awakened!");
                    return true;
                }
            });
            speechReceiver.setCombinedAudio(true);
            audio.setReceivingHandler(speechReceiver);
            audio.openAudioConnection(channel);

        }

    }
}
