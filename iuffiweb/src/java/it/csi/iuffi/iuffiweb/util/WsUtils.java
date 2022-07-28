package it.csi.iuffi.iuffiweb.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import it.csi.modolxp.modolxppdfgensrv.business.session.facade.ModolPdfGeneratorSrvFacade;
import it.csi.modolxp.modolxppdfgensrv.client.ModolxppdfgensrvServiceClient;
import it.csi.modolxp.modolxppdfgensrv.dto.publish.ModolxppdfgensrvServiceDto;
import it.csi.modolxp.modolxpsrv.business.session.facade.ModolSrvFacade;
import it.csi.modolxp.modolxpsrv.client.ModolxpsrvServiceClient;
import it.csi.modolxp.modolxpsrv.dto.publish.ModolxpsrvServiceDto;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONElencoListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.IMessaggisticaWS;
import it.csi.iuffi.iuffiweb.integration.ws.papuaserv.messaggistica.Messaggistica;
import it.csi.iuffi.iuffiweb.integration.ws.smrcomms.smrcommsrvservice.SmrcommSrvService;
import it.csi.iuffi.iuffiweb.model.VerbaleDTO;
import it.csi.papua.papuaserv.dto.gestioneutenti.Intermediario;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.smrcomms.agripagopasrv.business.pagopa.PagoPAService;
import it.csi.smrcomms.agripagopasrv.business.pagopa.PagoPAWS;
import it.csi.smrcomms.siapcommws.dto.smrcomm.SiapCommWsDatiAutenticazioneVO;
import it.csi.smrcomms.siapcommws.dto.smrcomm.SiapCommWsDatiInvioMailPecVO;
import it.csi.smrcomms.siapcommws.dto.smrcomm.SiapCommWsDatiProtocolloVO;
import it.csi.smrcomms.siapcommws.dto.smrcomm.SiapCommWsDocumentoInputVO;
import it.csi.smrcomms.siapcommws.dto.smrcomm.SiapCommWsDocumentoVO;
import it.csi.smrcomms.siapcommws.dto.smrcomm.SiapCommWsMetadatoVO;
import it.csi.smrcomms.siapcommws.interfacews.smrcomm.SmrcommSrv;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellDocumentoVO;

public class WsUtils
{
  public static String      PAPUASERV_MESSAGGISTICA_WSDL = null;
  public static String      SIAPCOMM_WSDL                = null;
  public static String      AGRIPAGOPA_WSDL                = null;
  public static final int   SHORT_CONNECT_TIMEOUT        = 1000;
  public static final int   SHORT_REQUEST_TIMEOUT        = 5000;
  /* 10 secondi */
  public static final int   MAX_CONNECT_TIMEOUT          = 10 * 1000;
  /* 280 secondi */
  public static final int   MAX_REQUEST_TIMEOUT          = 280 * 1000;
  public static final int[] SHORT_TIMEOUT                = new int[]
  { SHORT_CONNECT_TIMEOUT, SHORT_REQUEST_TIMEOUT };
  public static final int[] MAX_TIMEOUT                  = new int[]
  { MAX_CONNECT_TIMEOUT, MAX_REQUEST_TIMEOUT };
  private static final String MODOL_ENDPOINT;
  private static final int    MODOL_PORT;
  private static final String CONTEXT_MODOLSRV             = "/modolxp/modolxpsrv/";
  private static final String CONTEXT_MODOLGENSRV          = "/modolxp/modolxppdfgen/";
  public static final String SIAPPAGOPA_PAGE_REFERRAL;
  public static final String SIAPPAGOPA_PAGE_URL_ANNULLA;
  public static final String SIAPPAGOPA_PAGE_REFERRAL_SPID;
  public static final String SIAPPAGOPA_PAGE_URL_ANNULLA_SPID;
  public static final String SIAPPAGOPA_PAGE_LINK;
  public static final String SIAPPAGOPA_PAGE_LINK_SPID;
  static
  {
    ResourceBundle res = ResourceBundle.getBundle("config");
    PAPUASERV_MESSAGGISTICA_WSDL = res
        .getString("papuaserv.messaggistica.wsdl.server");
    SIAPCOMM_WSDL = res.getString("siapcomm.wsdl");
    AGRIPAGOPA_WSDL = res.getString("agripagopa.wsdl");
    MODOL_ENDPOINT = res.getString("modolxp.endpoint").trim();
    MODOL_PORT = getIntFromResource(res, "modolxp.port", 80);
    SIAPPAGOPA_PAGE_REFERRAL = res.getString("siappagopa.pageReferral");
    SIAPPAGOPA_PAGE_URL_ANNULLA = res.getString("siappagopa.pageUrlAnnulla");
    SIAPPAGOPA_PAGE_REFERRAL_SPID = res.getString("siappagopa.pageSpidReferral");
    SIAPPAGOPA_PAGE_URL_ANNULLA_SPID = res.getString("siappagopa.pageSpidUrlAnnulla");
    SIAPPAGOPA_PAGE_LINK = res.getString("siappagopa.link");
    SIAPPAGOPA_PAGE_LINK_SPID = res.getString("siappagopa.spidLink");
  }
  
