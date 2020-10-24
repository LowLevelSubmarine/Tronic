package com.tronic.bot.shortcuts;

public class ShortcutElement {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommands() {
        return commands;
    }

    public void setCommands(String commands) {
        this.commands = commands;
    }

    private String name;
    private String description;
    private String commands;

    public static ShortcutElement builder(String name,String commands)  {
        ShortcutElement se = new ShortcutElement();
        se.setName(name);
        se.setDescription("Shortcut for Tronic");
        se.setCommands(commands);
        return se;
    }


}
