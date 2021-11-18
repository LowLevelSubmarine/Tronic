package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;
import com.tronic.bot.statics.Presets
import net.dv8tion.jda.api.entities.User

class StaticStorage(private val shelf: Shelf): StorageElement(shelf) {

    private fun setCoHosters(hosters: List<String>) {
        super.set("cohoster",hosters);
    }

    fun addCoHoster(hoster: User) {
        val list = super.getList("cohoster",String::class)?.toMutableList() ?: mutableListOf()
        list.add(hoster.getId());
        super.set("cohoster",list);
    }

    fun removeCoHoster(hoster: User) {
        val list = super.getList("cohoster",String::class)?.toMutableList() ?: mutableListOf()
        list.removeIf { it == hoster.id }
        setCoHosters(list);
    }

    fun isCoHoster(hoster: User): Boolean {
        val list = super.getList("cohoster",String::class)
        return list?.contains(hoster.id) ?: false
    }

    fun setBotVolume (vol: Int){
        super.set("volume",vol)
    }

    fun getBotVolume(): Int {
        val vol = super.get("volume",Int::class);
        return vol ?: Presets.VOLUME
    }


}
