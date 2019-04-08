package core.buttons;

import core.interaction.Emojie;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.requests.RestAction;

public class ButtonMessage {

    private final Message message;
    private final ButtonListener listener;

    public ButtonMessage(Message message, ButtonListener listener) {
        this.message = message;
        this.listener = listener;
    }

    public String getId() {
        return this.message.getId();
    }

    public RestAction<Void> addButton(Emojie emojie) {
        return this.message.addReaction(emojie.getUnicode());
    }

    ButtonListener getListener() {
        return this.listener;
    }

}
