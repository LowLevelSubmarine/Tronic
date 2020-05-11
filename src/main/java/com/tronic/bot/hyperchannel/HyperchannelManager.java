package com.tronic.bot.hyperchannel;

import com.tronic.bot.core.Core;
import com.tronic.bot.storage.GuildStorage;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class HyperchannelManager {
    private Core tronic;
    private LinkedList<String> hyperIds = new LinkedList<>();
    private final static String CHANNEL_NAME = "Hyper Channel";

    public HyperchannelManager(Core tronic) {
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
        generateChannels();
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





    private void generateChannels() {
        for (Guild guild: tronic.getJDA().getGuilds()) {
            GuildStorage gs = this.tronic.getStorage().getGuild(guild);
            if (gs.getHyperchannelState()) {
                String channel = gs.getNewChannel();
                if (channel==null) {
                    createNewChannel(guild,gs);
                } else {
                    boolean include = false;
                    for(VoiceChannel vc :guild.getVoiceChannels()) {
                        if (vc.getId().equals(channel)) include=true;
                    }
                    if (!include) {
                        createNewChannel(guild,gs);
                    }
                }
            }
        }
    }

    private void createNewChannel(Guild guild,GuildStorage gs) {
        ChannelAction<VoiceChannel> newChannelProto = guild.createVoiceChannel(tronic.getStorage().getGuild(guild).getHyperName());
        if ( gs.getHyperCategory(guild)!=null) {
            newChannelProto.setPosition(0);
            newChannelProto.setParent( gs.getHyperCategory(guild));
        }
        VoiceChannel newChannel = newChannelProto.complete();
        gs.setNewChannel(newChannel.getId());
    }

    public void onUserJoins(GuildVoiceJoinEvent event) {
        onNewMember(event.getGuild(),event.getChannelJoined(),event.getMember());
    }

    public void onUserMove(GuildVoiceMoveEvent event) {
        if (hyperIds.contains(event.getChannelLeft().getId())) {
            testAndRemoveHyper(event.getChannelLeft());
        }
        if (this.tronic.getStorage().getGuild(event.getGuild()).getHyperchannelState()) {
            if (this.tronic.getStorage().getGuild(event.getGuild()).getNewChannel().equals(event.getChannelJoined().getId())) {
                onNewMember(event.getGuild(),event.getChannelJoined(),event.getMember());
            }
        }
    }

    private void onNewMember(Guild guild,VoiceChannel channel,Member member) {
        GuildStorage gs = this.tronic.getStorage().getGuild(guild);
        String hyperId =  gs.getNewChannel();
        String joinedId = channel.getId();
        if (hyperId ==null) return;
        if (hyperId.equals(joinedId)) {
            ChannelAction<VoiceChannel> vcAction = guild.createVoiceChannel(createChannelName());
            if (gs.getHyperCategory(guild)!= null) {
                vcAction.setParent(gs.getHyperCategory(guild));
                vcAction.setPosition(guild.getVoiceChannelById(gs.getNewChannel()).getPosition()+1);
            }
            VoiceChannel voiceChannel = vcAction.complete();
            this.hyperIds.add(voiceChannel.getId());
            setResidualHyper();
            guild.moveVoiceMember(member,voiceChannel).queue();
        }
    }

    private void testAndRemoveHyper(VoiceChannel channelLeft) {
        if (this.hyperIds.contains(channelLeft.getId()) &&channelLeft.getMembers().size() == 0) {
            channelLeft.delete().queue();
            this.hyperIds.remove(channelLeft.getId());
            setResidualHyper();
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
        for (Guild guild:this.tronic.getJDA().getGuilds()) {
            LinkedList<String> residualHyper = new LinkedList<>();
            for (VoiceChannel vc:guild.getVoiceChannels()) {
                if (this.hyperIds.contains(vc.getId())) {
                    residualHyper.add(vc.getId());
                }
            }
            this.tronic.getStorage().getGuild(guild).setResidualHyper(residualHyper);
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
               createNewChannel(event.getGuild(),this.tronic.getStorage().getGuild(event.getGuild()));
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

    public void setHyperCategory(@Nullable String name, Guild guild) {
        GuildStorage gs = this.tronic.getStorage().getGuild(guild);
        Category category;
        VoiceChannel vc =  guild.getVoiceChannelById(gs.getNewChannel());
        if (name!=null) {
            List<Category> categories = guild.getCategoriesByName(name,true);
            if (categories.size() == 0) {
                category = guild.createCategory(name).complete();
            } else {
                category = categories.get(0);
            }

            gs.setHyperCategory(category);
            if (vc == null) {
                gs.setNewChannel("");
                return;
            }
            vc.getManager().setParent(category).queue();
            vc.getManager().setPosition(0).queue();
            for (String id: this.hyperIds) {
                try {
                    guild.getVoiceChannelById(id).getManager().setParent(category).queue();
                    guild.getVoiceChannelById(id).getManager().setPosition(guild.getVoiceChannelById(gs.getNewChannel()).getPosition()+1).queue();
                } catch (NullPointerException ignored) {}
            }
        } else {
            if (vc != null) {
                vc.getManager().setParent(null).queue();
                gs.getHyperCategory(guild).delete().queue();
                gs.setHyperCategory(null);
            }
        }
    }
}
