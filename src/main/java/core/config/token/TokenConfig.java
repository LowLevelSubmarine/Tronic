package core.config.token;

import com.thoughtworks.xstream.XStream;
import core.config.Config;
import core.config.XStreamUtil;

import java.io.File;
import java.util.LinkedList;

public class TokenConfig implements Config {

    //0.0.0
    private boolean useFirst = true;
    private LinkedList<TokenInfo> tokenInfos = new LinkedList<>();

    public TokenConfig() {
        this.tokenInfos.add(new TokenInfo());
    }

    public static TokenConfig load(File file) {
        XStream xStream = new XStream();
        xStream.alias("root", TokenConfig.class);
        xStream.alias("token-info", TokenInfo.class);
        xStream.allowTypes(new Class[]{TokenConfig.class, TokenInfo.class});
        return XStreamUtil.loadConfigFrom(file, new TokenConfig(), xStream);
    }

    public boolean isUseFirst() {
        return this.useFirst;
    }

    public LinkedList<TokenInfo> getTokenInfos() {
        return this.tokenInfos;
    }

}
