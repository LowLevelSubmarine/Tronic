package com.tronic.arguments;

public class SingleArgument implements Argument<String> {

    @Override
    public String parse(Arguments arguments) {
        return arguments.getNext();
    }

}
