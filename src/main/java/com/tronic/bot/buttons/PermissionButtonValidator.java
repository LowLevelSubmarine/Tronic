package com.tronic.bot.buttons;

import com.tronic.bot.commands.CommandHandler;
import com.tronic.bot.commands.Permission;
import com.tronic.bot.core.Core;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

public class PermissionButtonValidator implements Button.Validator {

    private final Core core;
    private final Permission permission;

    public PermissionButtonValidator(Core core, Permission permission) {
        this.core = core;
        this.permission = permission;
    }

    @Override
    public boolean validate(GenericMessageReactionEvent event) {
        boolean validated = this.permission.isValid(event.getUser(), event.getGuild(), this.core);
        if (!validated) {
            event.getChannel().sendMessageEmbeds(CommandHandler.buildMissingPermissionMessage(this.permission).b()).queue();
        }
        return validated;
    }

}
