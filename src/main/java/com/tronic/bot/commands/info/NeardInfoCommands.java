package com.tronic.bot.commands.info;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Info;
import net.tetraowl.watcher.toolbox.JavaTools;

public class NeardInfoCommands implements Command {
    @Override
    public String invoke() {
        return "neardinfo";
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
        TronicMessage tm = new TronicMessage("NeardInfo","https://github.com/LowLevelSubmarine/Tronic");
        if (Info.VERSION != null) {
            tm.addField("Tronic version", Info.VERSION,false);
        }
        tm.addField("Jvm Version", System.getProperty("java.version"),false);
        tm.addField("OS", System.getProperty("os.name")+" "+System.getProperty("os.version"),false);
        tm.addField("Changelog", Info.CHANGELOG,false);
        info.getChannel().sendMessage(tm.b()).queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Info","Shows some neardy info about the bot","neardinfo");
    }
}
