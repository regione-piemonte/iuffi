package it.csi.iuffi.iuffiweb.business.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.commons.io.IOUtils;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import it.csi.iuffi.iuffiweb.business.IAsyncEJB;
import it.csi.iuffi.iuffiweb.dto.AziendaDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiIdentificativi;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa.StampaOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.QuadroNewDAO;
import it.csi.iuffi.iuffiweb.util.DumpUtils;
import it.csi.iuffi.iuffiweb.util.InputStreamDataSource;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.pdf.PDFCoordinateExtractor;
import it.csi.iuffi.iuffiweb.util.stampa.Stampa;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservProfilazioneServiceFactory;
import it.csi.smrcomms.siapcommws.dto.smrcomm.EsitoDocumentoVO;
import it.csi.smrcomms.siapcommws.dto.smrcomm.SiapCommWsDocumentoInputVO;
import it.csi.smrcomms.siapcommws.dto.smrcomm.SiapCommWsMetadatoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellDocumentoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellEsitoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellMetadatoVO;

@Stateless()
@EJB(name = "java:app/Async", beanInterface = IAsyncEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@Asynchronous()
@TransactionTimeout(value = 300, unit = TimeUnit.SECONDS)
public class AsyncEJB extends IuffiAbstractEJB<QuadroNewDAO>
    implements IAsyncEJB
{
  private static final String THIS_CLASS = AsyncEJB.class.getSimpleName();

  @Override
  public void generaStampePerProcedimento(long idProcedimentoOggetto,
      long idProcedimento)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "[" + THIS_CLASS
        + "::generaStampePerProcedimento]";
    final long idUtenteLogin = dao
        .findIdUtenteAggiornamentoProcedimentoOggetto(idProcedimentoOggetto);
    try
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " BEGIN.");
      }

      List<StampaOggettoDTO> elenco = dao
          .getElencoStampeOggetto(idProcedimentoOggetto, null);
      if (elenco != null)
      {
        logger.info(THIS_METHOD + " Generazione di " + elenco.size()
            + " stampa/e in corso per idProcedimentoOggetto = "
            + idProcedimentoOggetto
            + ", idProcedimento = " + idProcedimento + " e idUtente "
            + idUtenteLogin);
        for (StampaOggettoDTO stampa : elenco)
        {
          long idOggettoIcona = stampa.getIdOggettoIcona();
          logger.info(
              THIS_METHOD + " Generazione della stampa con idOggettoIcona = "
                  + idOggettoIcona + " per idProcedimentoOggetto = "
                  + idProcedimentoOggetto
                  + ", idProcedimento = " + idProcedimento + " e idUtente "
                  + idUtenteLogin);
          if (stampa.getIdProcedimOggettoStampa() != null)
          {
            privateGeneraStampa(idProcedimentoOggetto, idProcedimento, stampa);
          }
        }
      }
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
    catch (Exception e)
    {
      logger.error(THIS_METHOD
          + " Errore nella generazione delle stampa per il procedimento oggetto #"
          + idProcedimentoOggetto + " richiesto dall'utente #"
          + idUtenteLogin + "\nEccezione rilevata:\n"
          + DumpUtils.getExceptionStackTrace(e));
    }
  }

  @Override
  public void generaStampa(long idProcedimentoOggetto, long idProcedimento,
      long idOggettoIcona)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "[" + THIS_CLASS + "::generaStampa]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      List<StampaOggettoDTO> elenco = dao
          .getElencoStampeOggetto(idProcedimentoOggetto, idOggettoIcona);
      if (elenco != null && !elenco.isEmpty())
      {
        StampaOggettoDTO stampaOggetto = elenco.get(0);
        privateGeneraStampa(idProcedimentoOggetto, idProcedimento,
            stampaOggetto);
      }
    }
    catch (Exception e)
    {
      logger.error(THIS_METHOD
          + " Errore nella generazione delle stampa per il procedimento oggetto #"
          + idProcedimentoOggetto + "\nEccezione rilevata:\n"
          + DumpUtils.getExceptionStackTrace(e));
    }
  }

  public void privateGeneraStampa(long idProcedimentoOggetto,
      long idProcedimento, StampaOggettoDTO stampaOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "[" + THIS_CLASS + "::privateGeneraStampa]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    UtenteAbilitazioni utenteAbilitazioni = null;
    try
    {
      dao.lockOggettoIcona(stampaOggetto.getIdOggettoIcona());
      final long idUtenteLogin = dao
          .findIdUtenteAggiornamentoProcedimentoOggetto(idProcedimentoOggetto);
      utenteAbilitazioni = PapuaservProfilazioneServiceFactory
          .getRestServiceClient()
          .getUtenteAbilitazioniByIdUtenteLogin(idUtenteLogin);

      if (stampaOggetto != null)
      {
        final long idOggettoIcona = stampaOggetto.getIdOggettoIcona();
        final String codiceCdu = stampaOggetto.getCodiceCdu();
        Stampa stampa = IuffiUtils.STAMPA.getStampaFromCdU(codiceCdu);
        if (stampa == null)
        {
          logger.error(THIS_METHOD
              + " Errore nella generazione della stampa con idOggettoIcona #"
              + idOggettoIcona + " per il procedimento oggetto #"
              + idProcedimentoOggetto + " richiesto dall'utente #"
              + idUtenteLogin
              + "\nERRORE GRAVE:\nTipologia di stampa non registrata, non è stato trovata nessuna classe deputata a generare la stampa!\n\n");
          dao.updateProcedimOggettoStampaByIdOggetoIcona(
              idProcedimentoOggetto,
              idOggettoIcona,
              null,
              IuffiConstants.STATO.STAMPA.ID.STAMPA_FALLITA,
              "Errore nella generazione della stampa:\nERRORE INTERNO GRAVE: Tipologia di stampa non registrata, non è stato trovata nessuna classe deputata a generare la stampa!",
              idUtenteLogin);
          return;
        }
        byte[] pdf = null;
        try
        {
          pdf = stampa.findStampaFinale(idProcedimentoOggetto, codiceCdu)
              .genera(idProcedimentoOggetto, codiceCdu);
        }
        catch (Exception e)
        {
          // Registrare l'eccezione...
          dao.updateProcedimOggettoStampaByIdOggetoIcona(idProcedimentoOggetto,
              idOggettoIcona, null,
              IuffiConstants.STATO.STAMPA.ID.STAMPA_FALLITA,
              "Errore nella generazione della stampa:\n"
                  + DumpUtils.getExceptionStackTrace(e),
              idUtenteLogin);
          return;
        }

        Integer codice = null;
        long idDocumentoIndexArchiviato = 0;
        String messaggioErrore = "";

        try
        {
          List<AgriWellMetadatoVO> metadatiAggiuntivi = new ArrayList<AgriWellMetadatoVO>();
          String flagFirmaGrafometrica = getFlagFirmaGrafometrica(stampaOggetto,
              utenteAbilitazioni);
          if (IuffiConstants.FLAGS.FIRMA_GRAFOMETRICA.DA_FIRMARE_GRAFOMETRICAMENTE
              .equals(flagFirmaGrafometrica))
          {
            PDFCoordinateExtractor extractor = new PDFCoordinateExtractor();
            extractor.addMetadati(metadatiAggiuntivi, pdf,
                IuffiConstants.FIRMA_GRAFOMETRICA.TESTO.DATA,
                IuffiConstants.FIRMA_GRAFOMETRICA.TESTO.FIRMA);
          }
          Date dataLimiteFirma = dao
              .getDataLimiteFirmaGrafomemetrica(idProcedimentoOggetto);
          if (dataLimiteFirma != null)
          {
            AgriWellMetadatoVO metadatoVO = new AgriWellMetadatoVO();
            metadatoVO.setNomeEtichetta("data_limite_firma");
            metadatoVO.setValoreEtichetta(
                IuffiUtils.DATE.formatDateTime(dataLimiteFirma));
            metadatiAggiuntivi.add(metadatoVO);
          }

          AgriWellDocumentoVO agriWellDocumentoVO = getAgriWellDocumentoVO(
              idProcedimentoOggetto, idProcedimento, stampaOggetto, pdf,
              stampa.getDefaultFileName(idProcedimentoOggetto), utenteAbilitazioni,
              metadatiAggiuntivi, flagFirmaGrafometrica);

          Map<String, String> mapParametri = getParametri(new String[]
          { IuffiConstants.PARAMETRO.DOQUIAGRI_FLAG_PROT,
              IuffiConstants.PARAMETRO.DOQUIAGRI_FLAG_PEC,
              IuffiConstants.PARAMETRO.DOQUIAGRI_USR_PSW_SC,
              IuffiConstants.PARAMETRO.DOQUIAGRI_CLASS_REG,
              IuffiConstants.PARAMETRO.DOQUIAGRI_FASCICOLA,
          });

          BandoDTO bando = dao.getInformazioniBandoByIdProcedimento(idProcedimento);
          String codClassReg = dao.getCodClassRegionale(utenteAbilitazioni.getIdProcedimento(),bando.getIdBando());
          mapParametri.put(IuffiConstants.PARAMETRO.DOQUIAGRI_CLASS_REG, codClassReg);
          // Decido se richiamare i nuovi ws in base al parametro
          if (IuffiConstants.FLAGS.SI.equals(mapParametri
              .get(IuffiConstants.PARAMETRO.DOQUIAGRI_FLAG_PROT)))
          {
            String paramUsr = mapParametri
                .get(IuffiConstants.PARAMETRO.DOQUIAGRI_USR_PSW_SC);
            String user = paramUsr.split("#")[0];
            String password = paramUsr.split("#")[1];
            List<SiapCommWsMetadatoVO> metadatiAggiuntiviProt = new ArrayList<SiapCommWsMetadatoVO>();
            PDFCoordinateExtractor extractor = new PDFCoordinateExtractor();
            extractor.addMetadatiProtocollo(metadatiAggiuntiviProt,
                IOUtils.toByteArray(new ByteArrayInputStream(pdf)),
                IuffiConstants.ELABORAZIONE_STAMPA.TESTO.PROTOCOLLO);

            if (IuffiConstants.FLAGS.FIRMA_GRAFOMETRICA.DA_FIRMARE_GRAFOMETRICAMENTE
                .equals(flagFirmaGrafometrica))
            {
              extractor = new PDFCoordinateExtractor();
              extractor.addMetadatiSiapWs(metadatiAggiuntiviProt, pdf,
                  IuffiConstants.FIRMA_GRAFOMETRICA.TESTO.DATA,
                  IuffiConstants.FIRMA_GRAFOMETRICA.TESTO.FIRMA);
            }

            SiapCommWsDocumentoInputVO inputVO = IuffiUtils.WS
                .convertAgriWellVOToSiapComm(user, password,
                    agriWellDocumentoVO,
                    /* SiapCommWsDatiProtocolloVO datiProtocollo */ null,
                    /* SiapCommWsDatiInvioMailPecVO datiPEC */ null,
                    /* boolean flagInvioPEC */ false,
                    /* boolean flagProtocolla */ false,
                    /* boolean flagTimbroProtocollo */ false,
                    /* boolean flagEreditaProtocollo */ false,
                    /* boolean isUpdate */ false,
                    /* boolean isDocumentoPrincipale */ true,
                    /* Long idDocumentoIndexPrincipale */ null,
                    utenteAbilitazioni.getIdUtenteLogin().longValue(),
                    idProcedimento);
            if (dao.isOggettoIstanzaByProcOggetto(idProcedimentoOggetto))
            {
              if ("S".equals(stampaOggetto.getFlagVisibileTutti()))
                inputVO.getNuovoDocumento().setCodiceVisibilitaDoc(
                    IuffiConstants.AGRIWELL.ARCHIVIAZIONE.VISIBILITA.TUTTI);
              else
                inputVO.getNuovoDocumento().setCodiceVisibilitaDoc(
                    IuffiConstants.AGRIWELL.ARCHIVIAZIONE.VISIBILITA.PA);
            }
            else
            {
              inputVO.getNuovoDocumento().setCodiceVisibilitaDoc(
                  IuffiConstants.AGRIWELL.ARCHIVIAZIONE.VISIBILITA.NESSUNO);
            }
            AziendaDTO azienda = dao
                .getDatiAziendaAgricolaProcedimento(idProcedimento, null);
   		 	
   		 	String gruppoIdentificativo = dao.getIdentificativoMinProcedimentoOggettoByProcedimento(idProcedimento);
   		 	
            IuffiUtils.WS.addMetadati(inputVO.getNuovoDocumento(),
                metadatiAggiuntiviProt,
                dao.getDataLimiteFirmaGrafomemetrica(idProcedimentoOggetto),
                stampa.getDefaultFileName(idProcedimentoOggetto), azienda.getCuaa(),
                utenteAbilitazioni, mapParametri,
                bando.getDenominazione(), bando.getIdBando(), gruppoIdentificativo);
            DataHandler handler = new DataHandler(
                new InputStreamDataSource(new ByteArrayInputStream(pdf)));
            EsitoDocumentoVO esito = IuffiUtils.WS.getSiapComm()
                .archiviaProtocollaDocumento(inputVO, handler);
            codice = esito == null ? null : esito.getEsitoVO().getCodice();
            if (codice != null
                && codice == IuffiConstants.SERVICE.AGRIWELL.RETURN_CODE.SUCCESS)
            {
              idDocumentoIndexArchiviato = esito.getIdDocumentoIndex();
            }
            else
            {
              if (esito == null || esito.getEsitoVO() == null)
              {
                messaggioErrore = "esito==null";
              }
              else
              {
                messaggioErrore = esito.getEsitoVO().getMessaggio();
              }
            }
          }
          else
          {
            AgriWellEsitoVO esito = IuffiUtils.PORTADELEGATA
                .getAgriwellCSIInterface()
                .agriwellServiceScriviDoquiAgri(agriWellDocumentoVO);
            codice = esito == null ? null : esito.getEsito();
            if (codice != null
                && codice == IuffiConstants.SERVICE.AGRIWELL.RETURN_CODE.SUCCESS)
            {
              idDocumentoIndexArchiviato = esito.getIdDocumentoIndex();
            }
            else
            {
              if (esito == null)
              {
                messaggioErrore = "esito==null";
              }
              else
              {
                messaggioErrore = esito.getMessaggio();
              }
            }
          }
        }
        catch (Exception e)
        {
          logger.error(THIS_METHOD
              + " Eccezione nel richiamo di  agriwellServiceScriviDoquiAgri del documento con id_procedimento_oggetto = "
              + idProcedimentoOggetto
              + " e idOggettoIcona = "
              + idOggettoIcona + "\nStacktrace = " + e);
          // ERRORE ==> lo stato passa in Generazione stampa fallita
          dao.updateProcedimOggettoStampaByIdOggetoIcona(idProcedimentoOggetto,
              idOggettoIcona, null,
              IuffiConstants.STATO.STAMPA.ID.STAMPA_FALLITA,
              "Eccezione in agriwellServiceScriviDoquiAgri:\n"
                  + DumpUtils.getExceptionStackTrace(e),
              idUtenteLogin);
        }

        if (codice != null && codice
            .intValue() == IuffiConstants.SERVICE.AGRIWELL.RETURN_CODE.SUCCESS)
        {
          // Tutto OK ==> lo stato passa in stampata
          dao.updateProcedimOggettoStampaByIdOggetoIcona(idProcedimentoOggetto,
              idOggettoIcona, idDocumentoIndexArchiviato,
              getIdStatoStampa(utenteAbilitazioni,
                  stampaOggetto.getFlagFirmaGrafometrica()),
              null, idUtenteLogin);
        }
        else
        {
          logger.error(THIS_METHOD
              + " Errore nell'inserimento su index del documento con id_procedimento_oggetto = "
              + idProcedimentoOggetto + " e idOggettoIcona = "
              + idOggettoIcona + "\nMessaggio di errore: " + messaggioErrore);
          // ERRORE ==> lo stato passa in Generazione stampa fallita
          dao.updateProcedimOggettoStampaByIdOggetoIcona(idProcedimentoOggetto,
              idOggettoIcona, null,
              IuffiConstants.STATO.STAMPA.ID.STAMPA_FALLITA,
              "Errore in agriwellServiceScriviDoquiAgri:\n" + messaggioErrore,
              idUtenteLogin);
        }
      }
    }
    catch (Exception e)
    {
      logger.error(THIS_METHOD
          + " Errore nella generazione della stampa con idOggettoIcona #"
          + (stampaOggetto == null ? null : stampaOggetto.getIdOggettoIcona())
          + " per il procedimento oggetto #"
          + idProcedimentoOggetto + " richiesto dall'utente #"
          + (utenteAbilitazioni == null ? "utenteAbilitazioni==null"
              : utenteAbilitazioni.getIdUtenteLogin())
          + "\nEccezione rilevata:\n" + DumpUtils.getExceptionStackTrace(e));
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  private int getIdStatoStampa(UtenteAbilitazioni utenteAbilitazioni, String flagFirmaGrafometrica)
  {
    
      if (IuffiConstants.FLAGS.SI.equals(flagFirmaGrafometrica))
      {
        
        if (utenteAbilitazioni.getRuolo().isAziendaAgricola())
          return IuffiConstants.STATO.STAMPA.ID.IN_ATTESA_FIRMA_ELETTRONICA_LEGGERA;
        else
              return IuffiConstants.STATO.STAMPA.ID.IN_ATTESA_FIRMA_GRAFOMETRICA;

      }
      else
        if (IuffiConstants.FLAGS.NO.equals(flagFirmaGrafometrica))
        {
          if (utenteAbilitazioni.getRuolo().isAziendaAgricola())
          return IuffiConstants.STATO.STAMPA.ID.IN_ATTESA_FIRMA_ELETTRONICA_LEGGERA;
        else
              return IuffiConstants.STATO.STAMPA.ID.IN_ATTESA_FIRMA_SU_CARTA;

        } 


    if ((!"X".equals(flagFirmaGrafometrica)) && utenteAbilitazioni.getRuolo().isAziendaAgricola())
    {
      return IuffiConstants.STATO.STAMPA.ID.IN_ATTESA_FIRMA_ELETTRONICA_LEGGERA;
    }

    return IuffiConstants.STATO.STAMPA.ID.STAMPATA;
  }

  private AgriWellDocumentoVO getAgriWellDocumentoVO(long idProcedimentoOggetto,
      long idProcedimento, StampaOggettoDTO stampaOggetto, byte[] pdf,
      String fileName, UtenteAbilitazioni utenteAbilitazioni,
      List<AgriWellMetadatoVO> metadatiAggiuntivi, String flagFirmaGrafometrica)
      throws InternalUnexpectedException, IOException
  {
    Map<String, String> mapParametri = getParametri(new String[]
    { IuffiConstants.PARAMETRO.DOQUIAGRI_CARTELLA,
        IuffiConstants.PARAMETRO.DOQUIAGRI_CLASS_REG,
        IuffiConstants.PARAMETRO.DOQUIAGRI_FASCICOLA });
    TestataProcedimento testataProcedimento = dao
        .getTestataProcedimento(idProcedimento);
    String identificativo = dao.getIdentificativo(idProcedimentoOggetto);
    // ID Quadro in questo caso non è significativo quindi posso usare un valore
    // qualsiati (tanto mi servono solo id_azienda e cuaa che non sono
    // influenzati da
    // questo id)
    BandoDTO bando = dao.getInformazioniBandoByIdProcedimento(idProcedimento);
    String codClassReg = dao.getCodClassRegionale(utenteAbilitazioni.getIdProcedimento(),bando.getIdBando());
    mapParametri.put(IuffiConstants.PARAMETRO.DOQUIAGRI_CLASS_REG, codClassReg);
    
    DatiIdentificativi datiIdentificativi = dao
        .getDatiIdentificativiProcedimentoOggetto(idProcedimentoOggetto, -1,
            null, dao.getIdProcedimentoAgricoloByIdProcedimentoOggetto(idProcedimentoOggetto));
    return IuffiUtils.AGRIWELL.getAgriWellDocumentoVO(
        stampaOggetto.getExtIdTipoDocumento(), pdf, fileName, mapParametri,
        utenteAbilitazioni,
        flagFirmaGrafometrica, testataProcedimento, datiIdentificativi,
        identificativo, metadatiAggiuntivi);
  }

  private String getFlagFirmaGrafometrica(StampaOggettoDTO stampaOggetto,
      UtenteAbilitazioni utenteAbilitazioni)
  {
    String flagFirmaGrafometrica = IuffiConstants.FLAGS.FIRMA_GRAFOMETRICA.DA_NON_FIRMARE;
    if (IuffiConstants.FLAGS.SI
        .equals(stampaOggetto.getFlagFirmaGrafometrica()))
    {
      flagFirmaGrafometrica = IuffiConstants.FLAGS.FIRMA_GRAFOMETRICA.DA_FIRMARE_GRAFOMETRICAMENTE;
    }
    return flagFirmaGrafometrica;
  }

  @Override
  public void generaStampaListaLiquidazione(long idListaLiquidazione)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "[" + THIS_CLASS
        + "::generaStampaListaLiquidazione]";

    // dao.lockListaLiquidazione(idListaLiquidazione);

    try
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " BEGIN.");
      }

      logger.info(THIS_METHOD
          + " Generazione di stampa in corso per idListaLiquidazione = "
          + idListaLiquidazione);

      Stampa stampa = IuffiUtils.STAMPA.getStampaFromCdU(
          IuffiConstants.USECASE.LISTE_LIQUIDAZIONE.STAMPA);
      if (stampa == null)
      {
        logger.error(THIS_METHOD
            + " Errore nella generazione della stampa. \nERRORE GRAVE:\nTipologia di stampa non registrata, non è stato trovata nessuna classe deputata a generare la stampa!\n\n");
        dao.updateFileListaLiquidazione(
            idListaLiquidazione,
            IuffiConstants.STATO.STAMPA.ID.STAMPA_FALLITA,
            "Errore nella generazione della stampa:\nERRORE INTERNO GRAVE: Tipologia di stampa non registrata, non è stato trovata nessuna classe deputata a generare la stampa!");
        return;
      }
      byte[] pdf = null;
      try
      {
        pdf = stampa.genera(idListaLiquidazione,
            IuffiConstants.USECASE.LISTE_LIQUIDAZIONE.STAMPA);
        dao.caricaFileListaLiquidazione(idListaLiquidazione, pdf,
            IuffiConstants.STATO.STAMPA.ID.STAMPATA);
      }
      catch (Exception e)
      {
        // Registrare l'eccezione...
        dao.updateFileListaLiquidazione(idListaLiquidazione,
            IuffiConstants.STATO.STAMPA.ID.STAMPA_FALLITA,
            "Errore nella generazione della stampa.");
        logger.error(THIS_METHOD
            + " Errore nella generazione delle stampa per la lista con idListaLiqudazione #"
            + idListaLiquidazione + "Exception: "
            + DumpUtils.getExceptionStackTrace(e));
        return;
      }

      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }

    }
    catch (Exception e)
    {
      logger.error(THIS_METHOD
          + " Errore nella generazione delle stampa per la lista con idListaLiqudazione #"
          + idListaLiquidazione);
    }
  }

}
