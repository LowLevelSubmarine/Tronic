package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;
import com.tronic.bot.tools.StatisticsSaver;
import com.tronic.bot.tools.StatisticsTool;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang.ArrayUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.Key;
import java.security.KeyFactory;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class UserStorage extends StorageElement {

    UserStorage(Shelf shelf) {
        super(shelf);
    }

    public void storeSatistic(StatisticsSaver.StatisticElement element) {
        LinkedList<StatisticsSaver.StatisticElement> ll =getStatistics();
        ll.add(element);
        super.set("statistics",ll);
    }

    public LinkedList<StatisticsSaver.StatisticElement> getStatistics () {
        LinkedList<StatisticsSaver.StatisticElement> ll = new LinkedList<>();
        List<Object> list = super.getList("statistics", StatisticsSaver.StatisticElement.class);
        if (list!=null) {
            for (Object user :list) {
                ll.add((StatisticsSaver.StatisticElement) user);
            }
        }
        return ll;
    }

    public void saveKey(SecretKey key) {
        super.set("secret", Base64.getEncoder().encodeToString(key.getEncoded()));
    }

    public Key getKey() {
        String rawKey =  (String) super.get("secret",String.class);
        if (rawKey ==null) {
            SecretKey secretKey = createNewSecret();
            super.set("secret", Base64.getEncoder().encodeToString(secretKey.getEncoded()));
            return secretKey;
        } else {
            byte[] decodedKey = Base64.getDecoder().decode(rawKey);
            SecretKey secretKey = new SecretKeySpec(decodedKey,SignatureAlgorithm.HS256.getJcaName());
            return secretKey;
        }
    }

    private SecretKey createNewSecret () {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return key;
    }

}
