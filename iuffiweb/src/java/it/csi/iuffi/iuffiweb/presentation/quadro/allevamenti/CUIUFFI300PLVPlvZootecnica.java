package it.csi.iuffi.iuffiweb.presentation.quadro.allevamenti;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDettaglioPlvDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-300-PLV", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping("/cuiuffi300plv")
public class CUIUFFI300PLVPlvZootecnica extends BaseController
{
	  @Autowired
	  private IQuadroIuffiEJB quadroIuffiEJB = null;
	  
	  @RequestMapping(value = "/index", method = RequestMethod.GET)
	  public String index(HttpSession session, Model model) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
		  BigDecimal ufProdotte = quadroIuffiEJB.getPlvZootecnicaUfProdotte(idProcedimentoOggetto);
		  BigDecimal ufNecessarie = quadroIuffiEJB.getPlvZootecnicaUfNecessarie(idProcedimentoOggetto);
		  BigDecimal autosufficenzaForaggera;
		  BigDecimal zero = new BigDecimal("0.0");
		  if(ufNecessarie.compareTo(zero) == 0 && ufProdotte.compareTo(zero) == 0)
		  {
			  autosufficenzaForaggera = zero;
		  }
		  else if(ufNecessarie.compareTo(zero) == 0 && ufProdotte.compareTo(zero) > 0)
		  {
			  autosufficenzaForaggera = new BigDecimal("100.0");
		  }
		  else
		  {
			  autosufficenzaForaggera = ufProdotte.divide(ufNecessarie).multiply(new BigDecimal("100.0"));
		  }
		  BigDecimal rapportoUbaSau = getRapportoUbaSau(idProcedimentoOggetto);
		  
		  model.addAttribute("ufProdotte", IuffiUtils.FORMAT.formatDecimal2(ufProdotte));
		  model.addAttribute("ufNecessarie", IuffiUtils.FORMAT.formatDecimal2(ufNecessarie));
		  model.addAttribute("autosufficenzaForaggera", IuffiUtils.FORMAT.formatDecimal2(autosufficenzaForaggera));
		  model.addAttribute("rapportoUbaSau", IuffiUtils.FORMAT.formatDecimal4(rapportoUbaSau));
		  return "allevamenti/visualizzaPlvZootecnica";
	  }

	private BigDecimal getRapportoUbaSau(long idProcedimentoOggetto) throws InternalUnexpectedException
	{
		  BigDecimal uba = quadroIuffiEJB.getPlvZootecnicaUba(idProcedimentoOggetto);
		  BigDecimal sau = quadroIuffiEJB.getPlvZootecnicaSau(idProcedimentoOggetto);
		  BigDecimal rapportoUbaSau = BigDecimal.ZERO;
		  if(uba.compareTo(BigDecimal.ZERO) == 0
				  || sau.compareTo(BigDecimal.ZERO)== 0 )
		  {
			  rapportoUbaSau = BigDecimal.ZERO;
		  }
		  else
		  {
			  rapportoUbaSau = uba.divide(sau,MathContext.DECIMAL128).setScale(4,RoundingMode.HALF_UP);
		  }
		  return rapportoUbaSau;
	}
	  
	  @RequestMapping(value = "/get_list_plv_zootecnica_dettaglio_allevamenti.json", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public List<AllevamentiDettaglioPlvDTO> getListPlvZootecnicaDettaglioAllevamenti(HttpSession session, Model model) throws InternalUnexpectedException
	  {
		  long idProcedimentoOggetto = getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto();
		  List<AllevamentiDettaglioPlvDTO> lista =  quadroIuffiEJB.getListPlvZootecnicaDettaglioAllevamenti(idProcedimentoOggetto);
		  return lista;
	  }
}
