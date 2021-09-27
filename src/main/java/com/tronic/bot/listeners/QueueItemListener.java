package com.tronic.bot.listeners;

import com.tronic.bot.buttons.Button;
import com.tronic.bot.core.Core;
import com.tronic.bot.music.playing.QueueItem;
import com.tronic.bot.statics.Emoji;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class QueueItemListener extends Listener {

    public QueueItemListener(Core core) {
        super(core);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.getMessage().getContentRaw().startsWith("http")) return;
        QueueItem queueItem = getCore().getMusicManager().getTrackProvider().fromUrl(event.getMessage().getContentRaw());
        if (queueItem != null) {
            OnPressListener onPressListener = new OnPressListener(event, queueItem);
            Button queueButton = new Button(Emoji.ARROW_HEADING_DOWN, onPressListener);
            onPressListener.init(queueButton);
            getCore().getButtonHandler().register(queueButton, event.getMessage()).complete();
        }
    }

    private class OnPressListener implements Button.PressListener {

        private final GuildMessageReceivedEvent event;
        private final QueueItem queueItem;
        private Button button;

        public OnPressListener(GuildMessageReceivedEvent event, QueueItem queueItem) {
            this.event = event;
            this.queueItem = queueItem;
        }

        public void init(Button button) {
            this.button = button;
        }

        @Override
        public void onPress() {
            getCore().getButtonHandler().unregister(this.button, this.event.getMessage());
            getCore().getMusicManager().getPlayer(this.event.getGuild()).addToQueue(queueItem, this.event.getMember());
            this.event.getMessage().delete().complete();
        }

    }

}
