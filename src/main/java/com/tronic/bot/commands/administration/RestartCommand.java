package com.tronic.bot.commands.administration;

import com.tronic.bot.buttons.Button;
import com.tronic.bot.buttons.UserButtonValidator;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.MessageChanger;


public class RestartCommand implements Command {

    private MessageChanger messageChanger;
    private CommandInfo info;

    @Override
    public String invoke() {
        return "restart";
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
                    new TronicMessage("Do you really want to restart me?").b(),
                    new Button(Emoji.WHITE_CHECK_MARK, this::onConfirm, new UserButtonValidator(info)),
                    new Button(Emoji.X, this.messageChanger::delete, new UserButtonValidator(info)));
        }
    }
    private void onConfirm() {
        this.messageChanger.delete();
        this.info.getCore().restartTronic();
    }


    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Restart Tronic","Restart the bot","restart");
    }

}
