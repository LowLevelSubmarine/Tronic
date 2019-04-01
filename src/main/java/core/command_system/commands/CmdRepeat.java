package core.command_system.commands;

import core.command_system.Cmd;
import core.command_system.CmdParser;
import core.command_system.CmdInstance;
import core.command_system.arguments.CmdArgument;
import core.permissions.Permission;
import net.dv8tion.jda.core.EmbedBuilder;

public class CmdRepeat implements Cmd {

    @Override
    public String invoke() {
        return "repeat";
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
    public CmdInstance createInstance() {
        return new Instance();
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
