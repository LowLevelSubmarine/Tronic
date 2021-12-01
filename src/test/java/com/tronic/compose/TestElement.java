package com.tronic.compose;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TestElement extends Element {

    private Ent<TestElementTheSecond> testElementTheSecondEnt = new Ent<>(TestElementTheSecond.class);

    @NotNull
    @Override
    public Set<Ent<?>> getShadows() {
        return toSet(testElementTheSecondEnt);
    }

    @Override
    public void onInit() {
        testElementTheSecondEnt.get()
    }

    @Override
    public void onDestroy() {

    }

    @Override
    protected boolean blocked() {
        return false;
    }

}