  private static int getIntFromResource(ResourceBundle res, String property, int defaultValue)
  {
    try
    {
      return new Integer(res.getString(property));
    }
    catch (Exception e)
    {
      return defaultValue;
    }
  }


  public void setTimeout(BindingProvider provider, int[] timeouts)
      throws MalformedURLException
  {
    Map<String, Object> requestContext = provider.getRequestContext();
    requestContext.put("javax.xml.ws.client.connectionTimeout", timeouts[0]);
    requestContext.put("javax.xml.ws.client.receiveTimeout", timeouts[1]);
  }

  public IMessaggisticaWS getMessaggistica() throws MalformedURLException
  {
    return getMessaggisticaWithTimeout(MAX_TIMEOUT);
  }

  public IMessaggisticaWS getMessaggisticaWithTimeout(int timeouts[])
      throws MalformedURLException
  {
    final IMessaggisticaWS messaggisticaPort = new Messaggistica(
        new URL(PAPUASERV_MESSAGGISTICA_WSDL),
        new QName(
            "http://papuaserv.webservice.business.papuaserv.papua.csi.it/",
            "messaggistica")).getMessaggisticaPort();
    if (timeouts != null)
    {
      setTimeout((BindingProvider) messaggisticaPort, timeouts);
    }
    return messaggisticaPort;
  }

  public SmrcommSrv getSiapComm() throws MalformedURLException
  {
    final SmrcommSrv smrcomm = new SmrcommSrvService(new URL(SIAPCOMM_WSDL))
        .getSmrcommSrvPort();
    Client client = ClientProxy.getClient(smrcomm);
    client.getRequestContext().put(Message.ENDPOINT_ADDRESS, SIAPCOMM_WSDL);
    return smrcomm;
  }

