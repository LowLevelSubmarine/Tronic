package com.tronic.bot.commands.administration;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.LinkedList;
import java.util.List;

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
        LinkedList<Long> users = new LinkedList<>();
        for (Guild guild : info.getJDA().getGuilds()) {
            List<Member> loadedMembers = guild.getMembers();
            for (Member member : guild.loadMembers().get()) {
                if (!users.contains(member.getIdLong())) {
                    users.add(member.getIdLong());
                }
                if (!loadedMembers.contains(member)) {
                    guild.unloadMember(member.getIdLong());
                }
            }
            guilds += "\n" + guild.getName() + " / " + (guild.getMemberCount() - 1) + " members";
        }
        info.getChannel().sendMessage(new TronicMessage(
                "Range Report", null
        ).addField(
                "Unique users", users.size() + "", false
        ).addField(
                "Tronic runs on following guilds", guilds, false
        ).b()).queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Range Report", "Shows info about Tronics range", "rangereport");
    }

}
