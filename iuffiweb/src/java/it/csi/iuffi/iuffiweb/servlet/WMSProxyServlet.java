package it.csi.iuffi.iuffiweb.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import it.csi.iuffi.iuffiweb.util.IuffiConstants;

/**
 * Servlet implementation class WMSProxyServlet
 */
@WebServlet(name = "WMSProxyServlet", value = "/proxywms", loadOnStartup = 1)
public class WMSProxyServlet extends HttpServlet
{
  /** serialVersionUID */
  private static final long       serialVersionUID = -3807047194850080260L;
  private static final String     THIS_CLASS       = "WMSProxyServlet";
  //static Logger                   logger           = Logger.getLogger("it.csi.iuffi.iuffiweb.servlet");
  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".integration");
  private static final DateFormat GMT_FORMAT       = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");

  ResourceBundle res = ResourceBundle.getBundle("config");
  
  public WMSProxyServlet()
  {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    doAction(request, response);
  }

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException
  {
    doAction(request, response);
  }

  private void doAction(HttpServletRequest request,
      HttpServletResponse response) throws IOException
  {
    final String THIS_METHOD = "[" + THIS_CLASS + ".doAction]";
    InputStream in = null;
    String url = null;
    GetMethod httpMethod = null;
    try
    {
      logger.debug(THIS_METHOD);
      // SIAN_MAP_PROXY_URL
      String layerName = request.getParameter("LAYERS");
      //url = "http://<WEB_SERVER_HOST:PORT>/wmspiemonteagri/service";   // test 
      //url = "http://agrigeoweb.csi.it/wmspiemonteagri/service";       // prod
      url = res.getString("mapservice.baseurl");
      
      String queryString = request.getQueryString();
      if (url == null)
      {
        logger.error(THIS_METHOD + "Non è stato trovata nessuna configurazione WMS per il layer " + layerName);
        logger.error(THIS_METHOD + "Informazioni QueryString: " + queryString);
      }
      HttpClient client = new HttpClient();

      client.getHttpConnectionManager().getParams().setConnectionTimeout(20000);

      httpMethod = new GetMethod(url);
      httpMethod.setQueryString(queryString);
      int status = client.executeMethod(httpMethod);

      String contentType = "image/jpeg";
      Header[] responseHeaders = httpMethod.getResponseHeaders("content-type");
      if (responseHeaders.length > 0)
      {
        contentType = responseHeaders[0].getValue();
      }
      response.setContentType(contentType);
      if (status == HttpStatus.SC_OK)
      {
        if (contentType.toLowerCase().indexOf("xml") > -1)
        {
          String body = httpMethod.getResponseBodyAsString();
          body = body.replaceAll("xlink:href[ ]*=[ ]*\"[^\"]*\"", "xlink:href=\"" + request.getRequestURL() + "?\"");
          response.getOutputStream().write(body.getBytes());
        }
        else
        {
          in = httpMethod.getResponseBodyAsStream();
        }
      }
      else
      {
        logger.error("Errore nel download delle ortofoto: " + httpMethod.getStatusText() + " - "
            + httpMethod.getStatusLine().getReasonPhrase());
      }

      if (in != null)
      {
        response.setHeader("Cache-Control", "public, max-age:14400");
        response.setHeader("Expires", GMT_FORMAT.format(new java.util.Date(new java.util.Date().getTime() + 14400000)));
        logger.debug("Imposto content type image/png");
        response.setContentType("image/png");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        IOUtils.copy(in, response.getOutputStream());
      }
      
      response.getOutputStream().flush();
      response.getOutputStream().close();

    }
    catch (Exception e)
    {
      logger.error(THIS_METHOD + " ::: URL " + url + "?" + httpMethod.getQueryString(), e);
    }
    finally
    {
      if (httpMethod != null)
        httpMethod.releaseConnection();
    }
  }


}
