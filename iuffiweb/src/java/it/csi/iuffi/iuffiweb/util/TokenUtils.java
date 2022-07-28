package it.csi.iuffi.iuffiweb.util;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class TokenUtils
{

  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".presentation");

  public static String verifyToken(String jwt) throws Exception {
    // verifica il token e ritorna il codice fiscale contenuto all'interno
    String codiceFiscale = null;
    Context ctx = new InitialContext();
    String KEY = null;
    String token = jwt;
    Claims claims = null;
    try {
      KEY = (String) ctx.lookup(IuffiConstants.SECRETSTRING_JNDI_NAME);
    } catch (Exception e) {
        logger.error("SECRET KEY NON TROVATA");
        throw new Exception("SECRET KEY NON TROVATA");  // commentare in sviluppo
        //KEY = "Jjd2/73OK+41ZJLq0TxiDSieZQO2ri71NkD0+coBf4ygcUzlcfP54BLu43AYoe0YJCIXeeG0x5xSUoirRkpyJQ==";  // scommentare in sviluppo
    }
    try {
      if (token == null) {  // scommentare la riga sotto in sviluppo
          logger.debug("TOKEN NON TROVATO");
          //token = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI2NzcyYzIzZC05NTBiLTRmODgtOTVmMy1hNThlZTBkM2QzZjUiLCJpYXQiOjE2MDcwNzg1NjEsImV4cCI6MTYwNzA4MDM2MSwiaXNzIjoiaXVmZmlhdXRoIC0gQ1NJIFBpZW1vbnRlIiwiYXVkIjoiSVVGRkkgbW9iaWxlIHN5c3RlbSIsInN1YiI6IkFBQUFBQTAwQjc3QjAwMEYiLCJub21lIjoiQ1NJIFBJRU1PTlRFIiwiY29nbm9tZSI6IkRFTU8gMjAifQ.geYrzIDcv4OytDkRZpR-332m5M7qqnIELhgT0sxC8zootreE51CTIdtlkIQ-2Nl6r4KzhBwqmT-iNdNxYKfcpw";
          //token = "eyJhbGciOiJIUzUxMiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTYxNzI3NTIyOCwiaWF0IjoxNjE3Mjc1MjI4fQ.aMQd0AmHt66md1piFSHp9sqdICzwe_iPNxT2eKoU4N4sLreetmJHlw4j1PzSpHuDNc3-sRupcCP7RZ06UMRntw"; generato da web
          throw new Exception("TOKEN NON PRESENTE");  // commentare in sviluppo
      } else {
          token = token.substring(jwt.indexOf("Bearer ")+7);
      }
      claims = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
      codiceFiscale = (String) claims.get("sub");
      //String nome = (String) claims.get("nome");
      //String cognome = (String) claims.get("cognome");
    } catch (ExpiredJwtException e) {
        //codiceFiscale = (String) e.getClaims().get("sub");     // scommentare in sviluppo
        logger.error("TOKEN SCADUTO");
        throw new JwtException("TOKEN EXPIRED");  // commentare in sviluppo
    }
    return codiceFiscale;
  }

}
