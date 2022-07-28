package it.csi.iuffi.iuffiweb.presentation.quadro.dannicolture;




import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.assicurazionicolture.AssicurazioniColtureDTO;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.ColtureAziendaliDTO;
import it.csi.iuffi.iuffiweb.dto.dannicolture.DanniColtureDTO;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColtureDettaglioDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-307-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi307l")
public class CUIUFFI307LVisualizzaDanniColture extends BaseController
{
	  @Autowired
	  IQuadroIuffiEJB quadroIuffiEJB;
	  
	  @RequestMapping(value = "/index", method = RequestMethod.GET)
	  public String index(HttpSession session, 
			  HttpServletRequest request,
			  Model model) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		  long idProcedimento = getIdProcedimento(session);
		  
		  
		  List<SuperficiColtureDettaglioDTO> superfici = quadroIuffiEJB.getListSuperficiColtureDettaglio(idProcedimentoOggetto);
		  Long nColtureDanneggiate = quadroIuffiEJB.getNColtureDanneggiate(idProcedimentoOggetto);
		  model.addAttribute("nColtureDanneggiate", nColtureDanneggiate);
		  if(superfici == null || superfici.size() == 0)
		  {
			  model.addAttribute("hasDanni",Boolean.FALSE);
		  }
		  else
		  {
			  model.addAttribute("hasDanni",Boolean.TRUE);
		  }
		  
		  AssicurazioniColtureDTO riepilogoAssicurazioni = quadroIuffiEJB.getRiepilogoAssicurazioniColture(idProcedimentoOggetto);
		  model.addAttribute("riepilogoAssicurazioni", riepilogoAssicurazioni);
		  
		  ColtureAziendaliDTO riepilogo = quadroIuffiEJB.getRiepilogoColtureAziendali(idProcedimentoOggetto,idProcedimento);
		  LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(session);
		  String ultimaModifica = getUltimaModifica(quadroIuffiEJB, logOperationOggettoQuadroDTO.getIdProcedimentoOggetto(), logOperationOggettoQuadroDTO.getIdQuadroOggetto(),logOperationOggettoQuadroDTO.getIdBandoOggetto());
		  
		  //assicurazioni
		  List<AssicurazioniColtureDTO> listAssicurazioniColture = quadroIuffiEJB.getListAssicurazioniColture(idProcedimentoOggetto);
		  AssicurazioniColtureDTO assicurazioniColture = new AssicurazioniColtureDTO();
		  if(listAssicurazioniColture != null && listAssicurazioniColture.size()>0)
		  {
			  BigDecimal sommaImportoPremio=BigDecimal.ZERO,sommaImportoAssicurato=BigDecimal.ZERO,sommaImportoRimborso=BigDecimal.ZERO;
			  for(AssicurazioniColtureDTO assic : listAssicurazioniColture)
			  {
				  sommaImportoPremio = sommaImportoPremio.add(assic.getImportoPremio());
				  sommaImportoAssicurato = sommaImportoAssicurato.add(assic.getImportoAssicurato());
				  sommaImportoRimborso = sommaImportoRimborso.add(assic.getImportoRimborso());
			  }
			  assicurazioniColture.setImportoPremio(sommaImportoPremio);
			  assicurazioniColture.setImportoAssicurato(sommaImportoAssicurato);
			  assicurazioniColture.setImportoRimborso(sommaImportoRimborso);
		  }
		  else
		  {
			  assicurazioniColture.setImportoPremio(BigDecimal.ZERO);
			  assicurazioniColture.setImportoAssicurato(BigDecimal.ZERO);
			  assicurazioniColture.setImportoRimborso(BigDecimal.ZERO);
		  }
		  
		  if(riepilogo.getTotalePlvEffettiva() != null)
		  {
			  riepilogo.setPlvRicalcolataConAssicurazioni(
					  riepilogo.getTotalePlvEffettiva().subtract(assicurazioniColture.getImportoPremio()).add(assicurazioniColture.getImportoRimborso())
					  );
		  }
		  else
		  {
			  riepilogo.setPlvRicalcolataConAssicurazioni(null);

		  }
		  if(riepilogo.getTotalePlvOrdinaria() != null)
		  {
			  riepilogo.setPlvOrdinariaConAssicurazioni(
					  riepilogo.getTotalePlvOrdinaria().subtract(
							  IuffiUtils.NUMBERS.nvl(riepilogo.getPlvRicalcolataConAssicurazioni()))
					  );
		  }
		  else
		  {
			  riepilogo.setPlvOrdinariaConAssicurazioni(null);
		  }
		  
		  if(riepilogo.getTotalePlvOrdinaria() == null || riepilogo.getTotalePlvOrdinaria().equals(BigDecimal.ZERO))
		  {
			  riepilogo.setPercentualeDannoConAssicurazioni(BigDecimal.ZERO);
		  }
		  else
		  {
			  riepilogo.setPercentualeDannoConAssicurazioni(
					  riepilogo.getPlvOrdinariaConAssicurazioni().divide(riepilogo.getTotalePlvOrdinaria(), MathContext.DECIMAL64).multiply(new BigDecimal("100.00"))
					  );
		  }
		  
		  model.addAttribute("assicurazioniColture", assicurazioniColture);
		  model.addAttribute("riepilogo", riepilogo);
		  model.addAttribute("ultimaModifica", ultimaModifica);
		  return "dannicolture/visualizzaDanniColture";
	  }
	  
	  @RequestMapping(value = "/get_list_danni_colture.json", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public List<DanniColtureDTO> getListDanniColture(HttpSession session, Model model) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		  List<DanniColtureDTO> danniColture = quadroIuffiEJB.getListDanniColture(idProcedimentoOggetto,getIdProcedimento(session));;
		  return danniColture;
	  }
}
