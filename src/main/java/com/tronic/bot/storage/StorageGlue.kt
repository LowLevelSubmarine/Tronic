package com.tronic.bot.storage

import com.toddway.shelf.*
import java.io.File

class StorageGlue {
    companion object {
        fun newShelf(file:File): Shelf {
            val fileStorage = FileStorage(file)
            val serializer = GsonSerializer()
            return Shelf(fileStorage, serializer)
        }
    }
}
