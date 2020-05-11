package com.tronic.arguments;

public class BooleanArgument implements Argument<Boolean> {
    @Override
    public Boolean parse(Arguments arguments) throws InvalidArgumentException {
        return arguments.getString().equals("true");
    }
}
