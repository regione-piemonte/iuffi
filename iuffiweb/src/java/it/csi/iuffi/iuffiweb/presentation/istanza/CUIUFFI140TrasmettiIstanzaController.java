package it.csi.iuffi.iuffiweb.presentation.istanza;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.stampa.StampaOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException.ExceptionType;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi140")
@IuffiSecurity(value = "CU-IUFFI-140", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO_CHIUSO)
public class CUIUFFI140TrasmettiIstanzaController extends BaseController
{

  @Autowired
  private IQuadroEJB quadroEJB = null;

  @RequestMapping(value = "popupindex", method = RequestMethod.GET)
  @IsPopup
  public String popupIndex(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    String msgError = null;
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    final long idProcedimentoOggetto = procedimentoOggetto
        .getIdProcedimentoOggetto();
    final UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(
        session);
    if (utenteAbilitazioni.getRuolo().isUtenteTitolareCf()
        || utenteAbilitazioni.getRuolo().isUtenteLegaleRappresentante())
    {
      // Se l'utente è un beneficiario in proprio (quindi con ruolo titolare CF
      // o legale rappresentante)
      // deve avere potere di firma per poter trasmettere la domanda in quanto
      // il sistema segna la domanda come firmata con firma semplice dall'utente
      // connesso
      final boolean beneficiarioConPotereDiFirma = quadroEJB
          .isBeneficiarioAbilitatoATrasmettere(
              utenteAbilitazioni.getCodiceFiscale(), idProcedimentoOggetto);
      if (!beneficiarioConPotereDiFirma)
      {
        model.addAttribute("errore",
            "L'utente corrente non è autorizzato a firmare per conto dell'azienda, impossibile proseguire con l'operazione di trasmissione");
        return "dialog/soloErrore";
      }
    }
    List<StampaOggettoDTO> stampe = quadroEJB
        .getElencoStampeOggetto(idProcedimentoOggetto, null);
    msgError = checkProcOggetto(procedimentoOggetto, stampe);
    Vector<DecodificaDTO<String>> lStampeInAttesaFirma = null;
    Vector<DecodificaDTO<String>> lStampeInAttesaFirmaElettr = null;

    if (msgError == null)
    {
      if (stampe != null)
      {
        for (StampaOggettoDTO item : stampe)
        {
          if (item.getIdStatoStampa()
              .longValue() == IuffiConstants.STATO.STAMPA.ID.IN_ATTESA_FIRMA_GRAFOMETRICA)
          {
            if (lStampeInAttesaFirma == null)
            {
              lStampeInAttesaFirma = new Vector<DecodificaDTO<String>>();
            }
            lStampeInAttesaFirma
                .add(new DecodificaDTO<String>(
                    IuffiConstants.STATO.STAMPA.DESCRIZIONE.IN_ATTESA_FIRMA_GRAFOMETRICA,
                    item.getDescTipoDocumento()));
          }
          else
            if (item.getIdStatoStampa()
                .longValue() == IuffiConstants.STATO.STAMPA.ID.IN_ATTESA_FIRMA_ELETTRONICA_LEGGERA)
            {
              if (lStampeInAttesaFirmaElettr == null)
              {
                lStampeInAttesaFirmaElettr = new Vector<DecodificaDTO<String>>();
              }
              lStampeInAttesaFirmaElettr
                  .add(new DecodificaDTO<String>(
                      IuffiConstants.STATO.STAMPA.DESCRIZIONE.IN_ATTESA_FIRMA_ELETTRONICA_LEGGERA,
                      item.getDescTipoDocumento()));
            }
            else
              if (item.getIdStatoStampa()
                  .longValue() == IuffiConstants.STATO.STAMPA.ID.IN_ATTESA_FIRMA_SU_CARTA)
              {
                if (lStampeInAttesaFirma == null)
                {
                  lStampeInAttesaFirma = new Vector<DecodificaDTO<String>>();
                }
                lStampeInAttesaFirma
                    .add(new DecodificaDTO<String>(
                        IuffiConstants.STATO.STAMPA.DESCRIZIONE.IN_ATTESA_FIRMA_SU_CARTA,
                        item.getDescTipoDocumento()));
              }
        }
      }
      Map<String, String> parametri = quadroEJB.getParametri(new String[] {IuffiConstants.PARAMETRO.FIRMA_SU_CARTA});
      String paramFirmaSuCarta = parametri.get(IuffiConstants.PARAMETRO.FIRMA_SU_CARTA);
      
      
      List<DecodificaDTO<String>> listaConsensi = new ArrayList<DecodificaDTO<String>>();
      listaConsensi.add(new DecodificaDTO<String>(IuffiConstants.SCELTA_FIRMA_TRASMISSIONE.FIRMA_DIGITALE, "sono firmati digitalmente"));
      listaConsensi.add(new DecodificaDTO<String>(IuffiConstants.SCELTA_FIRMA_TRASMISSIONE.FIRMA_CARTA, "sono firmati su carta"));
      
      if(paramFirmaSuCarta!=null && paramFirmaSuCarta.trim().length()>0) {
        listaConsensi.add(new DecodificaDTO<String>(IuffiConstants.SCELTA_FIRMA_TRASMISSIONE.PARAMETRO, paramFirmaSuCarta));
      }
      
      model.addAttribute("listaConsensi", listaConsensi);
      //model.addAttribute("testoFirmaSuCarta", testoFirmaSuCarta);
      model.addAttribute("lStampeInAttesaFirma", lStampeInAttesaFirma);
      model.addAttribute("lStampeInAttesaFirmaElettr",lStampeInAttesaFirmaElettr);
      return "trasmissione/popupTrasmissione";
    }
    else
    {
      model.addAttribute("errore",
          "Impossibile procedere con la trasmissione: " + msgError);
      return "dialog/soloErrore";
    }
  }

