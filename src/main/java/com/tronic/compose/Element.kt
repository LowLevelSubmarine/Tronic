package com.tronic.compose

abstract class Element(val universe: Universe = DEFAULT_UNIVERSE) {

    protected abstract fun getShadows(): Set<Ent<*>>?
    protected abstract fun onInit()
    protected abstract fun onDestroy()
    protected abstract fun blocked(): Boolean

    private val state = State.WAITING
    private val shadows = setOf(this.getShadows())

    enum class State {
        WAITING, INITIATED, DESTROYED;
    }

    private fun ready(): Boolean {
        return state == State.WAITING && !blocked() && shadows.all { it. }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> toSet(vararg items: T): Set<T> {
        return items.toSet() as Set<T>
    }

}