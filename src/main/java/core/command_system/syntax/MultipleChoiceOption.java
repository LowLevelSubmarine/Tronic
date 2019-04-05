package core.command_system.syntax;

public class MultipleChoiceOption implements SyntaxOption {

    private final String[] options;
    public MultipleChoiceOption(String... options) {
        this.options = options;
    }

    @Override
    public String syntax() {
        String string = "<";
        for (String option : options) {
            string += "|" + option;
        }
        return string.substring(2) + ">";
    }

}
