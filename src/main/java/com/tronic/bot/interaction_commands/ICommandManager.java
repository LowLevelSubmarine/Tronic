package com.tronic.bot.interaction_commands;

import com.tronic.bot.core.Core;
import com.tronic.bot.io.Logger;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class ICommandManager extends ListenerAdapter {

    private final Core core;
    private final HashMap<String, ICommand> map = new HashMap<>();

    public ICommandManager(Core core) {
        this.core = core;
        core.addBootUpHook(this::onBootUp);
        registerCommands();
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        Class<? extends ICommand> clazz = this.map.get(event.getName()).getClass();
        try {
            clazz.getConstructor().newInstance().run(new ICommand.ICommandInfo(event, this.core));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Logger.log(this, "Could not instantiate CommandClass: " + clazz.getName(), e);
        }
    }

    private void registerCommands() {
        registerCommand(new PlayICommand());
    }

    private void registerCommand(ICommand iCommand) {
        this.map.put(iCommand.getName(), iCommand);
    }

    private void onBootUp() {
        this.core.getJDA().addEventListener(this);
        new Thread(() -> {
            List<Command> commandList = this.core.getJDA().retrieveCommands().complete();
            commandList.forEach(c -> c.delete().complete());
            this.map.values().forEach(v -> this.core.getJDA().upsertCommand(v.getCommandData(
                    new CommandData(v.getName(), v.getDescription())
            )).complete());
        }).start();
    }

}
