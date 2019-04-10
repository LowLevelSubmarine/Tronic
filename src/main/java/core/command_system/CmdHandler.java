package core.command_system;

import core.Tronic;
import core.command_system.arguments.CmdArgument;
import core.command_system.commands.administration.CmdRestart;
import core.command_system.commands.administration.CmdSetGame;
import core.command_system.commands.administration.CmdShutdown;
import core.command_system.commands.administration.CmdUpdate;
import core.command_system.commands.fun.CmdRepeat;
import core.command_system.commands.smartbot.CmdListen;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CmdHandler extends ListenerAdapter {

    private final Tronic tronic;

    private final Cmd[] cmds = {
            new CmdSetGame(),
            new CmdRepeat(),
            new CmdRestart(),
            new CmdUpdate(),
            new CmdListen(),
            new CmdShutdown()};

    public CmdHandler(Tronic tronic) {
        this.tronic = tronic;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        System.out.println(event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay());
        try {
            CmdParser parser = new CmdParser(tronic, event);
            for (Cmd cmd : this.cmds) {
                if (parser.getInvoke().equals(cmd.invoke())) {
                    if (parser.isGuildAccess() && cmd.guildAccess()) {
                        if (cmd.requiredPermission().hasPermission(parser.getMember(), this.tronic)) {
                            run(cmd, parser);
                            return;
                        }
                    } else if (cmd.privateAccess()) {
                        if (cmd.requiredPermission().hasPermission(parser.getUser(), this.tronic)) {
                            run(cmd, parser);
                            return;
                        }
                    }
                } else {
                    //TODO: Send unknown command warning
                }
            }
        } catch (CmdParser.InvalidSyntaxException e) {
            //Ignoring non commands
        }
    }

    private void run(Cmd cmd, CmdParser parser) {
        new Thread(new CmdExecutor(cmd.createInstance(), parser)).run();
    }

    private class CmdExecutor implements Runnable {

        private CmdInstance instance;
        private CmdParser parser;

        private CmdExecutor(CmdInstance instance, CmdParser parser) {
            this.instance = instance;
            this.parser = parser;
        }

        @Override
        public void run() {
            try {
                this.instance.run(this.parser);
            } catch (CmdArgument.InvalidArgumentException e) {
                System.out.println("Expected: " + e.getExpected() + " Input: " + e.getInput());
            }
        }

    }

}
