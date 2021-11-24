package com.tronic.bot.commands.settings;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class SetBroadcastsCommand implements Command {

    @Override
    public String invoke() {
        return "setbroadcasts";
    }

    @Override
    public Permission getPermission() {
        return Permission.ADMIN;
    }

    @Override
    public boolean silent() {
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.SETTINGS;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        boolean newSetting = !info.getGuildStorage().getBroadcastMuted();
        info.getGuildStorage().setBroadcastMuted(newSetting);
        String text;
        if (newSetting) {
            text = "Successfully muted broadcasts";
        } else {
            text = "Successfully unmuted broadcasts";
        }
        MessageEmbed embed = new TronicMessage("Broadcast", text).b();
        info.getChannel().sendMessageEmbeds(embed).complete();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Set Broadcasts", "Enable or disable broadcasts", invoke());
    }

}
