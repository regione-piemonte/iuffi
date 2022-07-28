package it.csi.iuffi.iuffiweb.presentation.chiusura;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.csi.iuffi.iuffiweb.business.IAsyncEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.AziendaDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.plsql.MainControlloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlli.ControlloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.controlli.FonteControlloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "CU-IUFFI-164", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi164")
public class CUIUFFI164ChiusuraOggettoDelProcedimentoNonIstanza
    extends BaseController
{
  public static final long ID_FONTE_CONTROLLO_SISTEMA_REGIONALE = 1;
  public static final long ID_ESITO_P                           = 1;
  @Autowired
  IQuadroEJB               quadroEJB                            = null;
  @Autowired
  IAsyncEJB                asyncEJB                             = null;

  @RequestMapping(value = "/index")
  public String index() throws InternalUnexpectedException
  {
    return "chiusuraoggetto/confermaChiusura";
  }

  @RequestMapping(value = "/attendere")
  public String attendere(Model model) throws InternalUnexpectedException
  {
    model.addAttribute("messaggio",
        "Attendere prego, il sistema sta eseguendo le verifiche necessarie alla chiusura dell'oggetto...");
    return "chiusuraoggetto/attenderePrego";
  }

  @RequestMapping(value = "/esegui_controlli")
  public String eseguiControlli(HttpSession session, Model model, @RequestParam(value="note") String note)
      throws InternalUnexpectedException, ApplicationException
  {
	  if(note!=null && note.length()>4000){
	    	return error(model,
	                "<br />Le note non possono superare i 4000 caratteri (attualmente il campo note contiene "+note.length()+" caratteri).");
	    }
    AziendaDTO aziendaDTO = quadroEJB
        .getDatiAziendaAgricolaProcedimento(getIdProcedimento(session), null);
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    aggiornaDatiAAEPSian(aziendaDTO, utenteAbilitazioni);
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    try
    {
      MainControlloDTO result = null;
      QuadroOggettoDTO quadro = po.findQuadroByCU("CU-IUFFI-166-V");
      EsitoFinaleDTO esito = null;
      result = quadroEJB.callMainControlli(po.getIdBandoOggetto(),
          po.getIdProcedimentoOggetto(), aziendaDTO.getIdAzienda(),
          utenteAbilitazioni.getIdUtenteLogin());

      if (quadro != null)
      {
        esito = quadroEJB.getEsitoFinale(po.getIdProcedimentoOggetto(),
            quadro.getIdQuadroOggetto());
        if (!quadroEJB.isOggettoIstanza(quadro.getIdOggetto()) && esito != null
            && esito
                .getIdEsito() == IuffiConstants.STATO.ESITO.ID.TIPO_ESITO_F.NEGATIVO)
        {
          if (!isAnomalieGiustificate(po) && result
              .getRisultato() == IuffiConstants.SQL.RESULT_CODE.ERRORE_GRAVE)
          {
            model.addAttribute("url", "../cuiuffi164/lista_errori_conferma.do");
            return "redirect";
          }
        }
      }
      switch (result.getRisultato())
      {
        case IuffiConstants.SQL.RESULT_CODE.NESSUN_ERRORE:
          return chiudiProcedimentoOggetto(model, po, utenteAbilitazioni,
              session, note);
        case IuffiConstants.SQL.RESULT_CODE.ERRORE_GRAVE:
          // Se le anomalie bloccanti sono state giustificate allora posso
          // andare avanti
          if (isAnomalieGiustificate(po))
          {
            return chiudiProcedimentoOggetto(model, po, utenteAbilitazioni,
                session, note);
          }
          else
          {
            model.addAttribute("url", "../cuiuffi164/lista_errori.do");
            model.addAttribute("error",
                "E' stato riscontrato almeno un errore grave. Impossibile chiudere l'oggetto");
            return "redirect";
          }
        case IuffiConstants.SQL.RESULT_CODE.ERRORE_CRITICO:
          return error(model,
              "<br />Si è verificato un errore di sistema nell'esecuzione dei controlli. Contattare l'assistenza tecnica comunicando il seguente messaggio: "
                  + result.getMessaggio());
        default:
          return error(model,
              "<br />Si è verificato un problema grave nell'esecuzione dei controlli dell'oggetto, se il problema persistesse contattare l'assistenza tecnica.");
      }
    }
    catch (ApplicationException e)
    {
      return error(model, e.getMessage());
    }
  }

  private boolean isAnomalieGiustificate(
      ProcedimentoOggetto procedimentoOggetto)
      throws InternalUnexpectedException
  {
    boolean anomalieGiustificate = true;
    List<FonteControlloDTO> fonteControlloDTOList = quadroEJB.getControlliList(
        procedimentoOggetto.getIdProcedimentoOggetto(),
        ((procedimentoOggetto.getDataFine() != null)
            ? procedimentoOggetto.getDataFine() : new Date()),
        true, true);
    if (fonteControlloDTOList.size() > 0)
    {
      for (FonteControlloDTO fonte : fonteControlloDTOList)
      {
        if (fonte.getIdFonteControllo() == 2)
        {
          // Controlli gis -> ignoro
          continue;
        }

        for (ControlloDTO controllo : fonte.getControlli())
        {
          if (controllo.getGravita() != null
              && controllo.getGravita().compareTo("B") == 0)
          {
            if (controllo.getIdSoluzioneAnomalia() == null
                || controllo.getIdSoluzioneAnomalia().longValue() == 0)
            {
              anomalieGiustificate = false;
              break;
            }
          }
        }
      }
    }
    return anomalieGiustificate;
  }

  @RequestMapping(value = "/chiudi_oggetto")
  public String chiudi(HttpSession session, Model model)
      throws InternalUnexpectedException, ApplicationException
  {
    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    return chiudiProcedimentoOggetto(model, po, utenteAbilitazioni, session, null);
  }

  public String error(Model model, String messaggio)
  {
    model.addAttribute("messaggio", messaggio);
    return "chiusuraoggetto/errore";
  }

  private String chiudiProcedimentoOggetto(Model model,
      ProcedimentoOggetto procedimentoOggetto,
      UtenteAbilitazioni utenteAbilitazioni, HttpSession session, String note)
      throws InternalUnexpectedException, ApplicationException
  {
    final long idProcedimentoOggetto = procedimentoOggetto
        .getIdProcedimentoOggetto();
    QuadroOggettoDTO quadro = procedimentoOggetto
        .findQuadroByCU("CU-IUFFI-166-V");
    Long idEsito = null;
    String tipoEsito = IuffiConstants.ESITO.TIPO.NON_ISTANZA;
    if (quadro != null)
    {
      EsitoFinaleDTO esito = quadroEJB.getEsitoFinale(idProcedimentoOggetto,
          quadro.getIdQuadroOggetto());
      if (esito != null)
      {
        idEsito = esito.getIdEsito();
        idEsito = quadroEJB.getIdEsitoEquivalente(idEsito, tipoEsito);
        if (idEsito == null)
        {
          // Questo è un errore di configurazione, se esiste un idEsito finale e
          // quindi è stato previsto il quadro esito finale DEVE ESSERE STATO
          // CONFIGURATO su db l'esito equivalente ==> Segnalo l'errore
          throw new ApplicationException(
              IuffiConstants.GENERIC_ERRORS.ERRORE_DI_SISTEMA
                  + ". Per procedere con la chiusura è necessario contattare"
                  + " l'assistenza tecnica comunicando il seguente messaggio: "
                  + "Errata configurazione delle decodifiche degli esiti finali, non è stato trovato un esito equivalente di tipo \""
                  + tipoEsito +
                  "\" per l'id esito " + esito.getIdEsito());
        }
        
      }
      else
      {
        throw new ApplicationException(
            "Impossibile procedere con la chiusura. E' necessario inserire un Esito finale");
      }
    }
    else
    {
      EsitoFinaleDTO esito = quadroEJB.getEsitoFinale(idProcedimentoOggetto);
      if (esito != null && esito.getIdEsitoLong() != null
          && esito.getIdEsitoLong().longValue() > 0)
      {
        idEsito = quadroEJB.getIdEsitoEquivalente(esito.getIdEsitoLong(),
            tipoEsito);
        if (idEsito == null)
        {
          // Questo è un errore di configurazione, se esiste un idEsito finale e
          // quindi è stato previsto il quadro esito finale DEVE ESSERE STATO
          // CONFIGURATO su db l'esito equivalente ==> Segnalo l'errore
          throw new ApplicationException(
              IuffiConstants.GENERIC_ERRORS.ERRORE_DI_SISTEMA
                  + ". Per procedere con la chiusura è necessario contattare"
                  + " l'assistenza tecnica comunicando il seguente messaggio: "
                  + "Errata configurazione delle decodifiche degli esiti finali, non è stato trovato un esito equivalente di tipo \""
                  + tipoEsito +
                  "\" per l'id esito " + esito.getIdEsito());
        }
      }
      else
        if (esito != null && (esito.getIdEsitoLong() == null
            || esito.getIdEsitoLong().longValue() == 0))
        {
          throw new ApplicationException(
              "Impossibile procedere con la chiusura. E' necessario inserire un Esito finale");
        }
        else
        {
          /*
           * Jira IUFFI-334 In mancanza del rek sulla tabella
           * IUF_T_ESITO_TECNICO recuperare il valore di default dato da
           * ID_ESITO del rek della tabella IUF_D_ESITO con TIPO_ESITO = 'O' e
           * CODICE = 'C'
           */
          List<DecodificaDTO<Long>> esiti = quadroEJB.getElencoEsiti(tipoEsito);
          for (DecodificaDTO<Long> tmp : esiti)
          {
            if ("C".equals(tmp.getCodice()))
            {
              idEsito = tmp.getId();
              break;
            }
          }
        }
    }
    DecodificaDTO<Integer> result = quadroEJB.chiudiOggetto(
        idProcedimentoOggetto, idEsito, note, utenteAbilitazioni);
    refreshTestataProcedimento(quadroEJB, session,
        procedimentoOggetto.getIdProcedimento());

    switch (result.getId())
    {
      case IuffiConstants.SQL.RESULT_CODE.NESSUN_ERRORE:
        asyncEJB.generaStampePerProcedimento(idProcedimentoOggetto,
            procedimentoOggetto.getIdProcedimento());
        model.addAttribute("url", "../cuiuffi164/riepilogo.do");
        model.addAttribute("success",
            "Chiusura oggetto terminata correttamente");
        return "redirect";
      case IuffiConstants.SQL.RESULT_CODE.ERRORE_CRITICO:
        return error(
            model,
            "<br />Si è verificato un errore di sistema. Contattare l'assistenza tecnica comunicando il seguente messaggio: "
                + result.getDescrizione());
      case IuffiConstants.SQL.RESULT_CODE.ERRORE_GRAVE:
        return error(
            model,
            "<br />E' stato riscontrato il seguente errore: "
                + result.getDescrizione());
      default:
        return error(
            model,
            "<br />Si è verificato un problema grave nel richiamo dei controlli dell'oggetto, se il problema persistesse contattare l'assistenza tecnica comunicando il seguente messaggio: codice di errore non previsto "
                + result.getId());
    }
  }

  @RequestMapping(value = "/lista_errori_conferma")
  public String listaErroriConferma(HttpSession session, Model model)
      throws InternalUnexpectedException
  {
    model.addAttribute("msgInfoConferma",
        "E' stato riscontrato almeno un errore grave, vuoi proseguire ugualmente con la chiusura dell'oggetto?");
    List<FonteControlloDTO> fonteControlloDTOList = quadroEJB
        .getControlliList(getIdProcedimentoOggetto(session), null, true);
    if (fonteControlloDTOList != null)
    {
      for (FonteControlloDTO fonte : fonteControlloDTOList)
      {
        if (fonte.getIdFonteControllo() == ID_FONTE_CONTROLLO_SISTEMA_REGIONALE)
        {
          model.addAttribute("fonte", fonte);
          break;
        }
      }
    }
    return "chiusuraoggetto/listaErrori";
  }

  @RequestMapping(value = "/lista_errori")
  public String listaErrori(HttpSession session, Model model)
      throws InternalUnexpectedException
  {
    List<FonteControlloDTO> fonteControlloDTOList = quadroEJB
        .getControlliList(getIdProcedimentoOggetto(session), null, true);
    if (fonteControlloDTOList != null)
    {
      for (FonteControlloDTO fonte : fonteControlloDTOList)
      {
    	  List<ControlloDTO> controlliNonNulli = new ArrayList<>();
    	  if(fonte!=null && fonte.getControlli()!=null){
    		  for(ControlloDTO controllo : fonte.getControlli()){
        		  if(!("N").equals(controllo.getGravita())){
        			  controlliNonNulli.add(controllo);
        		  }
        	  } 
    		  fonte.setControlli(controlliNonNulli);
    	  }
    	  
        if (fonte.getIdFonteControllo() == ID_FONTE_CONTROLLO_SISTEMA_REGIONALE)
        {
          model.addAttribute("fonte", fonte);
          break;
        }
      }
    }
    return "chiusuraoggetto/listaErrori";
  }

  @IuffiSecurity(value = "CU-IUFFI-164", controllo = IuffiSecurity.Controllo.NESSUNO)
  @RequestMapping(value = "/riepilogo")
  public String riepilogo(HttpSession session, Model model)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    model.addAttribute("po", po);
    return "chiusuraoggetto/riepilogo";
  }

}