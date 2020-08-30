package com.tronic.bot.rest;

import com.tronic.bot.storage.Storage;
import io.jsonwebtoken.Jwts;
import net.dv8tion.jda.internal.entities.UserById;
import net.dv8tion.jda.internal.utils.tuple.ImmutablePair;
import org.bouncycastle.util.Store;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.HashMap;

import static org.junit.Assert.*;

public class JWTStoreTest {
    @Test
    public void jwtTest() {
        JWTStore store = new JWTStore(new Storage());
        HashMap<String,String> args = new HashMap<>();
        args.put("lol","lol");
        args.put("lol","no");
        String jwt = store.createJWT(265955256439930882L,args);
        System.out.println(jwt);
        assertTrue(store.isJWTValid(jwt));
    }
}
