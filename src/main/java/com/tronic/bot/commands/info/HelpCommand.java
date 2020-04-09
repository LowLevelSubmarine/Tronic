package com.tronic.bot.commands.info;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;

import java.util.LinkedList;

public class HelpCommand implements Command {

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
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        Permission pem = Permission.getUserPermission(info.getEvent(),info.getCore());
        TronicMessage tm = new TronicMessage("Help","");
        LinkedList<Command> administartionCommand = new LinkedList<>();
        LinkedList<Command> infoCommand = new LinkedList<>();
        LinkedList<Command> funCommand = new LinkedList<>();
        LinkedList<Command> settingsCommand = new LinkedList<>();
        LinkedList<Command> musicCommand = new LinkedList<>();
        LinkedList<Command> commands = info.getCore().getCommandHandler().getCommands();
        for (Command c:commands) {
            if (c.getType() == CommandType.ADMINISTRATION) {
                administartionCommand.add(c);
            } else if (c.getType() == CommandType.INFO) {
                infoCommand.add(c);
            } else if (c.getType() == CommandType.FUN) {
                funCommand.add(c);
            } else if (c.getType() == CommandType.SETTINGS) {
                settingsCommand.add(c);
            } else if (c.getType() == CommandType.MUSIC) {
                musicCommand.add(c);
            }
        }
        tm.addField("","MUSIC COMMANDS",false);
        for (Command c:musicCommand) {
            if (testPem(pem,c)) {  tm.addField(c.getHelpInfo().getTitle()+"-"+c.getType().name(),c.getHelpInfo().getShortDescription()+"\n ```"+c.getHelpInfo().getSyntax()+"```",false);}
        }
        tm.addField("","FUN COMMANDS",false);

        for (Command c:funCommand) {
            if (testPem(pem,c)) {  tm.addField(c.getHelpInfo().getTitle(),c.getHelpInfo().getShortDescription()+"\n ```"+c.getHelpInfo().getSyntax()+"```",false);}
        }
        tm.addField("","INFO COMMANDS",false);
        for (Command c:infoCommand) {
            if (testPem(pem,c)) {  tm.addField(c.getHelpInfo().getTitle(),c.getHelpInfo().getShortDescription()+"\n ```"+c.getHelpInfo().getSyntax()+"```",false);}
        }
        tm.addField("","SETTINGS COMMANDS",false);
        for (Command c:settingsCommand) {
            if (testPem(pem,c)) {  tm.addField(c.getHelpInfo().getTitle(),c.getHelpInfo().getShortDescription()+"\n ```"+c.getHelpInfo().getSyntax()+"```",false);}
        }
        tm.addField("","ADMINISTRATION COMMANDS",false);
        for (Command c:administartionCommand) {
            if (testPem(pem,c)) {  tm.addField(c.getHelpInfo().getTitle(),c.getHelpInfo().getShortDescription()+"\n ```"+c.getHelpInfo().getSyntax()+"```",false);}
        }
        try {
            info.getAuthor().openPrivateChannel().queue((channel)->channel.sendMessage(tm.b()).queue());
        } catch (Exception e) {
            info.getChannel().sendMessage(new TronicMessage(info.getAuthor().getAsMention()+" Please allow directMessages from Tronic to you and try it again").b()).queue();
        }
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
        return new HelpInfo("Help","Returns this message","Help");
    }
}
