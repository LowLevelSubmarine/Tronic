package com.tronic.bot.commands.administration;

import com.tronic.bot.buttons.Button;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.updater.Updater;
import net.dv8tion.jda.api.entities.Message;
import updater.UpdaterSettings;

import java.io.IOException;
import java.net.URL;

public class UpdateCommand implements Command {
    @Override
    public String invoke() {
        return "update";
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
        Updater updater = new Updater();
        String build = getClass().getPackage().getImplementationVersion();
        System.out.println(build);
        String finalBuild = build;
        updater.getOnlineVersion((version, download) -> {
            if (Updater.isBigger(finalBuild,version)) {
                Message m = info.getChannel().sendMessage(new TronicMessage("Do you want to upgrade to version "+version+"?").b()).complete();
                info.createButton(m,  Emoji.WHITE_CHECK_MARK,(Button button)->{
                    try {
                        updater.doUpgrade(download);
                    } catch (IOException e) {
                        info.getChannel().sendMessage(new TronicMessage("An error occurs while the download").b()).queue();
                    }
                });
                info.createButton(m,Emoji.X,(Button button) -> {

                });
            } else {
                info.getChannel().sendMessage(new TronicMessage("No updates available").b()).queue();
            }
        },(String version,Exception e)->{
            info.getChannel().sendMessage(new TronicMessage("No updates available or an error happens").b()).queue();
        });
    }


    @Override
    public HelpInfo getHelpInfo() {
        return null;
    }
}
