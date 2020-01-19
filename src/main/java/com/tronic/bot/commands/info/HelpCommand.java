package com.tronic.bot.commands.info;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import net.dv8tion.jda.api.entities.PrivateChannel;

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
        return true;
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        TronicMessage tm = new TronicMessage("Help","");
        for (Command c:info.getTronic().getCommandHandler().getCommands()) {
            tm.addField(c.getHelpInfo().getTitle()+"-"+c.getType().name(),c.getHelpInfo().getShortDescription()+"\n ```"+c.getHelpInfo().getSyntax()+"```",false);
        }
        info.getAuthor().openPrivateChannel().queue((channel)->channel.sendMessage(tm.b()).queue());
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Help","Returns this message","Help");
    }
}
