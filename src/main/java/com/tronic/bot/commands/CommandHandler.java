package com.tronic.bot.commands;

import com.tronic.arguments.Arguments;
import com.tronic.bot.buttons.Button;
import com.tronic.bot.core.Core;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.shortcuts.ShortcutResolver;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.stats.CommandStatisticsElement;
import com.tronic.bot.stats.StatisticsHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simmetrics.StringMetric;
import org.simmetrics.metrics.Levenshtein;

import java.util.Collections;
import java.util.LinkedList;

public class CommandHandler {

    private final Core core;
    private final LinkedList<Command> commands = new LinkedList<>();
    private ShortcutResolver shortcutResolver;
    public CommandHandler(Core core) {
        this.core = core;
        this.shortcutResolver = new ShortcutResolver(core);
    }

    public void addCommand(Command command) {
        this.commands.add(command);
    }

    public LinkedList<Command> getCommands() {
        return this.commands;
    }

    @Nullable
    public Command getCommand(String invoke) {
        for (Command command: this.commands) {
            if (command.invoke().equals(invoke)) {
                return command;
            }
        }
        return null;
    }

    public void handle(String string, MessageReceivedEvent event) {
        CommandSeparator commandSeparator = new CommandSeparator(string);
        if (shortcutResolver.resolveShortcut(commandSeparator.getInvoke(),event)) return;
        LinkedList<CommandInvokeProximityComparable> proximities = new LinkedList<>();
        for (Command command : this.commands) {
            proximities.add(new CommandInvokeProximityComparable(command, commandSeparator.getInvoke()));
        }
        Collections.sort(proximities);
        CommandInvokeProximityComparable bestMatch = proximities.getFirst();
        new CommandThread(bestMatch, commandSeparator.getArguments(), event).start();
    }

    private static final class CommandSeparator {

        private static final String SEPARATOR_STRING = " ";

        private final String invoke;
        private final String arguments;

        public CommandSeparator(String raw) {
            int separatorIndex = raw.indexOf(SEPARATOR_STRING);
            if (separatorIndex < 0) {
                this.invoke = raw;
                this.arguments = null;
            } else {
                this.invoke = raw.substring(0, separatorIndex).toLowerCase();
                this.arguments = raw.substring(separatorIndex + SEPARATOR_STRING.length());
            }
        }

        public String getInvoke() {
            return this.invoke;
        }

        public String getArguments() {
            return this.arguments;
        }

    }

    private static final class CommandInvokeProximityComparable implements Comparable<Object> {

        private static final StringMetric COMPARATOR = new Levenshtein();

        float proximity;
        Command command;

        public CommandInvokeProximityComparable(Command command, String invoke) {
            this.proximity = COMPARATOR.compare(command.invoke(), invoke);
            this.command = command;
        }

        public Float getProximity() {
            return proximity;
        }

        public Command getCommand() {
            return command;
        }

        @Override
        public int compareTo(@NotNull Object obj) {
            if (obj instanceof CommandInvokeProximityComparable) {
                CommandInvokeProximityComparable other = (CommandInvokeProximityComparable) obj;
                return Float.compare(other.proximity, this.proximity);
            }
            return 0;
        }

    }

    private final class CommandThread extends Thread {

        private final Command command;
        private final float commandInvokeProximity;
        private final CommandInfo commandInfo;
        private Message confirmCorrectionMessage;
        private boolean isAutoCompleted;

        private CommandThread(CommandInvokeProximityComparable comparable, String rawArguments, MessageReceivedEvent event) {
            this.command = comparable.getCommand();
            this.commandInvokeProximity = comparable.getProximity();
            this.commandInfo = new CommandInfo(CommandHandler.this.core, new Arguments(rawArguments), event);
        }

        @Override
        public void run() {
            if (this.commandInvokeProximity > 0.3F) {
                this.isAutoCompleted = true;
                checkAndRun();
            } else if (this.commandInvokeProximity > 0.05F) {
                this.confirmCorrectionMessage = this.commandInfo.getChannel().sendMessage(
                        new TronicMessage("Did you mean `" + this.command.invoke() + "` ?").b()
                ).complete();
                Button noButton = new Button(Emoji.X, this::onPressNo);
                Button yesButton = new Button(Emoji.WHITE_CHECK_MARK, this::onPressYes);
                CommandHandler.this.core.getButtonHandler().register(noButton, this.confirmCorrectionMessage).queue();
                CommandHandler.this.core.getButtonHandler().register(yesButton, this.confirmCorrectionMessage).queue();
            }
        }

        private void onPressNo() {
            this.confirmCorrectionMessage.delete().queue();
        }

        private void onPressYes() {
            this.confirmCorrectionMessage.delete().queue();
            this.isAutoCompleted = true;
            checkAndRun();
        }

        private void checkAndRun() {
            saveCommandStatistic();
            if (this.command.silent()) {
                this.commandInfo.getEvent().getMessage().delete().queue();
            }
            if (!command.getPermission().isValid(this.commandInfo.getEvent(), CommandHandler.this.core)) {
                String s = Emoji.WARNING.getUtf8()+"You are not allowed to do this!";

                if (command.getPermission() == Permission.ADMIN) {
                    s += "\nTry asking your server owner for Permission.";
                }
                this.commandInfo.getEvent().getChannel().sendMessage(new TronicMessage(s).b()).queue();
            } else {
                try {
                    this.command.getClass().getDeclaredConstructor().newInstance().run(this.commandInfo);
                } catch (InvalidCommandArgumentsException e) {
                    this.commandInfo.getEvent().getChannel().sendMessage(new TronicMessage("Correct Syntax: "+this.command.getHelpInfo().getSyntax()).b()).queue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void saveCommandStatistic() {
            StatisticsHandler.storeCommandStatistics(new CommandStatisticsElement(
                    this.commandInfo.getEvent().getMessage().getContentRaw(),
                    this.command.invoke(),
                    this.isAutoCompleted
            ),this.commandInfo.getAuthor());
        }

    }

}
