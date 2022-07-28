package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.base;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;

public abstract class Elimina extends BaseController
{
  @Autowired
  IInterventiEJB interventiEJB;

  @IsPopup
  @RequestMapping(value = "/conferma_elimina_intervento_{idIntervento}", method = RequestMethod.GET)
  public String confermaEliminaIntervento(Model model, HttpSession session,
      @ModelAttribute("idIntervento") @PathVariable("idIntervento") String idIntervento)
      throws InternalUnexpectedException
  {
    String[] ids = new String[]
    { idIntervento };
    model.addAttribute("ids", ids);
    model.addAttribute("len", 1);
    return "interventi/confermaElimina";
  }

  @IsPopup
  @RequestMapping(value = "/conferma_elimina_interventi", method = RequestMethod.GET)
  public String confermaEliminaInterventi(Model model,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    String[] idIntervento = request.getParameterValues("idIntervento");
    model.addAttribute("ids", idIntervento);
    model.addAttribute("len", idIntervento.length);
    return "interventi/confermaElimina";
  }

  @IsPopup
  @RequestMapping(value = "/elimina", method = RequestMethod.POST)
  public String eliminaIntervento(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    HttpSession session = request.getSession();
    List<Long> idIntervento = IuffiUtils.LIST
        .toListOfLong(request.getParameterValues("idIntervento"));
    LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(
        session);
    for(Long id : idIntervento){
    	String codiceIdentificativoIntervento = interventiEJB.getCodiceIdentificativoIntervento(id);
        if(IuffiConstants.PUNTEGGI.CODICE_INTERVENTI_PREVENZIONE.equalsIgnoreCase(codiceIdentificativoIntervento)){
        	model.addAttribute("errore", "Intervento di Prevenzione non eliminabile");
    		return "errore/utenteNonAutorizzato";
        }
    }
    
    interventiEJB.eliminaIntervento(
        logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(), idIntervento,
        logOperationOggettoQuadroDTO);
    return "dialog/success";
  }
}