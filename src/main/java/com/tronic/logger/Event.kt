package com.tronic.logger

import com.lowlevelsubmarine.subsconsole.Align
import java.io.PrintWriter
import java.io.StringWriter

data class Event(val clazz: Class<*>, val level: Level, val string: String?, val exception: Exception?) {

    override fun toString(): String {
        val timestamp = Loggy.timestamp()
        val level = Align.LEFT.align(this.level.name.tint(this.level.color), 5)
        val location = Align.LEFT.align(Loggy.shortClassName(this.clazz), 25)
        val stamp = "[$level $timestamp $location]"
        if (this.exception == null) {
            return "$stamp $string"
        } else {
            val errorString = this.exception.toPrintString()
            if (this.string == null) {
                return "$stamp $errorString"
            } else {
                return "$stamp $string: $errorString"
            }
        }
    }

    fun Exception.toPrintString(): String {
        val errorString = StringWriter()
        this.printStackTrace(PrintWriter(errorString))
        return errorString.toString()
    }
    
}