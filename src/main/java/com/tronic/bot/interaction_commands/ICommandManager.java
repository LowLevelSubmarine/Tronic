package com.tronic.bot.interaction_commands;

import com.tronic.bot.core.Core;
import com.tronic.bot.io.Logger;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;

public class ICommandManager extends ListenerAdapter {

    private final Core core;
    private final HashMap<String, ICommand> map = new HashMap<>();
    private final IButtonManager buttonManager;

    public ICommandManager(Core core) {
        this.core = core;
        this.buttonManager = new IButtonManager(core);
        core.addBootupHook(this::onBootUp);
        registerCommands();
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        Class<? extends ICommand> clazz = this.map.get(event.getName()).getClass();
        new Thread(() -> {
            try {
                clazz.getConstructor().newInstance().run(new ICommand.ICommandInfo(event, this.core));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                Logger.log(this, "Could not instantiate CommandClass: " + clazz.getName(), e);
            }
        }).start();
    }

    public IButtonManager getIButtonManager() {
        return this.buttonManager;
    }

    private void registerCommands() {
        registerCommand(new PlayICommand());
        registerCommand(new SearchICommand());
    }

    private void registerCommand(ICommand iCommand) {
        this.map.put(iCommand.getName(), iCommand);
    }

    private void onBootUp() {
        this.core.getJDA().addEventListener(this);
        LinkedList<CommandData> commandDataList = new LinkedList<>();
        this.map.values().forEach(v -> commandDataList.add(v.getCommandData(new CommandData(v.getName(), v.getDescription()))));
        this.core.getJDA().updateCommands().addCommands(commandDataList).complete();
    }

}
