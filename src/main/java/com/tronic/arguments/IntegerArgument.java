package com.tronic.arguments;

public class IntegerArgument implements Argument<Integer> {

    @Override
    public Integer parse(Arguments arguments) throws InvalidArgumentException {
        try {
            return Integer.valueOf(arguments.getNext());
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException();
        }
    }

}
