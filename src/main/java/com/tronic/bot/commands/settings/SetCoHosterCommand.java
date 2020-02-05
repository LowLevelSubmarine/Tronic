package com.tronic.bot.commands.settings;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import net.dv8tion.jda.api.entities.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetCoHosterCommand implements Command {
    @Override
    public String invoke() {
        return "setcohoster";
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
        final Pattern pattern = Pattern.compile("(?<=<@!)(.*)(?=>)", Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher( info.getArguments().getString());
        if (matcher.find()) {
            User user = info.getJDA().getUserById(matcher.group());
            info.getCore().getStorage().getStatic().addCoHoster(user);
            info.getChannel().sendMessage(new TronicMessage("Add user "+user.getAsMention()+" to coHoster list").b()).queue();
        } else {
            info.getChannel().sendMessage(new TronicMessage("No valid mention found! Please type  ```setcohoster @Mention```").b()).queue();
        }
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("setCoHoster","add a new cohoster","setcohoster");
    }
}
