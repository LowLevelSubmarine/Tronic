package com.tronic.bot.commands;

import com.tronic.arguments.Arguments;
import com.tronic.bot.Tronic;
import net.dv8tion.jda.api.JDA;

public class CommandInfo {

    private final Tronic tronic;
    private final Arguments arguments;

    public CommandInfo(Tronic tronic, Arguments arguments) {
        this.tronic = tronic;
        this.arguments = arguments;
    }

    public Tronic getTronic() {
        return this.tronic;
    }

    public Arguments getArguments() {
        return this.arguments;
    }

    public JDA getJDA() {
        return this.tronic.getJDA();
    }

}
