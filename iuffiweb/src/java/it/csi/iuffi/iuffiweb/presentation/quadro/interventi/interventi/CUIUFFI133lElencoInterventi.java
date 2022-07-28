package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.interventi;

import java.util.ArrayList;
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
@IuffiSecurity(value = "CU-IUFFI-133-L", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi133l")
public class CUIUFFI133lElencoInterventi extends Elenco
{

  @Override
  public String getCodiceQuadro()
  {
    return IuffiConstants.QUADRO.CODICE.INTERVENTI_INFRASTRUTTURE;
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
	List<String> valoriFlag = new ArrayList<>();
    List<RigaElencoInterventi> elenco = elenco_json(model, request);
    if(elenco!=null){
    	 for(RigaElencoInterventi riga : elenco){
	    	valoriFlag = interventiEJB.getFlagCanaleOpereCondotta(getProcedimentoOggettoFromSession(session).getIdProcedimentoOggetto(),
	    			riga.getIdIntervento());
	    	if(valoriFlag!=null && valoriFlag.size()>0){
	    		if("S".equals(valoriFlag.get(0))){riga.setFlagCanale("SÌ");} else{riga.setFlagCanale("NO");}
	    		if("S".equals(valoriFlag.get(1))){riga.setFlagOpereDiPresa("SÌ");} else{riga.setFlagOpereDiPresa("NO");}
	    		if("S".equals(valoriFlag.get(2))){riga.setFlagCondotta("SÌ");} else{riga.setFlagCondotta("NO");}
	    	}
	    	
	    }
    }   
    
    return new ModelAndView("excelElencoInterventiView", "elenco", elenco);
  }

	@Override
	protected void isInterventoWithDanni(Model model)
	{
		model.addAttribute("withDanni", Boolean.FALSE);
	}
}