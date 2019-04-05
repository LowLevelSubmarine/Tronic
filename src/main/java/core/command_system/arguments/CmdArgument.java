package core.command_system.arguments;

public interface CmdArgument<T> {

    T validate(String raw) throws InvalidArgumentException;

    class InvalidArgumentException extends Exception {

        private final String input;
        private final String expected;

        public InvalidArgumentException(String input, String expected) {
            this.input = input;
            this.expected = expected;
        }

        public InvalidArgumentException(String expected) {
            this.input = null;
            this.expected = expected;
        }

        public String getInput() {
            return this.input;
        }

        public String getExpected() {
            return this.expected;
        }

    }

}
