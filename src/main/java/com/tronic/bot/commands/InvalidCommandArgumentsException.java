package com.tronic.bot.commands;

import com.tronic.bot.io.TronicMessage;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class InvalidCommandArgumentsException extends Exception {

    public MessageEmbed getErrorMessage() {
        return new TronicMessage("LOL!").b();
    }

}
