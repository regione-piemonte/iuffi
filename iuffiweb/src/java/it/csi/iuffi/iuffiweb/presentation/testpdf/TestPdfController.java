package it.csi.iuffi.iuffiweb.presentation.testpdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa.StampaOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalServiceException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.iuffi.iuffiweb.util.stampa.Stampa;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellEsitoVO;
import it.csi.smrcomms.smrcomm.exception.SmrcommInternalException;

@Controller
@RequestMapping("/testpdf/")
@NoLoginRequired
public class TestPdfController
{
  public static final String   TIPO_VERBALE                   = "verbale";
  public static final String   TIPO_LETTERA                   = "lettera";
  public static final String   TIPO_ISTANZA                   = "istanza";
  public static final String   TIPO_ISTANZA_PREMIO            = "istanza_premio";
  public static final long[]   ID_PROCEDIMENTO_ISTRUTTORIA    =
  { 603, 1282, 1409 };                                                                        // 1224,
  public static final long[]   ID_PROCEDIMENTO_ISTANZA        =
  { 601, 1283, 1410 };                                                                        // 1215,

  public static final long[]   ID_PROCEDIMENTO_ISTANZA_PREMIO =
  { 2145 };                                                                                   // 1215,

  public static final String[] TIPI                           =
  { TIPO_VERBALE, TIPO_LETTERA, TIPO_ISTANZA, TIPO_ISTANZA_PREMIO };
  public static final Random   RANDOM                         = new Random(
      System.currentTimeMillis());
  private static final String  BASE_PATH                      = "/home/tst-jboss640-043/pdf/";
  private static final String  BASE_PATH_ERRORS               = BASE_PATH
      + "/errors/";
  private static int           PROGRESSIVO                    = 0;
  @Autowired
  IQuadroEJB                   quadroEJB                      = null;

  @RequestMapping("/genera_{tipo}")
  @ResponseBody
  public String generaTipo(@PathVariable("tipo") String tipo) throws Exception
  {
    byte[] pdf = null;
    InfoStampa infoStampa = getInfoStampa(tipo);
    final Stampa findStampaFinale = IuffiUtils.STAMPA
        .getStampaFromCdU(infoStampa.useCase).findStampaFinale(
            infoStampa.idProcedimentoOggetto,
            infoStampa.useCase);
    Date dateBegin = new Date();
    pdf = findStampaFinale
        .genera(infoStampa.idProcedimentoOggetto, infoStampa.useCase);
    if (elaboraPdf(pdf, tipo, infoStampa.idProcedimentoOggetto, dateBegin,
        infoStampa.useCase))
    {
      return "Stampa Corretta";
    }
    else
    {
      return "Stampa Errata";
    }
  }

  @RequestMapping("/genera")
  @ResponseBody
  public String genera() throws Exception
  {
    return generaTipo(TIPI[RANDOM.nextInt(5)]);
  }

  @RequestMapping("/rigenera")
  @ResponseBody
  public String rigeneraTutto() throws Exception
  {
    StringBuilder sb = new StringBuilder();
    for (String tipo : TIPI)
    {
      sb.append("<br />").append(salvaTipo(tipo));
    }
    return "Tutte le stampe sono state rigenerate correttamente:" + sb;
  }

  @RequestMapping("/registra_{tipo}")
  @ResponseBody
  public String salvaTipo(@PathVariable("tipo") String tipo) throws Exception
  {
    byte[] pdf = null;
    InfoStampa infoStampa = getInfoStampa(tipo);
    final Stampa findStampaFinale = IuffiUtils.STAMPA
        .getStampaFromCdU(infoStampa.useCase).findStampaFinale(
            infoStampa.idProcedimentoOggetto,
            infoStampa.useCase);
    pdf = findStampaFinale
        .genera(infoStampa.idProcedimentoOggetto, infoStampa.useCase);
    writeFile(pdf, BASE_PATH + tipo + ".pdf");
    return "Stampa \"" + tipo + "\" registrata correttamente (dimensione file: "
        + pdf.length + " byte)";
  }

