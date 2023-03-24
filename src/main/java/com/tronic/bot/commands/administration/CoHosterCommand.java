package com.tronic.bot.commands.administration;

import com.tronic.arguments.InvalidArgumentException;
import com.tronic.arguments.SingleArgument;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import net.dv8tion.jda.api.entities.Member;

public class CoHosterCommand implements Command {

    @Override
    public String invoke() {
        return "cohoster";
    }

    @Override
    public Permission getPermission() {
        return Permission.HOST;
    }

    @Override
    public boolean silent() {
        return true;
    }

    @Override
    public CommandType getType() {
        return CommandType.ADMINISTRATION;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        try {
            String option = info.getArguments().splitParse(new SingleArgument()).getOrThrowException();
            String users = "";
            if (info.getEvent().getMessage().getMentions().getMembers().isEmpty()) {
                throw new InvalidCommandArgumentsException();
            }
            for (Member member : info.getEvent().getMessage().getMentions().getMembers()) {
                users += member.getAsMention();
                switch (option) {
                    case "add":
                        info.getCore().getStorage().getStatic().addCoHoster(member.getUser());
                        break;
                    case "remove":
                        info.getCore().getStorage().getStatic().removeCoHoster(member.getUser());
                        break;
                    default:
                        throw new InvalidCommandArgumentsException();
                }
            }
            String action = option.equals("add")? "Added":"Removed";
            String word = option.equals("add")? "to":"from";
            info.getChannel().sendMessageEmbeds(new TronicMessage(action+": "+users+" "+word+" the Cohoster list").b()).queue();
        } catch (InvalidArgumentException|NullPointerException e) {
            throw new InvalidCommandArgumentsException();
        }
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Set/Remove Cohoster","Add or remove a cohoster.","cohoster add/remove <@mentions>" );
    }
}
