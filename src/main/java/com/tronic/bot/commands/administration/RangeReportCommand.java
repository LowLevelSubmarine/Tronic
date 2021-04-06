package com.tronic.bot.commands.administration;

import com.tronic.bot.buttons.Button;
import com.tronic.bot.buttons.UserButtonValidator;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.MessageChanger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.*;

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
        if (info.isGuildContext()) {
            this.messageChanger.change(
                    new TronicMessage(Emoji.WARNING + " This command reveals sensitive information. " +
                            "Are you sure you want to proceed on this server?").b(),
                    new Button(Emoji.WHITE_CHECK_MARK, this::onAccept, new UserButtonValidator(info)),
                    new Button(Emoji.X, this.messageChanger::delete, new UserButtonValidator(info)));
        }
    }

    private void onAccept() {
        this.messageChanger.change(new TronicMessage(Emoji.HOURGLASS + " loading results").b());
        new UniqueUserCounter(info.getJDA().getGuilds(), this::onUniqueUserCounterSuccess);
    }

    private void onUniqueUserCounterSuccess(Set<Guild> guilds, int uniqueUserCount, long msTime) {
        String guildInfo = "";
        List<Guild> guildsByMemberCount = new LinkedList<>(guilds);
        guildsByMemberCount.sort(Comparator.comparingInt(Guild::getMemberCount));
        Collections.reverse(guildsByMemberCount);
        if (guildsByMemberCount.size() > 20) guildsByMemberCount = guildsByMemberCount.subList(0, 20);
        for (Guild guild : guildsByMemberCount) {
            guildInfo += "\n" + guild.getName() + " / " + (guild.getMemberCount() - 1) + " members";
        }
        this.messageChanger.change(new TronicMessage(
                "Range Report", "Fetched data from " + guilds.size() + " servers in " + msTime + "ms."
        ).addField(
                "Unique users", uniqueUserCount + "", false
        ).addField(
                "Top 20 guild ", guildInfo, false
        ).b());
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Range Report", "Shows info about Tronics range", "rangereport");
    }

    private static class UniqueUserCounter {

        private final HashMap<Guild, Boolean> map = new HashMap<>();
        private final LinkedList<Long> uniqueUsers = new LinkedList<>();
        private final long startTime;
        private final Hook hook;

        public UniqueUserCounter(List<Guild> guilds, Hook hook) {
            this.hook = hook;
            this.startTime = System.currentTimeMillis();
            for (Guild guild : guilds) {
                this.map.put(guild, false);
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
            this.map.put(guild, true);
            if (this.map.values().stream().allMatch(bool -> true)) {
                this.hook.onSuccess(this.map.keySet(), this.uniqueUsers.size(), System.currentTimeMillis() - this.startTime);
            }
        }

        private interface Hook {
            void onSuccess(Set<Guild> guilds, int uniqueUserCount, long msTime);
        }

    }


}
