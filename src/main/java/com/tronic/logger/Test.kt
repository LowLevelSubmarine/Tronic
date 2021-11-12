package com.tronic.logger

fun main() {
    Loggy.addReceiver(Loggy.SysOutReceiver())
    Test()
}

class Test {
    init {
        Loggy.logW("Dies ist eine Warnung")
        InnerTest()
    }
    class InnerTest {
        init {
            Loggy.logE("FEHLER!!1")
        }
    }
}