
package it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica;

import javax.xml.ws.WebFault;

/**
 * This class was generated by Apache CXF 2.4.6-redhat-1
 * 2016-02-02T10:41:23.499+01:00 Generated source version: 2.4.6-redhat-1
 */

@SuppressWarnings("serial")
@WebFault(name = "LogoutException", targetNamespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/")
public class LogoutException_Exception extends Exception
{

  private it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.LogoutException logoutException;

  public LogoutException_Exception()
  {
    super();
  }

  public LogoutException_Exception(String message)
  {
    super(message);
  }

  public LogoutException_Exception(String message, Throwable cause)
  {
    super(message, cause);
  }

  public LogoutException_Exception(String message,
      it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.LogoutException logoutException)
  {
    super(message);
    this.logoutException = logoutException;
  }

  public LogoutException_Exception(String message,
      it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.LogoutException logoutException,
      Throwable cause)
  {
    super(message, cause);
    this.logoutException = logoutException;
  }

  public it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.LogoutException getFaultInfo()
  {
    return this.logoutException;
  }
}
