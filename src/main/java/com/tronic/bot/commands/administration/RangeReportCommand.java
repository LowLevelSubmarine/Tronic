package com.tronic.bot.commands.administration;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import net.dv8tion.jda.api.entities.Guild;

public class RangeReportCommand implements Command {

    @Override
    public String invoke() {
        return "rangereport";
    }

    @Override
    public Permission getPermission() {
        return Permission.CO_HOST;
    }

    @Override
    public boolean silent() {
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.ADMINISTRATION;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        String guilds = "";
        for (Guild guild : info.getJDA().getGuilds()) {
            guilds += "\n" + guild.getName() + " / " + guild.getMembers().size() + " members";
        }
        info.getChannel().sendMessage(new TronicMessage(
                "Range Report", null
        ).addField(
                "Unique users", info.getJDA().getUsers().size() + "", false
        ).addField(
                "Tronic runs on following guilds", guilds, false
        ).b()).queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Range Report", "Shows info about Tronics range", "");
    }

}
