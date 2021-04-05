package com.tronic.bot.commands.administration;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.MessageChanger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RangeReportCommand implements Command {

    private CommandInfo info;
    private MessageChanger messageChanger;

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
        this.info = info;
        this.messageChanger = new MessageChanger(info.getCore(), info.getChannel());
        this.messageChanger.change(new TronicMessage(Emoji.HOURGLASS + " loading results").b());
        new UniqueUserCounter(info.getJDA().getGuilds(), this::onUniqueUserCounterSuccess);
    }

    private void onUniqueUserCounterSuccess(int uniqueUserCount) {
        String guilds = "";
        for (Guild guild : this.info.getJDA().getGuilds()) {
            guilds += "\n" + guild.getName() + " / " + (guild.getMemberCount() - 1) + " members";
        }
        this.messageChanger.change(new TronicMessage(
                "Range Report", null
        ).addField(
                "Unique users", uniqueUserCount + "", false
        ).addField(
                "Tronic runs on following guilds", guilds, false
        ).b());
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Range Report", "Shows info about Tronics range", "rangereport");
    }

    private static class UniqueUserCounter {

        private final HashMap<Long, Boolean> map = new HashMap<>();
        private final LinkedList<Long> uniqueUsers = new LinkedList<>();
        private final Hook hook;

        public UniqueUserCounter(List<Guild> guilds, Hook hook) {
            this.hook = hook;
            for (Guild guild : guilds) {
                this.map.put(guild.getIdLong(), false);
                new Thread(() -> {
                    List<Member> preCachedUsers = guild.getMembers();
                    LinkedList<Long> usersToUnload = new LinkedList<>();
                    for (Member member : guild.loadMembers().get()) {
                        if (!this.uniqueUsers.contains(member.getIdLong())) {
                            this.uniqueUsers.add(member.getIdLong());
                        }
                        if (!preCachedUsers.contains(member)) {
                            usersToUnload.add(member.getIdLong());
                        }
                    }
                    setGuildFetched(guild);
                    for (long userToUnload : usersToUnload) {
                        guild.unloadMember(userToUnload);
                    }
                }).start();
            }
        }

        private void setGuildFetched(Guild guild) {
            this.map.put(guild.getIdLong(), true);
            if (this.map.values().stream().allMatch(aBoolean -> true)) {
                this.hook.onSuccess(this.uniqueUsers.size());
            }
        }

        private interface Hook {
            void onSuccess(int uniqueUserCount);
        }

    }


}
