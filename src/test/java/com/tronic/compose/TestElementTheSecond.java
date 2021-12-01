package com.tronic.compose;

import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class TestElementTheSecond extends Element {

    @Nullable
    @Override
    protected Set<Ent<?>> getShadows() {
        return null;
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onDestroy() {

    }

    @Override
    protected boolean blocked() {
        return false;
    }

}
