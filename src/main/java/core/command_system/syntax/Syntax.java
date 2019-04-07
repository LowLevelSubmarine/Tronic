package core.command_system.syntax;

import java.util.LinkedList;

public class Syntax {

    private LinkedList<SyntaxOption> options = new LinkedList<>();

    public Syntax add(SyntaxOption option) {
        this.options.add(option);
        return this;
    }

    @Override
    public String toString() {
        String string = "";
        for (SyntaxOption option : options) {
            string += " " + option.syntax();
        }
        return string.substring(1);
    }

}
