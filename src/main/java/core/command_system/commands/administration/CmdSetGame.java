package core.command_system.commands.administration;

import core.command_system.Cmd;
import core.command_system.CmdInstance;
import core.command_system.CmdParser;
import core.command_system.arguments.CmdArgument;
import core.command_system.syntax.Syntax;
import core.command_system.syntax.TextOption;
import core.permissions.Permission;
import net.dv8tion.jda.core.entities.Game;

public class CmdSetGame implements Cmd {

    @Override
    public String invoke() {
        return "setgame";
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
        return Permission.BOT_ADMIN;
    }

    @Override
    public Syntax syntax() {
        return new Syntax().addOption(new TextOption("the game"));
    }

    @Override
    public CmdInstance createInstance() {
        return new Instance();
    }

    @Override
    public String info() {
        return "Sets the game im playing";
    }

    @Override
    public String description() {
        return "Set the text below my username - the game im playing. " +
                "Keep in mind that \"playing\" is always in front.";
    }

    private class Instance implements CmdInstance {
        @Override
        public void run(CmdParser parser) throws CmdArgument.InvalidArgumentException {
            String game = parser.getText(0);
            parser.getCore().getStorage().getBot().setGame(game);
            parser.getCore().setGame(game);
        }
    }

}
