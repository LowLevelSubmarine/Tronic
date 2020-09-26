package com.tronic.bot.commands.administration;

import com.tronic.bot.buttons.Button;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.statics.Info;
import com.tronic.bot.tools.MessageChanger;
import com.tronic.updater.Updater;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toolbox.os.OSTools;
import toolbox.process.OSnotDetectedException;

import java.io.IOException;
import java.net.URL;

public class UpdateCommand implements Command {

    private final Updater updater = new Updater();
    private CommandInfo info;
    private MessageChanger messageChanger;
    private URL updateUrl;
    private final Logger logger = LogManager.getLogger(Updater.class);

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
        this.info = info;
        this.messageChanger = new MessageChanger(this.info.getCore(), this.info.getChannel());
        messageChanger.change(new TronicMessage("Scan for new releases...").b());
        this.updater.getOnlineVersion(this::onVersionReceived, this::onVersionReceiveFail);
    }

    private void onVersionReceived(String updateVersion, URL updateUrl) {
        String currentVersion = Info.VERSION;
        this.updateUrl = updateUrl;
        if (Updater.isBigger(currentVersion, updateVersion)) {
            Button acceptButton = new Button(Emoji.WHITE_CHECK_MARK, this::onAccept);
            Button declineButton = new Button(Emoji.X, this::onDecline);
            this.messageChanger.change(
                    new TronicMessage("Do you want to update Tronic to version " + updateVersion + "?").b(),
                    acceptButton,
                    declineButton
            );
        } else {
            this.messageChanger.change(new TronicMessage("Tronic is already up to date!").b());
        }
    }

    private void onVersionReceiveFail(String version, Exception e) {
        this.messageChanger.change(new TronicMessage("A problem occurred while contacting the update server!").b());
    }

    private void onAccept() {
        this.messageChanger.change(new TronicMessage("Downloading update ...").b());
        downloadUpdate();
    }

    private void onDecline() {
        this.messageChanger.delete();
    }

    private void downloadUpdate() {
        try {
            this.updater.doUpgrade(this.updateUrl, this::onDownloadFinished);
        } catch (IOException e) {
            this.messageChanger.change(new TronicMessage("An error occurred while downloading!").b());
        }
    }

    private void onDownloadFinished(String updateFilePath) {
        this.messageChanger.change(new TronicMessage("Download successful. Restarting!").b());
        update();
    }

    private void update() {
        selfDestructFile();
        info.getCore().shutdown();
        System.exit(0);
    }

    private void selfDestructFile() {
        try {
            int os = OSTools.getOS();
            if (os == OSTools.OS_WINDOWS) {
                Updater.selfDestructWindowsJARFile();
            } else if (os == OSTools.OS_LINUX || os == OSTools.OS_MAC) {
                Updater.selfDestructLinuxFile();
            }
        } catch (OSnotDetectedException e) {
            logger.warn("OS could not be detected!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Update Tronic","Automatically updates the bot to the newest version","update");
    }

}
