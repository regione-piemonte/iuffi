package it.csi.iuffi.iuffiweb.presentation.quadro.consistenzaaziendale;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ConsistenzaAziendale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ProduzioneAnimale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ProduzioneVegetale;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/cuiuffi320m")
@IuffiSecurity(value = "CU-IUFFI-320-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI320ModificaConsistenzaAziendale extends BaseController{

	public static final String CU_NAME = "CU-IUFFI-320-M";
	
	  @Autowired
	  IQuadroIuffiEJB                 quadroIuffiEJB;
	  @Autowired
	  IQuadroEJB                 quadroEJB;

	  @RequestMapping("/index")
	  public String index(Model model, HttpSession session)
	      throws InternalUnexpectedException
	  { 
	    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
	        session);
	    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);
	    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
	    long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);
	    List<ProduzioneAnimale> listaProduzioniAnimali = quadroEJB.getProduzioniAnimali(idDatiProcedimento);
	    List<ProduzioneVegetale> listaProduzioniVegetali = quadroEJB.getProduzioniVegetali(idDatiProcedimento);
	    ConsistenzaAziendale datiConsistenza = new ConsistenzaAziendale();
	    
	    boolean datiInseritiA = false;
	    boolean datiInseritiV = false;
	    if((listaProduzioniAnimali != null && !listaProduzioniAnimali.isEmpty()) || (listaProduzioniVegetali != null && !listaProduzioniVegetali.isEmpty())) {
	      // Sono in modifica
	      if(listaProduzioniVegetali != null && !listaProduzioniVegetali.isEmpty()) {
	        datiInseritiV = true;
	        datiConsistenza.setProduzioniVegetali(listaProduzioniVegetali);
	        for (ProduzioneVegetale produzioneVegetale : listaProduzioniVegetali) {
	          produzioneVegetale.setIdconduzione(produzioneVegetale.getId());
	        }      
	      }
	      if(listaProduzioniAnimali != null && !listaProduzioniAnimali.isEmpty()) {
	        datiInseritiA = true;
	        datiConsistenza.setProduzioniAnimali(listaProduzioniAnimali);
	        for (ProduzioneAnimale produzioneAnimale : listaProduzioniAnimali) {
	          produzioneAnimale.setIdallevamento(produzioneAnimale.getId());
	        }      
	      }
	    }
	    else {
	      // Sono in inserimento
	      List<ProduzioneAnimale> listaProduzioniAnimaliInserimento = quadroEJB.getProduzioniAnimaliInserimento(idProcedimentoOggetto);
	      if(listaProduzioniAnimaliInserimento != null) {
	        datiInseritiA = true;
	        for (ProduzioneAnimale produzioneAnimale : listaProduzioniAnimaliInserimento) {
	          produzioneAnimale.setPubblica("No");
	        }
	        datiConsistenza.setProduzioniAnimali(listaProduzioniAnimaliInserimento);
	      }
	      
	      List<ProduzioneVegetale> listaProduzioniVegetaliInserimento = quadroEJB.getProduzioniVegetaliInserimento(idProcedimentoOggetto);
	      if(listaProduzioniVegetaliInserimento != null) {
	        datiInseritiV = true;
	        for (ProduzioneVegetale produzioneVegetale : listaProduzioniVegetaliInserimento) {
	          produzioneVegetale.setPubblica("No");
	        }
	        datiConsistenza.setProduzioniVegetali(listaProduzioniVegetaliInserimento);
	      }
	      
	      
	    }
	    model.addAttribute("datiConsistenza", datiConsistenza);
	    model.addAttribute("datiInseriti", datiInseritiA || datiInseritiV);
	    model.addAttribute("datiInseritiA", datiInseritiA);
	    model.addAttribute("datiInseritiV", datiInseritiV);
	    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB,
	        procedimentoOggetto.getIdProcedimentoOggetto(),
	        quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
	  
	    return "consistenzaaziendale/modificaConsistenzaAziendale";	  
	    }
	  
	  
	  
	  @RequestMapping("/confermaModifica")
	  public String confermaModifica(Model model, HttpSession session, HttpServletRequest request)
	      throws InternalUnexpectedException
	  { 
	    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
          session);
      long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
      long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);

      QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);
      List<ProduzioneAnimale> listaProduzioniAnimali = quadroEJB.getProduzioniAnimali(idDatiProcedimento);
      List<ProduzioneVegetale> listaProduzioniVegetali = quadroEJB.getProduzioniVegetali(idDatiProcedimento);
      if((listaProduzioniAnimali != null && !listaProduzioniAnimali.isEmpty()) || (listaProduzioniVegetali != null && !listaProduzioniVegetali.isEmpty())) {
        //UPDATE
        if(listaProduzioniVegetali != null) {
          for (ProduzioneVegetale produzioneVegetale : listaProduzioniVegetali) {
            produzioneVegetale.setIdconduzione(produzioneVegetale.getId());
            String pubblica = IuffiUtils.STRING.trim(request.getParameter("chkPubblico_" + produzioneVegetale.getIdconduzione()));
            if(pubblica == null) {
              pubblica = "N";
            }
            quadroIuffiEJB.updateConsistenzaAziendaleVegetale(getLogOperationOggettoQuadroDTO(session), produzioneVegetale.getId(), pubblica);
          }
        }
        if(listaProduzioniAnimali != null) {
          for (ProduzioneAnimale produzioneAnimale : listaProduzioniAnimali) {
            produzioneAnimale.setIdallevamento(produzioneAnimale.getId());
            String pubblica = IuffiUtils.STRING.trim(request.getParameter("chkPubblico_" + produzioneAnimale.getIdallevamento()));
            if(pubblica == null) {
              pubblica = "N";
            }
            quadroIuffiEJB.updateConsistenzaAziendaleAnimale(getLogOperationOggettoQuadroDTO(session), produzioneAnimale.getId(), pubblica);
          }
        }
      }
      else {
        List<ProduzioneAnimale> listaProduzioniAnimaliInserimento = quadroEJB.getProduzioniAnimaliInserimento(idProcedimentoOggetto);
        List<ProduzioneVegetale> listaProduzioniVegetaliInserimento = quadroEJB.getProduzioniVegetaliInserimento(idProcedimentoOggetto); 
        if(listaProduzioniVegetaliInserimento != null) {
          for (ProduzioneVegetale produzioneVegetale : listaProduzioniVegetaliInserimento) {
            String pubblica = IuffiUtils.STRING.trim(request.getParameter("chkPubblico_" + produzioneVegetale.getIdconduzione() + "_" + produzioneVegetale.getIdutilizzo()));
            if(pubblica == null) {
              pubblica = "N";
            }
            quadroIuffiEJB.insertConsistenzaAziendaleVegetale(getLogOperationOggettoQuadroDTO(session), idDatiProcedimento, produzioneVegetale.getIdconduzione(), produzioneVegetale.getIdutilizzo(), produzioneVegetale.getIdutilizzodichirato(), pubblica);
          }
        }
        if(listaProduzioniAnimaliInserimento != null) {
          for (ProduzioneAnimale produzioneAnimale : listaProduzioniAnimaliInserimento) {
            String pubblica = IuffiUtils.STRING.trim(request.getParameter("chkPubblico_" + produzioneAnimale.getIdallevamento()));
            if(pubblica == null) {
              pubblica = "N";
            }
            quadroIuffiEJB.insertConsistenzaAziendaleAnimale(getLogOperationOggettoQuadroDTO(session), idDatiProcedimento, produzioneAnimale.getIdallevamento(), produzioneAnimale.getIdspecieanimale(), produzioneAnimale.getIdcategoria(), pubblica);
          }
        }
      }
      
      model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB,
          idProcedimentoOggetto,
          quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
	   

      return "redirect:../cuiuffi320v/index.do";
	    
	  }
	  
}