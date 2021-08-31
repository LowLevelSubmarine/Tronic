package com.tronic.bot.interaction_commands;

import com.tronic.bot.core.Core;
import com.tronic.bot.statics.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Random;

public class IButtonManager extends ListenerAdapter {

    private static final int ID_LENGTH = 16;
    private static final String ID_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
    private static final Random DICE = new Random();

    private final Core core;
    private final HashMap<String, ButtonInfo> map = new HashMap<>();

    public IButtonManager(Core core) {
        this.core = core;
        core.addBootUpHook(this::onBootUp);
    }

    private void onBootUp() {
        this.core.getJDA().addEventListener(this);
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        ButtonInfo info = this.map.get(event.getComponentId());
        if (info != null) info.onPress(event);
    }

    public Button generateButton(ButtonStyle style, Object key, ButtonHook hook, String string) {
        String id = generateId();
        this.map.put(id, new ButtonInfo(key, hook));
        return Button.of(style, id, string);
    }

    public Button generateButton(ButtonStyle style, Object key, ButtonHook hook, Emoji emoji) {
        String id = generateId();
        this.map.put(id, new ButtonInfo(key, hook));
        return Button.of(style, id, emoji.getJDAEmoji());
    }

    public interface ButtonHook {
        void onClick(ButtonClickEvent event, Object key);
    }

    private static class ButtonInfo {
        private final Object key;
        private final ButtonHook hook;
        public ButtonInfo(Object key, ButtonHook hook) {
            this.key = key;
            this.hook = hook;
        }
        public void onPress(ButtonClickEvent event) {
            this.hook.onClick(event, this.key);
        }
        public <T> T getAs() {
            try {
                return (T) key;
            } catch (ClassCastException e) {
                return null;
            }
        }
    }

    private static String generateId() {
        char[] chars = new char[ID_LENGTH];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = ID_ALPHABET.charAt(DICE.nextInt(ID_ALPHABET.length()));
        }
        return String.valueOf(chars);
    }

}
