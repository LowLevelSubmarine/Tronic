package com.tronic.bot.buttons;

import com.tronic.bot.commands.CommandInfo;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

public class UserButtonValidator implements Button.Validator {

    private final User user;

    public UserButtonValidator(CommandInfo info) {
        this(info.getAuthor());
    }

    public UserButtonValidator(User user) {
        this.user = user;
    }

    @Override
    public boolean validate(GenericMessageReactionEvent event) {
        return this.user.equals(event.getUser());
    }

}
