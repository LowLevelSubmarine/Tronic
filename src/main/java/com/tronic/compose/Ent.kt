package com.tronic.compose

class Ent<T : Element> @JvmOverloads constructor(
    private val clazz: Class<T>,
    private val universe: Universe = DEFAULT_UNIVERSE
) {

    private var element: T? = null

    fun get(): T {
        if (this.element != null) {
            return this.element!!
        } else {
            throw InvalidStateException()
        }
    }

}