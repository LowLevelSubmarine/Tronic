package com.tronic.logger
    data class Event(val clazz: Class<*>, val level: Level, val string: String)