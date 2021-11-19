package com.tronic.logger

private const val PREFIX = "\u001B["
private const val SUFFIX = "m"

fun String.tint(color: Color?): String {
    return color?.tint(this) ?: this
}

enum class Color(code: Int) {

    RESET(0),
    BLACK(30),
    RED(31),
    GREEN(32),
    YELLOW(33),
    BLUE(34),
    PURPLE(35),
    CYAN(36),
    WHITE(37);

    private val string: String

    init {
        this.string = PREFIX + code + SUFFIX
    }

    fun tint(string: String): String {
        return this.toString() + string + RESET
    }

    override fun toString(): String {
        return string
    }

}