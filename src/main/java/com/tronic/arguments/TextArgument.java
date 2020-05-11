package com.tronic.arguments;

public class TextArgument implements Argument<String> {

    @Override
    public String parse(Arguments arguments) throws InvalidArgumentException {
        return arguments.getString();
    }

}
