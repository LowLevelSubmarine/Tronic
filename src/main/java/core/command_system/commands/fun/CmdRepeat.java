package core.command_system.commands.fun;

import core.command_system.Cmd;
import core.command_system.CmdCategory;
import core.command_system.CmdParser;
import core.command_system.CmdInstance;
import core.command_system.arguments.CmdArgument;
import core.command_system.syntax.Syntax;
import core.command_system.syntax.TextOption;
import core.permissions.Permission;
import net.dv8tion.jda.core.EmbedBuilder;

public class CmdRepeat implements Cmd {

    @Override
    public String invoke() {
        return "repeat";
    }

    @Override
    public CmdCategory category() {
        return CmdCategory.FUN;
    }

    @Override
    public boolean guildAccess() {
        return true;
    }

    @Override
    public boolean privateAccess() {
        return true;
    }

    @Override
    public Permission requiredPermission() {
        return Permission.NONE;
    }

    @Override
    public Syntax syntax() {
        return new Syntax().add(new TextOption("the text u want me to repeat"));
    }

    @Override
    public CmdInstance createInstance() {
        return new Instance();
    }

    @Override
    public String info() {
        return "repeats a given text";
    }

    @Override
    public String description() {
        return "reposts the given text into the current channel";
    }

    private class Instance implements CmdInstance {

        @Override
        public void run(CmdParser event) throws CmdArgument.InvalidArgumentException {
            String text = event.getText(0);
            if (text == null) {
                throw new CmdArgument.InvalidArgumentException("", "");
            }
            event.getChannel().sendMessage(new EmbedBuilder().setDescription("Here you go: " + text).build()).queue();
        }

    }

}
