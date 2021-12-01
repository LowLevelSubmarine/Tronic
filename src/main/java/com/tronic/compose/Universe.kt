package com.tronic.compose

class Universe(private val id: String = "default_universe") {

    private val elements = mutableListOf<Element>()

    fun registerElement(element: Element) {
        this.elements.add(element)
    }

    fun unregisterElement(element: Element) {
        this.elements.remove(element)
    }

}