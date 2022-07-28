package it.csi.iuffi.iuffiweb.integration.ws.regata;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import it.csi.iuffi.iuffiweb.integration.ws.regata.exception.InternalException;

public class RegataRESTClient
{
  private String                   restServiceUrl    = null;
  private String                   restUser          = null;
  private String                   restPsw           = null;
  private Logger                   logger;
  public static final ObjectMapper JACKSON_MAPPER    = new ObjectMapper().configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      // .configure(Feature.UNWRAP_ROOT_VALUE, true)
      .configure(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
  private static final String      THIS_CLASS        = RegataRESTClient.class.getSimpleName();
  private int                      timeout           = 4000;
  private int                      connectionTimeout = 4000;

  public RegataRESTClient(String restServiceUrl, String usr, String psw)
  {
    ResourceBundle res = ResourceBundle.getBundle("config");
    restServiceUrl = res.getString("regata.rest.baseurl");
    usr = res.getString("regata.rest.usr");
    psw = res.getString("regata.rest.psw");
    
    this.restServiceUrl = restServiceUrl;
    this.restUser = usr;
    this.restPsw = psw;
  }

  @SuppressWarnings("unchecked")
  protected <T> T callServiceUsingPutMethod(String serviceName, Object payload, Class<T> clazz, Param... params) throws InternalException
  {
    String inputStream = null;
    try
    {
      String finalUrl = createUrl(serviceName, params);
      boolean debugEnabled = logger != null && logger.isDebugEnabled();
      if (debugEnabled)
      {
        logger.debug("[" + THIS_CLASS + ".callServiceUsingGetMethod] calling papuaserv rest service at url: " + finalUrl);
      }
      PutMethod method = new PutMethod(restServiceUrl + finalUrl);

      HttpClient client = new HttpClient();

      Credentials credentials = new UsernamePasswordCredentials(this.restUser, this.restPsw);
      client.getState().setCredentials(AuthScope.ANY, credentials);

      setTimeout(client);

      if (payload != null)
      {
        String payloadStr = JACKSON_MAPPER.writeValueAsString(payload);
        method.setRequestBody(payloadStr);
        method.setRequestHeader("Content-Type", "application/json");
      }

      int status = client.executeMethod(method);
      if (debugEnabled)
      {
        logger.debug("[" + THIS_CLASS + ".callServiceUsingGetMethod] return code " + status);
      }
      if (status == 200)
      {
        inputStream = method.getResponseBodyAsString();
        if (inputStream == null || inputStream.length() == 0)
        {
          return null;
        }
        return JACKSON_MAPPER.readValue(inputStream, clazz);
      }
      else
      {
        inputStream = method.getResponseBodyAsString();
        Map<String, String> map = JACKSON_MAPPER.readValue(inputStream, HashMap.class);
        String exceptionType = map.get("exceptionType");
        Class<Exception> exceptionClass = null;
        if (exceptionType != null)
        {
          try
          {
            exceptionClass = (Class<Exception>) Class.forName(exceptionType);
          }
          catch (ClassNotFoundException e)
          {
            exceptionClass = Exception.class; // Non ho la classe nel mio
                                              // classpath e quindi la leggo
                                              // come un'eccezione
                                              // generica
          }
        }
        else
        {
          exceptionClass = Exception.class; // Non conosco il tipo e quindi la
                                            // leggo come un'eccezione generica
        }
        throw (Exception) JACKSON_MAPPER.readValue(inputStream, exceptionClass);
      }
    }
    catch (InternalException e)
    {
      throw e;
    }
    catch (JsonParseException e)
    {
      throw new InternalException(e.getMessage(), InternalException.ERROR_CODE_REST_CLIENT_DESERIALIZATION);
    }
    catch (JsonMappingException e)
    {
      throw new InternalException(e.getMessage(), InternalException.ERROR_CODE_REST_CLIENT_DESERIALIZATION);
    }
    catch (MalformedURLException e)
    {
      throw new InternalException(e.getMessage(), InternalException.ERROR_CODE_REST_CLIENT_NET);
    }
    catch (IOException e)
    {
      throw new InternalException(e.getMessage(), InternalException.ERROR_CODE_REST_CLIENT_NET);
    }
    catch (Exception e)
    {
      throw new InternalException(e.getMessage(), InternalException.ERROR_CODE_REST_CLIENT_UNKNOWN);
    }
  }

  @SuppressWarnings("deprecation")
  private void setTimeout(HttpClient httpClient)
  {
    httpClient.setConnectionTimeout(connectionTimeout);
    httpClient.setTimeout(timeout);
  }

  @SuppressWarnings("unchecked")
  protected <T> T callServiceUsingPostMethod(String serviceName, Class<T> clazz, Param... params) throws InternalException
  {
    String inputStream = null;
    try
    {
      String finalUrl = restServiceUrl + serviceName;
      boolean debugEnabled = logger != null && logger.isDebugEnabled();
      if (debugEnabled)
      {
        logger.debug("[" + THIS_CLASS + ".callServiceUsingPostMethod] calling papuaserv rest service at url: " + finalUrl);
      }
      PostMethod method = new PostMethod(finalUrl);
      HttpClient client = new HttpClient();
      if (params != null)
      {
        for (Param param : params)
        {
          param.setHttpParam(method);
        }
      }
      int status = client.executeMethod(method);
      if (debugEnabled)
      {
        logger.debug("[" + THIS_CLASS + ".callServiceUsingPostMethod] return code " + status);
      }
      if (status == 200)
      {
        inputStream = method.getResponseBodyAsString();
        if (inputStream == null || inputStream.length() == 0)
        {
          return null;
        }
        return JACKSON_MAPPER.readValue(inputStream, clazz);
      }
      else
      {
        inputStream = method.getResponseBodyAsString();
        Map<String, String> map = JACKSON_MAPPER.readValue(inputStream, HashMap.class);
        String exceptionType = map.get("exceptionType");
        Class<Exception> exceptionClass = null;
        if (exceptionType != null)
        {
          try
          {
            exceptionClass = (Class<Exception>) Class.forName(exceptionType);
          }
          catch (ClassNotFoundException e)
          {
            exceptionClass = Exception.class; // Non ho la classe nel mio
                                              // classpath e quindi la leggo
                                              // come un'eccezione
                                              // generica
          }
        }
        else
        {
          exceptionClass = Exception.class; // Non conosco il tipo e quindi la
                                            // leggo come un'eccezione generica
        }
        throw (Exception) JACKSON_MAPPER.readValue(inputStream, exceptionClass);
      }
    }
    catch (InternalException e)
    {
      throw e;
    }
    catch (JsonParseException e)
    {
      throw new InternalException(e.getMessage(), InternalException.ERROR_CODE_REST_CLIENT_DESERIALIZATION);
    }
    catch (JsonMappingException e)
    {
      throw new InternalException(e.getMessage(), InternalException.ERROR_CODE_REST_CLIENT_DESERIALIZATION);
    }
    catch (MalformedURLException e)
    {
      throw new InternalException(e.getMessage(), InternalException.ERROR_CODE_REST_CLIENT_NET);
    }
    catch (IOException e)
    {
      throw new InternalException(e.getMessage(), InternalException.ERROR_CODE_REST_CLIENT_NET);
    }
    catch (Exception e)
    {
      throw new InternalException(e.getMessage(), InternalException.ERROR_CODE_REST_CLIENT_UNKNOWN);
    }
  }

  protected String createUrl(String url, Param[] params) throws UnsupportedEncodingException
  {
    if (params == null || params.length == 0)
    {
      return url;
    }
    return restServiceUrl + url + "?" + getHttpParameters(params);
  }

  protected String stringOf(Object value)
  {
    return value == null ? "" : value.toString();
  }

  public EsitoRegataVO chiediIdRichiesta(long idUtenteIride) throws InvalidParameterException, InternalException
  {
    try
    {
      HashMap<String, Long> map = new HashMap<String, Long>();
      map.put("idUtenteIride", idUtenteIride);
      return callServiceUsingPutMethod("ChiediIdRichiesta", map, EsitoRegataVO.class);
    }
    finally
    {
    }
  }

  public EsitoRegataMassiveVO inviaAziendeMassivo(PayloadMassiveDTO input) throws InvalidParameterException, InternalException
  {
    return callServiceUsingPutMethod("InsertRichiestaVisureMassiva", input, EsitoRegataMassiveVO.class);
  }

  protected String getHttpParameters(Param[] params) throws UnsupportedEncodingException
  {
    StringBuilder sb = new StringBuilder();
    if (params != null)
    {
      for (Param param : params)
      {
        if (sb.length() > 0)
        {
          sb.append("&");
        }
        sb.append(param.toHttpParam());
      }
    }
    return sb.toString();
  }

  protected class Param
  {
    String name;
    Object value;

    public Param(String name, Object value)
    {
      this.name = name;
      this.value = value;
    }

    public String toHttpParam() throws UnsupportedEncodingException
    {
      StringBuilder sb = new StringBuilder();
      if (value.getClass().isArray())
      {
        int len = java.lang.reflect.Array.getLength(value);
        boolean amp = false;
        for (int i = 0; i < len; ++i)
        {
          Object item = java.lang.reflect.Array.get(value, i);
          if (!amp)
          {
            amp = true;
          }
          else
          {
            sb.append("&");
          }
          sb.append(name).append("=");
          sb.append(java.net.URLEncoder.encode(stringOf(item), "ISO-8859-1"));
        }
      }
      else
      {
        sb.append(name).append("=");
        sb.append(java.net.URLEncoder.encode(stringOf(value), "ISO-8859-1"));
      }
      return sb.toString();
    }

    public void setHttpParam(PostMethod method) throws UnsupportedEncodingException
    {
      if (value.getClass().isArray())
      {
        int len = java.lang.reflect.Array.getLength(value);
        for (int i = 0; i < len; ++i)
        {
          Object item = java.lang.reflect.Array.get(value, i);
          method.addParameter(name, java.net.URLEncoder.encode(stringOf(item), "ISO-8859-1"));
        }
      }
      else
      {
        method.setParameter(name, java.net.URLEncoder.encode(stringOf(value), "ISO-8859-1"));
      }
    }
  }

  public void setLogger(Logger logger)
  {
    this.logger = logger;
  }

  public int getTimeout()
  {
    return timeout;
  }

  public void setTimeout(int timeout)
  {
    this.timeout = timeout;
  }

  public int getConnectionTimeout()
  {
    return connectionTimeout;
  }

  public void setConnectionTimeout(int connectionTimeout)
  {
    this.connectionTimeout = connectionTimeout;
  }

  public String getRestServiceUrl()
  {
    return restServiceUrl;
  }

}
