package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;
import com.tronic.bot.hyperchannel.HyperchannelManager;
import com.tronic.bot.tools.StatisticsSaver;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.LinkedList;
import java.util.List;

public class GuildStorage extends StorageElement {
    private static final String DEFAULT_PREFIX="!";
    private static final String DEFAULT_HYPERNAME ="New Channel";

    GuildStorage(Shelf shelf) {
        super(shelf);
    }

    public String getPrefix() {
        String prefix = (String) super.get("prefix",String.class);
        return  prefix!=null ? prefix : DEFAULT_PREFIX ;
    }

    public void setPrefix(String prefix) {
        super.set("prefix",prefix);
    }

    public String getNewChannel() {
        return (String) super.get("hyperchannel", String.class);
    }

    public void setNewChannel(String channel) {
        super.set("hyperchannel", channel);
    }

    public String getHyperName() {
        String name = (String) super.get("hypername",String.class);
        return  name != null ? name : DEFAULT_HYPERNAME;
    }

    public void setHyperName(String hyperName) {
        super.set("hypername",hyperName);
    }

    public boolean getHyperchannelState() {
        try {
            boolean state = (boolean) super.get("hyperstate",boolean.class);
            return state;
        } catch (Exception e) {
            setHyperchannelState(false);
            return false;
        }
    }

    public void setHyperchannelState(boolean state) {
        super.set("hyperstate", state);
    }

    public LinkedList<String> getResiduallHyper() {
        LinkedList<String> ll = new LinkedList<>();
        List<Object> list = super.getList("residualhyper",String.class);
        if (list!=null) {
            for (Object hyperId :list) {
                ll.add((String) hyperId);
            }
        }
        return ll;
    }

    public void setResidualHyper(LinkedList<String> residualHyper) {
        super.set("residualhyper",residualHyper);
    }

    public Category getHyperCategory(Guild guild) {
        String id =  (String) super.get("hypercategory",String.class);
        return (id.equals(""))? null : guild.getCategoryById(id);
    }

    public void setHyperCategory(Category category) {
        if (category!= null) {
            super.set("hypercategory",category.getId());
        } else {
            super.set("hypercategory","");
        }
    }
}
