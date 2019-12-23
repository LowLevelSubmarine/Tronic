package com.tronic.bot.hyperchannel;

import com.tronic.bot.Tronic;
import com.tronic.bot.storage.GuildStorage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import java.util.LinkedList;
import java.util.Random;

public class HyperchannelManager {
    private Tronic tronic;
    private LinkedList<String> hyperIds = new LinkedList<>();
    private final static String CHANNEL_NAME = "Hyper Channel";

    public HyperchannelManager(Tronic tronic) {
    this.tronic =tronic;
        for (Guild guild:this.tronic.getJDA().getGuilds()) {
            for (String string: this.tronic.getStorage().getGuild(guild).getResiduallHyper()) {
                for (VoiceChannel vc:guild.getVoiceChannels()) {
                    if (vc.getId().equals(string)) {
                        if (vc.getMembers().size()==0) {
                            vc.delete().queue();
                        } else {
                            hyperIds.add(vc.getId());
                        }
                    }
                }
            }
        }
        generateChannels(tronic);
    }

    public void refreshHyper(Guild guild) {
        if (tronic.getStorage().getGuild(guild).getHyperchannelState()) {
            String channel = tronic.getStorage().getGuild(guild).getNewChannel();
            if (channel==null||channel.equals("")) {
                VoiceChannel newChannel = guild.createVoiceChannel(tronic.getStorage().getGuild(guild).getHyperName()).complete();
                tronic.getStorage().getGuild(guild).setNewChannel(newChannel.getId());
            }
        } else {
            VoiceChannel ac = guild.getVoiceChannelById(tronic.getStorage().getGuild(guild).getNewChannel());
            this.tronic.getStorage().getGuild(guild).setNewChannel("");
            try {
                ac.delete().queue();
            } catch (NullPointerException ignored) {}
        }
    }





    private void generateChannels(Tronic tronic) {
        for (Guild guild: tronic.getJDA().getGuilds()) {
            if (tronic.getStorage().getGuild(guild).getHyperchannelState()) {
                String channel = tronic.getStorage().getGuild(guild).getNewChannel();
                if (channel==null) {
                    VoiceChannel newChannel = guild.createVoiceChannel(tronic.getStorage().getGuild(guild).getHyperName()).complete();
                    tronic.getStorage().getGuild(guild).setNewChannel(newChannel.getId());
                } else {
                    boolean include = false;
                    for(VoiceChannel vc :guild.getVoiceChannels()) {
                        if (vc.getId().equals(channel)) include=true;
                    }
                    if (!include) {
                        VoiceChannel newChannel = guild.createVoiceChannel(tronic.getStorage().getGuild(guild).getHyperName()).complete();
                        tronic.getStorage().getGuild(guild).setNewChannel(newChannel.getId());
                    }
                }
            }
        }
    }

    public void onUserJoins(GuildVoiceJoinEvent event) {
        String hyperId =  this.tronic.getStorage().getGuild(event.getGuild()).getNewChannel();
        String joinedId = event.getChannelJoined().getId();
        if (hyperId ==null) return;
        if (hyperId.equals(joinedId)) {
            VoiceChannel voiceChannel = event.getGuild().createVoiceChannel(createChannelName()).complete();
            this.hyperIds.add(voiceChannel.getId());
            setResidualHyper();
            Member joined = event.getEntity();
            event.getGuild().moveVoiceMember(joined,voiceChannel).queue();
        }
    }

    private String createChannelName() {
        Random random = new Random();
        return CHANNEL_NAME+" "+random.nextInt(9999);
    }

    public void onUserLeaves(GuildVoiceLeaveEvent event) {
        if (this.hyperIds.contains(event.getChannelLeft().getId()) && event.getChannelLeft().getMembers().size() == 0) {
            event.getChannelLeft().delete().queue();
            this.hyperIds.remove(event.getChannelLeft().getId());
            setResidualHyper();
        }
    }

    public void onShutdown() {
        for (Guild guild:this.tronic.getJDA().getGuilds()) {
            String newChannel = this.tronic.getStorage().getGuild(guild).getNewChannel();
            setResidualHyper();
            try {
                guild.getVoiceChannelById(newChannel).delete().complete();
            } catch (Exception ignored){}
        }
    }

    private void setResidualHyper() {
        LinkedList<String> residualHyper = new LinkedList<>();
        for (Guild guild:this.tronic.getJDA().getGuilds()) {
            for (VoiceChannel vc:guild.getVoiceChannels()) {
                if (this.hyperIds.contains(vc.getId())) {
                    residualHyper.add(vc.getId());
                }
            }
            this.tronic.getStorage().getGuild(guild).setResidualHyper(this.hyperIds);
        }
    }

    public void onChannelDelete (VoiceChannelDeleteEvent event) {
        String nc = this.tronic.getStorage().getGuild(event.getGuild()).getNewChannel();
        if ( this.tronic.getStorage().getGuild(event.getGuild()).getHyperchannelState()&& !this.tronic.getStorage().getGuild(event.getGuild()).getNewChannel().equals("") ) {
            boolean state = false;
           for (VoiceChannel vc:event.getGuild().getVoiceChannels()) {
               if (vc.getId().equals(nc)) state=true;
           }
           if (!state) {
               VoiceChannel newChannel = event.getGuild().createVoiceChannel(tronic.getStorage().getGuild(event.getGuild()).getHyperName()).complete();
               tronic.getStorage().getGuild(event.getGuild()).setNewChannel(newChannel.getId());
           }
        }
    }

    public void hyperChannelRename(String string, Guild guild) {
        GuildStorage guildStorage =this.tronic.getStorage().getGuild(guild);
        if (guildStorage.getHyperchannelState() && guildStorage.getNewChannel()!= null) {
            guild.getVoiceChannelById(guildStorage.getNewChannel()).getManager().setName(string).queue();
            guildStorage.setHyperName(string);
        }
    }
}