  protected InfoStampa getInfoStampa(String tipo) throws ApplicationException
  {
    InfoStampa infoStampa = new InfoStampa();
    switch (tipo)
    {
      case TIPO_VERBALE:
        infoStampa.idProcedimentoOggetto = ID_PROCEDIMENTO_ISTRUTTORIA[0];
        infoStampa.useCase = IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO;
        break;
      case TIPO_LETTERA:
        infoStampa.idProcedimentoOggetto = ID_PROCEDIMENTO_ISTRUTTORIA[0];
        infoStampa.useCase = IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_AMMISSIONE_FINANZIAMENTO;
        break;
      case TIPO_ISTANZA:
        infoStampa.idProcedimentoOggetto = ID_PROCEDIMENTO_ISTRUTTORIA[0];
        infoStampa.useCase = IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_STAMPA_PRATICA;
        break;
      case TIPO_ISTANZA_PREMIO:
        infoStampa.idProcedimentoOggetto = ID_PROCEDIMENTO_ISTANZA_PREMIO[0];
        infoStampa.useCase = IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_STAMPA_PRATICA;
        break;
      default:
        throw new ApplicationException("Tipologia di stampa non riconosciuta");
    }
    return infoStampa;
  }

  private boolean elaboraPdf(byte[] pdfGenerato, String tipo,
      long idProcedimentoOggetto, Date dateBegin,
      String useCase)
      throws Exception
  {
    byte[] pdfCorretto = readFile(BASE_PATH + tipo + ".pdf");
    if (pdfGenerato.length == pdfCorretto.length)
    {
      return true;
    }
    else
    {
      // Sono differenti ==> questo è un caso da segnalare
      writePdfErrato(pdfGenerato, tipo, idProcedimentoOggetto, dateBegin);
      return false;
    }
  }

  /** @deprecated */
  protected byte[] getPdfFromAgriwell(long idProcedimentoOggetto,
      String useCase)
      throws InternalUnexpectedException, SmrcommInternalException,
      InternalServiceException, UnrecoverableException,
      ApplicationException
  {
    List<StampaOggettoDTO> elenco = quadroEJB
        .getElencoStampeOggetto(idProcedimentoOggetto, null);
    StampaOggettoDTO stampaCorrente = null;
    for (StampaOggettoDTO stampa : elenco)
    {
      if (stampa.getCodiceCdu().equals(useCase))
      {
        stampaCorrente = stampa;
        break;
      }
    }
    AgriWellEsitoVO esito = IuffiUtils.PORTADELEGATA
        .getAgriwellCSIInterface()
        .agriwellServiceLeggiDoquiAgri(stampaCorrente.getExtIdDocumentoIndex());
    Integer codice = esito == null ? null : esito.getEsito();
    if (codice != null
        && codice == IuffiConstants.SERVICE.AGRIWELL.RETURN_CODE.SUCCESS)
    {
      return esito.getContenutoFile();
    }
    // Se arrivo qua c'è stato un errore!
    throw new ApplicationException(
        "Impossibile trovare il pdf su DOQUIAGRI per l'idProcedimento #"
            + idProcedimentoOggetto + " e il Caso d'uso ["
            + useCase + "] con idDocumentoIndex #"
            + stampaCorrente.getExtIdDocumentoIndex());
  }

  private void writePdfErrato(byte[] pdfGenerato, String tipo,
      long idProcedimentoOggetto, Date dateBegin)
      throws IOException
  {
    final String fileName = BASE_PATH_ERRORS + tipo + "_" + getProgressivo()
        + "_" + idProcedimentoOggetto + "_"
        + IuffiUtils.DATE.formatDateTime(dateBegin).replaceAll("[ /:]", "")
        + ".pdf";
    writeFile(pdfGenerato, fileName);
  }

  private void writeFile(byte[] pdfGenerato, String fileName)
      throws IOException
  {
    FileOutputStream fos = new FileOutputStream(
        fileName);
    fos.write(pdfGenerato);
    fos.close();
  }

  private byte[] readFile(String fileName) throws IOException
  {
    File file = new File(fileName);
    FileInputStream is = new FileInputStream(file);
    byte[] b = new byte[(int) file.length()];
    is.read(b);
    is.close();
    return b;
  }

  private synchronized int getProgressivo()
  {
    return ++PROGRESSIVO;
  }

  private static class InfoStampa
  {
    public long   idProcedimentoOggetto;
    public String useCase;
  }
}
