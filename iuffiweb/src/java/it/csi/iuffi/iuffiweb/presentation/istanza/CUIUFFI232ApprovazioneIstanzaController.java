package it.csi.iuffi.iuffiweb.presentation.istanza;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa.StampaOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa.StampaOggettoIconaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException.ExceptionType;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/cuiuffi232")
@IuffiSecurity(value = "CU-IUFFI-232", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO_CHIUSO)
public class CUIUFFI232ApprovazioneIstanzaController extends BaseController
{

  @Autowired
  private IQuadroEJB quadroEJB = null;

  @RequestMapping(value = "popupindex", method = RequestMethod.GET)
  @IsPopup
  public String popupIndex(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    String msgError = null;
    String msgError2 = null;
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    final long idProcedimentoOggetto = procedimentoOggetto
        .getIdProcedimentoOggetto();

    List<StampaOggettoDTO> stampe = quadroEJB
        .getElencoStampeOggetto(idProcedimentoOggetto, null);
    msgError = checkProcOggetto(procedimentoOggetto, stampe);

    List<StampaOggettoIconaDTO> stampeConfigurate = quadroEJB
        .getElencoDocumentiStampeDaVerificare(idProcedimentoOggetto, null,
            null);
    msgError2 = checkStampeFirmate(procedimentoOggetto, stampeConfigurate);

    if (msgError2 != null)
    {
      return controlloVerbali(procedimentoOggetto, model, session, msgError,
          msgError2);
    }

    if (msgError == null && msgError2 == null)
    {
      return "approvazione/popupApprovazione";
    }
    else
    {
      model.addAttribute("errore",
          "Impossibile procedere con l'approvazione: "
              + IuffiUtils.STRING.nvl(msgError) + " "
              + IuffiUtils.STRING.nvl(msgError2));
      return "dialog/soloErrore";
    }
  }

  @RequestMapping(value = "popupindex", method = RequestMethod.POST)
  @ResponseBody
  public String popupIndexPost(Model model, HttpSession session,
      HttpServletRequest request, @RequestParam(value="note") String note) throws InternalUnexpectedException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    List<StampaOggettoDTO> stampe = quadroEJB.getElencoStampeOggetto(
        procedimentoOggetto.getIdProcedimentoOggetto(), null);
    String msgError = checkProcOggetto(procedimentoOggetto, stampe);

    if (msgError != null)
    {
      return "Impossibile procedere con l'approvazione: " + msgError;
    }
    if(note!=null && note.length()>4000){
    	return "Le note non possono superare i 4000 caratteri (attualmente il campo note contiene "+note.length()+" caratteri).";
    }
    QuadroOggettoDTO quadro = procedimentoOggetto
        .findQuadroByCodiceQuadro("ESIFN");
    long idQuadroOggetto = 0;
    if (quadro != null)
    {
      idQuadroOggetto = quadro.getIdQuadroOggetto();
    }

    Date dataAmiissione = null;

    if (IuffiConstants.FLAGS.SI
        .equals(procedimentoOggetto.getFlagAmmissione()))
    {
      EsitoFinaleDTO esitoFinale = quadroEJB.getEsitoFinale(
          procedimentoOggetto.getIdProcedimentoOggetto(), idQuadroOggetto);
      if (esitoFinale == null || esitoFinale.getFlagAltreInfoAtto() == null
          || "S".equals(esitoFinale.getFlagAltreInfoAtto()))
      {
        if (esitoFinale == null || esitoFinale.getDataAtto() == null)
        {
          return "Impossibile procedere con l'approvazione: data ammissione non presente su esito finale";
        }
        if ("P".equals(esitoFinale.getCodiceEsito())
            || ("PN".equals(esitoFinale.getCodiceEsito())
                && conteggioIstruttorie(procedimentoOggetto.getIdProcedimento(),
                    procedimentoOggetto.getIdProcedimentoOggetto()) > 1))
          dataAmiissione = esitoFinale.getDataAtto();
      }
    }

    msgError = quadroEJB.approvaIstanza(getIdProcedimento(session),
        procedimentoOggetto.getIdProcedimentoOggetto(), idQuadroOggetto,
        procedimentoOggetto.getIdBandoOggetto(),
        procedimentoOggetto.getIdentificativo(), getUtenteAbilitazioni(session),
        dataAmiissione, note);

