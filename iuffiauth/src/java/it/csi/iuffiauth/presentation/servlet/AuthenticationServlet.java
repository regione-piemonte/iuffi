/**
* 
*/
package it.csi.iuffiauth.presentation.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import java.io.IOException;

import it.csi.iride2.policy.entity.Identita;
import it.csi.iuffiauth.exception.SecretStringNotFoundException;
import it.csi.iuffiauth.exception.SecretStringNotValidException;
import it.csi.iuffiauth.util.IuffiauthConstants;
import it.csi.iuffiauth.util.IuffiauthConstants.LOGGING;
import it.csi.iuffiauth.util.IuffiauthUtils;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.util.Calendar;
import java.util.UUID;
/**
* @author Michele Piantà
*
*/
public class AuthenticationServlet extends HttpServlet
{
	private static final long serialVersionUID = 3112424421398866899L;
	private static final String THIS_CLASS = AuthenticationServlet.class.getSimpleName();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
    	final String THIS_METHOD = "[" + THIS_CLASS + ".doGet()] ";

    	StringBuffer sb = null;
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        resp.setHeader("Pragma", "no-cache"); // HTTP 1.0
        resp.setDateHeader("Expires", 0); // Proxies
    	
        Identita identita = (Identita)req.getSession().getAttribute(this.getServletConfig().getInitParameter("IRIDE_ID_SESSIONATTR"));
        if (identita != null)
        {
            // trasformo l'oggetto identità in JWS
        	try
        	{
                String identitaJwts = creaJWT(identita);       		
                sb = new StringBuffer("{\"authenticationToken\":\"").append(identitaJwts).append("\"}");
                resp.setHeader("Authorization", "Bearer "+identitaJwts);
                resp.setStatus(HttpServletResponse.SC_OK);
        	}
        	catch (SecretStringNotFoundException ssnfex)
        	{
                sb = new StringBuffer("{\"errorMessage\":\"").append(ssnfex.getMessage()).append("\"}");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);    		
        	}
        	catch (SecretStringNotValidException ssnvex)
        	{
                sb = new StringBuffer("{\"errorMessage\":\"").append(ssnvex.getMessage()).append("\"}");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);    		
        	}
        }
        else
        {
            // qualcosa è andato storto, stortissimo
        	LOGGING.LOGGER.PRESENTATION.error(THIS_METHOD + IuffiauthConstants.GENERIC_ERRORS.OGGETTO_IDENTITA_NON_TROVATO);
            sb = new StringBuffer("{\"errorMessage\":\"").append(IuffiauthConstants.GENERIC_ERRORS.OGGETTO_IDENTITA_NON_TROVATO).append("\"}");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        resp.getWriter().write(sb.toString());
        resp.getWriter().flush();
        resp.getWriter().close();            
    }

    private String creaJWT(Identita identita)
    	throws SecretStringNotFoundException, SecretStringNotValidException
    {
    	final String THIS_METHOD = "[" + THIS_CLASS + ".creaJWT()] ";

    	Context ctx = null;
    	String secretString = null;
    	try
    	{
    		ctx = new InitialContext();
    		secretString = (String)ctx.lookup(IuffiauthConstants.SECRETSTRING_JNDI_NAME);
        	LOGGING.LOGGER.PRESENTATION.debug(THIS_METHOD + "secretString: " + secretString);
    	}
        catch(NamingException nex)
        {
        	SecretStringNotFoundException ssnfex = new SecretStringNotFoundException(nex);
        	LOGGING.LOGGER.PRESENTATION.error(THIS_METHOD + ssnfex.getMessage());
        	throw ssnfex;
        }
        
    	Key key = null;
    	try
    	{
    		key = IuffiauthUtils.creaKey(secretString);
    	}
    	catch(IllegalArgumentException iaex) // creaKey
    	{
    		SecretStringNotValidException ssnvex = new SecretStringNotValidException(iaex);
        	LOGGING.LOGGER.PRESENTATION.error(THIS_METHOD + ssnvex.getMessage());
        	throw ssnvex;
    	}
    	
       
        // imposto la scadenza del token (parametrizzabile, volendo)
        Calendar cal_iat = Calendar.getInstance();
        Calendar cal_exp = Calendar.getInstance();
        
        cal_exp.add(Calendar.MINUTE, Integer.parseInt((String)IuffiauthConstants.PROPERTIES().get("jwts.timeout.minutes")));

        // creo un JWT (anzi, JWS) utilizzando solo ciò che mi serve dell'oggetto identita
        String jwtString = Jwts.builder().setId(UUID.randomUUID().toString()) // claim standard "jti"
        		.setIssuedAt(cal_iat.getTime())  // claim standard "iat"
        		.setExpiration(cal_exp.getTime()) // claim standard "exp"
        		.setIssuer("iuffiauth - CSI Piemonte") // claim standard "iss"
        		.setAudience("IUFFI mobile system") // claim standard "aud"
        		.setSubject(identita.getCodFiscale()) // claim standard "sub"      		
        		.claim("nome", identita.getNome()) // claim custom "nome" 
        		.claim("cognome", identita.getCognome()) // claim custom "cognome"
        		.signWith(key, IuffiauthConstants.SIG_ALG) // firma
        		.compact(); // conversione in stringa
        
        return jwtString;
    }

}
