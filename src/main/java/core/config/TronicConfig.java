package core.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import core.storage.serialized.UserSerialized;
import net.dv8tion.jda.core.entities.User;

import java.io.File;
import java.util.LinkedList;

public class TronicConfig implements Config {

    //0.0.0
    private boolean useFirst = true;
    private LinkedList<TokenInfo> tokenInfos = new LinkedList<>();
    private String hostId = "the user id of the host";

    public TronicConfig() {
        this.tokenInfos.add(new TokenInfo());
    }

    public static TronicConfig load(File file) {
        XStream xStream = new XStream();
        xStream.alias("tronic", TronicConfig.class);
        xStream.alias("token-info", TokenInfo.class);
        xStream.allowTypes(new Class[]{TronicConfig.class, TokenInfo.class});
        return XStreamUtil.loadConfigFrom(file, new TronicConfig(), xStream);
    }

    public boolean isUseFirst() {
        return this.useFirst;
    }

    public LinkedList<TokenInfo> getTokenInfos() {
        return this.tokenInfos;
    }

    public String getHostId() {
        return this.hostId;
    }


}
