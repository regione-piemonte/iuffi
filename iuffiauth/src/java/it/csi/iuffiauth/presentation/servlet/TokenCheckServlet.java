package it.csi.iuffiauth.presentation.servlet;

import java.io.IOException;
import java.security.Key;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import it.csi.iuffiauth.util.IuffiauthConstants;
import it.csi.iuffiauth.util.IuffiauthUtils;
import it.csi.iuffiauth.util.IuffiauthConstants.LOGGING;

/**
* @author Michele Piantà
*
*/
public class TokenCheckServlet extends HttpServlet
{
	private static final long serialVersionUID = 4970127469085252304L;
	private static final String THIS_CLASS = TokenCheckServlet.class.getSimpleName();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	        throws IOException, ServletException
	{
	    final String THIS_METHOD = "[" + THIS_CLASS + ".doGet()] ";
	    
	    String jwtString = null;
	    String[] authorizationHeader = req.getHeader("Authorization").split(" ");
	    if (!"Bearer".equals(authorizationHeader[0]))
	    {
	    	// L'header non è formato correttamente
        	ServletException sex = new ServletException(IuffiauthConstants.GENERIC_ERRORS.AUTHSCHEME_NON_VALIDO);
        	LOGGING.LOGGER.PRESENTATION.error(THIS_METHOD + IuffiauthConstants.GENERIC_ERRORS.AUTHSCHEME_NON_VALIDO);
        	throw sex;
	    }
	    else
	    {
	    	// Se la prima stringa è "Bearer", allora la seconda è il JWTS
	    	jwtString = authorizationHeader[1];
	    	Context ctx = null;
	    	String secretString = null;
			try
	    	{
				ctx = new InitialContext();
		    	secretString = (String)ctx.lookup(IuffiauthConstants.SECRETSTRING_JNDI_NAME);
			}
	        catch(NamingException nex)
	        {
	        	ServletException sex = new ServletException(nex);
	        	LOGGING.LOGGER.PRESENTATION.error(THIS_METHOD + sex.getMessage());
	        	throw sex;
	        }
    		Key key = IuffiauthUtils.creaKey(secretString);

    		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtString).getBody();
        	LOGGING.LOGGER.PRESENTATION.debug(THIS_METHOD + claims.getId());
        	LOGGING.LOGGER.PRESENTATION.debug(THIS_METHOD + claims.getIssuedAt());
        	LOGGING.LOGGER.PRESENTATION.debug(THIS_METHOD + claims.getExpiration());
        	LOGGING.LOGGER.PRESENTATION.debug(THIS_METHOD + claims.getIssuer());
        	LOGGING.LOGGER.PRESENTATION.debug(THIS_METHOD + claims.getAudience());
        	LOGGING.LOGGER.PRESENTATION.debug(THIS_METHOD + claims.getSubject());
        	LOGGING.LOGGER.PRESENTATION.debug(THIS_METHOD + claims.get("nome"));
        	LOGGING.LOGGER.PRESENTATION.debug(THIS_METHOD + claims.get("cognome"));
	    }
	}

}
