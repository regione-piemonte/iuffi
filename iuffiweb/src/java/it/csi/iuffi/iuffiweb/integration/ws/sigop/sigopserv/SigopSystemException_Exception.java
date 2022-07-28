
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.ws.WebFault;

/**
 * This class was generated by Apache CXF 3.1.6 2017-03-07T16:14:32.012+01:00
 * Generated source version: 3.1.6
 */

@SuppressWarnings("serial")
@WebFault(name = "SigopSystemException", targetNamespace = "http://ws.business.sigop.csi.it/")
public class SigopSystemException_Exception extends Exception
{

  private it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv.SigopSystemException sigopSystemException;

  public SigopSystemException_Exception()
  {
    super();
  }

  public SigopSystemException_Exception(String message)
  {
    super(message);
  }

  public SigopSystemException_Exception(String message, Throwable cause)
  {
    super(message, cause);
  }

  public SigopSystemException_Exception(String message,
      it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv.SigopSystemException sigopSystemException)
  {
    super(message);
    this.sigopSystemException = sigopSystemException;
  }

  public SigopSystemException_Exception(String message,
      it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv.SigopSystemException sigopSystemException,
      Throwable cause)
  {
    super(message, cause);
    this.sigopSystemException = sigopSystemException;
  }

  public it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv.SigopSystemException getFaultInfo()
  {
    return this.sigopSystemException;
  }
}
