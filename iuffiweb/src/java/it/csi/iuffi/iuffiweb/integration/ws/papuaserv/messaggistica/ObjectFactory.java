
package it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory
{

  private final static QName _ConfermaLetturaMessaggio_QNAME         = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "confermaLetturaMessaggio");
  private final static QName _ConfermaLetturaMessaggioResponse_QNAME = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "confermaLetturaMessaggioResponse");
  private final static QName _GetListaMessaggiResponse_QNAME         = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "getListaMessaggiResponse");
  private final static QName _InternalException_QNAME                = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "InternalException");
  private final static QName _GetAllegatoResponse_QNAME              = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "getAllegatoResponse");
  private final static QName _LogoutException_QNAME                  = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "LogoutException");
  private final static QName _VerificaLogout_QNAME                   = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "verificaLogout");
  private final static QName _GetAllegato_QNAME                      = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "getAllegato");
  private final static QName _GetDettagliMessaggioResponse_QNAME     = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "getDettagliMessaggioResponse");
  private final static QName _GetDettagliMessaggio_QNAME             = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "getDettagliMessaggio");
  private final static QName _GetListaMessaggi_QNAME                 = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "getListaMessaggi");
  private final static QName _VerificaLogoutResponse_QNAME           = new QName(
      "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
      "verificaLogoutResponse");

  /**
   * Create a new ObjectFactory that can be used to create new instances of
   * schema derived classes for package:
   * it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica
   * 
   */
  public ObjectFactory()
  {
  }

  /**
   * Create an instance of {@link GetDettagliMessaggio }
   * 
   */
  public GetDettagliMessaggio createGetDettagliMessaggio()
  {
    return new GetDettagliMessaggio();
  }

  /**
   * Create an instance of {@link GetListaMessaggi }
   * 
   */
  public GetListaMessaggi createGetListaMessaggi()
  {
    return new GetListaMessaggi();
  }

  /**
   * Create an instance of {@link VerificaLogoutResponse }
   * 
   */
  public VerificaLogoutResponse createVerificaLogoutResponse()
  {
    return new VerificaLogoutResponse();
  }

  /**
   * Create an instance of {@link GetDettagliMessaggioResponse }
   * 
   */
  public GetDettagliMessaggioResponse createGetDettagliMessaggioResponse()
  {
    return new GetDettagliMessaggioResponse();
  }

  /**
   * Create an instance of {@link GetAllegato }
   * 
   */
  public GetAllegato createGetAllegato()
  {
    return new GetAllegato();
  }

  /**
   * Create an instance of {@link LogoutException }
   * 
   */
  public LogoutException createLogoutException()
  {
    return new LogoutException();
  }

  /**
   * Create an instance of {@link VerificaLogout }
   * 
   */
  public VerificaLogout createVerificaLogout()
  {
    return new VerificaLogout();
  }

  /**
   * Create an instance of {@link ConfermaLetturaMessaggio }
   * 
   */
  public ConfermaLetturaMessaggio createConfermaLetturaMessaggio()
  {
    return new ConfermaLetturaMessaggio();
  }

  /**
   * Create an instance of {@link InternalException }
   * 
   */
  public InternalException createInternalException()
  {
    return new InternalException();
  }

  /**
   * Create an instance of {@link GetListaMessaggiResponse }
   * 
   */
  public GetListaMessaggiResponse createGetListaMessaggiResponse()
  {
    return new GetListaMessaggiResponse();
  }

  /**
   * Create an instance of {@link ConfermaLetturaMessaggioResponse }
   * 
   */
  public ConfermaLetturaMessaggioResponse createConfermaLetturaMessaggioResponse()
  {
    return new ConfermaLetturaMessaggioResponse();
  }

  /**
   * Create an instance of {@link GetAllegatoResponse }
   * 
   */
  public GetAllegatoResponse createGetAllegatoResponse()
  {
    return new GetAllegatoResponse();
  }

  /**
   * Create an instance of {@link ListaMessaggi }
   * 
   */
  public ListaMessaggi createListaMessaggi()
  {
    return new ListaMessaggi();
  }

  /**
   * Create an instance of {@link DettagliMessaggio }
   * 
   */
  public DettagliMessaggio createDettagliMessaggio()
  {
    return new DettagliMessaggio();
  }

  /**
   * Create an instance of {@link UtenteAggiornamento }
   * 
   */
  public UtenteAggiornamento createUtenteAggiornamento()
  {
    return new UtenteAggiornamento();
  }

  /**
   * Create an instance of {@link Allegato }
   * 
   */
  public Allegato createAllegato()
  {
    return new Allegato();
  }

  /**
   * Create an instance of {@link Messaggio }
   * 
   */
  public Messaggio createMessaggio()
  {
    return new Messaggio();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ConfermaLetturaMessaggio }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/", name = "confermaLetturaMessaggio")
  public JAXBElement<ConfermaLetturaMessaggio> createConfermaLetturaMessaggio(
      ConfermaLetturaMessaggio value)
  {
    return new JAXBElement<ConfermaLetturaMessaggio>(
        _ConfermaLetturaMessaggio_QNAME, ConfermaLetturaMessaggio.class, null,
        value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ConfermaLetturaMessaggioResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/", name = "confermaLetturaMessaggioResponse")
  public JAXBElement<ConfermaLetturaMessaggioResponse> createConfermaLetturaMessaggioResponse(
      ConfermaLetturaMessaggioResponse value)
  {
    return new JAXBElement<ConfermaLetturaMessaggioResponse>(
        _ConfermaLetturaMessaggioResponse_QNAME,
        ConfermaLetturaMessaggioResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link GetListaMessaggiResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/", name = "getListaMessaggiResponse")
  public JAXBElement<GetListaMessaggiResponse> createGetListaMessaggiResponse(
      GetListaMessaggiResponse value)
  {
    return new JAXBElement<GetListaMessaggiResponse>(
        _GetListaMessaggiResponse_QNAME, GetListaMessaggiResponse.class, null,
        value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link InternalException
   * }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/", name = "InternalException")
  public JAXBElement<InternalException> createInternalException(
      InternalException value)
  {
    return new JAXBElement<InternalException>(_InternalException_QNAME,
        InternalException.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link GetAllegatoResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/", name = "getAllegatoResponse")
  public JAXBElement<GetAllegatoResponse> createGetAllegatoResponse(
      GetAllegatoResponse value)
  {
    return new JAXBElement<GetAllegatoResponse>(_GetAllegatoResponse_QNAME,
        GetAllegatoResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link LogoutException }
   * {@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/", name = "LogoutException")
  public JAXBElement<LogoutException> createLogoutException(
      LogoutException value)
  {
    return new JAXBElement<LogoutException>(_LogoutException_QNAME,
        LogoutException.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link VerificaLogout }
   * {@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/", name = "verificaLogout")
  public JAXBElement<VerificaLogout> createVerificaLogout(VerificaLogout value)
  {
    return new JAXBElement<VerificaLogout>(_VerificaLogout_QNAME,
        VerificaLogout.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link GetAllegato }
   * {@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/", name = "getAllegato")
  public JAXBElement<GetAllegato> createGetAllegato(GetAllegato value)
  {
    return new JAXBElement<GetAllegato>(_GetAllegato_QNAME, GetAllegato.class,
        null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link GetDettagliMessaggioResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/", name = "getDettagliMessaggioResponse")
  public JAXBElement<GetDettagliMessaggioResponse> createGetDettagliMessaggioResponse(
      GetDettagliMessaggioResponse value)
  {
    return new JAXBElement<GetDettagliMessaggioResponse>(
        _GetDettagliMessaggioResponse_QNAME, GetDettagliMessaggioResponse.class,
        null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link GetDettagliMessaggio }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/", name = "getDettagliMessaggio")
  public JAXBElement<GetDettagliMessaggio> createGetDettagliMessaggio(
      GetDettagliMessaggio value)
  {
    return new JAXBElement<GetDettagliMessaggio>(_GetDettagliMessaggio_QNAME,
        GetDettagliMessaggio.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link GetListaMessaggi
   * }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/", name = "getListaMessaggi")
  public JAXBElement<GetListaMessaggi> createGetListaMessaggi(
      GetListaMessaggi value)
  {
    return new JAXBElement<GetListaMessaggi>(_GetListaMessaggi_QNAME,
        GetListaMessaggi.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link VerificaLogoutResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://papuaserv.webservice.business.papuaserv.papua.csi.it/", name = "verificaLogoutResponse")
  public JAXBElement<VerificaLogoutResponse> createVerificaLogoutResponse(
      VerificaLogoutResponse value)
  {
    return new JAXBElement<VerificaLogoutResponse>(
        _VerificaLogoutResponse_QNAME, VerificaLogoutResponse.class, null,
        value);
  }

}
