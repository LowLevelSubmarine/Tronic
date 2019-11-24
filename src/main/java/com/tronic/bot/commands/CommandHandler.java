package com.tronic.bot.commands;

import com.tronic.arguments.Arguments;
import com.tronic.bot.Tronic;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.LinkedList;

public class CommandHandler {

    private final Tronic tronic;
    private final LinkedList<Command> commands = new LinkedList<>();

    public CommandHandler(Tronic tronic) {
        this.tronic = tronic;
    }

    public void addCommand(Command command) {
        this.commands.add(command);
    }

    public void handle(String string, MessageReceivedEvent event) {
        String separator = " ";
        int separatorIndex = string.indexOf(separator);
        String invoke;
        String arguments;
        if (separatorIndex != -1) {
            invoke = string.substring(0, separatorIndex).toLowerCase();
            arguments = string.substring(separatorIndex + separator.length());
        } else {
            invoke = string;
            arguments = "";
        }
        for (Command command : this.commands) {
            if (command.invoke().equals(invoke)) {
                runChecked(command, arguments, event);
                return;
            }
        }
    }

    private void runChecked(Command command, String string, MessageReceivedEvent event) {
        if (command.silent()) {
            event.getMessage().delete().queue();
        }
        if (command.getPermission().isValid()) {
            new CommandThread(command, string, event).start();
        }
    }

    private class CommandThread extends Thread {

        private final Command command;
        private final CommandInfo commandInfo;

        private CommandThread(Command command, String string, MessageReceivedEvent event) {
            this.command = command;
            this.commandInfo = new CommandInfo(CommandHandler.this.tronic, new Arguments(string), event);
        }

        @Override
        public void run() {
            try {
                this.command.getClass().newInstance().run(this.commandInfo);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvalidCommandArgumentsException e) {
                this.commandInfo.getEvent().getChannel().sendMessage(e.getErrorMessage()).queue();
            }
        }

    }

}
