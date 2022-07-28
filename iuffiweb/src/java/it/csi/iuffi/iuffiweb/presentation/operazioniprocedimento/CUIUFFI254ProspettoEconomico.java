package it.csi.iuffi.iuffiweb.presentation.operazioniprocedimento;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.ProspettoEconomicoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-254-L", controllo = IuffiSecurity.Controllo.DEFAULT)
@RequestMapping(value = "/cuiuffi254l")
public class CUIUFFI254ProspettoEconomico extends BaseController
{
  @Autowired
  private IQuadroEJB quadroEJB;

  @RequestMapping(value = "/index")
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException, ApplicationException
  {
    Procedimento procedimento = getProcedimentoFromSession(session);
    BandoDTO bando = quadroEJB.getInformazioniBando(procedimento.getIdBando());

    if (bando.getCodiceTipoBando().compareTo("I") != 0
        && bando.getCodiceTipoBando().compareTo("G") != 0)
    {
      // bando a premio - non abilitato
      model.addAttribute("errore",
          "Funzionalità disponibile solo per bandi di misure ad investimento.");
    }
    else
    {
      List<ProspettoEconomicoDTO> prospetto = quadroEJB
          .getProspettoEconomico(procedimento.getIdProcedimento());
      if (prospetto == null || prospetto.isEmpty())
        model.addAttribute("pagamentiNonPresenti",
            "Non sono presenti pagamenti approvati dall'ente istruttore in associazione al procedimento selezionato.");
      else
        model.addAttribute("prospetto", prospetto);
    }

    return "operazioniprocedimento/prospettoEconomico";
  }

}
