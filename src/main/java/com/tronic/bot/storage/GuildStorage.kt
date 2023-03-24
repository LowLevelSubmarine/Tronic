package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;
import com.tronic.bot.shortcuts.ShortcutElement;
import com.tronic.bot.statics.Presets;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.Guild;

import kotlin.NoSuchElementException
import kotlin.jvm.Throws

class GuildStorage(val shelf: Shelf): StorageElement(shelf) {

    //MUTED BROADCAST
    fun setBroadcastMuted(broadcastMuted: Boolean) {
        super.set("broadcast_muted", broadcastMuted)
    }

    fun getBroadcastMuted(): Boolean {
        val broadcastMuted =  super.get("broadcast_muted", Boolean::class)
        return broadcastMuted ?: false
    }

    //PREFIX
    fun getPrefix(): String {
        val prefix = super.get("prefix",String::class)
        return prefix ?: Presets.PREFIX
    }

    fun setPrefix(prefix: String) {
        super.set("prefix",prefix)
    }

    //HYPERCHANNELL
    fun getNewChannel(): String? {
        return super.get("hyperchannel", String::class)
    }
    fun setNewChannel(channel: String) {
        super.set("hyperchannel", channel);
    }
    fun getHyperName(): String {
        val name =  super.get("hypername",String::class)
        return  name ?: Presets.NEW_CHANNEL;
    }

    fun setHyperName(hyperName: String) {
        super.set("hypername",hyperName);
    }

    fun getHyperchannelState(): Boolean {
        val  state =  super.get("hyperstate", Boolean::class);
        return state ?: false;
    }

    fun setHyperchannelState(state: Boolean) {
        super.set("hyperstate", state);
    }

    fun getResiduallHyper():MutableList<String> {
        val list = super.getList("residualhyper",String::class)
        return list?.toMutableList() ?: mutableListOf();
    }

    fun setResidualHyper(residualHyper: List<String>) {
        super.set("residualhyper",residualHyper);
    }

    fun getHyperCategory(guild: Guild): Category? {
        val id =  super.get("hypercategory",String::class)
        return if (id == null || id == "") null else guild.getCategoryById(id);
    }

    fun setHyperCategory(category: Category?) {
        if (category!= null) {
            super.set("hypercategory",category.getId());
        } else {
            super.set("hypercategory","");
        }
    }

    // SHORTCUTS
    fun getShortcuts():MutableList<ShortcutElement> {
        val list = super.getList("shortcuts",ShortcutElement::class);
        return list?.toMutableList()?: mutableListOf()
    }

    @Throws(ObjectExistsException::class)
    fun addShortcut(shortcut: ShortcutElement) {
        val shortcuts = getShortcuts();
        if (containsShortcutName(shortcuts,shortcut.getName())) {
            throw ObjectExistsException();
        } else {
            shortcuts.add(shortcut);
            setShortcuts(shortcuts);
        }
    }

    @Throws(NoSuchElementException::class)
    fun removeShortcut(name: String) {
        val shortcuts = getShortcuts()
        if (!containsShortcutName(shortcuts,name)) {
            throw NoSuchElementException();
        } else {
            shortcuts.removeIf { it.name == name }
            setShortcuts(shortcuts);
        }
    }

    private fun setShortcuts(s: List<ShortcutElement> ) {
        super.set("shortcuts", s);
    }

    private fun containsShortcutName(shortcuts: List<ShortcutElement>, name:String): Boolean {
        var inn = false;
        for (s in shortcuts) {
            if (s.name.equals(name)) {
                inn = true;
                break;
            }
        }
        return  inn
    }
}

class ObjectExistsException : Exception();