  @RequestMapping(value = "popupindex", method = RequestMethod.POST)
  @ResponseBody
  public String popupIndexPost(Model model, HttpSession session,
      HttpServletRequest request,  @RequestParam(value="note") String note) throws InternalUnexpectedException
  {
    Procedimento procedimento = getProcedimentoFromSession(session);
    long idProcedimentoAgricolo = procedimento.getIdProcedimentoAgricolo();
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    List<StampaOggettoDTO> stampe = quadroEJB.getElencoStampeOggetto(
        procedimentoOggetto.getIdProcedimentoOggetto(), null);
    String msgError = checkProcOggetto(procedimentoOggetto, stampe);
    if (msgError != null)
    {
      return "Impossibile procedere con la trasmissione: " + msgError;
    }
    if(note!=null && note.length()>4000){
    	return"Le note non possono superare i 4000 caratteri (attualmente il campo note contiene "+note.length()+" caratteri).";
    }
    if (!GenericValidator
        .isBlankOrNull(request.getParameter("elencoStampeAttesa")))
    {
      if (GenericValidator
          .isBlankOrNull(request.getParameter("radioConsensoFirme")))
        return "E' necessario selezionare il consenso riguardo  i documenti in attesa di firma grafometrica/su carta.";
    }
    
    
    String consensoFirma = request.getParameter("radioConsensoFirme");
    if(consensoFirma==null)
      consensoFirma = "";
    boolean trovataStampaFirmataDigitale = false;
    for(StampaOggettoDTO item : stampe) {
      if(String.valueOf(item.getIdStatoStampa().longValue()).equals(IuffiConstants.STATO.STAMPA.ID.FIRMATO_DIGITALMENTE+"") )
      {
        trovataStampaFirmataDigitale=true;
        continue;
      }
    }
    
    if(trovataStampaFirmataDigitale && consensoFirma.trim().length()<=0) {
      consensoFirma = IuffiConstants.SCELTA_FIRMA_TRASMISSIONE.FIRMA_DIGITALE;
    }
    
    if(consensoFirma.equals(IuffiConstants.SCELTA_FIRMA_TRASMISSIONE.FIRMA_DIGITALE) && !trovataStampaFirmataDigitale) {
      return "E' necessario caricare la stampa firmata digitalmente";
    }else if(!consensoFirma.equals(IuffiConstants.SCELTA_FIRMA_TRASMISSIONE.FIRMA_DIGITALE) && trovataStampaFirmataDigitale){
      return "E' stato allegato il documento con firma digitale, selezionare il radio button 'sono firmati digitalmente'";
    }
    
    
    
    if (quadroEJB.isPraticaInElencoPerTrasmissioneMassiva(procedimentoOggetto.getIdProcedimentoOggetto()))
    {   
      msgError = "la pratica risulta già essere in elenco per la trasmissione massiva, non è possibile trasmetterla manualmente";
    }
    else
    {
      msgError = quadroEJB.trasmettiIstanza(getIdProcedimento(session),
          procedimentoOggetto.getIdProcedimentoOggetto(),
          procedimentoOggetto.getIdBandoOggetto(), idProcedimentoAgricolo,
          procedimentoOggetto.getIdentificativo(),
          getUtenteAbilitazioni(session), note,consensoFirma);
    }
    
    
    
    // Se cerco di tx la prima istanza di questo gruppo - se c'è il tecnico
    // valorizzato, storicizzo e inserisco un record nuovo senza tecnico e senza
    // note
    if (procedimentoOggetto.getFlagIstanza().compareTo("S") == 0
        && quadroEJB.isPrimoOggettoDelGruppo(
            procedimentoOggetto.getIdProcedimento(),
            procedimentoOggetto.getCodiceRaggruppamento())
        && quadroEJB.hasTecnico(procedimentoOggetto.getIdProcedimento()))
    {
      quadroEJB.storicizzaProcedimentoOggetto(procedimentoOggetto,
          getUtenteAbilitazioni(session).getIdUtenteLogin());
    }

    if (msgError == null)
    {
      return "redirect:../cuiuffi140/riepilogo.do";

    }
    else
    {
      return "Impossibile procedere con la trasmissione: "
          + HtmlUtils.htmlEscape(msgError);
    }
  }

