import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

public class TestJwts
{
	private static SignatureAlgorithm SIG_ALG = SignatureAlgorithm.HS512;
	private static String secretString = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SIG_ALG).getEncoded());
	private static Key KEY = creaKey(secretString);
	
    public static void main (String[] args)
    {
    	// imposto la scadenza del token (parametrizzabile, volendo)
        Calendar cal_iat = Calendar.getInstance();
        Calendar cal_exp = Calendar.getInstance();
        cal_exp.add(Calendar.MINUTE, 30);

        //System.out.println("SECRET_STRING: " + SECRET_STRING);
        
        // creo un JWT (anzi, JWS) utilizzando solo ciò che mi serve dell'oggetto identita
        String jwtString = Jwts.builder().setId(UUID.randomUUID().toString()) // claim standard "jti"
        		.setIssuedAt(cal_iat.getTime())  // claim standard "iat"
        		.setExpiration(cal_exp.getTime()) // claim standard "exp"
        		.setIssuer("iuffiauth - CSI Piemonte") // claim standard "iss"
        		.setAudience("IUFFI mobile system") // claim standard "aud"
        		.setSubject("PNTMHL70C19L219W") // claim standard "sub", da Identita
        		.claim("nome", "Michele") // claim custom "nome", da Identita
        		.claim("cognome", "Piantà") // claim custom "cognome", da Identita
        		.signWith(KEY, SIG_ALG) // firma
        		.compact(); // conversione in stringa        
        
        Claims claims = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(jwtString).getBody();
        System.out.println("Subject [sub]: " + claims.getSubject() );
        System.out.println("Nome: " + claims.get("nome") );
        
    	/* SVIL */
        //jwtString = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIwNGMwNDBkNy0wOWFiLTQ1NWQtODljMy1mZDEwYzQxOWNmZmMiLCJpYXQiOjE2MDI2NzEyNjcsImV4cCI6MTYwMjY3MzA2NywiaXNzIjoiaXVmZmlhdXRoIC0gQ1NJIFBpZW1vbnRlIiwiYXVkIjoiSVVGRkkgbW9iaWxlIHN5c3RlbSIsInN1YiI6IkFBQUFBQTAwQTExQjAwMEoiLCJub21lIjoiQ1NJIFBJRU1PTlRFIiwiY29nbm9tZSI6IkRFTU8gMjEifQ.3l1PiejFWMAN94eEVb_twdoOgC6W4NvFnYTtbj986AxXCueqG-F4pj_gUhj_IRubnslbx5iDP82-9b8UbGOLWw";
    	//secretString = "Js4XLwT09j0M5FJHDxqxM9ddp6/RoSXi78Ki7YjMJLUQbOVcR/RnIciArK9COwui/dPPAqvCv7HHgDnuX3BmiQ==";
    	
    	/* TEST */
        //jwtString = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI4ZjRmMjA4Ni1lYWZhLTQ4ZTYtYmNlZi0wMTBhZTEyMTgwZDQiLCJpYXQiOjE2MDI2NzI3MzYsImV4cCI6MTYwMjY3NDUzNiwiaXNzIjoiaXVmZmlhdXRoIC0gQ1NJIFBpZW1vbnRlIiwiYXVkIjoiSVVGRkkgbW9iaWxlIHN5c3RlbSIsInN1YiI6IkFBQUFBQTAwQTExQjAwMEoiLCJub21lIjoiQ1NJIFBJRU1PTlRFIiwiY29nbm9tZSI6IkRFTU8gMjEifQ.Qd1TUY8lujic2dxWRu85LrhfSIetfxqbiwl-jOELCazk6WmVmZbNd7nw6gG2E5u2-pzfvxnHWUlinORP4yqvzA";
    	//secretString = "Jjd2/73OK+41ZJLq0TxiDSieZQO2ri71NkD0+coBf4ygcUzlcfP54BLu43AYoe0YJCIXeeG0x5xSUoirRkpyJQ==";
    	
    	/* TEST_REF */
        //jwtString = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkYjljMzc5NC0yN2FhLTQ5ZTQtYjRlOS04ZjMwZThlOWI2NjEiLCJpYXQiOjE2MDI2NzMzNzIsImV4cCI6MTYwMjY3NTE3MiwiaXNzIjoiaXVmZmlhdXRoIC0gQ1NJIFBpZW1vbnRlIiwiYXVkIjoiSVVGRkkgbW9iaWxlIHN5c3RlbSIsInN1YiI6IkFBQUFBQTAwQTExQzAwMEsiLCJub21lIjoiQ1NJIFBJRU1PTlRFIiwiY29nbm9tZSI6IkRFTU8gMjIifQ.UNfYqA0Qg0XbIKP19tnZWJfnsshvDENS3U0B_5UUdGuiRJpxrYKWxI-B9UH9dJ7OldCuh_UcBIOkuMv8DU77yA";
    	//secretString = "NtpWlcTzx2HJDPaz867Z6n4R2gCQRPU22+XTtheZKGIlgc+kAbkth0fkN6uwYgm4AX8bdP0pvX7D/aekT1BGFw==";

    	/* TEST_COL */
        //jwtString = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxODdkMjU4Yy1hZDI0LTQwN2MtODI2Mi00M2UxYTU0ZTNiZWQiLCJpYXQiOjE2MDI2NzQzMjgsImV4cCI6MTYwMjY3NjEyOCwiaXNzIjoiaXVmZmlhdXRoIC0gQ1NJIFBpZW1vbnRlIiwiYXVkIjoiSVVGRkkgbW9iaWxlIHN5c3RlbSIsInN1YiI6IkFBQUFBQTAwQjc3QjAwMEYiLCJub21lIjoiQ1NJIFBJRU1PTlRFIiwiY29nbm9tZSI6IkRFTU8gMjAifQ.OjcitkDXInFp16nxiKSnH9dq4rM4F8a8mFvyQpO_OoyAPr8PpQx7Wn0WtJCt_4Na_s2fi8_vZlGS0tGMfWnatA";
    	secretString = "yUfSJJ/S9ezfvXARmkRyfkqdfpcUcPxvLYFruk9qPkGiTTkFFvignjLguLDEUPqKna+HOx/xJGvAVPW/n3dOvQ==";

    	System.out.println(jwtString);
        System.out.println(secretString);

        StringBuffer sb = new StringBuffer("{\"accessToken\":\"").append(jwtString).append("\"}");
        System.out.println(sb.toString());

        try
    	{
    		Key key2 = creaKey(secretString);
        	Claims claims2 = Jwts.parserBuilder().setSigningKey(key2).build().parseClaimsJws(jwtString).getBody();
            System.out.println("\nSubject [sub]: " + claims2.getSubject() );
            System.out.println("Exp. date [exp]: " + claims2.getExpiration().toString() );
            System.out.println("Nome: " + claims2.get("nome") );
            System.out.println("Cognome: " + claims2.get("cognome") );
           
    	}
    	catch(IllegalArgumentException iaex) // creaKey
    	{
    		System.err.println(iaex.getMessage());
    	}
    	catch(ExpiredJwtException ejex) // setSigninKey
    	{
    		System.err.println(ejex.getMessage());
    	}
    	catch(SignatureException sex) // parseClaimsJws
    	{
    		System.err.println(sex.getMessage());
    	}
    	
        /* Questo potrebbe non servire a niente
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256); 
        Key keyPrivate = keyPair.getPrivate();
        Key keyPublic = keyPair.getPublic();
        */
        
    }

    public static Key creaKey(String s)
    {
    	byte[] secretBytes = Base64.getDecoder().decode(s);
        Key key = Keys.hmacShaKeyFor(secretBytes);
        return key;
    }
}
