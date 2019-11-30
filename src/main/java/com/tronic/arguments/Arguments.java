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

    public <T> ArgumentResult<T> splitParse(Argument<T> argument) {
        if (!isEmpty()) {
            ArgumentResult<T> result = parse(argument);
            split();
            return result;
        }
        return null;
    }

    public <T> ArgumentResult<T> parse(Argument<T> argument) {
        if (!isEmpty()) {
            try {
                return new ArgumentResult<>(argument.parse(this));
            } catch (InvalidArgumentException e) {}
        }
        return new ArgumentResult<>();
    }

    public void split() {
        int nextSeparatorIndex = getNextSeparatorIndex();
        if (nextSeparatorIndex != -1) {
            this.string = this.string.substring(nextSeparatorIndex + this.separator.length());
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
            return this.string.substring(0, nextSeparatorIndex);
        }
        return this.string;
    }

    public String getString() {
        return this.string;
    }

    public String getOriginal() {
        return this.original;
    }

    public Arguments copy() {
        return new Arguments(this.string);
    }

    private int getNextSeparatorIndex() {
        return this.string.indexOf(this.separator);
    }

    public static class ArgumentResult<T> {

        private final T result;
        private final boolean valid;

        private ArgumentResult(T result) {
            this.result = result;
            this.valid = true;
        }

        private ArgumentResult() {
            this.result = null;
            this.valid = false;
        }

        public T get() {
            return this.result;
        }

        public boolean valid() {
            return this.valid;
        }

        public void throwException() throws InvalidArgumentException {
            if(!valid()) {
                throw new InvalidArgumentException();
            }
        }

        public T getOrThrowException() throws InvalidArgumentException {
            if (valid()) {
                return this.result;
            } else {
                throw new InvalidArgumentException();
            }
        }

    }

}
