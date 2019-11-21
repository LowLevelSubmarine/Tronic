package com.tronic.arguments;

public class Arguments {

    private final static String DEFAULT_SEPARATOR = " ";

    private final String separator;
    private final String original;
    private String string;

    public Arguments(String original) {
        this(original, DEFAULT_SEPARATOR);
    }

    public Arguments(String original, String separator) {
        this.original = original;
        this.string = original;
        this.separator = separator;
    }

    public <T> T parseAndSplit(Argument<T> argument) throws ArgumentParseException {
        if (!isEmpty()) {
            T t = argument.parse(this);
            split();
            return t;
        }
        return null;
    }

    public <T> T parse(Argument<T> argument) throws ArgumentParseException {
        if (!isEmpty()) {
            return argument.parse(this);
        }
        return null;
    }

    public void split() {
        int nextSeparatorIndex = getNextSeparatorIndex();
        if (nextSeparatorIndex != -1) {
            this.string = this.string.substring(getNextSeparatorIndex());
        } else {
            this.string = "";
        }
    }

    public boolean isEmpty() {
        return this.string.isEmpty();
    }

    public String getNext() {
        int nextSeparatorIndex = getNextSeparatorIndex();
        if (nextSeparatorIndex != -1) {
            return this.string.substring(0, nextSeparatorIndex - this.separator.length());
        }
        return this.string;
    }

    public String getString() {
        return this.string;
    }

    public String getOriginal() {
        return this.original;
    }

    private int getNextSeparatorIndex() {
        return this.string.indexOf(this.separator) + this.separator.length();
    }

}
