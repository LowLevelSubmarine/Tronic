package com.tronic.logger

import java.io.PrintWriter
import java.io.StringWriter

data class Event(val clazz: Class<*>, val level: Level, val string: String?, val exception: Exception?) {

    override fun toString(): String {
        val stamp = "[${this.level.name.tint(this.level.color)} ${Loggy.timestamp()} ${Loggy.shortClassName(this.clazz)}]"
        if (this.exception == null) {
            return "$stamp $string"
        } else {
            val errorString = StringWriter()
            this.exception.printStackTrace(PrintWriter(errorString))
            if (this.string == null) {
                return "$stamp $errorString"
            } else {
                return "$stamp $string: $errorString"
            }
        }
    }
    
}