package core.command_system.commands.administration;

import core.buttons.ButtonEvent;
import core.buttons.ButtonHandler;
import core.buttons.ButtonMessage;
import core.command_system.Cmd;
import core.command_system.CmdInstance;
import core.command_system.CmdParser;
import core.command_system.syntax.Syntax;
import core.command_system.syntax.TextOption;
import core.interaction.Emojie;
import core.interaction.TronicMessage;
import core.permissions.Permission;
import main.Main;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

public class CmdShutdown implements Cmd {

    @Override
    public String invoke() {
        return "shutdown";
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
        return new Syntax().add(new TextOption("reason"));
    }

    @Override
    public CmdInstance createInstance() {
        return new Instance();
    }

    private class Instance implements CmdInstance {

        private Message msg;
        private User user;

        @Override
        public void run(CmdParser parser) {
            this.user = parser.getUser();
            MessageEmbed embed = new TronicMessage("Shutdown", "Do you really want to shut me down?").build();
            parser.getChannel().sendMessage(embed).queue(m -> {
                ButtonMessage btnMsg = new ButtonMessage(m, this::onPress);
                btnMsg.addButton(Emojie.WHITE_CHECK_MARK).queue();
                btnMsg.addButton(Emojie.X).queue();
                parser.getTronic().getButtonHandler().addMessage(btnMsg);
                this.msg = m;
            });
        }

        private boolean onPress(ButtonEvent event) {
            if (event.getUser() != this.user) {
                return true;
            } else if (event.is(Emojie.WHITE_CHECK_MARK)) {
                this.msg.delete().queue();
                Main.shutdown();
                return false;
            } else if (event.is(Emojie.X) && this.msg != null) {
                this.msg.delete().queue();
                return false;
            } else {
                return true;
            }
        }

    }

    @Override
    public String info() {
        return "shuts me down";
    }

    @Override
    public String description() {
        return "shuts me down and posts a reason for that into every guild im on";
    }

}
