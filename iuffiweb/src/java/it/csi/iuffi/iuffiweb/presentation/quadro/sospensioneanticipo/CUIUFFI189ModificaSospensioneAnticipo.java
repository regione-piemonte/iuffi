package it.csi.iuffi.iuffiweb.presentation.quadro.sospensioneanticipo;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.Radio;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.anticipo.SospensioneAnticipoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-189", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("cuiuffi189")
public class CUIUFFI189ModificaSospensioneAnticipo extends BaseController
{

  @Autowired
  private IQuadroEJB quadroEjb;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public final String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    List<Radio> r = new LinkedList<Radio>();
    r.add(new Radio("S", "Sì"));
    r.add(new Radio("N", "No"));
    model.addAttribute("radio", r);
    List<SospensioneAnticipoDTO> elenco = quadroEjb
        .getElencoSospensioniAnticipoDisponibili(
            getIdProcedimentoOggetto(session));
    model.addAttribute("elenco", elenco);
    return "sospensioneanticipo/modificaSospensioneAnticipo";
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public final String conferma(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("prfvalues", Boolean.TRUE);
    Errors errors = new Errors();

    String flagSospensione = "";
    String motivazione = "";

    List<SospensioneAnticipoDTO> elenco = quadroEjb
        .getElencoSospensioniAnticipoDisponibili(
            getIdProcedimentoOggetto(session));
    for (int i = 0; i < elenco.size(); i++)
    {
      flagSospensione = request.getParameter("sospensione_" + i);
      motivazione = request.getParameter("motivazione_" + i);
      errors.validateMandatory(flagSospensione, "sospensione_" + i);

      if ("S".equals(flagSospensione))
      {
        errors.validateMandatoryFieldMaxLength(motivazione, "motivazione_" + i,
            1000);
      }
      else
      {
        errors.validateFieldMaxLength(motivazione, "motivazione_" + i, 1000);
      }

      if (errors.isEmpty())
      {
        elenco.get(i).setFlagSospensione(flagSospensione);
        elenco.get(i).setMotivazione(motivazione);
      }
    }

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      return index(model, session);
    }
    else
    {
      quadroEjb.updateSospensioneAnticipo(getIdProcedimentoOggetto(session),
          elenco, getLogOperationOggettoQuadroDTO(session));
      return "redirect:../cuiuffi188/index.do";
    }
  }

}
