package it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by Apache CXF 2.4.6-redhat-1
 * 2016-02-02T10:41:23.557+01:00 Generated source version: 2.4.6-redhat-1
 * 
 */
@WebServiceClient(name = "messaggistica", wsdlLocation = "http://<WEB_SERVER_HOST:PORT>/papuaserv/ws/messaggistica?wsdl", targetNamespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/")
public class Messaggistica extends Service
{

  public final static URL   WSDL_LOCATION;

  public final static QName SERVICE           = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "messaggistica");
  public final static QName MessaggisticaPort = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "messaggisticaPort");
  static
  {
    URL url = null;
    try
    {
      url = new URL(
          "http://<WEB_SERVER_HOST:PORT>/papuaserv/ws/messaggistica?wsdl");
    }
    catch (MalformedURLException e)
    {
      java.util.logging.Logger.getLogger(Messaggistica.class.getName())
          .log(java.util.logging.Level.INFO,
              "Can not initialize the default wsdl from {0}",
              "http://<WEB_SERVER_HOST:PORT>/papuaserv/ws/messaggistica?wsdl");
    }
    WSDL_LOCATION = url;
  }

  public Messaggistica(URL wsdlLocation)
  {
    super(wsdlLocation, SERVICE);
  }

  public Messaggistica(URL wsdlLocation, QName serviceName)
  {
    super(wsdlLocation, serviceName);
  }

  public Messaggistica()
  {
    super(WSDL_LOCATION, SERVICE);
  }

  /**
   *
   * @return returns IMessaggisticaWS
   */
  @WebEndpoint(name = "messaggisticaPort")
  public IMessaggisticaWS getMessaggisticaPort()
  {
    return super.getPort(MessaggisticaPort, IMessaggisticaWS.class);
  }

  /**
   * 
   * @param features
   *          A list of {@link javax.xml.ws.WebServiceFeature} to configure on
   *          the proxy. Supported features not in the <code>features</code>
   *          parameter will have their default values.
   * @return returns IMessaggisticaWS
   */
  @WebEndpoint(name = "messaggisticaPort")
  public IMessaggisticaWS getMessaggisticaPort(WebServiceFeature... features)
  {
    return super.getPort(MessaggisticaPort, IMessaggisticaWS.class, features);
  }

}
