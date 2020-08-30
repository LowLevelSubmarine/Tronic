package com.tronic.bot.rest;

import com.tronic.bot.storage.Storage;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.utils.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class JWTStore {
    private final Logger logger = LogManager.getLogger(RestServer.class);
    private Storage storage;

    public JWTStore(Storage storage) {
        this.storage = storage;
    }

    public String createJWT(Long user, HashMap<String,String> claims) {
       JwtBuilder jwtBuilder = Jwts.builder();
       jwtBuilder.claim("user",user);
       for (Map.Entry<String,String> claim:claims.entrySet()) {
            jwtBuilder.claim(claim.getKey(),claim.getValue());
       }
       jwtBuilder.signWith(storage.getUser(User.fromId(user)).getKey(), SignatureAlgorithm.HS256);

       return jwtBuilder.compact();
    }

    public boolean isJWTValid(String jwt) throws UnsupportedJwtException,DeserializationException {
        Long user = getUser(jwt);
        if (user == null) return false;
        try {
            Jwts.parserBuilder().setSigningKey(this.storage.getUser(User.fromId(user)).getKey()).build().parseClaimsJws(jwt).getBody();
            return true;
        } catch (SignatureException e) {
            return false;
        } catch (UnsupportedJwtException|DeserializationException f) {
            throw f;
        }
    }

    public Claims getClaims(String jwt) throws NullPointerException,SignatureException,DeserializationException, UnsupportedJwtException {
       Long user = getUser(jwt);
       return Jwts.parserBuilder().setSigningKey(this.storage.getUser(User.fromId(user)).getKey()).build().parseClaimsJws(jwt).getBody();
    }

    private Long getUser(String jwt) throws NullPointerException, DeserializationException, UnsupportedJwtException {
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i+1);
        Jwt<Header, Claims> untrusted = Jwts.parserBuilder().build().parseClaimsJwt(withoutSignature);
        Long user = untrusted.getBody().get("user",Long.class);
        if (user==null){
            throw new NullPointerException();
        }
        return user;
    }


}
