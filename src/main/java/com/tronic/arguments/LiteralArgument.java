package com.tronic.arguments;

public class LiteralArgument implements Argument<Void> {

    private final String literal;

    public LiteralArgument(String literal) {
        this.literal = literal;
    }

    @Override
    public Void parse(Arguments arguments) throws ArgumentParseException {
        if (!arguments.getNext().equals(this.literal)) {
            throw new ArgumentParseException();
        }
        return null;
    }

}
