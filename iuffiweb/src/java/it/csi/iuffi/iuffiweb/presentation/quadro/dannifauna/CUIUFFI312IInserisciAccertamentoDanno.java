package it.csi.iuffi.iuffiweb.presentation.quadro.dannifauna;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.danniFauna.AccertamentoDannoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "CU-IUFFI-312-I", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi312i")
public class CUIUFFI312IInserisciAccertamentoDanno
    extends CUIUFFI312BaseController
{

  @Autowired
  protected IQuadroIuffiEJB     quadroIuffiEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String index(HttpServletRequest request, HttpSession session,
      Model model) throws InternalUnexpectedException
  {
    common(request, session, model);

    AccertamentoDannoDTO acdan = new AccertamentoDannoDTO();
    long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    BigDecimal importoTotaleAccertato = quadroIuffiEJB
        .getImportoTotaleAccertato(idProcedimentoOggetto);
    acdan.setImportoTotaleAccertato(importoTotaleAccertato);
    model.addAttribute("acdan", acdan);

    return "danniFauna/inserisciAccertamentoDanno";
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
      model.addAttribute("preferRequest", Boolean.TRUE);
      return "danniFauna/inserisciAccertamentoDanno";
    }

    long idProcedimentoOggetto = getIdProcedimentoOggetto(session);

    UtenteAbilitazioni utente = getUtenteAbilitazioni(session);
    quadroIuffiEJB.inserisciAccertamentoDanno(acdan, idProcedimentoOggetto,
        utente.getIdUtenteLogin().longValue());
    acdan = quadroIuffiEJB.getAccertamentoDanno(idProcedimentoOggetto);
    BigDecimal importoTotaleAccertato = quadroIuffiEJB
        .getImportoTotaleAccertato(idProcedimentoOggetto);
    acdan.setImportoTotaleAccertato(importoTotaleAccertato);

    model.addAttribute("acdan", acdan);
    return "danniFauna/visualizzaAccertamentoDanno";
  }

}
