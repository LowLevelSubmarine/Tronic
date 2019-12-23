package com.tronic.bot.commands;

import com.tronic.arguments.Arguments;
import com.tronic.bot.Tronic;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.tools.StatisticsSaver;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.simmetrics.metrics.Levenshtein;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
        ArrayList<CommandHandler.DifferencesObj> differences = new ArrayList<>();
        for (Command command : this.commands) {
            CommandHandler.DifferencesObj obj = new DifferencesObj(new Levenshtein().compare(invoke, command.invoke()), command);
            differences.add(new DifferencesObj(new Levenshtein().compare(invoke, command.invoke()), command));
        }
        if (differences.size()>=1 ) {
            Collections.sort(differences);
            DifferencesObj obj = differences.get(differences.size()-1);
            if (obj.getFl()== 1f) {
                this.tronic.getStorage().getUser(event.getAuthor()).storeSatistic(new StatisticsSaver.StatisticElement(string,new Date(System.currentTimeMillis()),event.getAuthor().getId(),false));
                runChecked(obj.getCommand(),arguments,event);
            } else if (obj.getFl()>= 0.7f) {
                this.tronic.getStorage().getUser(event.getAuthor()).storeSatistic(new StatisticsSaver.StatisticElement(string,new Date(System.currentTimeMillis()),event.getAuthor().getId(),true));
                runChecked(obj.getCommand(),arguments,event);
            } else if (obj.getFl()>= 0.3f) {
                event.getChannel().sendMessage(new TronicMessage("Do you mean `"+obj.getCommand().invoke()+"` ?").b()).queue();
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

    private static class DifferencesObj implements Comparable<Object> {

        Float fl;
        Command command;

        public DifferencesObj(Float fl, Command command) {
            this.fl =fl;
            this.command = command;
        }

        public Float getFl() {
            return fl;
        }

        public Command getCommand() {
            return command;
        }

        @Override
        public int compareTo(@NotNull Object o) {
            DifferencesObj diff = (DifferencesObj) o;
            return this.fl.compareTo(diff.getFl());
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
                this.command.getClass().getDeclaredConstructor().newInstance().run(this.commandInfo);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            } catch (InvalidCommandArgumentsException e) {
                this.commandInfo.getEvent().getChannel().sendMessage(e.getErrorMessage()).queue();
            }
        }

    }

}
