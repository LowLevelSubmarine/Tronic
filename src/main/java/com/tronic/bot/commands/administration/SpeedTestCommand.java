package com.tronic.bot.commands.administration;

import com.tronic.bot.commands.*;
import com.tronic.bot.io.TronicMessage;
import com.tronic.bot.statics.Emoji;
import com.tronic.bot.tools.DoubleToolkit;
import com.tronic.bot.tools.MessageChanger;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import fr.bmartel.speedtest.model.SpeedTestMode;

import java.math.BigDecimal;

public class SpeedTestCommand implements Command {

    SpeedTestSocket speedTestSocket = new SpeedTestSocket();
    private static final String DOWNLOAD_URI = "http://scaleway.testdebit.info/5M/5M.iso";
    private static final String UPLOAD_URI = "https://scaleway.testdebit.info";
    private static final int UPLOAD_INTERVAL = 1000000;
    private String downloadSpeed = Emoji.HOURGLASS.toString();
    private String uploadSpeed = Emoji.HOURGLASS.toString();
    private MessageChanger messageChanger;


    @Override
    public String invoke() {
        return "speedtest";
    }

    @Override
    public Permission getPermission() {
        return Permission.CO_HOST;
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
        this.messageChanger = new MessageChanger(info.getCore(), info.getChannel());
        this.messageChanger.change(createMessage().b());
        this.speedTestSocket.addSpeedTestListener(new Speedtest());
        this.speedTestSocket.startDownload(DOWNLOAD_URI);
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("Test the hosts connection","Run a speedtest","speedtest");
    }

    private void updateMessage() {
        this.messageChanger.change(createMessage().b());
    }
    private  TronicMessage createMessage() {
        TronicMessage message = new TronicMessage("Speedtest","");
        message.addField("Download",this.downloadSpeed,true);
        message.addField("Upload",this.uploadSpeed,true);
        return message;
    }


    private class Speedtest implements ISpeedTestListener {

        @Override
        public void onCompletion(SpeedTestReport report) {
            if (report.getSpeedTestMode() == SpeedTestMode.DOWNLOAD) {
                SpeedTestCommand.this.downloadSpeed = getMbits(report.getTransferRateBit());
                SpeedTestCommand.this.speedTestSocket.startUpload(UPLOAD_URI, UPLOAD_INTERVAL);
                SpeedTestCommand.this.updateMessage();
            }
            if (report.getSpeedTestMode() == SpeedTestMode.UPLOAD) {
                SpeedTestCommand.this.uploadSpeed = getMbits(report.getTransferRateBit());
                SpeedTestCommand.this.updateMessage();
            }
        }

        @Override
        public void onProgress(float percent, SpeedTestReport report) {
            //
        }

        @Override
        public void onError(SpeedTestError speedTestError, String errorMessage) {
            //
        }

        private String getMbits(BigDecimal decimal) {
            return DoubleToolkit.round(decimal.doubleValue() / 1024 / 1024, 2) + " MBit/s";
        }

    }
}
