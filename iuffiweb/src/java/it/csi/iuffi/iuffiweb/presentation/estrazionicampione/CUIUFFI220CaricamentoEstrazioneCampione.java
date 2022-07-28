package it.csi.iuffi.iuffiweb.presentation.estrazionicampione;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IEstrazioniEJB;
import it.csi.iuffi.iuffiweb.dto.RigaFiltroDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin;

@Controller
@IuffiSecurity(value = "CU-IUFFI-220", controllo = IuffiSecurity.Controllo.DEFAULT)
@RequestMapping("cuiuffi220")
public class CUIUFFI220CaricamentoEstrazioneCampione extends BaseController
{

  @Autowired
  private IEstrazioniEJB estrazioniEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public final String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    addCommonData(model);
    return "estrazioniacampione/caricamento";
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public final String indexPost(Model model, HttpSession session,
      HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    Errors errors = new Errors();
    String idTipoEstrazione = request.getParameter("idTipoEstrazione");
    errors.validateMandatoryLong(idTipoEstrazione, "idTipoEstrazione");
    model.addAttribute("prfvalues", Boolean.TRUE);

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      return index(model, session);
    }
    else
    {
      long idUtenteLogin = new Long(getIdUtenteLogin(session));
      List<Long> l = new LinkedList<Long>();
      l.add(idUtenteLogin);
      List<UtenteLogin> utenti = super.loadRuoloDescr(l);
      long idNumeroLotto = estrazioniEJB.caricamentoEstrazioni(
          Long.parseLong(idTipoEstrazione), "Estrazione effettuata da "
              + getUtenteDescrizione(idUtenteLogin, utenti));
      return "redirect:../cuiuffi219/index_" + idNumeroLotto + ".do";
    }
  }

  private void addCommonData(Model model) throws InternalUnexpectedException
  {
    List<RigaFiltroDTO> elenco = estrazioniEJB
        .getElencoTipoEstrazioniCaricabili();
    if (elenco == null)
    {
      elenco = new ArrayList<>();
    }
    model.addAttribute("listTipoEstrazione", elenco);
  }

}
