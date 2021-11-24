package com.tronic.logger

fun main() {
    println("Test".tint(Color.GREEN))
    Loggy.quickStart()
    Test()
}

class Test {
    init {
        Loggy.logW("Dies ist eine Warnung")
        Loggy.logT("logW() seems to be working ...")
        InnerTest()
    }
    class InnerTest {
        init {
            Loggy.logE("FEHLER!!1")
        }
    }
}