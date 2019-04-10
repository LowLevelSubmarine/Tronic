package core.command_system.commands.administration;

import core.command_system.Cmd;
import core.command_system.CmdCategory;
import core.command_system.CmdInstance;
import core.command_system.CmdParser;
import core.command_system.arguments.CmdArgument;
import core.command_system.syntax.MultipleChoiceOption;
import core.command_system.syntax.Syntax;
import core.command_system.syntax.TextOption;
import core.permissions.Permission;
import net.dv8tion.jda.core.EmbedBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class CmdUpdate implements Cmd {
    @Override
    public String invoke() {
        return "update";
    }

    @Override
    public CmdCategory category() {
        return CmdCategory.ADMINISTRATION;
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
        return null;
    }

    @Override
    public CmdInstance createInstance() {
        return new Updater();
    }

    @Override
    public String info() {
        return "Updates me";
    }

    @Override
    public String description() {
        return "updates the Bot to a stable or the newest (or another) version.";
    }

    private class Updater implements CmdInstance {

        @Override
        public void run(CmdParser event) {
            stableDwnl();
            event.getChannel().sendMessage(new EmbedBuilder().setDescription("Stable").build()).queue();


        }

        private void stableDwnl() {

        }

        private void expBuild() {
            try {
                InputStream stream = new URL("https://github.com/LowLevelSubmarine/Tronic/archive/master.zip").openStream();
                Files.copy(stream, Paths.get("H:\\Jonas\\Desktop\\CmdTest\\Bot_Tmp.zip"), StandardCopyOption.REPLACE_EXISTING);

            } catch (Exception e) {
                return;
            }

        }

    }
}

