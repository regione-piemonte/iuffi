package it.csi.iuffi.iuffiweb.presentation.superficicolture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.superficicolture.ControlloColturaDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureRiepilogoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-299-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi299l")
public class CUIUFFI299LVisualizzaSuperficiColture extends BaseController
{
	  @Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB = null;
	  
	  @RequestMapping(value = "/index", method = RequestMethod.GET)
	  public String index(HttpSession session, Model model) throws InternalUnexpectedException
	  {
		  List<String> tableNamesToRemove = new ArrayList<>();
		  tableNamesToRemove.add("tableSuperificiColturePlvVegetale");
		  tableNamesToRemove.add("tableDettaglioParticellareSuperificiColture");
		  cleanTableMapsInSession(session, tableNamesToRemove);
		  
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		  SuperficiColtureRiepilogoDTO elencoSuperficiColtureRiepilogo = quadroIuffiEJB.getSuperficiColtureRiepilogo(idProcedimentoOggetto);

		  Map<Long,StringBuilder> mapIdSuperficieColturaAnomalia = new HashMap<Long,StringBuilder>();
		  Map<Long,Map<String,Boolean>> mapIdSuperficieColturaBloccante = new HashMap<Long,Map<String,Boolean>>();
		  List<ControlloColturaDTO> elencoControlloColtura = quadroIuffiEJB.getListControlloColtura(idProcedimentoOggetto,null);
		  for(ControlloColturaDTO cc : elencoControlloColtura)
		  {
			  if(mapIdSuperficieColturaAnomalia.containsKey(cc.getIdSuperficieColtura()))
			  {
				  mapIdSuperficieColturaAnomalia
				  		.get(cc.getIdSuperficieColtura())
				  		.append("; ")
				  		.append(cc.getDescrizioneAnomalia());
				  
				  if(cc.getBloccante()!= null && !cc.getBloccante().equals(""))
				  {
					  mapIdSuperficieColturaBloccante.get(cc.getIdSuperficieColtura()).put(cc.getBloccante(),Boolean.TRUE);
				  }
			  }
			  else
			  {
				  StringBuilder sb = new StringBuilder(cc.getDescrizioneAnomalia());
				  mapIdSuperficieColturaAnomalia.put(cc.getIdSuperficieColtura(), sb);
				  Map<String,Boolean> map = new HashMap<String,Boolean>();
				  if(cc.getBloccante() != null && !cc.getBloccante().equals(""))
				  {
					  map.put(cc.getBloccante(),Boolean.TRUE);
				  }
				  mapIdSuperficieColturaBloccante.put(cc.getIdSuperficieColtura(), map);
			  }
		  }
		  model.addAttribute("elencoSuperficiColtureRiepilogo",elencoSuperficiColtureRiepilogo);
		  model.addAttribute("mapIdSuperficieColturaAnomalia",mapIdSuperficieColturaAnomalia);
		  model.addAttribute("mapIdSuperficieColturaBloccante",mapIdSuperficieColturaBloccante);
		  return "superficicolture/visualizzaSuperficiColture";
	  }
	  
	  @RequestMapping(value = "/get_elenco_superfici_colture_dettaglio.json", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public List<SuperficiColtureDettaglioDTO> getElencoSuperficiColtureDettaglio(HttpSession session, Model model) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		  List<SuperficiColtureDettaglioDTO> listSuperficiColtureDettaglio = quadroIuffiEJB.getListSuperficiColtureDettaglio(idProcedimentoOggetto);
		  if(listSuperficiColtureDettaglio == null)
		  {
			  listSuperficiColtureDettaglio = new ArrayList<SuperficiColtureDettaglioDTO>();
		  }
		  return listSuperficiColtureDettaglio;
	  }
}
