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
            Button queueButton = new Button(Emoji.ARROW_HEADING_DOWN, () -> {
                getCore().getMusicManager().getPlayer(event.getGuild()).addToQueue(queueItem, event.getMember());
                event.getMessage().delete().complete();
            });
            getCore().getButtonHandler().register(queueButton, event.getMessage()).complete();
        }
    }

}
