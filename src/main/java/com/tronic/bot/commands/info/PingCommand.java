package com.tronic.bot.commands.info;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements Command {

    @Override
    public String invoke() {
        return "ping";
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
    public void run(CommandInfo info) {
        JDA jda = info.getJDA();
        long pingTime = jda.getGatewayPing();
        info.getEvent().getChannel().sendMessage(new TronicMessage("The current API response time is  "+pingTime+"ms").b()).queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Ping","Shows you the ping of the Discord API.","ping");
    }
}
