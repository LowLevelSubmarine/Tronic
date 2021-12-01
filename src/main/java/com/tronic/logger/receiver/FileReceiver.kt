package com.tronic.logger.receiver

import com.tronic.logger.Event
import com.tronic.logger.Level
import java.io.File
import java.io.FileWriter

class FileReceiver(private val level: Level = Level.TRACE, file: File) : Receiver {

    private val writer = FileWriter(file, true)

    override fun logLevel(): Level {
        return this.level
    }

    override fun handleLogEvent(event: Event) {
        this.writer.write(event.toString() + "\n")
        this.writer.flush()
    }

}