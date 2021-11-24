package com.tronic.logger.receiver

import com.tronic.logger.Event
import com.tronic.logger.Level

class SysOutReceiver(private val level: Level = Level.TRACE) : Receiver {

    constructor() : this(Level.TRACE)

    override fun logLevel(): Level {
        return this.level
    }

    override fun handleLogEvent(event: Event) {
        println(event)
    }

}