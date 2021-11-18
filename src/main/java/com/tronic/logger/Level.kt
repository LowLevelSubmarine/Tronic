package com.tronic.logger;

enum class Level(val value: Int) {
    ERROR(4000),
    WARN(3000),
    INFO(2000),
    DEBUG(1000),
    TRACE(0);
}