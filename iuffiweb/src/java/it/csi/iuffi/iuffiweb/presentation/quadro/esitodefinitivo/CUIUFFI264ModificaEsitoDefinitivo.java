package it.csi.iuffi.iuffiweb.presentation.quadro.esitodefinitivo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi264m")
@IuffiSecurity(value = "CU-IUFFI-264-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI264ModificaEsitoDefinitivo extends BaseController
{
  @Autowired
  IQuadroEJB quadroEJB = null;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String get(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);

    List<DecodificaDTO<Long>> elencoTecnici = quadroEJB.getElencoTecniciDisponibiliPerAmmCompetenza(po.getIdProcedimentoOggetto(),getUtenteAbilitazioni(session).getIdProcedimento());
    List<DecodificaDTO<Long>> elencoEsitiFinali = new ArrayList<>();// quadroEJB.getElencoEsiti(IuffiConstants.ESITO.TIPO.FINALE);
    List<DecodificaDTO<Long>> elencoEsitiDefinitivi = quadroEJB
        .getElencoEsiti(IuffiConstants.ESITO.TIPO.DEFINITIVO);
    List<DecodificaDTO<String>> ufficiZona = getListUfficiZonaFunzionari();

    EsitoFinaleDTO esito = quadroEJB
        .getEsitoDefinitivo(po.getIdProcedimentoOggetto());
    elencoEsitiFinali
        .add(new DecodificaDTO<>(esito.getIdEsito(), esito.getDescrEsito()));

    model.addAttribute("esito", esito);
    model.addAttribute("elencoEsiti", elencoEsitiFinali);
    model.addAttribute("elencoEsitiDefinitivi", elencoEsitiDefinitivi);
    model.addAttribute("elencoTecnici", elencoTecnici);
    model.addAttribute("elencoTecniciSup", elencoTecnici);
    model.addAttribute("ufficiZona", ufficiZona);

    return "esitodefinitivo/modifica";
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public String post(Model model, HttpSession session,
      HttpServletRequest request) throws Exception
  {

    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    Errors errors = new Errors();
    EsitoFinaleDTO esito = quadroEJB
        .getEsitoDefinitivo(po.getIdProcedimentoOggetto());

    Long idEsitoDefinitivo = errors.validateMandatoryLong(
        request.getParameter("idEsitoDefinitivo"), "idEsitoDefinitivo");
    String motivazione = request.getParameter("motivazione");

    if (errors.isEmpty())
      if (idEsitoDefinitivo != null)
        if (checkIfMotivazioniObbligatorie(idEsitoDefinitivo, model, session))
        {
          errors.validateMandatory(motivazione, "motivazione");
        }

    Long idGradoSup = errors.validateMandatoryLong(
        request.getParameter("idGradoSup"), "idGradoSup");
    Long idTecnico = errors
        .validateMandatoryLong(request.getParameter("idTecnico"), "idTecnico");

    if (!errors.isEmpty())
    {
      model.addAttribute("preferRequestValueEsito", Boolean.TRUE);
      model.addAttribute("errors", errors);
      return get(model, session);
    }

    esito.setExtIdFunzionarioGradoSup(idGradoSup);
    esito.setExtIdFunzionarioIstruttore(idTecnico);
    esito.setMotivazione(motivazione);
    esito.setIdEsitoDefinitivo(idEsitoDefinitivo);

    quadroEJB.updateEsitoDefinitivo(po.getIdProcedimentoOggetto(), esito,
        getLogOperationOggettoQuadroDTO(session), getIdUtenteLogin(session));

    return "redirect:../cuiuffi264l/index.do";
  }

  @RequestMapping(value = "/checkIfMotivazioniObbligatorie_{idEsito}", produces = "application/json")
  @ResponseBody
  public boolean checkIfMotivazioniObbligatorie(
      @PathVariable("idEsito") Long idEsito, Model model, HttpSession session)
      throws Exception
  {
    return quadroEJB.checkIfMotivazioniObbligatorieEsito(idEsito);
  }
}