package com.tronic.arguments;

public class IntegerArgument extends SingleArgument {

    private int integer;

    public IntegerArgument(Argument argument) {
        super(argument);
    }

    @Override
    protected void singleParse(String string) throws ParseException {
        try {
            this.integer = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throw new ParseException();
        }
    }

    public int getInteger() {
        return this.integer;
    }

}
