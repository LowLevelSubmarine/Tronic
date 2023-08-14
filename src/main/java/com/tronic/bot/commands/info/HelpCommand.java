package com.tronic.bot.commands.info;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class HelpCommand implements Command {
    public static final String URL = "https://tronicbot.com/help?token=";

    @Override
    public String invoke() {
        return "help";
    }

    @Override
    public Permission getPermission() {
        return Permission.NONE;
    }

    @Override
    public boolean silent() {
        return true;
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        TronicMessage tm = new TronicMessage("Helptext for Tronic","");
        HashMap<CommandType,LinkedList<Command>> hm  = new HashMap<>();
        LinkedList<Command> commands = info.getCore().getCommandHandler().getCommands();
        for (Command c:commands) {
            if (!hm.containsKey(c.getType())) {
                LinkedList<Command> l = new LinkedList<>();
                l.add(c);
                hm.put(c.getType(),l);
            } else {
                LinkedList<Command> l = hm.get(c.getType());
                l.add(c);
            }
        }

        for(CommandType comT:CommandType.values()) {
            String s ="";
            for (Command c: hm.get(comT)) {
                s += "`"+c.getHelpInfo().getSyntax()+"` - "+c.getHelpInfo().getShortDescription()+"\n";
            }
            tm.addField(comT.getDisplayName()+" "+comT.getEmoji().getUtf8(),s,false);
        }

        info.getAuthor().openPrivateChannel().complete().sendMessageEmbeds(tm.b()).queue();
    }

    private boolean testPem(Permission pem,Command c) {
        if (c.getPermission() == Permission.ADMIN||c.getPermission() == Permission.NONE) {
            return true;
        } else if (c.getPermission()==Permission.CO_HOST && pem == Permission.CO_HOST ||c.getPermission()==Permission.CO_HOST && pem == Permission.HOST) {
            return true;
        } else return c.getPermission() == Permission.HOST && pem == Permission.HOST;
    }


    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Help","Returns this message","help");
    }
}