    // verifico se devo aggiornare la data ammissione
    if ("S".equals(procedimentoOggetto.getFlagAmmissione()) && quadroEJB
        .isDataAmmissioneDaGestire(procedimentoOggetto.getIdProcedimento()))
    {
      EsitoFinaleDTO esitoFinale = quadroEJB.getEsitoFinale(
          procedimentoOggetto.getIdProcedimentoOggetto(), idQuadroOggetto);
      if (esitoFinale == null || esitoFinale.getFlagAltreInfoAtto() == null
          || "N".equals(esitoFinale.getFlagAltreInfoAtto()))
      {
        /*
         * Terminato il passo 10, in DATA_AMMISSIONE di
         * IUF_R_PROCEDIMENTO_LIVELLO scrivere
         * nvl(nvl(data_protocollo,data_protocollo_emergenza),trunc di sysdate)
         * (le date protocollo sono su IUF_T_PROCEDIM_OGGETTO_STAMP legate
         * alla comunicazione esito istruttoria firmata) (sempre solo per i
         * livelli con CONTRIBUTO_CONCESSO>0 e DATA_AMMISSIONE= null).
         */

        // ESITO = P oppure (ESITO = PN e conteggio istruttorie >1)

        if ("P".equals(esitoFinale.getCodiceEsito())
            || ("PN".equals(esitoFinale.getCodiceEsito())
                && conteggioIstruttorie(procedimentoOggetto.getIdProcedimento(),
                    procedimentoOggetto.getIdProcedimentoOggetto()) > 1))
        {
          Date dataAmmissione = quadroEJB.findDataProtocolloLettera(
              procedimentoOggetto.getIdProcedimentoOggetto());
          if (dataAmmissione == null)
          {
            dataAmmissione = new Date();
          }
          quadroEJB.updateDataAmmissione(
              procedimentoOggetto.getIdProcedimento(), dataAmmissione);
        }
      }
    }

