package it.csi.iuffi.iuffiweb.presentation.quadro.documentiRichiesti;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DocumentiRichiestiDTO;
import it.csi.iuffi.iuffiweb.dto.SezioneDocumentiRichiestiDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/cuiuffi308l")
@IuffiSecurity(value = "CU-IUFFI-308-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI308LVisualizzaDocumentiRichiestiController extends BaseController{

	 public static final String CU_NAME = "CU-IUFFI-308-L";
	  @Autowired
	  IQuadroIuffiEJB                 quadroIuffiEJB;
	  

	  @RequestMapping("/index")
	  public String index(Model model, HttpSession session)
	      throws InternalUnexpectedException
	  { 
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		  List<SezioneDocumentiRichiestiDTO> sezioniDaVisualizzare = quadroIuffiEJB.getListDocumentiRichiestiDaVisualizzare(idProcedimentoOggetto, true);
		  if(sezioniDaVisualizzare==null) model.addAttribute("numeroDoc", 0);
		  else model.addAttribute("numeroDoc", sezioniDaVisualizzare.size());
		  model.addAttribute("sezioniDaVisualizzare", sezioniDaVisualizzare);
		  model.addAttribute("visualizzazione", Boolean.TRUE);
	    return "documentiRichiesti/visualizzaDocumentiRichiesti";
	  }
	  
	  

	public List<DocumentiRichiestiDTO> getListDocumentiRichiesti(long idProcedimentoOggetto) throws InternalUnexpectedException
	  {
		  List<DocumentiRichiestiDTO> documentiRichiesti = quadroIuffiEJB.getDocumentiRichiesti(idProcedimentoOggetto);
		  return documentiRichiesti;
	  }
	
}
