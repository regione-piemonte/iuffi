package it.csi.iuffi.iuffiweb.presentation.danni;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.dto.danni.DanniDTO;
import it.csi.iuffi.iuffiweb.dto.danni.ParticelleDanniDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.QuadroIuffiDAO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-298-D", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi298d")
public class CUIUFFI298DDanniDettaglioController extends CUIUFFI298DanniBaseController
{
	  @RequestMapping(value = "/index_{idDannoAtm}", method = RequestMethod.GET)
	  public String index(
			  HttpServletRequest request, 
			  HttpSession session,
			  Model model,
			  @PathVariable("idDannoAtm") long idDannoAtm) throws InternalUnexpectedException
	  {
		long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		long[] arrayIdDannoAtm = new long[]{idDannoAtm};
		List<DanniDTO> danni = quadroIuffiEJB.getDanniByIdDannoAtm(arrayIdDannoAtm, idProcedimentoOggetto, getUtenteAbilitazioni(session).getIdProcedimento());
		DanniDTO danno = danni.get(0);
		List<Long> listIdDannoAtm = new ArrayList<Long>();
		List<Integer> listIdDannoConduzioni = QuadroIuffiDAO
				.getListDanniEquivalenti(IuffiConstants.DANNI.TERRENI_RIPRISTINABILI);
		listIdDannoAtm.add(danno.getIdDannoAtm());
		if (listIdDannoConduzioni.contains(danno.getIdDanno()))
		{
			model.addAttribute("isConduzioni", Boolean.TRUE);
		} 
		model.addAttribute("danno", danno);
		return "danni/dettaglioDanni";
	  }
	  
	  @RequestMapping(value = "/get_list_conduzioni_danno_{idDannoAtm}", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public List<ParticelleDanniDTO> getListConduzioniDanno(
			  HttpSession session, 
			  Model model,
			  @PathVariable("idDannoAtm") long idDannoAtm)  throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		  List<ParticelleDanniDTO> listConduzioniDanno = quadroIuffiEJB.getListConduzioniDanno(idProcedimentoOggetto, idDannoAtm);
		  return listConduzioniDanno;
	  }
}
