package com.tronic.bot.music.playing;

import com.tronic.bot.buttons.Button;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.Markdown;
import com.tronic.bot.tools.MessageChanger;
import net.dv8tion.jda.api.entities.Member;

public class QueueMessage implements Player.EventListener {

    private final QueueItem queueItem;
    private final Member owner;
    private final Player player;
    private final MessageChanger messageChanger;
    private final Button deleteButton = new Button(Emoji.X, this::onDelete);
    private final Button requeueButton = new Button(Emoji.ARROWS_CLOCKWISE, this::onRequeue);
    private final Button pauseButton = new Button(Emoji.PAUSE_BUTTON, this::onPause);
    private final Button resumeButton = new Button(Emoji.ARROW_FORWARD, this::onResume);
    private final Button skipButton = new Button(Emoji.FAST_FORWARD, this::onSkip);
    private final Button skipPlaylistButton = new Button(Emoji.ARROW_DOUBLE_DOWN, this::onPlaylistSkip);

    public QueueMessage(QueueItem queueItem, Member owner, Player player) {
        this.queueItem = queueItem;
        this.owner = owner;
        this.player = player;
        this.messageChanger = new MessageChanger(player.getCore(), player.getChannel());
        setQueued();
        this.player.addEventListener(this);
    }

    public Member getOwner() {
        return this.owner;
    }

    @Override
    public void onQueueItemRemoved(QueueItem queueItem, Player player) {
        if (queueItem.equals(this.queueItem)) {
            this.player.removeEventListener(this);
            this.messageChanger.delete();
        }
    }

    @Override
    public void onQueueItemChanged(QueueItem oldQueueItem, QueueItem newQueueItem, boolean skipped, Player player) {
        if (this.queueItem.equals(newQueueItem)) {
            if (!this.player.isPaused()) {
                setPlaying();
            } else {
                setPaused();
            }
        } else if (this.queueItem.equals(oldQueueItem)) {
            this.player.removeEventListener(this);
            if (!skipped) {
                setPlayed();
            } else {
                setSkipped();
            }
        }
    }

    @Override
    public void onVolumeChanged(float oldVolume, float newVolume, Player player) {}

    @Override
    public void onStateChanged(boolean paused, Player player) {
        if (player.getCurrentQueueItem().equals(this.queueItem)) {
            if (!player.isPaused()) {
                setPlaying();
            } else {
                setPaused();
            }
        }
    }

    @Override
    public QueueMessage registerSelf(QueueItem queueItem) {
        if (queueItem.equals(this.queueItem)) return this;
        return null;
    }

    public void setQueued() {
        changeMessage(Emoji.ARROW_HEADING_DOWN, true, this.deleteButton);
    }

    public void setPaused() {
        changeMessage(Emoji.PAUSE_BUTTON, true, this.skipButton, this.resumeButton);
    }

    public void setPlaying() {
        if (!queueItem.isMultiTrack()) {
            changeMessage(Emoji.ARROW_FORWARD, true, this.skipButton, this.pauseButton);
        } else {
            changeMessage(Emoji.ARROW_FORWARD, true, this.skipButton, this.skipPlaylistButton, this.pauseButton);
        }
    }

    public void setSkipped() {
        changeMessage(Emoji.FAST_FORWARD, true, this.requeueButton);
    }

    public void setPlayed() {
        changeMessage(Emoji.STOP_BUTTON, true, this.requeueButton);
    }

    private void changeMessage(Emoji emoji, boolean showOwner, Button... buttons) {
        String content = emoji.getUtf8() + "  | ";
        if (showOwner) {
            content += this.owner.getAsMention() + ": ";
        }
        if (this.queueItem.isMultiTrack()) content += Markdown.uri(this.queueItem.getName(), this.queueItem.getUrl()) + ": ";
        content += Markdown.uri(this.queueItem.getNextPossibleTrack().getDisplayName(), this.queueItem.getNextPossibleTrack().getUrl());
        this.messageChanger.change(new TronicMessage(content).b(), buttons);
    }

    private void onDelete() {
        this.player.removeFromQueue(this.queueItem);
    }

    private void onRequeue() {
        this.player.addToQueue(this.queueItem.copy(), this.owner);
    }

    private void onPause() {
        this.player.setPaused(true);
    }

    private void onResume() {
        this.player.setPaused(false);
    }

    private void onSkip() {
        this.player.skip();
    }

    private void onPlaylistSkip() {
        this.player.playlistSkip();
    }

}
