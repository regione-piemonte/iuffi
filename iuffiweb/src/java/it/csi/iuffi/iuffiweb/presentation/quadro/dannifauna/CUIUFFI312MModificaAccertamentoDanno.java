package it.csi.iuffi.iuffiweb.presentation.quadro.dannifauna;




import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.danniFauna.AccertamentoDannoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "CU-IUFFI-312-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi312m")
public class CUIUFFI312MModificaAccertamentoDanno extends CUIUFFI312BaseController
{
  protected static final String FIELD_NAME_ID     = "idAccertamentoDanno";
  protected static final String FIELD_NAME_SOPRALLUOGO     = "sopralluogo";
  protected static final String FIELD_NAME_DATA            = "dataSopralluogo";
  protected static final String FIELD_NAME_PERITO          = "perito";
  protected static final String FIELD_NAME_NUMERO          = "numeroPerizia";
  protected static final String FIELD_NAME_IMPORTOTA       = "importoTotaleAccertato";
  protected static final String FIELD_NAME_IMPORTORIP      = "importoRipristino";
  protected static final String FIELD_NAME_IMPORTOTOTDALIQ = "importoTotaleDaLiquidare";
  protected static final String FIELD_NAME_SPESEPERIZIA    = "spesePerizia";
  protected static final String FIELD_NAME_SPESEPREV       = "spesePrevenzione";
  protected static final String FIELD_NAME_DESCRIZIONE     = "descrizionePrevenzione";
  protected static final String FIELD_NAME_REITERATI       = "reiteratiDanni";
  protected static final String FIELD_NAME_ESITO           = "esitoDomanda";
  protected static final String FIELD_NAME_FUNZIONARIO     = "extIdFunzionarioIstruttore";
  protected static final String FIELD_NAME_NOTE            = "note";

  protected static final String ESITO_POSITIVO             = "Positivo";
  protected static final String ESITO_NEGATIVO             = "Negativo";

  @Autowired
  protected IQuadroIuffiEJB     quadroIuffiEJB;
  
  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String index(HttpServletRequest request, HttpSession session,
      Model model) throws InternalUnexpectedException
  {
    long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    AccertamentoDannoDTO acdan = quadroIuffiEJB.getAccertamentoDanno(idProcedimentoOggetto);
    BigDecimal importoTotaleAccertato = quadroIuffiEJB.getImportoTotaleAccertato(idProcedimentoOggetto);
    acdan.setImportoTotaleAccertato(importoTotaleAccertato);
    model.addAttribute("acdan", acdan);
    model.addAttribute("isModifica", true);
    common(request, session, model);
    return "danniFauna/inserisciAccertamentoDanno";
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public String delete(HttpServletRequest request, HttpSession session,
      Model model) throws InternalUnexpectedException
  {
    long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    AccertamentoDannoDTO acdan = quadroIuffiEJB.getAccertamentoDanno(idProcedimentoOggetto);
    quadroIuffiEJB.eliminaAccertamentoDanno(acdan.getIdAccertamentoDanno());
    return "danniFauna/visualizzaAccertamentoDanno";
  }
  
  @RequestMapping(value = "popupConfermaDelete", method = RequestMethod.GET)
  @IsPopup
  public String popupIndex(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    setModelDialogWarning(model,
        "Si procederà con l'eliminazione di questo accertamento danno, vuoi continuare ?",
        "../cuiuffi312m/delete.do");
    return "dialog/conferma";
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public String conferma(HttpServletRequest request, HttpSession session,
      Model model) throws InternalUnexpectedException
  {
    
    AccertamentoDannoDTO acdan = new AccertamentoDannoDTO();
    Errors errors = validaInserimento(request, acdan);

    if (errors.addToModelIfNotEmpty(model))
    {
      common(request, session, model);
      long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
      BigDecimal importoTotaleAccertato = quadroIuffiEJB
          .getImportoTotaleAccertato(idProcedimentoOggetto);
      acdan.setImportoTotaleAccertato(importoTotaleAccertato);
      model.addAttribute("acdan", acdan);
      model.addAttribute("isModifica", true);
      model.addAttribute("preferRequest", Boolean.TRUE);
      return "danniFauna/inserisciAccertamentoDanno";
    }
    
    long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    
    UtenteAbilitazioni utente = getUtenteAbilitazioni(session);
    quadroIuffiEJB.modificaAccertamentoDanno(acdan, idProcedimentoOggetto, utente.getIdUtenteLogin().longValue());
    acdan = quadroIuffiEJB.getAccertamentoDanno(idProcedimentoOggetto);
    BigDecimal importoTotaleAccertato = quadroIuffiEJB.getImportoTotaleAccertato(idProcedimentoOggetto);
    acdan.setImportoTotaleAccertato(importoTotaleAccertato);
    
    model.addAttribute("acdan", acdan);
    return "danniFauna/visualizzaAccertamentoDanno";
  }

	  
}
