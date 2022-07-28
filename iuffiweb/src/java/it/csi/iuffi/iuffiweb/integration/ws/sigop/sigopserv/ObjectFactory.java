
package it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv package.
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

  private final static QName _SigopMaxRecordException_QNAME                             = new QName(
      "http://ws.business.sigop.csi.it/", "SigopMaxRecordException");
  private final static QName _SigopSystemException_QNAME                                = new QName(
      "http://ws.business.sigop.csi.it/", "SigopSystemException");
  private final static QName _SigopServiceException_QNAME                               = new QName(
      "http://ws.business.sigop.csi.it/", "SigopServiceException");
  private final static QName _ServiceEstraiPagamentiBeneficiariCompleto_QNAME           = new QName(
      "http://ws.business.sigop.csi.it/",
      "serviceEstraiPagamentiBeneficiariCompleto");
  private final static QName _ServiceEstraiPagamentiBeneficiariCompletoResponse_QNAME   = new QName(
      "http://ws.business.sigop.csi.it/",
      "serviceEstraiPagamentiBeneficiariCompletoResponse");
  private final static QName _ServiceEstraiRecuperiPregressi_QNAME                      = new QName(
      "http://ws.business.sigop.csi.it/", "serviceEstraiRecuperiPregressi");
  private final static QName _ServiceEstraiRecuperiPregressiResponse_QNAME              = new QName(
      "http://ws.business.sigop.csi.it/",
      "serviceEstraiRecuperiPregressiResponse");
  private final static QName _ServiceEstraiInterventi_QNAME                             = new QName(
      "http://ws.business.sigop.csi.it/", "serviceEstraiInterventi");
  private final static QName _ServiceEstraiInterventiResponse_QNAME                     = new QName(
      "http://ws.business.sigop.csi.it/", "serviceEstraiInterventiResponse");
  private final static QName _ServiceAggiornaCoordinateBancariePagamento_QNAME          = new QName(
      "http://ws.business.sigop.csi.it/",
      "serviceAggiornaCoordinateBancariePagamento");
  private final static QName _ServiceAggiornaCoordinateBancariePagamentoResponse_QNAME  = new QName(
      "http://ws.business.sigop.csi.it/",
      "serviceAggiornaCoordinateBancariePagamentoResponse");
  private final static QName _TestResources_QNAME                                       = new QName(
      "http://ws.business.sigop.csi.it/", "testResources");
  private final static QName _TestResourcesResponse_QNAME                               = new QName(
      "http://ws.business.sigop.csi.it/", "testResourcesResponse");
  private final static QName _ServiceEstraiPagamentiBeneficiariLimite_QNAME             = new QName(
      "http://ws.business.sigop.csi.it/",
      "serviceEstraiPagamentiBeneficiariLimite");
  private final static QName _ServiceEstraiPagamentiBeneficiariLimiteResponse_QNAME     = new QName(
      "http://ws.business.sigop.csi.it/",
      "serviceEstraiPagamentiBeneficiariLimiteResponse");
  private final static QName _ServiceVisualizzaDebiti_QNAME                             = new QName(
      "http://ws.business.sigop.csi.it/", "serviceVisualizzaDebiti");
  private final static QName _ServiceVisualizzaDebitiResponse_QNAME                     = new QName(
      "http://ws.business.sigop.csi.it/", "serviceVisualizzaDebitiResponse");
  private final static QName _ServiceScriviNuovoSoggettoRegistroAntimafia_QNAME         = new QName(
      "http://ws.business.sigop.csi.it/",
      "serviceScriviNuovoSoggettoRegistroAntimafia");
  private final static QName _ServiceScriviNuovoSoggettoRegistroAntimafiaResponse_QNAME = new QName(
      "http://ws.business.sigop.csi.it/",
      "serviceScriviNuovoSoggettoRegistroAntimafiaResponse");
  private final static QName _ServiceEstraiPagamentiBeneficiari_QNAME                   = new QName(
      "http://ws.business.sigop.csi.it/", "serviceEstraiPagamentiBeneficiari");
  private final static QName _ServiceEstraiPagamentiBeneficiariResponse_QNAME           = new QName(
      "http://ws.business.sigop.csi.it/",
      "serviceEstraiPagamentiBeneficiariResponse");
  private final static QName _ServiceEstraiDecreti_QNAME                                = new QName(
      "http://ws.business.sigop.csi.it/", "serviceEstraiDecreti");
  private final static QName _ServiceEstraiDecretiResponse_QNAME                        = new QName(
      "http://ws.business.sigop.csi.it/", "serviceEstraiDecretiResponse");
  private final static QName _ServiceEstraiPagamentiErogati_QNAME                       = new QName(
      "http://ws.business.sigop.csi.it/", "serviceEstraiPagamentiErogati");
  private final static QName _ServiceEstraiPagamentiErogatiResponse_QNAME               = new QName(
      "http://ws.business.sigop.csi.it/",
      "serviceEstraiPagamentiErogatiResponse");
  private final static QName _ServiceEstraiFondi_QNAME                                  = new QName(
      "http://ws.business.sigop.csi.it/", "serviceEstraiFondi");
  private final static QName _ServiceEstraiFondiResponse_QNAME                          = new QName(
      "http://ws.business.sigop.csi.it/", "serviceEstraiFondiResponse");
  private final static QName _IsAlive_QNAME                                             = new QName(
      "http://ws.business.sigop.csi.it/", "isAlive");
  private final static QName _IsAliveResponse_QNAME                                     = new QName(
      "http://ws.business.sigop.csi.it/", "isAliveResponse");

  /**
   * Create a new ObjectFactory that can be used to create new instances of
   * schema derived classes for package:
   * it.csi.iuffi.iuffiweb.integration.ws.sigop.sigopserv
   * 
   */
  public ObjectFactory()
  {
  }

  /**
   * Create an instance of {@link SigopMaxRecordException }
   * 
   */
  public SigopMaxRecordException createSigopMaxRecordException()
  {
    return new SigopMaxRecordException();
  }

  /**
   * Create an instance of {@link SigopSystemException }
   * 
   */
  public SigopSystemException createSigopSystemException()
  {
    return new SigopSystemException();
  }

  /**
   * Create an instance of {@link SigopServiceException }
   * 
   */
  public SigopServiceException createSigopServiceException()
  {
    return new SigopServiceException();
  }

  /**
   * Create an instance of {@link ServiceEstraiPagamentiBeneficiariCompleto }
   * 
   */
  public ServiceEstraiPagamentiBeneficiariCompleto createServiceEstraiPagamentiBeneficiariCompleto()
  {
    return new ServiceEstraiPagamentiBeneficiariCompleto();
  }

  /**
   * Create an instance of
   * {@link ServiceEstraiPagamentiBeneficiariCompletoResponse }
   * 
   */
  public ServiceEstraiPagamentiBeneficiariCompletoResponse createServiceEstraiPagamentiBeneficiariCompletoResponse()
  {
    return new ServiceEstraiPagamentiBeneficiariCompletoResponse();
  }

  /**
   * Create an instance of {@link ServiceEstraiRecuperiPregressi }
   * 
   */
  public ServiceEstraiRecuperiPregressi createServiceEstraiRecuperiPregressi()
  {
    return new ServiceEstraiRecuperiPregressi();
  }

  /**
   * Create an instance of {@link ServiceEstraiRecuperiPregressiResponse }
   * 
   */
  public ServiceEstraiRecuperiPregressiResponse createServiceEstraiRecuperiPregressiResponse()
  {
    return new ServiceEstraiRecuperiPregressiResponse();
  }

  /**
   * Create an instance of {@link ServiceEstraiInterventi }
   * 
   */
  public ServiceEstraiInterventi createServiceEstraiInterventi()
  {
    return new ServiceEstraiInterventi();
  }

  /**
   * Create an instance of {@link ServiceEstraiInterventiResponse }
   * 
   */
  public ServiceEstraiInterventiResponse createServiceEstraiInterventiResponse()
  {
    return new ServiceEstraiInterventiResponse();
  }

  /**
   * Create an instance of {@link ServiceAggiornaCoordinateBancariePagamento }
   * 
   */
  public ServiceAggiornaCoordinateBancariePagamento createServiceAggiornaCoordinateBancariePagamento()
  {
    return new ServiceAggiornaCoordinateBancariePagamento();
  }

  /**
   * Create an instance of
   * {@link ServiceAggiornaCoordinateBancariePagamentoResponse }
   * 
   */
  public ServiceAggiornaCoordinateBancariePagamentoResponse createServiceAggiornaCoordinateBancariePagamentoResponse()
  {
    return new ServiceAggiornaCoordinateBancariePagamentoResponse();
  }

  /**
   * Create an instance of {@link TestResources }
   * 
   */
  public TestResources createTestResources()
  {
    return new TestResources();
  }

  /**
   * Create an instance of {@link TestResourcesResponse }
   * 
   */
  public TestResourcesResponse createTestResourcesResponse()
  {
    return new TestResourcesResponse();
  }

  /**
   * Create an instance of {@link ServiceEstraiPagamentiBeneficiariLimite }
   * 
   */
  public ServiceEstraiPagamentiBeneficiariLimite createServiceEstraiPagamentiBeneficiariLimite()
  {
    return new ServiceEstraiPagamentiBeneficiariLimite();
  }

  /**
   * Create an instance of
   * {@link ServiceEstraiPagamentiBeneficiariLimiteResponse }
   * 
   */
  public ServiceEstraiPagamentiBeneficiariLimiteResponse createServiceEstraiPagamentiBeneficiariLimiteResponse()
  {
    return new ServiceEstraiPagamentiBeneficiariLimiteResponse();
  }

  /**
   * Create an instance of {@link ServiceVisualizzaDebiti }
   * 
   */
  public ServiceVisualizzaDebiti createServiceVisualizzaDebiti()
  {
    return new ServiceVisualizzaDebiti();
  }

  /**
   * Create an instance of {@link ServiceVisualizzaDebitiResponse }
   * 
   */
  public ServiceVisualizzaDebitiResponse createServiceVisualizzaDebitiResponse()
  {
    return new ServiceVisualizzaDebitiResponse();
  }

  /**
   * Create an instance of {@link ServiceScriviNuovoSoggettoRegistroAntimafia }
   * 
   */
  public ServiceScriviNuovoSoggettoRegistroAntimafia createServiceScriviNuovoSoggettoRegistroAntimafia()
  {
    return new ServiceScriviNuovoSoggettoRegistroAntimafia();
  }

  /**
   * Create an instance of
   * {@link ServiceScriviNuovoSoggettoRegistroAntimafiaResponse }
   * 
   */
  public ServiceScriviNuovoSoggettoRegistroAntimafiaResponse createServiceScriviNuovoSoggettoRegistroAntimafiaResponse()
  {
    return new ServiceScriviNuovoSoggettoRegistroAntimafiaResponse();
  }

  /**
   * Create an instance of {@link ServiceEstraiPagamentiBeneficiari }
   * 
   */
  public ServiceEstraiPagamentiBeneficiari createServiceEstraiPagamentiBeneficiari()
  {
    return new ServiceEstraiPagamentiBeneficiari();
  }

  /**
   * Create an instance of {@link ServiceEstraiPagamentiBeneficiariResponse }
   * 
   */
  public ServiceEstraiPagamentiBeneficiariResponse createServiceEstraiPagamentiBeneficiariResponse()
  {
    return new ServiceEstraiPagamentiBeneficiariResponse();
  }

  /**
   * Create an instance of {@link ServiceEstraiDecreti }
   * 
   */
  public ServiceEstraiDecreti createServiceEstraiDecreti()
  {
    return new ServiceEstraiDecreti();
  }

  /**
   * Create an instance of {@link ServiceEstraiDecretiResponse }
   * 
   */
  public ServiceEstraiDecretiResponse createServiceEstraiDecretiResponse()
  {
    return new ServiceEstraiDecretiResponse();
  }

  /**
   * Create an instance of {@link ServiceEstraiPagamentiErogati }
   * 
   */
  public ServiceEstraiPagamentiErogati createServiceEstraiPagamentiErogati()
  {
    return new ServiceEstraiPagamentiErogati();
  }

  /**
   * Create an instance of {@link ServiceEstraiPagamentiErogatiResponse }
   * 
   */
  public ServiceEstraiPagamentiErogatiResponse createServiceEstraiPagamentiErogatiResponse()
  {
    return new ServiceEstraiPagamentiErogatiResponse();
  }

  /**
   * Create an instance of {@link ServiceEstraiFondi }
   * 
   */
  public ServiceEstraiFondi createServiceEstraiFondi()
  {
    return new ServiceEstraiFondi();
  }

  /**
   * Create an instance of {@link ServiceEstraiFondiResponse }
   * 
   */
  public ServiceEstraiFondiResponse createServiceEstraiFondiResponse()
  {
    return new ServiceEstraiFondiResponse();
  }

  /**
   * Create an instance of {@link IsAlive }
   * 
   */
  public IsAlive createIsAlive()
  {
    return new IsAlive();
  }

  /**
   * Create an instance of {@link IsAliveResponse }
   * 
   */
  public IsAliveResponse createIsAliveResponse()
  {
    return new IsAliveResponse();
  }

  /**
   * Create an instance of {@link PagamentoBeneficiarioExtVO }
   * 
   */
  public PagamentoBeneficiarioExtVO createPagamentoBeneficiarioExtVO()
  {
    return new PagamentoBeneficiarioExtVO();
  }

  /**
   * Create an instance of {@link InterventoVO }
   * 
   */
  public InterventoVO createInterventoVO()
  {
    return new InterventoVO();
  }

  /**
   * Create an instance of {@link SettoreExtVO }
   * 
   */
  public SettoreExtVO createSettoreExtVO()
  {
    return new SettoreExtVO();
  }

  /**
   * Create an instance of {@link SettoreVO }
   * 
   */
  public SettoreVO createSettoreVO()
  {
    return new SettoreVO();
  }

  /**
   * Create an instance of {@link RecuperiPregressiVO }
   * 
   */
  public RecuperiPregressiVO createRecuperiPregressiVO()
  {
    return new RecuperiPregressiVO();
  }

  /**
   * Create an instance of {@link EsitoServizioVO }
   * 
   */
  public EsitoServizioVO createEsitoServizioVO()
  {
    return new EsitoServizioVO();
  }

  /**
   * Create an instance of {@link RecuperoPregressoVO }
   * 
   */
  public RecuperoPregressoVO createRecuperoPregressoVO()
  {
    return new RecuperoPregressoVO();
  }

  /**
   * Create an instance of {@link PagamentoBeneficiarioVO }
   * 
   */
  public PagamentoBeneficiarioVO createPagamentoBeneficiarioVO()
  {
    return new PagamentoBeneficiarioVO();
  }

  /**
   * Create an instance of {@link SchedaCreditoVO }
   * 
   */
  public SchedaCreditoVO createSchedaCreditoVO()
  {
    return new SchedaCreditoVO();
  }

  /**
   * Create an instance of {@link DisposizioniTrasgrediteVO }
   * 
   */
  public DisposizioniTrasgrediteVO createDisposizioniTrasgrediteVO()
  {
    return new DisposizioniTrasgrediteVO();
  }

  /**
   * Create an instance of {@link DecretoVO }
   * 
   */
  public DecretoVO createDecretoVO()
  {
    return new DecretoVO();
  }

  /**
   * Create an instance of {@link PagamentiErogatiVO }
   * 
   */
  public PagamentiErogatiVO createPagamentiErogatiVO()
  {
    return new PagamentiErogatiVO();
  }

  /**
   * Create an instance of {@link PagamentoErogatoVO }
   * 
   */
  public PagamentoErogatoVO createPagamentoErogatoVO()
  {
    return new PagamentoErogatoVO();
  }

  /**
   * Create an instance of {@link FondoVO }
   * 
   */
  public FondoVO createFondoVO()
  {
    return new FondoVO();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link SigopMaxRecordException }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "SigopMaxRecordException")
  public JAXBElement<SigopMaxRecordException> createSigopMaxRecordException(
      SigopMaxRecordException value)
  {
    return new JAXBElement<SigopMaxRecordException>(
        _SigopMaxRecordException_QNAME, SigopMaxRecordException.class, null,
        value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link SigopSystemException }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "SigopSystemException")
  public JAXBElement<SigopSystemException> createSigopSystemException(
      SigopSystemException value)
  {
    return new JAXBElement<SigopSystemException>(_SigopSystemException_QNAME,
        SigopSystemException.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link SigopServiceException }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "SigopServiceException")
  public JAXBElement<SigopServiceException> createSigopServiceException(
      SigopServiceException value)
  {
    return new JAXBElement<SigopServiceException>(_SigopServiceException_QNAME,
        SigopServiceException.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiPagamentiBeneficiariCompleto }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiPagamentiBeneficiariCompleto")
  public JAXBElement<ServiceEstraiPagamentiBeneficiariCompleto> createServiceEstraiPagamentiBeneficiariCompleto(
      ServiceEstraiPagamentiBeneficiariCompleto value)
  {
    return new JAXBElement<ServiceEstraiPagamentiBeneficiariCompleto>(
        _ServiceEstraiPagamentiBeneficiariCompleto_QNAME,
        ServiceEstraiPagamentiBeneficiariCompleto.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiPagamentiBeneficiariCompletoResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiPagamentiBeneficiariCompletoResponse")
  public JAXBElement<ServiceEstraiPagamentiBeneficiariCompletoResponse> createServiceEstraiPagamentiBeneficiariCompletoResponse(
      ServiceEstraiPagamentiBeneficiariCompletoResponse value)
  {
    return new JAXBElement<ServiceEstraiPagamentiBeneficiariCompletoResponse>(
        _ServiceEstraiPagamentiBeneficiariCompletoResponse_QNAME,
        ServiceEstraiPagamentiBeneficiariCompletoResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiRecuperiPregressi }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiRecuperiPregressi")
  public JAXBElement<ServiceEstraiRecuperiPregressi> createServiceEstraiRecuperiPregressi(
      ServiceEstraiRecuperiPregressi value)
  {
    return new JAXBElement<ServiceEstraiRecuperiPregressi>(
        _ServiceEstraiRecuperiPregressi_QNAME,
        ServiceEstraiRecuperiPregressi.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiRecuperiPregressiResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiRecuperiPregressiResponse")
  public JAXBElement<ServiceEstraiRecuperiPregressiResponse> createServiceEstraiRecuperiPregressiResponse(
      ServiceEstraiRecuperiPregressiResponse value)
  {
    return new JAXBElement<ServiceEstraiRecuperiPregressiResponse>(
        _ServiceEstraiRecuperiPregressiResponse_QNAME,
        ServiceEstraiRecuperiPregressiResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiInterventi }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiInterventi")
  public JAXBElement<ServiceEstraiInterventi> createServiceEstraiInterventi(
      ServiceEstraiInterventi value)
  {
    return new JAXBElement<ServiceEstraiInterventi>(
        _ServiceEstraiInterventi_QNAME, ServiceEstraiInterventi.class, null,
        value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiInterventiResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiInterventiResponse")
  public JAXBElement<ServiceEstraiInterventiResponse> createServiceEstraiInterventiResponse(
      ServiceEstraiInterventiResponse value)
  {
    return new JAXBElement<ServiceEstraiInterventiResponse>(
        _ServiceEstraiInterventiResponse_QNAME,
        ServiceEstraiInterventiResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceAggiornaCoordinateBancariePagamento }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceAggiornaCoordinateBancariePagamento")
  public JAXBElement<ServiceAggiornaCoordinateBancariePagamento> createServiceAggiornaCoordinateBancariePagamento(
      ServiceAggiornaCoordinateBancariePagamento value)
  {
    return new JAXBElement<ServiceAggiornaCoordinateBancariePagamento>(
        _ServiceAggiornaCoordinateBancariePagamento_QNAME,
        ServiceAggiornaCoordinateBancariePagamento.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceAggiornaCoordinateBancariePagamentoResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceAggiornaCoordinateBancariePagamentoResponse")
  public JAXBElement<ServiceAggiornaCoordinateBancariePagamentoResponse> createServiceAggiornaCoordinateBancariePagamentoResponse(
      ServiceAggiornaCoordinateBancariePagamentoResponse value)
  {
    return new JAXBElement<ServiceAggiornaCoordinateBancariePagamentoResponse>(
        _ServiceAggiornaCoordinateBancariePagamentoResponse_QNAME,
        ServiceAggiornaCoordinateBancariePagamentoResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link TestResources }
   * {@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "testResources")
  public JAXBElement<TestResources> createTestResources(TestResources value)
  {
    return new JAXBElement<TestResources>(_TestResources_QNAME,
        TestResources.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link TestResourcesResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "testResourcesResponse")
  public JAXBElement<TestResourcesResponse> createTestResourcesResponse(
      TestResourcesResponse value)
  {
    return new JAXBElement<TestResourcesResponse>(_TestResourcesResponse_QNAME,
        TestResourcesResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiPagamentiBeneficiariLimite }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiPagamentiBeneficiariLimite")
  public JAXBElement<ServiceEstraiPagamentiBeneficiariLimite> createServiceEstraiPagamentiBeneficiariLimite(
      ServiceEstraiPagamentiBeneficiariLimite value)
  {
    return new JAXBElement<ServiceEstraiPagamentiBeneficiariLimite>(
        _ServiceEstraiPagamentiBeneficiariLimite_QNAME,
        ServiceEstraiPagamentiBeneficiariLimite.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiPagamentiBeneficiariLimiteResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiPagamentiBeneficiariLimiteResponse")
  public JAXBElement<ServiceEstraiPagamentiBeneficiariLimiteResponse> createServiceEstraiPagamentiBeneficiariLimiteResponse(
      ServiceEstraiPagamentiBeneficiariLimiteResponse value)
  {
    return new JAXBElement<ServiceEstraiPagamentiBeneficiariLimiteResponse>(
        _ServiceEstraiPagamentiBeneficiariLimiteResponse_QNAME,
        ServiceEstraiPagamentiBeneficiariLimiteResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceVisualizzaDebiti }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceVisualizzaDebiti")
  public JAXBElement<ServiceVisualizzaDebiti> createServiceVisualizzaDebiti(
      ServiceVisualizzaDebiti value)
  {
    return new JAXBElement<ServiceVisualizzaDebiti>(
        _ServiceVisualizzaDebiti_QNAME, ServiceVisualizzaDebiti.class, null,
        value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceVisualizzaDebitiResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceVisualizzaDebitiResponse")
  public JAXBElement<ServiceVisualizzaDebitiResponse> createServiceVisualizzaDebitiResponse(
      ServiceVisualizzaDebitiResponse value)
  {
    return new JAXBElement<ServiceVisualizzaDebitiResponse>(
        _ServiceVisualizzaDebitiResponse_QNAME,
        ServiceVisualizzaDebitiResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceScriviNuovoSoggettoRegistroAntimafia }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceScriviNuovoSoggettoRegistroAntimafia")
  public JAXBElement<ServiceScriviNuovoSoggettoRegistroAntimafia> createServiceScriviNuovoSoggettoRegistroAntimafia(
      ServiceScriviNuovoSoggettoRegistroAntimafia value)
  {
    return new JAXBElement<ServiceScriviNuovoSoggettoRegistroAntimafia>(
        _ServiceScriviNuovoSoggettoRegistroAntimafia_QNAME,
        ServiceScriviNuovoSoggettoRegistroAntimafia.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceScriviNuovoSoggettoRegistroAntimafiaResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceScriviNuovoSoggettoRegistroAntimafiaResponse")
  public JAXBElement<ServiceScriviNuovoSoggettoRegistroAntimafiaResponse> createServiceScriviNuovoSoggettoRegistroAntimafiaResponse(
      ServiceScriviNuovoSoggettoRegistroAntimafiaResponse value)
  {
    return new JAXBElement<ServiceScriviNuovoSoggettoRegistroAntimafiaResponse>(
        _ServiceScriviNuovoSoggettoRegistroAntimafiaResponse_QNAME,
        ServiceScriviNuovoSoggettoRegistroAntimafiaResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiPagamentiBeneficiari }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiPagamentiBeneficiari")
  public JAXBElement<ServiceEstraiPagamentiBeneficiari> createServiceEstraiPagamentiBeneficiari(
      ServiceEstraiPagamentiBeneficiari value)
  {
    return new JAXBElement<ServiceEstraiPagamentiBeneficiari>(
        _ServiceEstraiPagamentiBeneficiari_QNAME,
        ServiceEstraiPagamentiBeneficiari.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiPagamentiBeneficiariResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiPagamentiBeneficiariResponse")
  public JAXBElement<ServiceEstraiPagamentiBeneficiariResponse> createServiceEstraiPagamentiBeneficiariResponse(
      ServiceEstraiPagamentiBeneficiariResponse value)
  {
    return new JAXBElement<ServiceEstraiPagamentiBeneficiariResponse>(
        _ServiceEstraiPagamentiBeneficiariResponse_QNAME,
        ServiceEstraiPagamentiBeneficiariResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiDecreti }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiDecreti")
  public JAXBElement<ServiceEstraiDecreti> createServiceEstraiDecreti(
      ServiceEstraiDecreti value)
  {
    return new JAXBElement<ServiceEstraiDecreti>(_ServiceEstraiDecreti_QNAME,
        ServiceEstraiDecreti.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiDecretiResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiDecretiResponse")
  public JAXBElement<ServiceEstraiDecretiResponse> createServiceEstraiDecretiResponse(
      ServiceEstraiDecretiResponse value)
  {
    return new JAXBElement<ServiceEstraiDecretiResponse>(
        _ServiceEstraiDecretiResponse_QNAME, ServiceEstraiDecretiResponse.class,
        null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiPagamentiErogati }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiPagamentiErogati")
  public JAXBElement<ServiceEstraiPagamentiErogati> createServiceEstraiPagamentiErogati(
      ServiceEstraiPagamentiErogati value)
  {
    return new JAXBElement<ServiceEstraiPagamentiErogati>(
        _ServiceEstraiPagamentiErogati_QNAME,
        ServiceEstraiPagamentiErogati.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiPagamentiErogatiResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiPagamentiErogatiResponse")
  public JAXBElement<ServiceEstraiPagamentiErogatiResponse> createServiceEstraiPagamentiErogatiResponse(
      ServiceEstraiPagamentiErogatiResponse value)
  {
    return new JAXBElement<ServiceEstraiPagamentiErogatiResponse>(
        _ServiceEstraiPagamentiErogatiResponse_QNAME,
        ServiceEstraiPagamentiErogatiResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiFondi }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiFondi")
  public JAXBElement<ServiceEstraiFondi> createServiceEstraiFondi(
      ServiceEstraiFondi value)
  {
    return new JAXBElement<ServiceEstraiFondi>(_ServiceEstraiFondi_QNAME,
        ServiceEstraiFondi.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ServiceEstraiFondiResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "serviceEstraiFondiResponse")
  public JAXBElement<ServiceEstraiFondiResponse> createServiceEstraiFondiResponse(
      ServiceEstraiFondiResponse value)
  {
    return new JAXBElement<ServiceEstraiFondiResponse>(
        _ServiceEstraiFondiResponse_QNAME, ServiceEstraiFondiResponse.class,
        null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link IsAlive }
   * {@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "isAlive")
  public JAXBElement<IsAlive> createIsAlive(IsAlive value)
  {
    return new JAXBElement<IsAlive>(_IsAlive_QNAME, IsAlive.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link IsAliveResponse }
   * {@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://ws.business.sigop.csi.it/", name = "isAliveResponse")
  public JAXBElement<IsAliveResponse> createIsAliveResponse(
      IsAliveResponse value)
  {
    return new JAXBElement<IsAliveResponse>(_IsAliveResponse_QNAME,
        IsAliveResponse.class, null, value);
  }

}
