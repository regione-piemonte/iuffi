package it.csi.iuffi.iuffiweb.integration.ws.regata;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class RegataServiceFactory
{
  public static final String         REGATA_REST_PROPERTY_BASE_URL 				= "regata.rest.baseurl";
  public static final String         REGATA_REST_PROPERTY_PSW 				    = "regata.rest.psw";
  public static final String         REGATA_REST_PROPERTY_USR 				    = "regata.rest.usr";
  private static RegataRESTClient    REST_SERVICE_CLIENT                        = getClient();
  private static Logger              logger                                     = null;
  private static final String        THIS_CLASS                                 = RegataServiceFactory.class.getSimpleName();

  public static RegataRESTClient getRestServiceClient()
  {
    if (REST_SERVICE_CLIENT == null)
    {
      REST_SERVICE_CLIENT = getClient();
      if (logger != null && REST_SERVICE_CLIENT != null)
      {
        REST_SERVICE_CLIENT.setLogger(logger);
      }
    }
    return REST_SERVICE_CLIENT;
  }
  
  private static ResourceBundle safeGetBundle(String name)
  {
    try
    {
      return ResourceBundle.getBundle(name);
    }
    catch (Exception e)
    {
      return null;
    }
  }

 
  private static RegataRESTClient getClient()
  {
    String url = System.getProperty(REGATA_REST_PROPERTY_BASE_URL);
    if (url == null)
    {
      ResourceBundle regata = safeGetBundle("regata");
      if (regata != null)
      {
        url = regata.getString(REGATA_REST_PROPERTY_BASE_URL);
      }
      if (url == null)
      {
        ResourceBundle config = safeGetBundle("config");
        if (config != null)
        {
          url = config.getString(REGATA_REST_PROPERTY_BASE_URL);
        }
      }
    }
    if (url == null)
    {
      if (logger != null)
      {
        logger.error("[" + THIS_CLASS + ".getClient] Errore fatale nel tentativo di reperire il puntamento all'url del servizio REST: Impossibile trovare la proprietà \""
            + REGATA_REST_PROPERTY_BASE_URL + "\" nel sistema!");
      }
      return null;
    }
    if (logger != null)
    {
      logger.info("[" + THIS_CLASS + ".getClient] Generazione singleton RegataRESTClient con url = " + url);
    }
    RegataRESTClient client = new RegataRESTClient(url,getRestUsr(),getRestPsw());
    if (logger != null)
    {
      client.setLogger(logger);
    }
    if (logger != null)
    {
      logger.info("[" + THIS_CLASS + ".getClient] Generazione singleton RegataRESTClient con url = " + url + " eseguita con successo");
    }
    return client;
  }

  
  private static String getRestUsr()
  {
    String usr = System.getProperty(REGATA_REST_PROPERTY_USR);
    if (usr == null)
    {
      ResourceBundle regata = safeGetBundle("regata");
      if (regata != null)
      {
    	  usr = regata.getString(REGATA_REST_PROPERTY_USR);
      }
      if (usr == null)
      {
        ResourceBundle config = safeGetBundle("config");
        if (config != null)
        {
        	usr = config.getString(REGATA_REST_PROPERTY_USR);
        }
      }
    }
    if (usr == null)
    {
      if (logger != null)
      {
        logger.error("[" + THIS_CLASS + ".getRestUsr] Errore fatale nel tentativo di reperire le credenziali del servizio REST: Impossibile trovare la proprietà \""
            + REGATA_REST_PROPERTY_USR + "\" nel sistema!");
      }
      return null;
    }
    if (logger != null)
    {
      logger.info("[" + THIS_CLASS + ".getRestUsr] Generazione singleton REGATA_REST_PROPERTY_USR");
    }
    
    return usr;
  }
  
  private static String getRestPsw()
  {
    String psw = System.getProperty(REGATA_REST_PROPERTY_PSW);
    if (psw == null)
    {
      ResourceBundle regata = safeGetBundle("regata");
      if (regata != null)
      {
    	  psw = regata.getString(REGATA_REST_PROPERTY_PSW);
      }
      if (psw == null)
      {
        ResourceBundle config = safeGetBundle("config");
        if (config != null)
        {
        	psw = config.getString(REGATA_REST_PROPERTY_PSW);
        }
      }
    }
    if (psw == null)
    {
      if (logger != null)
      {
        logger.error("[" + THIS_CLASS + ".getRestPsw] Errore fatale nel tentativo di reperire le credenziali del servizio REST: Impossibile trovare la proprietà \""
            + REGATA_REST_PROPERTY_USR + "\" nel sistema!");
      }
      return null;
    }
    if (logger != null)
    {
      logger.info("[" + THIS_CLASS + ".getRestPsw] Generazione singleton REGATA_REST_PROPERTY_PSW");
    }
    
    return psw;
  }
  
  public static void setLoggerName(String loggerName)
  {
    logger = Logger.getLogger(loggerName);
    if (REST_SERVICE_CLIENT != null)
    {
      REST_SERVICE_CLIENT.setLogger(logger);
    }
  }

  public static void setBaseUrl(String baseUrl)
  { 
    REST_SERVICE_CLIENT = new RegataRESTClient(baseUrl,getRestUsr(),getRestPsw());
    if (logger != null)
    {
      REST_SERVICE_CLIENT.setLogger(logger);
    }
  }
}
