package com.tronic.arguments;

public class LiteralArgument extends SingleArgument {
    private final String string;

    public LiteralArgument(String string, Argument argument) {
        super(argument);
        this.string = string;
    }

    @Override
    protected void singleParse(String string) throws ParseException {
        if (!this.string.equals(string)) {
            throw new ParseException();
        }
    }
}
