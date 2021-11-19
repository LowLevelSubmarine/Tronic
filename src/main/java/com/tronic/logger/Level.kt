package com.tronic.logger;

enum class Level(val value: Int, val color: Color?) {
    ERROR(4000, Color.RED),
    WARN(3000, Color.YELLOW),
    INFO(2000, Color.GREEN),
    DEBUG(1000, Color.BLUE),
    TRACE(0, null);
}