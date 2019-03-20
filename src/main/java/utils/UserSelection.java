package utils;

import java.util.LinkedList;
import java.util.Scanner;

public class UserSelection {

    private final LinkedList<String> options = new LinkedList<>();
    private final String question;

    public UserSelection(String question) {
        this.question = question;
    }

    public void addOption(String option) {
        options.add(option);
    }

    public int getSelection() {
        String optionsString = optionsString();
        System.out.println(question + "\n" + optionsString);
        int selection = -1;
        Scanner scanner = new Scanner(System.in);
        while (selection == -1) {
            try {
                selection = Integer.parseInt(scanner.next()) - 1;
                if (selection < 0 || selection > options.size() - 1) {
                    System.out.println("Selection must be between 1 & " + options.size());
                    selection = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("\nNot a number, try again:\n" + optionsString);
            }
        }
        return selection;
    }

    private String optionsString() {
        String optionsString = "";
        for (int i = 0; i < options.size(); i++) {
            optionsString += "\n" + (i+1) + " - " + options.get(i);
        }
        return optionsString.substring(1);
    }

}