    if (msgError == null)
    {
      return "redirect:../cuiuffi232/riepilogo.do";

    }
    else
    {
      return "Impossibile procedere con l'approvazione: "
          + HtmlUtils.htmlEscape(msgError);
    }
  }

  private int conteggioIstruttorie(long idProcedimento,
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    int progressivoIstanza = 0;
    int progressivoIstanzaISAMM = 0;
    int progressivoIstanzaISAMB = 0;
    int progressivoIstanzaISVAR = 0;

    boolean isIsamm = false;
    boolean isIsvar = false;

    ProcedimentoDTO procIter = quadroEJB.getIterProcedimento(idProcedimento);
    for (ProcedimentoOggettoDTO procOggDTO : procIter.getProcedimentoOggetto())
    {
      if (IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIONE_FINANZIAMENTO
          .equals(procOggDTO.getCodOggetto())
          || IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIONE_FINANZIAMENTO_GAL
              .equals(procOggDTO.getCodOggetto()))
      {
        progressivoIstanzaISAMM++;
      }
      if (IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIBILITA
          .equals(procOggDTO.getCodOggetto())
          || IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIBILITA_FINANZIAMENTO_GAL
              .equals(procOggDTO.getCodOggetto()))
      {
        progressivoIstanzaISAMB++;
      }
      if (IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_VARIANTE
          .equals(procOggDTO.getCodOggetto()))
      {
        progressivoIstanzaISVAR++;
      }
      if (procOggDTO.getIdProcedimentoOggetto() == idProcedimentoOggetto)
      {
        if (IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIONE_FINANZIAMENTO
            .equals(procOggDTO.getCodOggetto())
            || IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_AMMISSIONE_FINANZIAMENTO_GAL
                .equals(procOggDTO.getCodOggetto()))
        {
          isIsamm = true;
        }
        else
          if (IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_VARIANTE
              .equals(procOggDTO.getCodOggetto()))
          {
            isIsvar = true;
          }
        break;
      }
    }

    if (isIsamm)
    {
      progressivoIstanza = progressivoIstanzaISAMM;
    }
    else
      if (isIsvar)
      {
        progressivoIstanza = progressivoIstanzaISVAR;
      }
      else
      {
        progressivoIstanza = progressivoIstanzaISAMB;
      }

    if (progressivoIstanza == 0)
    {
      progressivoIstanza = 1;
    }

    if (progressivoIstanza > 2)
      progressivoIstanza = 2;

    return progressivoIstanza;
  }

  @IuffiSecurity(value = "CU-IUFFI-232", controllo = IuffiSecurity.Controllo.NESSUNO)
  @RequestMapping(value = "/riepilogo", method = RequestMethod.GET)
  public String riepilogo(HttpSession session, Model model)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    model.addAttribute("po", po);

    refreshTestataProcedimento(quadroEJB, session, po.getIdProcedimento());

    return "approvazione/riepilogo";
  }

  private String checkProcOggetto(ProcedimentoOggetto procedimentoOggetto,
      List<StampaOggettoDTO> stampe) throws InternalUnexpectedException
  {
    if (procedimentoOggetto.getDataFine() == null
        || (procedimentoOggetto.getIdEsito() == null))
    {
      return "l'oggetto selezionato non risulta chiuso con esito positivo o negativo.";
    }
    else
      if (procedimentoOggetto.getIdStatoOggetto()
          .longValue() <= (new Long("10")).longValue())
      {
        return "l'oggetto selezionato non risulta essere nello stato corretto.";
      }

    try
    {
      quadroEJB.canUpdateProcedimentoOggetto(
          procedimentoOggetto.getIdProcedimentoOggetto(), true);
    }
    catch (IuffiPermissionException e)
    {
      if (e.getType().equals(ExceptionType.PROCEDIMENTO_CHIUSO))
      {
        return "sono scaduti i termini di presentazione.";
      }
    }
    catch (InternalUnexpectedException e)
    {
    }

    return null;
  }

  private String checkStampeFirmate(ProcedimentoOggetto procedimentoOggetto,
      List<StampaOggettoIconaDTO> stampe) throws InternalUnexpectedException
  {
    List<StampaOggettoDTO> stampafirmata = null;
    if (stampe != null)
    {
      for (StampaOggettoIconaDTO item : stampe)
      {
        stampafirmata = quadroEJB.getElencoStampeOggetto(
            procedimentoOggetto.getIdProcedimentoOggetto(),
            item.getIdOggettoIcona());
        if (stampafirmata == null || stampafirmata.size() <= 0
            || stampafirmata.get(0) == null)
        {
          return "E' necessario allegare il documento '"
              + item.getDescTipoDocumento()
              + "' prima di procedere con l'approvazione.";
        }
      }
    }
    return null;
  }

  private String controlloVerbali(ProcedimentoOggetto procedimentoOggetto,
      Model model, HttpSession session, String errore1, String errore2)
      throws InternalUnexpectedException
  {
    QuadroOggettoDTO quadro = procedimentoOggetto
        .findQuadroByCodiceQuadro("ESIFN");
    long idQuadroOggetto = 0;
    if (quadro != null)
    {
      idQuadroOggetto = quadro.getIdQuadroOggetto();
    }
    EsitoFinaleDTO esitoFinale = quadroEJB.getEsitoFinale(
        procedimentoOggetto.getIdProcedimentoOggetto(), idQuadroOggetto);
    if (esitoFinale != null && "S".equals(esitoFinale.getFlagAltreInfoAtto()))
    {
      if (GenericValidator.isBlankOrNull(esitoFinale.getNumeroAtto()) ||
          esitoFinale.getDataAmmissione() == null)
      {
        model.addAttribute("errore",
            "Impossibile procedere con l'approvazione: E' necessario inserire i dati relativi al documento utilizzato");
        return "dialog/soloErrore";
      }
      List<StampaOggettoIconaDTO> stampeConfigurate = quadroEJB
          .getElencoDocumentiStampeDaVerificare(
              procedimentoOggetto.getIdProcedimentoOggetto(), null,
              IuffiConstants.IUFFI.ID_CATEGORIA_DOC_VERBALI_IUF_SU_DOQUIAGRI);
      String msgError = checkStampeFirmate(procedimentoOggetto,
          stampeConfigurate);
      if (msgError == null)
      {
        return "approvazione/popupApprovazioneFuoriLinea";
      }
      else
      {
        model.addAttribute("errore",
            "Impossibile procedere con l'approvazione: "
                + HtmlUtils.htmlEscape(msgError));
        return "dialog/soloErrore";
      }
    }
    else
    {
      model.addAttribute("errore",
          "Impossibile procedere con l'approvazione: "
              + IuffiUtils.STRING.nvl(errore1) + " "
              + IuffiUtils.STRING.nvl(errore2));
      return "dialog/soloErrore";
    }
  }

}