  @IuffiSecurity(value = "CU-IUFFI-140", controllo = IuffiSecurity.Controllo.NESSUNO)
  @RequestMapping(value = "/riepilogo", method = RequestMethod.GET)
  public String riepilogo(HttpSession session, Model model)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    model.addAttribute("po", po);

    refreshTestataProcedimento(quadroEJB, session, po.getIdProcedimento());
    return "trasmissione/riepilogo";
  }

  private String checkProcOggetto(ProcedimentoOggetto procedimentoOggetto,
      List<StampaOggettoDTO> stampe) throws InternalUnexpectedException
  {
    if (procedimentoOggetto.getDataFine() == null || (procedimentoOggetto
        .getIdEsito()
        .longValue() != IuffiConstants.PROCEDIMENTO_OGGETTO.ESITO.POSITIVO))
    {
      return "l'oggetto selezionato non risulta chiuso con esito positivo.";
    }
    else
    {
      if (procedimentoOggetto.getIdStatoOggetto()
          .longValue() >= (new Long("10")).longValue())
      {
        return "l'oggetto selezionato non risulta essere nello stato corretto.";
      }
    }
    if (IuffiUtils.VALIDATION
        .isEsitoOggettoNonRiapribile(procedimentoOggetto.getCodiceEsito()))
    {
      return "l'oggetto non è più apribile perchè già approvato ";
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

    if (stampe != null)
    {
      for (StampaOggettoDTO item : stampe)
      {
        if (item.getDataFineStampa() == null &&
            ((item.getIdStatoStampa()
                .longValue() == IuffiConstants.STATO.STAMPA.ID.GENERAZIONE_STAMPA_IN_CORSO)
                || (item.getIdStatoStampa()
                    .longValue() == IuffiConstants.STATO.STAMPA.ID.STAMPA_FALLITA)))
        {
          return "risultano esserci stampe collegate in corso o fallite.";
        }
      }
    }
    return null;
  }

}
