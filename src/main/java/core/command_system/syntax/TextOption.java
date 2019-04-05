package core.command_system.syntax;

public class TextOption implements SyntaxOption {

    private final String description;

    public TextOption(String description) {
        this.description = "§" + description + "§";
    }

    @Override
    public String syntax() {
        return description;
    }
}
