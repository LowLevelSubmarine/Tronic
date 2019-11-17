package com.tronic.arguments;

public abstract class SingleArgument implements Argument{
    protected abstract void singleParse(String string) throws ParseException;
    private final Argument argument;

    public SingleArgument(Argument argument) {
        this.argument = argument;
    }

    public Argument getArgument() {
        return this.argument;
    }

    @Override
    public void parse(Argument.Info info) throws ParseException {
    }

}
