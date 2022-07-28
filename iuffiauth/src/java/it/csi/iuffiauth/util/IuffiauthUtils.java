package it.csi.iuffiauth.util;

import java.security.Key;
import java.util.Base64;

import io.jsonwebtoken.security.Keys;

public class IuffiauthUtils {
	public static Key creaKey(String s)
    {
    	byte[] secretBytes = Base64.getDecoder().decode(s);
        Key key = Keys.hmacShaKeyFor(secretBytes);
        return key;
    }

}
