package core.command_system.arguments;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgInteger implements CmdArgument<Integer> {

    private static final int OPTIONS_OUTPUT_TOO_LONG_AFTER = 10;

    private Integer min;
    private Integer max;
    private LinkedList<Integer> options;

    public ArgInteger() {}

    public ArgInteger(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public ArgInteger(Integer... options) {
        this.options = new LinkedList<>(Arrays.asList(options));
    }

    @Override
    public Integer validate(String raw) throws InvalidArgumentException {
        Integer integer;
        try {
            int input = Integer.valueOf(raw);
            integer = input;
        } catch (NumberFormatException e) {
            integer = null;
        }
        if (min != null) {
            if (integer == null || integer < min || integer > max) {
                throw new InvalidArgumentException(raw, "A value between " + min + " and " + max + ".");
            }
        } else if (options != null) {
            if (integer == null || options.contains(integer)) {
                throw new InvalidArgumentException(raw, "One of the following values: " + optionsAsString());
            }
        }
        return integer;
    }

    private String optionsAsString() {
        StringBuilder builder = new StringBuilder();
        boolean tooLong = this.options.size() > OPTIONS_OUTPUT_TOO_LONG_AFTER;
        List<Integer> outOptions = this.options;
        if (tooLong) {
            outOptions = outOptions.subList(0, OPTIONS_OUTPUT_TOO_LONG_AFTER);
        }
        for (Integer option : outOptions) {
            builder.append(", " + option);
        }
        if (tooLong) {
            builder.append(", ...");
        } else {
            builder.append(".");
        }
        return builder.delete(0, 2).toString();
    }
}
