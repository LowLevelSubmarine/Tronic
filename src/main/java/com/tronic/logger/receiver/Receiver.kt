package com.tronic.logger.receiver

import com.tronic.logger.Event
import com.tronic.logger.Level

interface Receiver {
    fun logLevel(): Level
    fun handleLogEvent(event: Event)
}