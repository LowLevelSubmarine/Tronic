import core.Core;
import core.config.token.TokenConfig;
import core.config.token.TokenInfo;
import statics.Files;
import utils.UserSelection;

import javax.security.auth.login.LoginException;

public class Tronic {

    private static String TOKEN;
    private static Core core;

    public static void main(String[] args) throws Exception {
        System.out.println("For help and more info, read the readme created in the applications folder :)");
        TOKEN = getToken();
        try {
            core = new Core(TOKEN);
        } catch (LoginException e) {
            System.out.println("Failed access the Discord API with token: " + TOKEN + "\nYou can change it in: " + Files.TOKENS_CONFIG.getAbsolutePath() + ".");
        }
    }

    private static String getToken() {
        TokenConfig tConfig = TokenConfig.load(Files.TOKENS_CONFIG);
        int selection = 0;
        if (!tConfig.isUseFirst()) {
            UserSelection uSelection = new UserSelection("Choose the token the bot should start with:");
            for (TokenInfo tokenInfo : tConfig.getTokenInfos()) {
                uSelection.addOption(tokenInfo.name);
            }
            selection = uSelection.getSelection();
        }
        return tConfig.getTokenInfos().get(selection).token;
    }

}
