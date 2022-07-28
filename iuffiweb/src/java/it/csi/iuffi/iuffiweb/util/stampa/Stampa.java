package it.csi.iuffi.iuffiweb.util.stampa;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.modolxp.modolxppdfgensrv.dto.pdfstatic.PdfStaticInputRequest;
import it.csi.modolxp.modolxpsrv.dto.Applicazione;
import it.csi.modolxp.modolxpsrv.dto.Modello;
import it.csi.modolxp.modolxpsrv.dto.Modulo;
import it.csi.modolxp.modolxpsrv.dto.RendererModality;
import it.csi.modolxp.modolxpsrv.dto.RiferimentoAdobe;
import it.csi.modolxp.modolxpsrv.dto.XmlModel;
import it.csi.iuffi.iuffiweb.business.IListeLiquidazioneEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONElencoListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.AmmissioneFinanziamentoPremio_SezioniTesto;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.AmmissioneFinanziamento_SezioniTesto;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.FirmaLetteraAmmissioneFinanziamento;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.FirmaLetteraAmmissionePremio;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.FirmaListaLiquidazione;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.FirmaVerbaleAmmissioneFinanziamento;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.Fragment;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.Global;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.Header;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.HeaderLetteraAmmissioneFinanziamento;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.ListaLiquidazione;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroAccertamentoAcconto;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroAllegati;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroAttestazioneCAA;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroCanaliVendita;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroCaratteristicheAziendali;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroColtureAziendali;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroConsistenzaAziendale;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroContiCorrenti;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroDanni;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroDanniFauna;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroDataFineLavori;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroDatiAnticipo;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroDatiBilancio;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroDatiIdentificativi;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroDatiIdentificativiDanno;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroDichiarazioni;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroDinamicoElenco;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroDocumentiRichiesti;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroEconomico;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroImpegni;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroIntegrazione;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroInterventiDanni;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroInterventiInfrastrutture;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroInterventiLetteraAmmissioneFinanziamento;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroPLVVegetale;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroPLVZootecnica;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroPianoCinghiali;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroPianoSelettivoCamoscio;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroPianoSelettivoCapriolo;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroPianoSelettivoCervo;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroPianoSelettivoDaino;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroPianoSelettivoMuflone;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroPremio;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroProduzioniCertificate;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroRegimeAiuto;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroRendicontazioneSpese;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroScorte;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroSpecieOGUR;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroTrasformazioneProdotti;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.QuadroVoltura;
import it.csi.iuffi.iuffiweb.util.stampa.fragment.TitoliListaLiquidazione;

