package core.buttons;

import core.interaction.Emojie;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;

public class ButtonEvent {

    private final GenericMessageReactionEvent event;

    public ButtonEvent(GenericMessageReactionEvent event) {
        this.event = event;
    }

    public User getUser() {
        return event.getUser();
    }

    public Member getMember() {
        return event.getMember();
    }

    public boolean is(Emojie emojie) {
        return emojie.getUnicode().equals(this.event.getReactionEmote().getName());
    }

}
