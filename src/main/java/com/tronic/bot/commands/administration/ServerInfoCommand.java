package com.tronic.bot.commands.administration;

import com.lowlevelsubmarine.subsconsole.Align;
import com.lowlevelsubmarine.subsconsole.graph_fragments.GraphFragment;
import com.lowlevelsubmarine.subsconsole.graph_fragments.StaticGraphFragment;
import com.lowlevelsubmarine.subsconsole.graphs.GraphComposer;
import com.lowlevelsubmarine.subsconsole.graphs.Vertex;
import com.lowlevelsubmarine.subsconsole.utils.AlignedString;
import com.tronic.bot.buttons.Button;
import com.tronic.bot.buttons.UserButtonValidator;
import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.MessageChanger;
import net.dv8tion.jda.api.entities.Guild;

public class ServerInfoCommand implements Command {

    private CommandInfo info;
    private MessageChanger messageChanger;

    @Override
    public String invoke() {
        return "serverinfo";
    }

    @Override
    public Permission getPermission() {
        return Permission.CO_HOST;
    }

    @Override
    public boolean silent() {
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.ADMINISTRATION;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        this.info = info;
        this.messageChanger = new MessageChanger(info.getCore(), info.getChannel());
        if (info.isGuildContext()) {
            this.messageChanger.change(
                    new TronicMessage(Emoji.WARNING + " This command reveals sensitive information. " +
                            "Are you sure you want to proceed on this server?").b(),
                    new Button(Emoji.WHITE_CHECK_MARK, this::onAccept, new UserButtonValidator(info)),
                    new Button(Emoji.X, this.messageChanger::delete, new UserButtonValidator(info)));
        }
    }

    private void onAccept() {
        this.messageChanger.delete();
        GuildGraph graph = new GuildGraph();
        for (Guild guild : this.info.getJDA().getGuilds()) {
            graph.addVertex(new Vertex<>(null, guild));
        }
        this.info.getChannel().sendFile(graph.render().getBytes(), "guild-overview.txt").queue();
    }

    private class GuildGraph extends GraphComposer<Guild> {

        public GuildGraph() {
            super(
                    new StaticGraphFragment<>("|"),
                    new GuildNameGraphFragment(),
                    new StaticGraphFragment<>("|"),
                    new GuildAdminPermissionGraphFragment(),
                    new StaticGraphFragment<>("|")
            );
        }

    }

    private class GuildNameGraphFragment implements GraphFragment<Guild> {

        @Override
        public AlignedString getLegend() {
            return Align.LEFT.createString("Name");
        }

        @Override
        public AlignedString render(Vertex<Guild> vertex) {
            return Align.LEFT.createString(vertex.getValue().getName());
        }

    }

    private class GuildAdminPermissionGraphFragment implements GraphFragment<Guild> {

        @Override
        public AlignedString getLegend() {
            return Align.LEFT.createString("Admin Permission");
        }

        @Override
        public AlignedString render(Vertex<Guild> vertex) {
            return Align.LEFT.createString(vertex.getValue().getSelfMember().hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR) ? "X" : "-");
        }

    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("serverinfo", "Shows Info about servers", "serverinfo");
    }

}