  public SiapCommWsDocumentoInputVO convertAgriWellVOToSiapComm(
      String user,
      String password,
      AgriWellDocumentoVO agriWellDocumentoVO,
      SiapCommWsDatiProtocolloVO datiProtocollo,
      SiapCommWsDatiInvioMailPecVO datiPEC,
      boolean flagInvioPEC,
      boolean flagProtocolla,
      boolean flagTimbroProtocollo,
      boolean flagEreditaProtocollo,
      boolean isUpdate,
      boolean isDocumentoPrincipale,
      Long idDocumentoIndexPrincipale,
      long idUtenteAggiornamento,
      long idProcedimento)
  {
    SiapCommWsDatiAutenticazioneVO datiAutenticazione = new SiapCommWsDatiAutenticazioneVO();
    SiapCommWsDocumentoInputVO inputVO = new SiapCommWsDocumentoInputVO();
    SiapCommWsDocumentoVO documentoVO = new SiapCommWsDocumentoVO();

    datiAutenticazione.setUsername(user);
    datiAutenticazione.setPassword(password);

    documentoVO.setAnno(agriWellDocumentoVO.getAnno());
    documentoVO.setDaFirmare(agriWellDocumentoVO.getDaFirmare());
    documentoVO.setDataInserimento(IuffiUtils.DATE
        .toXMLGregorianCalendar(agriWellDocumentoVO.getDataInserimento()));
    /* DA CAPIRE COME VALORIZZARE */
    documentoVO.setDataRepertorio(null);
    documentoVO.setNumeroRepertorio(null);
    /* ---------------------------- */
    documentoVO.setEstensione(agriWellDocumentoVO.getEstensione());
    documentoVO
        .setFlagCartellaDaCreare(agriWellDocumentoVO.getFlagCartellaDaCreare());
    documentoVO.setIdAzienda(agriWellDocumentoVO.getExtIdAzienda());
    documentoVO.setIdentificativo(agriWellDocumentoVO.getIdentificativo());
    documentoVO.setIdIntermediario(agriWellDocumentoVO.getExtIdIntermediario());
    documentoVO.setIdLivello(agriWellDocumentoVO.getIdLivello());
    documentoVO.setIdTipoDocumento(agriWellDocumentoVO.getIdTipoDocumento());
    documentoVO.setNomeCartella(agriWellDocumentoVO.getNomeCartella());
    documentoVO.setNomeFile(agriWellDocumentoVO.getNomeFile());
    documentoVO.setNoteDocumento(agriWellDocumentoVO.getNoteDocumento());
    documentoVO.setNumeroDomanda(agriWellDocumentoVO.getNumeroDomanda());
    documentoVO.setCodiceVisibilitaDoc("T");

    inputVO.setDatiAutenticazione(datiAutenticazione);
    inputVO.setIdProcedimento(Integer
        .parseInt(String.valueOf(agriWellDocumentoVO.getIdProcedimento())));
    inputVO.setDatiInvioPec(datiPEC);
    inputVO.setDatiProtocollo(datiProtocollo);
    inputVO.setFlagEreditaProtocollo((flagEreditaProtocollo)
        ? IuffiConstants.FLAGS.SI : IuffiConstants.FLAGS.NO);
    inputVO.setFlagInvioPec((flagInvioPEC) ? IuffiConstants.FLAGS.SI
        : IuffiConstants.FLAGS.NO);
    inputVO.setFlagProtocolla((flagProtocolla) ? IuffiConstants.FLAGS.SI
        : IuffiConstants.FLAGS.NO);
    inputVO.setFlagTimbroProtocollo((flagTimbroProtocollo)
        ? IuffiConstants.FLAGS.SI : IuffiConstants.FLAGS.NO);
    inputVO.setIdDocumentoIndex(agriWellDocumentoVO.getIdDocumentoIndex());
    inputVO.setIdDocumentoIndexPadre(idDocumentoIndexPrincipale);
    inputVO.setIdUtenteAggiornamento(
        agriWellDocumentoVO.getIdUtenteAggiornamento());
    inputVO.setNuovoDocumento(documentoVO);
    inputVO.setTipoArchiviazione((isUpdate) ? "U" : "N");
    inputVO.setTipoDocumento((isDocumentoPrincipale) ? "P" : "A");
    inputVO.setIdUtenteAggiornamento(idUtenteAggiornamento);
    inputVO.setIdentificativoPraticaOrigine(String.valueOf(idProcedimento));
    inputVO.setFlagProtocolloEsterno("N");

    return inputVO;
  }

