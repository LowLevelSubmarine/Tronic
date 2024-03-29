package com.tronic.bot.core;

import com.lowlevelsubmarine.envelope.build_provider.Build;
import com.lowlevelsubmarine.envelope.build_provider.BuildProvider;
import com.lowlevelsubmarine.envelope.build_provider.GitHubReleasesBuildProvider;
import com.lowlevelsubmarine.envelope.core.Envelope;
import com.lowlevelsubmarine.envelope.core.EnvelopeConnector;
import com.lowlevelsubmarine.envelope.core.MalformedUpdateException;
import com.lowlevelsubmarine.envelope.core.Update;
import com.lowlevelsubmarine.envelope.versioning.VersionInterpreter;
import com.lowlevelsubmarine.envelope.versioning.WrappedSemanticVersionInterpreter;
import com.tronic.bot.statics.GitHub;
import com.tronic.bot.statics.Info;
import com.tronic.logger.Loggy;

import java.io.IOException;

public class Updater implements EnvelopeConnector {

    private final Tronic tronic;
    private final Envelope envelope;

    public Updater(Tronic tronic) {
        this.tronic = tronic;
        this.envelope = new Envelope(this);
    }

    public Build getLatestBuild() throws IOException {
        return this.envelope.getLatestAvailableBuild();
    }

    public boolean isUpToDate() throws IOException {
        return this.envelope.isUpToDate();
    }

    public Update download(Build build) {
        Loggy.logD("Downloading build " + build.getVersion() + " ...");
        Update update = this.envelope.download(build);
        Loggy.logD("Successfully downloaded build " + build.getVersion() + ".");
        return update;
    }

    public void update(Update update) throws MalformedUpdateException, IOException {
        Loggy.logD("Initiating update from " + getCurrentVersion() + " to " + update.getBuild().getVersion() + " ...");
        this.envelope.install(update);
    }

    @Override
    public BuildProvider getBuildProvider() {
        return new GitHubReleasesBuildProvider(GitHub.USERNAME, GitHub.REPO_NAME);
    }

    @Override
    public VersionInterpreter getVersionInterpreter() {
        return new WrappedSemanticVersionInterpreter();
    }

    @Override
    public String getCurrentVersion() {
        return Info.VERSION;
    }

    @Override
    public void prepareShutdown() {
        Loggy.logD("Update successfull, initiating shutdown ...");
        this.tronic.shutdown(true, false);
    }

}
