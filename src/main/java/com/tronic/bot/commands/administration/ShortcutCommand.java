package com.tronic.bot.commands.administration;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.SingleArgument;
import com.tronic.bot.commands.*;
import com.tronic.bot.core.Core;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.shortcuts.ShortcutElement;
import com.tronic.bot.storage.GuildStorage;
import com.tronic.bot.storage.ObjectExistsException;

import java.util.NoSuchElementException;

public class ShortcutCommand implements Command {

    @Override
    public String invoke() {
        return "shortcut";
    }

    @Override
    public Permission getPermission() {
        return Permission.ADMIN;
    }

    @Override
    public boolean silent() {
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.ADMINISTRATION;
    }


    //Monstermethode I know, take some cat video https://www.youtube.com/watch?v=dQw4w9WgXcQ
    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        try {
            String option = info.getArguments().splitParse(new SingleArgument()).getOrThrowException();
            String name = info.getArguments().splitParse(new SingleArgument()).getOrThrowException();
            switch (option) {
                case "create":
                    if (isNameBlocked(name,info.getCore())) {
                        info.getChannel().sendMessageEmbeds(new TronicMessage("Cannot create shortcut '"+name+"', the name is reserved for a Tronic feature").b()).queue();
                        return;
                    }
                    String commands = info.getArguments().getString();
                    try {
                        String[]cs = commands.split("\\\\");
                        for(String c:cs) {
                            String invoke = c.contains(" ")? c.split(" ")[0]: c;
                            Command command = info.getCore().getCommandHandler().getCommand(invoke);
                            if (command== null) {
                                info.getChannel().sendMessageEmbeds(new TronicMessage("Syntax error! Command '"+invoke+"' not found").b()).queue();
                                return;
                            }else {
                                if (command.getPermission() != Permission.NONE) {
                                    info.getChannel().sendMessageEmbeds(new TronicMessage("Syntax error! Command '"+c+"' has a permission level over NONE.").b()).queue();
                                    return;
                                }
                            }
                        }
                        info.getCore().getStorage().getGuild(info.getGuild()).addShortcut(ShortcutElement.builder(name,commands));
                        info.getChannel().sendMessageEmbeds(new TronicMessage("Created shortcut '"+name+"'").b()).queue();
                    }catch (ObjectExistsException e) {
                        info.getChannel().sendMessageEmbeds(new TronicMessage("Cannot create shortcut '"+name+"', because it already exists").b()).queue();
                    }
                    break;
                case "delete":
                    try {
                        info.getCore().getStorage().getGuild(info.getGuild()).removeShortcut(name);
                        info.getChannel().sendMessageEmbeds(new TronicMessage("Delete shortcut '"+name+"'").b()).queue();
                    }catch (NoSuchElementException e) {
                        info.getChannel().sendMessageEmbeds(new TronicMessage("Cannot delete shortcut '"+name+"', because it does not exists").b()).queue();
                    }
                    break;
            }
        } catch (InvalidArgumentException |NullPointerException e) {
            info.getChannel().sendMessageEmbeds(new TronicMessage("Please use create|delete as an option!").b()).queue();
        }
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Shortcut","Manages shortcuts on the server","shortcut create/delete <name> <commands>");
    }

    private boolean isNameBlocked(String name, Core core) {
        boolean isBlocked = false;
        for (Command command : core.getCommandHandler().getCommands()) {
            if (command.invoke().equals(name)) {
                isBlocked =true;
                break;
            }
        }
        return isBlocked;
    }
}