  public SiapCommWsDocumentoInputVO getSiapCommDocumentoVOPerArchiviazioneListeLiquidazione(
      RigaJSONElencoListaLiquidazioneDTO lista,
      Map<String, String> mapParametri,
      String fileName,
      String emailAmmCompetenza,
      long idUtenteAggiornamento,
      int idProcedimentoAgricolo)
  {
    SiapCommWsDatiAutenticazioneVO datiAutenticazione = new SiapCommWsDatiAutenticazioneVO();
    SiapCommWsDocumentoInputVO inputVO = new SiapCommWsDocumentoInputVO();
    SiapCommWsDocumentoVO documentoVO = new SiapCommWsDocumentoVO();
    String paramUsr = mapParametri
        .get(IuffiConstants.PARAMETRO.DOQUIAGRI_USR_PSW_SC);
    String user = paramUsr.split("#")[0];
    String password = paramUsr.split("#")[1];
//USER E PASSWORD POSSIAMO MANDARLO VUOTO
    datiAutenticazione.setUsername(user);
    datiAutenticazione.setPassword(password);

    documentoVO.setAnno(lista.getAnnoDataCreazione()); //DA CHIEDERE
    documentoVO.setDaFirmare(null);
    documentoVO.setDataInserimento(
        IuffiUtils.DATE.toXMLGregorianCalendar(new Date()));
    /* DA CAPIRE COME VALORIZZARE */
    documentoVO.setDataRepertorio(null);
    documentoVO.setNumeroRepertorio(null);
    /* ---------------------------- */
    final String fileExtension = IuffiUtils.FILE.getFileExtension(fileName,
        true);
    documentoVO.setEstensione(fileExtension); //(VA INDICATO CHE E' PDF)
    documentoVO.setFlagCartellaDaCreare(IuffiConstants.FLAGS.NO);  //DA CAPIRE CON ENTICO
    documentoVO.setIdAzienda(null); //DA CAPIRE CON ENRICO
    documentoVO.setIdentificativo(null);
    documentoVO.setIdIntermediario(null);
    documentoVO.setIdLivello(null);

    long idTipoDocumento = IuffiUtils.NUMBERS.getNumericValue(
        mapParametri
            .get(
                IuffiConstants.PARAMETRO.DOQUIAGRI_ID_TIPO_DOCUMENTO_LISTA_LIQUIDAZIONE)
            .trim());
    documentoVO.setIdTipoDocumento(idTipoDocumento);
    documentoVO.setNomeCartella(
        mapParametri.get(IuffiConstants.PARAMETRO.DOQUIAGRI_CARTELLA));
    documentoVO.setNomeFile(fileName);
    documentoVO.setNoteDocumento(null);
    documentoVO.setNumeroDomanda(null);
    documentoVO.setCodiceVisibilitaDoc(
        IuffiConstants.AGRIWELL.ARCHIVIAZIONE.VISIBILITA.PA);
    addMetadatiListaLiquidazione(documentoVO, mapParametri, lista);

    inputVO.setDatiAutenticazione(datiAutenticazione);
    inputVO.setIdProcedimento(idProcedimentoAgricolo);
    inputVO.setFlagEreditaProtocollo(IuffiConstants.FLAGS.NO);
    inputVO.setFlagInvioPec(IuffiConstants.FLAGS.NO);
    inputVO.setFlagProtocolla(IuffiConstants.FLAGS.SI);
    inputVO.setFlagTimbroProtocollo(
        ("pdf".equalsIgnoreCase(fileExtension)) ? IuffiConstants.FLAGS.SI
            : IuffiConstants.FLAGS.NO);
    inputVO.setIdDocumentoIndex(null);
    inputVO.setIdDocumentoIndexPadre(null);
    inputVO.setNuovoDocumento(documentoVO);
    inputVO.setTipoArchiviazione("N");
    inputVO.setTipoDocumento("P");
    inputVO.setIdUtenteAggiornamento(idUtenteAggiornamento);
    inputVO.setFlagProtocolloEsterno("N");
    SiapCommWsDatiProtocolloVO datiProtocollo = new SiapCommWsDatiProtocolloVO();
    datiProtocollo.setTipoProtocollo(
        IuffiConstants.SIAPCOMMWS.DATI_PROTOCOLLO.TIPO_PROTOCOLLO.USCITA);
    datiProtocollo.setEmailMittenteDestinatario(emailAmmCompetenza);
    datiProtocollo
        .setDenominazioneMittenteDestinatario(lista.getOrganismoDelegato());
    inputVO.setDatiProtocollo(datiProtocollo);

    return inputVO;
  }

  public void addMetadatiListaLiquidazione(SiapCommWsDocumentoVO documentoVO,
      Map<String, String> mapParametri,
      RigaJSONElencoListaLiquidazioneDTO lista)
  {
    // Metadati
    List<SiapCommWsMetadatoVO> metadati = new ArrayList<SiapCommWsMetadatoVO>();
    // METADATI DA PASSARE SEMPRE - INIZIO
    SiapCommWsMetadatoVO metadato = new SiapCommWsMetadatoVO();

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("classificazione_regione");
    metadato.setValoreElemento(mapParametri.get(
        IuffiConstants.PARAMETRO.DOQUIAGRI_CLASS_REG_LISTE_LIQUIDAZIONE));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("fascicolazione");
    metadato.setValoreElemento(mapParametri.get(
        IuffiConstants.PARAMETRO.DOQUIAGRI_FASCICOLA_LISTE_LIQUIDAZIONE));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("data_lista");
    metadato.setValoreElemento(lista.getDataCreazione());
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("numero_lista");
    metadato.setValoreElemento(String.valueOf(lista.getNumeroLista()));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("id_bando");
    metadato.setValoreElemento(String.valueOf(lista.getIdBando()));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("id_amministrazione_competenza");
    metadato.setValoreElemento(String.valueOf(lista.getExtIdAmmCompetenza()));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("id_tipo_importo");
    metadato.setValoreElemento(String.valueOf(lista.getIdIdTipoImporto()));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("id_tecnico_liquidatore");
    metadato
        .setValoreElemento(String.valueOf(lista.getExtIdTecnicoLiquidatore()));
    metadati.add(metadato);

    if (documentoVO.getMetadati() != null)
    {
      documentoVO.getMetadati().addAll(metadati);
    }
    else
    {
      documentoVO.setMetadati(metadati);
    }
  }

