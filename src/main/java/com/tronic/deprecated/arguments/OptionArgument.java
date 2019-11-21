package com.tronic.deprecated.arguments;

public class OptionArgument <T extends Argument> extends SingleArgument<T> {

    public OptionArgument(T argument) {
        super(argument);
    }

    @Override
    protected void singleParse(String string) throws ParseException {

    }

}
