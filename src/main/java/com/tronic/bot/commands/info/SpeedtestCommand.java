package com.tronic.bot.commands.info;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.DoubleToolkit;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import fr.bmartel.speedtest.model.SpeedTestMode;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.math.BigDecimal;

public class SpeedtestCommand implements Command {

    SpeedTestSocket speedTestSocket = new SpeedTestSocket();
    private static final String DOWNLOAD_URI = "http://scaleway.testdebit.info/5M/5M.iso";
    private static final String UPLOAD_URI = "https://scaleway.testdebit.info";
    private static final int UPLOAD_INTERVAL = 1000000;
    private String downloadSpeed = Emoji.CLOCK1.getUtf8();
    private String uploadSpeed = Emoji.CLOCK1.getUtf8();
    private Message message;


    @Override
    public String invoke() {
        return "speedtest";
    }

    @Override
    public Permission getPermission() {
        return Permission.NONE;
    }

    @Override
    public boolean silent() {
        return false;
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        this.speedTestSocket.addSpeedTestListener(new Speedtest(info.getEvent()));
        this.speedTestSocket.startDownload(DOWNLOAD_URI);
        this.message = info.getEvent().getChannel().sendMessage(createMessage().b()).complete();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Test the hosts connection","Run a speedtest","speedtest");
    }

    private void updateMessage() {
        this.message.editMessage(createMessage().b()).queue();
    }
    private  TronicMessage createMessage() {
        TronicMessage message = new TronicMessage("Speedtest","");
        message.addField("Download",this.downloadSpeed,true);
        message.addField("Upload",this.uploadSpeed,true);
        return message;
    }


    private class Speedtest implements ISpeedTestListener {

        private final MessageReceivedEvent event;

        Speedtest(MessageReceivedEvent event) {
            this.event = event;
        }

        @Override
        public void onCompletion(SpeedTestReport report) {
            if (report.getSpeedTestMode() == SpeedTestMode.DOWNLOAD) {
                SpeedtestCommand.this.downloadSpeed = getMbits(report.getTransferRateBit());
                SpeedtestCommand.this.speedTestSocket.startUpload(UPLOAD_URI, UPLOAD_INTERVAL);
                SpeedtestCommand.this.updateMessage();
            }
            if (report.getSpeedTestMode() == SpeedTestMode.UPLOAD) {
                SpeedtestCommand.this.uploadSpeed = getMbits(report.getTransferRateBit());
                SpeedtestCommand.this.updateMessage();
            }
        }

        @Override
        public void onProgress(float percent, SpeedTestReport report) {

        }

        @Override
        public void onError(SpeedTestError speedTestError, String errorMessage) {

        }

        private String getMbits(BigDecimal decimal) {
            return DoubleToolkit.round(decimal.doubleValue() / 1024 / 1024, 2) + " MBit/s";
        }

    }
}
