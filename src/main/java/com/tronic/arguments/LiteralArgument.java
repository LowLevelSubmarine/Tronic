package com.tronic.arguments;

public class LiteralArgument<T extends Argument> extends SingleArgument<T> {

    private final String string;

    public LiteralArgument(String string, T argument) {
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
