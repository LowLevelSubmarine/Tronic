package com.tronic.bot.commands.administration;

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
    private static final String DOWNLOAD_URI = "http://2.testdebit.info/5M.iso";
    private static final String UPLOAD_URI = "http://2.testdebit.info/";
    private static final int UPLOAD_INTERVAL = 1000000;
    private String downloadspeed = "please wait";
    private String uploadspeed = "please wait";
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
        return CommandType.ADMINISTRATION;
    }

    @Override
    public void run(CommandInfo info) throws InvalidCommandArgumentsException {
        this.speedTestSocket.addSpeedTestListener(new Speedtest(info.getEvent()));
        this.speedTestSocket.startDownload(DOWNLOAD_URI);
        this.message = info.getEvent().getChannel().sendMessage(createMessage().b()).complete();
    }

    @Override
    public HelpInfo getHelpInfo() {
        return new HelpInfo("speedtest","Run a speedtest","speedtest");
    }

    private void updateMessage() {
        this.message.editMessage(createMessage().b()).queue();
    }
    private  TronicMessage createMessage() {
        TronicMessage message = new TronicMessage("Speedtest","");
        message.addField("Download",this.downloadspeed,true);
        message.addField("Upload",this.uploadspeed,true);
        return message;
    }


    class Speedtest implements ISpeedTestListener {
        private final MessageReceivedEvent event;

        Speedtest(MessageReceivedEvent event) {
            this.event = event;
        }

        @Override
        public void onCompletion(SpeedTestReport report) {
            if (report.getSpeedTestMode() == SpeedTestMode.DOWNLOAD) {
                SpeedtestCommand.this.downloadspeed = getMbits(report.getTransferRateBit());
                SpeedtestCommand.this.speedTestSocket.startUpload(UPLOAD_URI, UPLOAD_INTERVAL);
                SpeedtestCommand.this.updateMessage();
            }
            if (report.getSpeedTestMode() == SpeedTestMode.UPLOAD) {
                SpeedtestCommand.this.uploadspeed = getMbits(report.getTransferRateBit());
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
