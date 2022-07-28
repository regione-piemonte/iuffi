package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.danni;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaElencoInterventi;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base.Elenco;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-1304-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi1304l")
public class CUIUFFI1304LVisualizzaInterventi extends Elenco
{

  @Override
  public String getCodiceQuadro()
  {
    return IuffiConstants.QUADRO.CODICE.INTERVENTI;
  }

  @Override
  public String getFlagEscludiCatalogo()
  {
    return IuffiConstants.FLAGS.NO;
  }

  @Override
  protected boolean isBandoConPercentualeRiduzione(long idProcedimentoOggetto)
      throws InternalUnexpectedException
  {
    return interventiEJB.isBandoConPercentualeRiduzione(idProcedimentoOggetto);
  }

  @Override
  protected void addExtraAttributeToModel(Model model,
      HttpServletRequest request)
  {
    // Nessun attributo da aggiungere rispetto alla gestione standard
  }

  @RequestMapping(value = "elencoInterventiExcel")
  public ModelAndView downloadExcel(Model model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    List<RigaElencoInterventi> elenco = elenco_json(model, request);
    request.setAttribute("withDanni",true);
    return new ModelAndView("excelElencoInterventiView", "elenco", elenco);
  }

@Override
protected void isInterventoWithDanni(Model model)
{
	model.addAttribute("withDanni", Boolean.TRUE);
}
  
  
}