package core.command_system;

import core.Tronic;
import core.command_system.arguments.CmdArgument;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CmdParser {

    private final Tronic tronic;
    private final String invoke;
    private final String[] args;
    private final MessageReceivedEvent event;

    public CmdParser(Tronic tronic, MessageReceivedEvent event) throws InvalidSyntaxException {
        this.tronic = tronic;
        String prefix = ">";
        String raw = event.getMessage().getContentDisplay();
        if (!raw.startsWith(">")) {
            throw new InvalidSyntaxException();
        }
        raw = raw.substring(prefix.length());
        String[] rawSplit = raw.split(" ");
        if (rawSplit.length == 0) {
            throw new InvalidSyntaxException();
        }
        this.invoke = rawSplit[0];
        this.args = Arrays.copyOfRange(rawSplit, 1, rawSplit.length);
        this.event = event;
    }

    public boolean isGuildAccess() {
        return this.event.getTextChannel() != null;
    }

    public boolean isPrivateAccess() {
        return this.event.getPrivateChannel() != null;
    }

    public MessageChannel getChannel() {
        return this.event.getChannel();
    }

    public User getUser() {
        return event.getAuthor();
    }

    public Member getMember() {
        return event.getMember();
    }

    public Guild getGuild() {
        return event.getGuild();
    }

    public String getInvoke() {
        return this.invoke;
    }

    public <T> T getArgument(int index, CmdArgument<T> arg) throws CmdArgument.InvalidArgumentException {
        if (validIndex(index)) {
            return arg.validate(args[index]);
        }
        return null;
    }

    public String getText(int index) throws CmdArgument.InvalidArgumentException {
        if (validIndex(index)) {
            return Arrays.stream(this.args).skip(index).map(s -> " " + s).collect(Collectors.joining()).substring(1);
        } else {
            throw new CmdArgument.InvalidArgumentException("Any text");
        }
    }

    public Tronic getTronic() {
        return this.tronic;
    }

    private boolean validIndex(int index) {
        return this.args.length > index;
    }

    public class InvalidSyntaxException extends Exception {}

}
