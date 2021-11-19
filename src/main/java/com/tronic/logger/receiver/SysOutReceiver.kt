package com.tronic.logger.receiver

import com.tronic.logger.Event
import com.tronic.logger.Level
import com.tronic.logger.Loggy
import com.tronic.logger.tint

class SysOutReceiver(private val level: Level) : Receiver {

    constructor() : this(Level.TRACE)

    override fun logLevel(): Level {
        return this.level
    }

    override fun handleLogEvent(event: Event) {
        println(("[" + event.level + " " +
                Loggy.timestamp() + "] " +
                Loggy.shortClassName(event.clazz) + ": " +
                event.string).tint(event.level.color))
    }

}