  public void addMetadatiCoordinateProtocolloListaLiquidazione(
      SiapCommWsDocumentoVO documentoVO, Map<String, String> mapParametri)
  {
    // Metadati
    List<SiapCommWsMetadatoVO> metadati = new ArrayList<SiapCommWsMetadatoVO>();
    SiapCommWsMetadatoVO metadato = new SiapCommWsMetadatoVO();

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("numeroPaginaProtocollo");
    metadato.setValoreElemento("1");
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("coordinataXsinistraProtocollo");
    metadato.setValoreElemento(mapParametri.get(
        IuffiConstants.PARAMETRO.DOQUIAGRI_LISTE_LIQUIDAZIONE_X_SINISTRA));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("coordinataXdestraProtocollo");
    metadato.setValoreElemento(mapParametri.get(
        IuffiConstants.PARAMETRO.DOQUIAGRI_LISTE_LIQUIDAZIONE_X_DESTRA));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("coordinataYbassoProtocollo");
    metadato.setValoreElemento(mapParametri
        .get(IuffiConstants.PARAMETRO.DOQUIAGRI_LISTE_LIQUIDAZIONE_Y_BASSO));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("coordinataYaltoProtocollo");
    metadato.setValoreElemento(mapParametri
        .get(IuffiConstants.PARAMETRO.DOQUIAGRI_LISTE_LIQUIDAZIONE_Y_ALTO));
    metadati.add(metadato);

    if (documentoVO.getMetadati() != null)
    {
      documentoVO.getMetadati().addAll(metadati);
    }
    else
    {
      documentoVO.setMetadati(metadati);
    }
  }

  public void addMetadati(SiapCommWsDocumentoVO documentoVO,
      List<SiapCommWsMetadatoVO> metadatiAggiuntivi,
      Date dataLimiteFirma, String originalFileName, String cuaa,
      UtenteAbilitazioni utenteAbilitazioni,
      Map<String, String> mapParametri, String descrizioneBando, long numeroBandoDoc, String identificativo)
  {
    String fileName = IuffiUtils.FILE.getFileName(originalFileName);
    // Metadati
    List<SiapCommWsMetadatoVO> metadati = new ArrayList<SiapCommWsMetadatoVO>();
    // METADATI DA PASSARE SEMPRE - INIZIO
    SiapCommWsMetadatoVO metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("cuaa");
    metadato.setValoreElemento(cuaa);
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("data_inizio");
    metadato.setValoreElemento(IuffiUtils.DATE.formatDateTime(new Date()));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("descrizioneBando");
    metadato.setValoreElemento(descrizioneBando);
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("numeroBandoDoc");
    metadato.setValoreElemento(String.valueOf(numeroBandoDoc));
    metadati.add(metadato);
    
    if(identificativo != null)
    {
    	metadato = new SiapCommWsMetadatoVO();
    	metadato.setNomeEtichetta("GruppoIdentificativo");
    	metadato.setValoreElemento(identificativo);
    	metadati.add(metadato);
    }

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("id_tipo_visibilita");
    metadato.setValoreElemento("1");
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("codice_ente_utente");
    metadato.setValoreElemento(
        IuffiUtils.PAPUASERV.extractInfoEnteBaseFromEnteLogin(
            utenteAbilitazioni.getEnteAppartenenza()).getCodiceEnte());
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("titolo_documento");
    metadato.setValoreElemento(fileName.replaceAll("\\s", "_"));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("classificazione_regione");
    metadato.setValoreElemento(
        mapParametri.get(IuffiConstants.PARAMETRO.DOQUIAGRI_CLASS_REG));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("fascicolazione");
    metadato.setValoreElemento(
        mapParametri.get(IuffiConstants.PARAMETRO.DOQUIAGRI_FASCICOLA));
    metadati.add(metadato);

    if (dataLimiteFirma != null)
    {
      metadato = new SiapCommWsMetadatoVO();
      metadato.setNomeEtichetta("data_limite_firma");
      metadato.setValoreElemento(
          IuffiUtils.DATE.formatDateTime(dataLimiteFirma));
      metadati.add(metadato);
    }

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("documento_firmato");
    metadato.setValoreElemento("FALSE");
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("documento_verificato");
    metadato.setValoreElemento("FALSE");
    metadati.add(metadato);
    if (metadatiAggiuntivi != null && !metadatiAggiuntivi.isEmpty())
    {
      metadati.addAll(metadatiAggiuntivi);
    }
    if (documentoVO.getMetadati() != null)
    {
      documentoVO.getMetadati().addAll(metadati);
    }
    else
    {
      documentoVO.setMetadati(metadati);
    }
  }

