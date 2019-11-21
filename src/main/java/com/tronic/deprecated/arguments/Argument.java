package com.tronic.deprecated.arguments;

public interface Argument {

    void parse(Info info) throws ParseException;

    class ParseException extends Exception {}

    class Info {

        private String string;
        private String separator;
        private final String original;

        public Info(String string, String separator) {
            this.separator = separator;
            this.string = string;
            this.original = string;
        }

        public String getString() {
            return string;
        }

        public String getSeparator() {
            return separator;
        }

        public String getOriginal() {
            return this.original;
        }

        public String splice() {
            int indOf = this.string.indexOf(this.separator);
            if (indOf != -1) {
                String temp = this.string.substring(0, indOf);
                this.string = this.string.substring(indOf + this.separator.length());
                return temp;
            } else {
                return this.string;
            }
        }

        public boolean isEnd() {
            return this.string.isEmpty();
        }

    }

}

