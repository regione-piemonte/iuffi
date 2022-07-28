package it.csi.iuffi.iuffiweb.presentation.quadro.pianoselettivocinghiali;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.PianoSelettivoDTO;
import it.csi.iuffi.iuffiweb.dto.danni.UnitaMisuraDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-2000-M", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi2000m")
public class CUIUFFI2000InserisciModificaPianoSelettivoBase extends BaseController
{

  @Autowired
  IQuadroEJB      quadroEJB;
  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;


  public String modificaInserisci(Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("elencoMetodiCensimento", quadroIuffiEJB.getElencoMetodiCensimento());
    return "pianoselettivocinghiali/inserisci";
  }

  public String conferma(Model model, HttpServletRequest request, String metodo) throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    Long censitiAdultiM = errors.validateMandatoryLong(request.getParameter("censitiAdultiM"), "censitiAdultiM");
    Long censitiAdultiF = errors.validateMandatoryLong(request.getParameter("censitiAdultiF"), "censitiAdultiF");
    Long censitiPiccoliStriati = errors.validateMandatoryLong(request.getParameter("censitiPiccoliStriati"), "censitiPiccoliStriati");
    Long censitiPiccoliRossi = errors.validateMandatoryLong(request.getParameter("censitiPiccoliRossi"), "censitiPiccoliRossi");
    Long prelievoAdultiM = errors.validateMandatoryLong(request.getParameter("prelievoAdultiM"), "prelievoAdultiM");
    Long prelievoAdultiF = errors.validateMandatoryLong(request.getParameter("prelievoAdultiF"), "prelievoAdultiF");
    Long prelievoPiccoli = errors.validateMandatoryLong(request.getParameter("prelievoPiccoli"), "prelievoPiccoli");
    
    Long idMetodoCensimento = errors.validateMandatoryLong(request.getParameter("idMetodoCensimento"), "idMetodoCensimento");
    BigDecimal valoreMetodoCensimento = errors.validateMandatoryBigDecimal(request.getParameter("valoreMetodoCensimento"), "valoreMetodoCensimento", 2);
    
    String altro = null;
    if(idMetodoCensimento!=null)
    {
      UnitaMisuraDTO um = quadroIuffiEJB.getUnitaMisuraByIdMetodoCensimento(idMetodoCensimento);
      if(um == null || um.getIdUnitaMisura()==25l || um.getIdMetodoCensimento() == 0) //altro
      {
        
        altro = request.getParameter("altro");
        errors.validateMandatoryFieldLength(altro, 1, 200, "altro");
      }idMetodoCensimento = um.getIdMetodoSpecie();
    }
    
    //validate date
    String[] elencoDate = request.getParameterValues("dataCensimento");
    List<Date> date = new ArrayList<>();
    if (elencoDate == null || elencoDate.length == 0)
    {
      errors.addError("dataCensimento", "Inserire almeno una data.");
      model.addAttribute("msgError", "Inserire almeno una data.");
    }
    else
    {
      for(String dataStr : elencoDate)
      {
        Date data = errors.validateDate(dataStr, "dataCensimento", true);
        date.add(data);
      }
    }
    
    PianoSelettivoDTO piano = new PianoSelettivoDTO();
    piano.setElencoDate(date);
   
    if (errors.isEmpty())
    {
      piano.setIdProcedimentoOggetto(getIdProcedimentoOggetto(request.getSession()));
      piano.setCensitiAdultiM(censitiAdultiM);
      piano.setCensitiAdultiF(censitiAdultiF);
      piano.setCensitiPiccoliRossi(censitiPiccoliRossi);
      piano.setPrelievoAdultiF(prelievoAdultiF);
      piano.setPrelievoAdultiM(prelievoAdultiM);
      piano.setPrelievoPiccoli(prelievoPiccoli);
      piano.setCensitiPiccoliStriati(censitiPiccoliStriati);
      piano.setIdMetodoCensimento(idMetodoCensimento);;
      piano.setDescrAltraMetod(altro);
      piano.setValoreMetodoCensimento(valoreMetodoCensimento);
      if("M".equals(metodo))
        quadroIuffiEJB.updateInfoCinghiale(piano,getLogOperationOggettoQuadroDTO(request.getSession()));
      if("I".equals(metodo))
        quadroIuffiEJB.inserisciInfoCinghiale(piano, getLogOperationOggettoQuadroDTO(request.getSession()));
      
      return "redirect:../cuiuffi2000v/index.do";
    }

    model.addAttribute("piano", piano);
    model.addAttribute("errors", errors);
    model.addAttribute("preferRequest", true);
    model.addAttribute("elencoMetodiCensimento", quadroIuffiEJB.getElencoMetodiCensimento());

    return "pianoselettivocinghiali/inserisci";
  }


}