  public SiapCommWsDocumentoInputVO prepareInputSiapComm(
      String user,
      String password,
      SiapCommWsDatiProtocolloVO datiProtocollo,
      SiapCommWsDatiInvioMailPecVO datiPEC,
      boolean flagInvioPEC,
      boolean flagProtocolla,
      boolean flagTimbroProtocollo,
      boolean flagEreditaProtocollo,
      boolean isUpdate,
      Long idDocumentoIndex,
      boolean isDocumentoPrincipale,
      Long idDocumentoIndexPrincipale,
      long idUtenteAggiornamento,
      long idProcedimento, int idProcedimentoAgricoltura)
  {
    SiapCommWsDatiAutenticazioneVO datiAutenticazione = new SiapCommWsDatiAutenticazioneVO();
    SiapCommWsDocumentoInputVO inputVO = new SiapCommWsDocumentoInputVO();
    SiapCommWsDocumentoVO documentoVO = new SiapCommWsDocumentoVO();

    datiAutenticazione.setUsername(user);
    datiAutenticazione.setPassword(password);

    /* DA CAPIRE COME VALORIZZARE */
    documentoVO.setDataRepertorio(null);
    documentoVO.setNumeroRepertorio(null);
    /* ---------------------------- */

    inputVO.setDatiAutenticazione(datiAutenticazione);
    inputVO.setIdProcedimento(
        Integer
            .parseInt(String.valueOf((long) idProcedimentoAgricoltura)));
    inputVO.setDatiInvioPec(datiPEC);
    inputVO.setDatiProtocollo(datiProtocollo);
    inputVO.setFlagEreditaProtocollo((flagEreditaProtocollo)
        ? IuffiConstants.FLAGS.SI : IuffiConstants.FLAGS.NO);
    inputVO.setFlagInvioPec((flagInvioPEC) ? IuffiConstants.FLAGS.SI
        : IuffiConstants.FLAGS.NO);
    inputVO.setFlagProtocolla((flagProtocolla) ? IuffiConstants.FLAGS.SI
        : IuffiConstants.FLAGS.NO);
    inputVO.setFlagTimbroProtocollo((flagTimbroProtocollo)
        ? IuffiConstants.FLAGS.SI : IuffiConstants.FLAGS.NO);
    inputVO.setIdDocumentoIndexPadre(idDocumentoIndexPrincipale);
    inputVO.setIdDocumentoIndex(idDocumentoIndex);
    inputVO.setNuovoDocumento(documentoVO);
    inputVO.setTipoArchiviazione((isUpdate) ? "U" : "N");
    inputVO.setTipoDocumento((isDocumentoPrincipale) ? "P" : "A");
    inputVO.setIdUtenteAggiornamento(idUtenteAggiornamento);
    inputVO.setIdentificativoPraticaOrigine(String.valueOf(idProcedimento));
    inputVO.setFlagProtocolloEsterno("N");
    return inputVO;
  }

