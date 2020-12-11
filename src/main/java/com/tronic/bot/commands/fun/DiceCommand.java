package com.tronic.bot.commands.fun;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;

import java.util.Random;

public class DiceCommand implements Command {

    @Override
    public String invoke() {
        return "dice";
    }

    @Override
    public Permission getPermission() {
        return Permission.NONE;
    }

    @Override
    public boolean silent() {
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.FUN;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        int value = new Random().nextInt(6) + 1;
        info.getEvent().getChannel().sendMessage(new TronicMessage("The dice says: " + value).b()).queue();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Dice","Role the dice!","dice");
    }

}
