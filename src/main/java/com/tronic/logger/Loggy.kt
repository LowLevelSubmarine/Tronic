package com.tronic.logger

import com.tronic.logger.receiver.Receiver
import com.tronic.logger.receiver.SysOutReceiver
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Loggy {

    companion object {

        private var INITZIALIZED = false;
        private val PATTERN_INLINE_CLASS_NAME = Pattern.compile("[^.]+\$")
        private val TIMESTAMP_FORMAT =  SimpleDateFormat("dd.mm.yyyy HH:mm:ss")
        private val RECEIVERS = mutableSetOf<Receiver>()

        @JvmStatic
        fun init(receivers: Set<Receiver>) {
            INITZIALIZED = true
            this.RECEIVERS.clear()
            this.RECEIVERS.addAll(receivers)
        }

        @JvmStatic
        fun quickStart() {
            init(setOf(SysOutReceiver()))
        }

        @JvmStatic
        fun addReceiver(receiver: Receiver) {
            RECEIVERS.add(receiver)
        }

        @JvmStatic
        fun logE(errorString: String) {
            log(Level.ERROR, errorString)
        }

        @JvmStatic
        fun logW(warnString: String) {
            log(Level.WARN, warnString)
        }

        @JvmStatic
        fun logI(infoString: String) {
            log(Level.INFO, infoString)
        }

        @JvmStatic
        fun logD(debugString: String) {
            log(Level.DEBUG, debugString)
        }

        @JvmStatic
        fun logT(traceString: String) {
            log(Level.TRACE, traceString)
        }

        @JvmStatic
        fun log(level: Level, string: String) {
            val event = Event(getCallingClass(), level, string)
            for (receiver in RECEIVERS) {
                if (receiver.logLevel().value <= event.level.value) {
                    receiver.handleLogEvent(event)
                }
            }
        }

        @JvmStatic
        fun shortClassName(clazz: Class<*>): String {
            val matcher = PATTERN_INLINE_CLASS_NAME.matcher(clazz.name)
            if (matcher.find()) {
                return matcher.group(0)
            }
            return clazz.simpleName
        }

        fun timestamp(): String {
            return TIMESTAMP_FORMAT.format(Date())
        }

        @JvmStatic
        private fun getCallingClass(): Class<*> {
            val stackTrace = Thread.currentThread().stackTrace
            val callingClassName = stackTrace.asList().stream()
                .skip(1).filter { !this::class.java.name.startsWith(it.className) }
                .findFirst().get().className
            return Class.forName(callingClassName)
        }

    }

}