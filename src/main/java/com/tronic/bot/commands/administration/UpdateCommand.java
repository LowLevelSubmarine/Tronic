package com.tronic.bot.commands.administration;

import com.lowlevelsubmarine.envelope.build_provider.Build;
import com.lowlevelsubmarine.envelope.core.Update;
import com.tronic.bot.buttons.Button;
import com.tronic.bot.buttons.UserButtonValidator;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.MessageChanger;

import java.io.IOException;

public class UpdateCommand implements Command {

    private CommandInfo info;
    private MessageChanger msgChanger;
    private Build build;

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
        this.msgChanger = new MessageChanger(info.getCore(), info.getChannel());
        this.msgChanger.change(new TronicMessage(Emoji.HOURGLASS + " checking for updated ...").b());
        try {
            if (info.getCore().getTronicUpdater().isUpToDate()) {
                this.msgChanger.change(new TronicMessage(Emoji.WHITE_CHECK_MARK + " Tronic is already up to date!").b());
            } else {
                this.build = info.getCore().getTronicUpdater().getLatestBuild();
                TronicMessage msg = new TronicMessage(Emoji.WARNING + " Tronic can be updated to " + this.build.getVersion() + "\nClick " + Emoji.WHITE_CHECK_MARK + " if you want to update Tronic.");
                if (this.build.getChangelog() != null) {
                    msg.addField("Changelog", this.build.getChangelog(), false);
                }
                this.msgChanger.change(msg.b(),
                        new Button(Emoji.WHITE_CHECK_MARK, this::onUpdateAccept, new UserButtonValidator(info)),
                        new Button(Emoji.X, this::onUpdateDecline, new UserButtonValidator(info)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.msgChanger.change(new TronicMessage(Emoji.X + " something went wrong while checking for updates!").b());

        }
    }

    private void onUpdateAccept() {
        this.msgChanger.change(new TronicMessage(Emoji.HOURGLASS + " downloading " + this.build.getVersion() + " ...").b());
        Update update = this.info.getCore().getTronicUpdater().download(this.build);
        this.msgChanger.change(new TronicMessage(Emoji.WHITE_CHECK_MARK + " download complete. Update initiated!").b());
        try {
            this.info.getCore().getTronicUpdater().update(update);
        } catch (Exception e) {
            e.printStackTrace();
            this.msgChanger.change(new TronicMessage(Emoji.X + " something went wrong while installing the update!").b());
        }
    }

    private void onUpdateDecline() {
        this.msgChanger.delete();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Update Tronic","Install the newest version of Tronic","update");
    }

}