  public SiapCommWsDocumentoVO getSiapCommWsDocumentoVO(Long idAzienda,
      String identificativo, long idTipoDocumento,
      Map<String, String> mapParametri, Integer annoCampagna,
      Date dataInizioBando, String originalFileName,
      UtenteAbilitazioni utenteAbilitazioni, String flagFirmaGrafometrica)
  {
    SiapCommWsDocumentoVO documentoInputVO = new SiapCommWsDocumentoVO();
    Integer anno = annoCampagna;
    if (anno == null)
    {
      anno = IuffiUtils.DATE.getYearFromDate(dataInizioBando);
    }
    String fileName = IuffiUtils.FILE.getFileName(originalFileName);
    documentoInputVO.setAnno(anno);
    documentoInputVO.setDataInserimento(
        IuffiUtils.DATE.toXMLGregorianCalendar(new Date()));
    documentoInputVO
        .setEstensione(IuffiUtils.FILE.getOnlyFileExtension(fileName));
    documentoInputVO.setDaFirmare(flagFirmaGrafometrica);
    if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
    {
      Intermediario intermediario = utenteAbilitazioni.getEnteAppartenenza()
          .getIntermediario();
      documentoInputVO.setIdIntermediario(intermediario.getIdIntermediario());
    }
    documentoInputVO.setIdAzienda(idAzienda);
    documentoInputVO.setIdTipoDocumento(idTipoDocumento);
    documentoInputVO.setNomeCartella(
        mapParametri.get(IuffiConstants.PARAMETRO.DOQUIAGRI_CARTELLA));
    documentoInputVO.setNomeFile(fileName);
    documentoInputVO.setIdentificativo(identificativo);
    documentoInputVO.setFlagCartellaDaCreare(IuffiConstants.FLAGS.SI);
    documentoInputVO.setCodiceVisibilitaDoc("T");
    return documentoInputVO;
  }

  public ModolSrvFacade getModolServClient() throws Exception
  {

    ModolxpsrvServiceDto modolxpsrvService = new ModolxpsrvServiceDto();

    modolxpsrvService.setServer(MODOL_ENDPOINT);
    modolxpsrvService.setContext(CONTEXT_MODOLSRV);
    modolxpsrvService.setPort(MODOL_PORT);
    ModolSrvFacade modolxpsrv = ModolxpsrvServiceClient.getModolxpsrvService(modolxpsrvService);

    return modolxpsrv;
  }

  public ModolPdfGeneratorSrvFacade getModolPDFGenServClient() throws Exception
  {
    ModolxppdfgensrvServiceDto modolxpsrvService = new ModolxppdfgensrvServiceDto();
    modolxpsrvService.setServer(MODOL_ENDPOINT);
    modolxpsrvService.setContext(CONTEXT_MODOLGENSRV);
    modolxpsrvService.setPort(MODOL_PORT);
    ModolPdfGeneratorSrvFacade modolxpsrv = ModolxppdfgensrvServiceClient.getModolxppdfgensrvService(modolxpsrvService);

    return modolxpsrv;
  }

  public String getModolXPServClientURL()
  {
    return MODOL_ENDPOINT + ":" + MODOL_PORT + CONTEXT_MODOLSRV;
  }

  public String getModolXPPDFGenServClientURL()
  {
    return MODOL_ENDPOINT + ":" + MODOL_PORT +  CONTEXT_MODOLGENSRV;
  }
  
