package com.tronic.arguments;

public abstract class SingleArgument <T extends Argument> implements Argument {

    protected abstract void singleParse(String string) throws ParseException;

    private final T argument;

    public SingleArgument(T argument) {
        this.argument = argument;
    }

    public T getArgument() {
        return this.argument;
    }

    @Override
    public void parse(Argument.Info info) throws ParseException {
        singleParse(info.splice());
        if (this.argument != null) {
            this.argument.parse(info);
        }
    }

}
