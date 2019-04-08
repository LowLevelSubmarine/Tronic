package main;

import core.Tronic;
import core.config.TronicConfig;
import core.config.TokenInfo;
import statics.Files;
import utils.UserSelection;

import javax.security.auth.login.LoginException;

public class Main {

    private static String TOKEN;
    private static TronicConfig CONFIG;
    private static Tronic TRONIC;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(Main::shutdown));
        System.out.println("For help and more info, read the readme created in the applications folder :)");
        loadConfig();
        TOKEN = getToken();
        boot();
    }

    private static void boot() {
        try {
            TRONIC = new Tronic(TOKEN, CONFIG);
        } catch (LoginException e) {
            System.out.println("Failed access the Discord API with token: " + TOKEN + "\nYou can change it in: " + Files.TRONIC_CONFIG.getAbsolutePath() + ".");
        } catch (InterruptedException e) {
            throw new InternalError(e);
        }
    }

    public static void shutdown() {
        TRONIC.shutdown();
    }

    public static void restart() {
        shutdown();
        loadConfig();
        boot();
    }

    private static void loadConfig() {
        CONFIG = TronicConfig.load(Files.TRONIC_CONFIG);
    }

    private static String getToken() {
        int selection = 0;
        if (!CONFIG.isUseFirst()) {
            UserSelection uSelection = new UserSelection("Choose the token the bot should start with:");
            for (TokenInfo tokenInfo : CONFIG.getTokenInfos()) {
                uSelection.addOption(tokenInfo.name);
            }
            selection = uSelection.getSelection();
        }
        return CONFIG.getTokenInfos().get(selection).token;
    }

}
