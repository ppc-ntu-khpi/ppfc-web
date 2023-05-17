/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core.infrastructure

import kotlinx.atomicfu.atomic
import kotlin.js.Date

object Logger {
    private var logLevel = atomic(Level.ALL)
    private val date = Date()

    fun setLogLevel(logLevel: Level) {
        Logger.logLevel.value = logLevel
    }

    fun setLogLevel(logLevel: Long) {
        val level = Level.values().find { it.level == logLevel } ?: Level.ALL
        Logger.logLevel.value = level
    }

    private fun log(logLevel: Level, tag: String, message: String) {
        if (logLevel.level > Logger.logLevel.value.level) return

        val logMessage = "${date.toTimeString().substring(0, 8)} ${logLevel.name} - $tag: $message"

        when (logLevel) {
            Level.OFF -> {}
            Level.ERROR -> console.error(logMessage)
            Level.WARN -> console.warn(logMessage)
            Level.INFO -> console.info(logMessage)
            Level.DEBUG -> console.log(logMessage)
            Level.TRACE -> console.log(logMessage)
            Level.ALL -> {}
        }
    }

    fun error(tag: String, message: String) = log(Level.ERROR, tag, message)
    fun warn(tag: String, message: String) = log(Level.WARN, tag, message)
    fun info(tag: String, message: String) = log(Level.INFO, tag, message)
    fun debug(tag: String, message: String) = log(Level.DEBUG, tag, message)
    fun trace(tag: String, message: String) = log(Level.TRACE, tag, message)

    enum class Level(val level: Long) {
        OFF(0),
        ERROR(1),
        WARN(2),
        INFO(3),
        DEBUG(4),
        TRACE(5),
        ALL(6)
    }
}