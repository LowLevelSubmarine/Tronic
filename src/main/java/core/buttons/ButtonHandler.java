package core.buttons;

import core.Tronic;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;

public class ButtonHandler extends ListenerAdapter {

    private final HashMap<String, ButtonMessage> listeners = new HashMap<>();

    @Override
    public void onGenericMessageReaction(GenericMessageReactionEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        ButtonMessage btnMessage = this.listeners.get(event.getMessageId());
        if (btnMessage != null) {
            if (!btnMessage.getListener().onButtonPress(new ButtonEvent(event))) {
                listeners.remove(event.getMessageId());
            }
        }
    }

    public void addMessage(ButtonMessage message) {
        this.listeners.put(message.getId(), message);
    }

}