  public static PagoPAWS getPagoPAWS() throws Exception {
    final String THIS_METHOD = "[::getPagoPAWS]";
    PagoPAWS pagoPAWS = null;
    try {
        URI wsdlURI = new URI(AGRIPAGOPA_WSDL);
        URL wsdlURL = wsdlURI.toURL();
        QName SERVICE_NAME = new QName("http://it/csi/smrcomms/agripagopasrv/business/pagopa", "PagoPAService");
        PagoPAService service = new PagoPAService(wsdlURL, SERVICE_NAME);
        // Recupero lo stub
        pagoPAWS = service.getPagoPAWSPort();
        BindingProvider bp = (BindingProvider) pagoPAWS;
        String url = AGRIPAGOPA_WSDL.substring(0,AGRIPAGOPA_WSDL.indexOf("?"));
        java.util.Map<String, Object> context = bp.getRequestContext();
        context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        org.apache.cxf.endpoint.Client client = ClientProxy.getClient(pagoPAWS);
        HTTPConduit conduit = (HTTPConduit) client.getConduit();
        HTTPClientPolicy policy = conduit.getClient();
        policy.setConnectionTimeout(120000);
        policy.setReceiveTimeout(120000);
    }catch(Exception e){
       // CuneoLogger.error("/controller/verificaPagamento.jsp", "Exception during the invocation of getPagoPAWS method in AgrichimBO /controller/verificaPagamento.jsp "+e);
        throw e;
    }finally{
        return pagoPAWS;
    }
}
  
  
  public SiapCommWsDocumentoInputVO getSiapCommDocumentoVOPerArchiviazioneVerbali(VerbaleDTO verbale, String fileName, long idUtente)
  {
    SiapCommWsDatiAutenticazioneVO datiAutenticazione = new SiapCommWsDatiAutenticazioneVO();
    SiapCommWsDocumentoInputVO inputVO = new SiapCommWsDocumentoInputVO();
    SiapCommWsDocumentoVO documentoVO = new SiapCommWsDocumentoVO();

    documentoVO.setAnno(2021); //Mettere data odierna
    documentoVO.setDaFirmare("N");
    documentoVO.setDataInserimento(
        IuffiUtils.DATE.toXMLGregorianCalendar(new Date()));
    /* DA CAPIRE COME VALORIZZARE */
    documentoVO.setDataRepertorio(null);
    documentoVO.setNumeroRepertorio(null);
    /* ---------------------------- */
    final String fileExtension = IuffiUtils.FILE.getFileExtension(fileName,
        true);
    documentoVO.setEstensione(fileExtension); //(VA INDICATO CHE E' PDF, dovrebbe essere a posto così
    documentoVO.setFlagCartellaDaCreare(IuffiConstants.FLAGS.NO);  //Dovrebbe essere a posto così
    documentoVO.setIdAzienda(null); //Dovrebbe essere a posto così
    documentoVO.setIdentificativo(null); //Dovrebbe essere a posto così
    documentoVO.setIdIntermediario(null); //Dovrebbe essere a posto così
    documentoVO.setIdLivello(null);

//    long idTipoDocumento = IuffiUtils.NUMBERS.getNumericValue(
//        mapParametri
//            .get(
//                IuffiConstants.PARAMETRO.DOQUIAGRI_ID_TIPO_DOCUMENTO_LISTA_LIQUIDAZIONE)
//            .trim());
    documentoVO.setIdTipoDocumento(6300); //Metterlo in un parametro, vedere se serve la cosa fatta sopra
    documentoVO.setNomeCartella("DOCUMENTALE SIAP"); //Metterlo in un parametro
    documentoVO.setNomeFile(fileName); //E' il nome del file del nostro verbale
    documentoVO.setNoteDocumento(null);
    documentoVO.setNumeroDomanda(null);
    documentoVO.setCodiceVisibilitaDoc(
        IuffiConstants.AGRIWELL.ARCHIVIAZIONE.VISIBILITA.TUTTI);
    addMetadatiVerbali(documentoVO, verbale);

    inputVO.setDatiAutenticazione(datiAutenticazione);
    inputVO.setIdProcedimento(IuffiConstants.TIPO_PROCEDIMENTO_AGRICOLO.IUFFI); 
    inputVO.setFlagEreditaProtocollo(IuffiConstants.FLAGS.NO);
    inputVO.setFlagInvioPec(IuffiConstants.FLAGS.NO);
    inputVO.setFlagProtocolla(IuffiConstants.FLAGS.NO);
    inputVO.setFlagTimbroProtocollo(
        IuffiConstants.FLAGS.NO);
    inputVO.setIdDocumentoIndex(null);
    inputVO.setIdDocumentoIndexPadre(null);
    inputVO.setNuovoDocumento(documentoVO);
    inputVO.setTipoArchiviazione("N");
    inputVO.setTipoDocumento("P");
    

    inputVO.setIdUtenteAggiornamento(idUtente); //REcuperarlo da utente abilitazioni
    inputVO.setFlagProtocolloEsterno("N");
    SiapCommWsDatiProtocolloVO datiProtocollo = new SiapCommWsDatiProtocolloVO();


    return inputVO;
  }
  
  public void addMetadatiVerbali(SiapCommWsDocumentoVO documentoVO,
      VerbaleDTO verbale)
  {
    // Metadati
    List<SiapCommWsMetadatoVO> metadati = new ArrayList<SiapCommWsMetadatoVO>();
    // METADATI DA PASSARE SEMPRE - INIZIO
    SiapCommWsMetadatoVO metadato = new SiapCommWsMetadatoVO();

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("classificazione_regione");
    metadato.setValoreElemento("7.190.10");
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("fascicolazione");
    metadato.setValoreElemento("fa"); //METTERLO COME PARAMETRO
    metadati.add(metadato);


    if (documentoVO.getMetadati() != null)
    {
      documentoVO.getMetadati().addAll(metadati);
    }
    else
    {
      documentoVO.setMetadati(metadati);
    }
  }
}
