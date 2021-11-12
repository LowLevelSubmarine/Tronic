package com.tronic.logger

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Loggy {

    enum class Level(val value: Int) {
        ERROR(4000),
        WARN(3000),
        INFO(2000),
        DEBUG(1000),
        TRACE(0);
    }

    data class Event(val clazz: Class<*>, val level: Level, val string: String)

    interface Receiver {
        fun handleLogEvent(event: Event)
    }

    class SysOutReceiver : Receiver {

        override fun handleLogEvent(event: Event) {
            println("[" + event.level + " " + timestamp() + "] " + shortClassName(event.clazz) + ": " + event.string)
        }

    }

    companion object {

        val PATTERN_INLINE_CLASS_NAME = Pattern.compile("[^.]+\\\$[^.]+")
        val TIMESTAMP_FORMAT =  SimpleDateFormat("dd.mm.yyyy HH:mm:ss")
        private val RECEIVERS = mutableListOf<Receiver>()

        fun quickStart() {
            addReceiver(SysOutReceiver())
        }

        fun addReceiver(receiver: Receiver) {
            RECEIVERS.add(receiver)
        }

        fun logE(errorString: String) {
            log(Level.ERROR, errorString)
        }

        fun logW(warnString: String) {
            log(Level.WARN, warnString)
        }

        fun logI(infoString: String) {
            log(Level.INFO, infoString)
        }

        fun logD(debugString: String) {
            log(Level.DEBUG, debugString)
        }

        fun logT(traceString: String) {
            log(Level.TRACE, traceString)
        }

        fun log(level: Level, string: String) {
            val event = Event(getCallingClass(), level, string)
            for (receiver in RECEIVERS) {
                receiver.handleLogEvent(event)
            }
        }

        private fun shortClassName(clazz: Class<*>): String {
            val matcher = PATTERN_INLINE_CLASS_NAME.matcher(clazz.name)
            if (matcher.find()) {
                return matcher.group(0)
            }
            return clazz.simpleName
        }

        private fun getCallingClass(): Class<*> {
            val stackTrace = Thread.currentThread().stackTrace
            val callingClassName = stackTrace.asList().stream()
                .skip(1).filter { it.className != this.javaClass.name }.findFirst().get().className
            return Class.forName(callingClassName)
        }

        private fun timestamp(): String {
            return TIMESTAMP_FORMAT.format(Date())
        }

    }

}