public abstract class Stampa
{
  public static final String                BASE_RIF_ADOBE = "Applications/Iuffi/1.0/Forms/";
  public static final Map<String, Fragment> MAP_FRAGMENTS = new HashMap<String, Fragment>();
  static
  {
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.PREMIO,
        new QuadroPremio());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.CARATTERISTICHE_AZIENDALI,
        new QuadroCaratteristicheAziendali());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.CONSISTENZA_AZIENDALE,
        new QuadroConsistenzaAziendale());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.TRASFORMAZIONE_PRODOTTI,
        new QuadroTrasformazioneProdotti());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.PRODUZIONI_CERTIFICATE,
        new QuadroProduzioniCertificate());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.CANALI_VENDITA,
        new QuadroCanaliVendita());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.REGIME_AIUTO,
        new QuadroRegimeAiuto());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.DATI_IDENTIFICATIVI,
        new QuadroDatiIdentificativi());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.DICHIARAZIONI,
        new QuadroDichiarazioni());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.IMPEGNI,
        new QuadroImpegni());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.ALLEGATI,
        new QuadroAllegati());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.CONTI_CORRENTI,
        new QuadroContiCorrenti());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.INTERVENTI_INFRASTRUTTURE,
        new QuadroInterventiDanni());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.DATA_FINE_LAVORI,
        new QuadroDataFineLavori());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.VOLTURA,
        new QuadroVoltura());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.OGUR,
        new QuadroSpecieOGUR());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.QUADRO_DINAMICO_MODELLO_B1,
        new QuadroDinamicoElenco(IuffiConstants.QUADRO.CODICE.QUADRO_DINAMICO_MODELLO_B1));

    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.QUADRO_DINAMICO_MODELLO_C1,
        new QuadroDinamicoElenco(IuffiConstants.QUADRO.CODICE.QUADRO_DINAMICO_MODELLO_C1));

    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.DATI_ANTICIPO,
        new QuadroDatiAnticipo());
    final QuadroRendicontazioneSpese quadroRendicontazioneSpese = new QuadroRendicontazioneSpese();
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.RENDICONTAZIONE_SPESE,
        quadroRendicontazioneSpese);
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.RENDICONTAZIONE_SALDO,
        quadroRendicontazioneSpese);
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.QUADRO_ECONOMICO,
        new QuadroEconomico());

    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.ACCERTAMENTO_ACCONTO,
        new QuadroAccertamentoAcconto());

    // Liste liquidazione
    MAP_FRAGMENTS.put("FIRMA_LISTA_LIQUIDAZIONE", new FirmaListaLiquidazione());
    MAP_FRAGMENTS.put("TITOLI_LISTA_LIQUIDAZIONE",
        new TitoliListaLiquidazione());
    MAP_FRAGMENTS.put("LISTA_LIQUIDAZIONE", new ListaLiquidazione());

    MAP_FRAGMENTS.put("QUADRO_ATTESTAZIONE_CAA", new QuadroAttestazioneCAA());

    MAP_FRAGMENTS.put("HEADER", new Header());
    MAP_FRAGMENTS.put("GLOBAL", new Global());
    MAP_FRAGMENTS.put("AMMISSIONE_FINANZIAMENTO_SEZIONI_TESTO",
        new AmmissioneFinanziamento_SezioniTesto());
    MAP_FRAGMENTS.put("AMMISSIONE_FINANZIAMENTO_INTERVENTI",
        new QuadroInterventiLetteraAmmissioneFinanziamento());
    MAP_FRAGMENTS.put("HEADER_AMMISSIONE_FINANZIAMENTO",
        new HeaderLetteraAmmissioneFinanziamento());
    MAP_FRAGMENTS.put("FIRMA_AMMISSIONE_FINANZIAMENTO",
        new FirmaLetteraAmmissioneFinanziamento());
    MAP_FRAGMENTS.put("FIRMA_VERBALE_AMMISSIONE_FINANZIAMENTO",
        new FirmaVerbaleAmmissioneFinanziamento());

    // Misure a premio
    MAP_FRAGMENTS.put("MISURE_PREMIO_SEZIONI_TESTO",
        new AmmissioneFinanziamentoPremio_SezioniTesto());
    MAP_FRAGMENTS.put("FIRMA_AMMISSIONE_FINANZIAMENTO_PREMIO",
        new FirmaLetteraAmmissionePremio());
    MAP_FRAGMENTS.put("QUADRO_INTEGRAZIONE", new QuadroIntegrazione());

    // Quadri Iuffi
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.SUPERFICIE_COLTURA,
        new QuadroPLVVegetale());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.ALLEVAMENTI,
        new QuadroPLVZootecnica());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.SCORTE, new QuadroScorte());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.DANNI, new QuadroDanni());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.INTERVENTI,
        new QuadroInterventiDanni());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.COLTURE_AZIENDALI,
        new QuadroColtureAziendali());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.INTERVENTI_INFRASTRUTTURE,
        new QuadroInterventiInfrastrutture());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.DOCUMENTI_RICHIESTI,
        new QuadroDocumentiRichiesti());

    // Quadri Danfa
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.DATI_IDENTIFICATIVI_DANNO,
        new QuadroDatiIdentificativiDanno());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.DANNI_FAUNA,
        new QuadroDanniFauna());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.PIANO_SELETTIVO_CINGHIALI,
        new QuadroPianoCinghiali());
    
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.DATI_BILANCIO,
        new QuadroDatiBilancio());
    
    // PIANI SELETTIVI
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.PIANI_SELETTIVI.CAMOSCIO,
        new QuadroPianoSelettivoCamoscio());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.PIANI_SELETTIVI.MUFLONE,
        new QuadroPianoSelettivoMuflone());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.PIANI_SELETTIVI.CERVO,
        new QuadroPianoSelettivoCervo());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.PIANI_SELETTIVI.CAPRIOLO,
        new QuadroPianoSelettivoCapriolo());
    MAP_FRAGMENTS.put(IuffiConstants.QUADRO.CODICE.PIANI_SELETTIVI.DAINO,
        new QuadroPianoSelettivoDaino());

  }

  public static final String DEFAULT_ENCODING = "UTF-8";

  public abstract byte[] genera(long id, String cuName) throws Exception;

  protected XMLStreamWriter getXMLStreamWriter(
      ByteArrayOutputStream xmlOutputStream) throws XMLStreamException
  {
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    return factory.createXMLStreamWriter(xmlOutputStream, DEFAULT_ENCODING);
  }

  public byte[] callModol(byte[] xmlInput) throws Exception
  {
    /*
     * imposto la modalità di rendering da utilizzare per la restituzione dei
     * dati
     */
    RendererModality rm = new RendererModality();
    rm.setIdRendererModality(new Integer(3)); // PDF
    rm.setSelezionataPerRendering(true);

    /*
     * imposto il percorso di memorizzazione del template interno al server
     * LiveCycle
     */
    RiferimentoAdobe rifAdobe = new RiferimentoAdobe();
    rifAdobe.setXdpURI(getRifAdobe());

    /* definisco il Modello da utilizzare */
    Modello modello = new Modello();
    modello.setCodiceModello(getCodiceModello());
    modello.setRendererModality(new RendererModality[]
    { rm });
    modello.setRiferimentoAdobe(rifAdobe);

    /* definisco il Modulo da utilizzare */
    Modulo modulo = new Modulo();
    modulo.setCodiceModulo(getCodiceModulo());
    modulo.setModello(modello);

    /* definisco l'Applicazione da utilizzare */
    Applicazione applicazione = new Applicazione();
    applicazione.setCodiceApplicazione(getCodiceApplicazione());
    applicazione.setDescrizioneApplicazione(getDescrizioneApplicazione());

    /*
     * predispongo l'oggetto con i dati da associare al modulo e
     * all'applicazione
     */
    XmlModel xml = new XmlModel();
    xml.setXmlContent(xmlInput);

    /*
     * finalmente invoco il servizio tramite la PD già istanziata in precedenza
     */
    Modulo moduloMerged = IuffiUtils.WS.getModolServClient()
        .mergeModulo(applicazione, null, modulo, xml);

    /*
     * recupero l'array di byte contenente il PDF e lo restituisco al chiamante
     */
    byte[] ba = moduloMerged.getDataContent();
    ba = trasformStaticPDF(ba, applicazione);
    return ba;
  }

  private byte[] trasformStaticPDF(byte[] pdfBytes, Applicazione applicazione)
      throws Exception
  {
    byte[] bXmlModol = null;

    /*
     * predispongo l'oggetto con i dati da associare al modulo e
     * all'applicazione
     */
    XmlModel xml = new XmlModel();
    xml.setXmlContent(pdfBytes);

    /*
     * recupero l'array di byte contenente il PDF e lo restituisco al chiamante
     */
    PdfStaticInputRequest pdfStatic = new PdfStaticInputRequest();
    pdfStatic.setPdfInput(pdfBytes);

    it.csi.modolxp.modolxppdfgensrv.dto.Applicazione applicazione2 = new it.csi.modolxp.modolxppdfgensrv.dto.Applicazione();
    applicazione.setCodiceApplicazione(applicazione.getCodiceApplicazione());
    applicazione
        .setDescrizioneApplicazione(applicazione.getDescrizioneApplicazione());

    bXmlModol = IuffiUtils.WS.getModolPDFGenServClient()
        .toStaticPdf(applicazione2, null, pdfStatic);

    return bXmlModol;
  }

  protected void generaStampaQuadri(XMLStreamWriter writer,
      IQuadroEJB quadroEJB, ProcedimentoOggetto procedimentoOggetto,
      String cuName) throws Exception
  {
    List<QuadroOggettoDTO> quadri = procedimentoOggetto.getQuadri();
    if (quadri != null && !quadri.isEmpty())
    {
      for (QuadroOggettoDTO quadro : quadri)
      {
        writeFragment(quadro.getCodQuadro(), writer, quadroEJB,
            procedimentoOggetto, cuName);
      }
    }
  }

  public void writeFragment(String codFragment, XMLStreamWriter writer,
      IQuadroEJB quadroEJB, ProcedimentoOggetto procedimentoOggetto,
      String cuName)
      throws Exception
  {
    Fragment f = MAP_FRAGMENTS.get(codFragment);
    if (f != null)
    {
      f.writeFragment(writer, procedimentoOggetto, quadroEJB, cuName);
    }
  }

  public void writeFragment(String codFragment, XMLStreamWriter writer,
      RigaJSONElencoListaLiquidazioneDTO listaLiquidazione,
      IListeLiquidazioneEJB listeLiquidazioneEJB, long idListeLiquidazione,
      String cuName)
      throws Exception
  {
    Fragment f = MAP_FRAGMENTS.get(codFragment);
    if (f != null)
    {
      f.writeFragmentListaLiquidazione(writer, listaLiquidazione,
          idListeLiquidazione, listeLiquidazioneEJB, cuName);
    }
  }

  protected String getDescrizioneApplicazione()
  {
    return "Gestione Pratiche IUFFIWEB";
  }

  protected String getCodiceApplicazione()
  {
    return "IUFFIWEB";
  }

  protected abstract String getCodiceModulo();

  protected abstract String getCodiceModello();

  protected abstract String getRifAdobe();

  public abstract String getDefaultFileName(long idProcedimentoOggetto)
      throws InternalUnexpectedException, NamingException;

  /**
   * Implementazione di default, ritorna l'oggetto corrente come stampa finale.
   * E' da riscrivere nelle classi figlie per gestire le stampe "finte" che
   * servono a smistare su altre stampe, come ad esempio la lettera di
   * ammissione al finanziamento che deve smistare sulla lettera di ammissione
   * (positiva) e quella di rifiuto (negativa).
   */
  public Stampa findStampaFinale(long idProcedimentoOggetto, String cuName)
      throws Exception
  {
    return this;
  }
}
