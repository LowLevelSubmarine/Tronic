package com.tronic.bot.storage;

import com.toddway.shelf.Shelf;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class UserStorage extends StorageElement {

    UserStorage(Shelf shelf) {
        super(shelf);
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
