package com.tronic.bot.commands.administration;

import com.tronic.bot.buttons.Button;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.statics.Info;
import com.tronic.updater.Updater;
import net.dv8tion.jda.api.entities.Message;
import net.tetraowl.watcher.toolbox.JavaTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toolbox.os.OSTools;
import toolbox.process.OSnotDetectedException;

import java.io.IOException;
import java.net.URISyntaxException;

public class UpdateCommand implements Command {
    Logger logger = LogManager.getLogger(Updater.class);

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
        String build = Info.VERSION;
        updater.getOnlineVersion((version, download) -> {
            if (Updater.isBigger(build,version)) {
                Message m = info.getChannel().sendMessage(new TronicMessage("Do you want to update Tronic to version "+version+"?").b()).complete();
                info.createButton(m,  Emoji.WHITE_CHECK_MARK,(Button button)->{
                    try {
                        updater.doUpgrade(download,(String file)->{
                            m.editMessage(new TronicMessage("Download successful. Restarting!\n").b()).queue();
                            String ptJar = JavaTools.getJarUrl(Updater.class);
                            int os = 7777;
                            try {
                                os = OSTools.getOS();
                            } catch (OSnotDetectedException e) {
                                logger.warn("OS not detected!");
                            }
                            if (os == OSTools.OS_WINDOWS) {
                                try {
                                    Updater.selfDestructWindowsJARFile();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (os ==OSTools.OS_LINUX || os==OSTools.OS_MAC) {
                                try {
                                    Updater.selfDestructLinuxFile();
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }
                            }
                            info.getCore().shutdown();
                            System.exit(0);
                        });
                    } catch (IOException e) {
                        info.getChannel().sendMessage(new TronicMessage("An error occurred while downloading!").b()).queue();
                    }
                });
                info.createButton(m,Emoji.X,(Button button) -> {

                });
            } else {
                info.getChannel().sendMessage(new TronicMessage("There are currently no updates available").b()).queue();
            }
        },(String version,Exception e)->{
            info.getChannel().sendMessage(new TronicMessage("An error occurred").b()).queue();
        });
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Update Tronic","Automatically updates the bot to the newest version","update");
    }

}
