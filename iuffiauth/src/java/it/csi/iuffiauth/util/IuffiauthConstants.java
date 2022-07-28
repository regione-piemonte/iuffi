package it.csi.iuffiauth.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import io.jsonwebtoken.SignatureAlgorithm;

public class IuffiauthConstants
{
	private static final String THIS_CLASS = IuffiauthConstants.class.getSimpleName();

	public final static SignatureAlgorithm SIG_ALG = SignatureAlgorithm.HS512;
	public final static String SECRETSTRING_JNDI_NAME = "java:/iuffiauth/parameters/secretString";

	public static class LOGGING
	{
		public static class LOGGER
		{
			public static String LOGGER_NAME = "iuffiauth";
			public static Logger PRESENTATION = Logger.getLogger(LOGGER_NAME + ".presentation");
		}
	}

	public static class GENERIC_ERRORS
	{
	    public static final String OGGETTO_IDENTITA_NON_TROVATO = "Oggetto 'Identita' non trovato";
	    public static final String SECRETSTRING_NON_TROVATA = "Errore in reperimento secretString";
	    public static final String SECRETSTRING_NON_VALIDA = "La secretString non è valida";
	    public static final String AUTHSCHEME_NON_VALIDO = "L'Authorization Scheme indicato nella request non è corretto";
	    public static final String CONFIG_FILE_NON_TROVATO = "File config.properties non trovato in WEB-INF/classes";
	}

	public static Properties PROPERTIES()
	{
    	final String THIS_METHOD = "[" + THIS_CLASS + ".CONFIG()] ";
		Properties properties = null;
		InputStream stream = IuffiauthConstants.class.getResourceAsStream("/config.properties");
		try
		{
			properties = new Properties();
			properties.load(stream);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
	    	LOGGING.LOGGER.PRESENTATION.error(THIS_METHOD + GENERIC_ERRORS.CONFIG_FILE_NON_TROVATO);
    		//SecretStringNotValidException ssnvex = new SecretStringNotValidException(iaex);
        	//LOGGING.LOGGER.PRESENTATION.error(THIS_METHOD + ssnvex.getMessage());
        	//throw ssnvex;

		}
		return properties;
	}
}
