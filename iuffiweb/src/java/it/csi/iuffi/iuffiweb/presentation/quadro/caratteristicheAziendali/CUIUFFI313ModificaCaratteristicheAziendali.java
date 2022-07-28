package it.csi.iuffi.iuffiweb.presentation.quadro.caratteristicheAziendali;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.permission.UpdatePermissionProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.caratteristicheaziendali.CaratteristicheAziendali;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.caratteristicheaziendali.OrganismoControllo;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi313m")
@IuffiSecurity(value = "CU-IUFFI-313-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI313ModificaCaratteristicheAziendali extends BaseController{

	public static final String CU_NAME = "CU-IUFFI-313-M";
	
	  @Autowired
	  IQuadroIuffiEJB                 quadroIuffiEJB;
	  @Autowired
	  IQuadroEJB                 quadroEJB;
	  @Autowired
	  IInterventiEJB interventiEJB;

	  @RequestMapping("/index")
	  public String index(Model model, HttpSession session)
	      throws InternalUnexpectedException
	  { 
	    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
	        session);
	    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);
	    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
	    //Cerco le caratteristiche aziendali salvate per il procedimento oggetto selezionato
	    CaratteristicheAziendali dati = quadroEJB.getCaratteristicheAziendali(idProcedimentoOggetto);
	    
	    if(dati == null) {
	      // Sono in inserimento
	      dati = new CaratteristicheAziendali();
	      dati.setDenominazione(quadroEJB.getDenominazioneAziendaById(procedimentoOggetto.getIdProcedimento()));
	    }
	    else {
	      // Se  trovo le caratteritiche aziendali vado a cercare tutte le info da visualizzare (metodi di coltivazione, filiere etc)
	      // costruisco le descrizioni per attivita, filiere, metodi coltivazione e multifunzionalita
	      List<DecodificaDTO<Long>> metodiColtAtt = quadroEJB.getListaMetodiAttivi(dati.getId());
	      List<Long> metodiColtAttInt = new ArrayList<Long>();
	      for (DecodificaDTO<Long> decodificaDTO : metodiColtAtt) {
	        metodiColtAttInt.add(decodificaDTO.getId());
        }
	      List<DecodificaDTO<Long>> filiereAtt = quadroEJB.getListaFiliereAttivi(dati.getId());
	      List<Long> filiereAttInt = new ArrayList<Long>();
        for (DecodificaDTO<Long> decodificaDTO : filiereAtt) {
          filiereAttInt.add(decodificaDTO.getId());
        }
	      
	      List<DecodificaDTO<Long>> multiAtt = quadroEJB.getListaMultiAttivi(dati.getId());
	      List<Long> multiAttInt = new ArrayList<Long>();
        for (DecodificaDTO<Long> decodificaDTO : multiAtt) {
          multiAttInt.add(decodificaDTO.getId());
        }
        // setto i valorei selezionati delle combo
	      model.addAttribute("metodiColtAtt", metodiColtAttInt);
	      model.addAttribute("filiereAtt", filiereAttInt);
	      model.addAttribute("multiAtt", multiAttInt);
	    }
	    OrganismoControllo oc = quadroEJB.getOrganismoControllo(procedimentoOggetto.getIdProcedimento());
	    if(oc != null) {
	      dati.setDescodc(oc.getCodueodc() + " - " + oc.getDescodc());
	    }   
      List<String> tipiAttivita = quadroEJB.getListaTipoAttivita(procedimentoOggetto.getIdProcedimento());
      if(tipiAttivita != null && tipiAttivita.size() > 0) {
        dati.setDesccatattiva(tipiAttivita.get(0));
      }
      List<DecodificaDTO<Integer>> metodiColt = quadroEJB.getListaMetodiColtivazione();
      List<DecodificaDTO<Integer>> filiere = quadroEJB.getListaFiliere();
      List<DecodificaDTO<Integer>> multi = quadroEJB.getListaMultifunzionalita();

	    model.addAttribute("canUpdate", canUpdate(idProcedimentoOggetto, session));
	    model.addAttribute("caratteristiche", dati);
	    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB,
	        procedimentoOggetto.getIdProcedimentoOggetto(),
	        quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
	    model.addAttribute("listMetodiColt", metodiColt);
	    model.addAttribute("listFiliere", filiere);
	    model.addAttribute("listMulti", multi);
	    
	    
	    return "caratteristicheAziendali/modificaCaratteristicheAziendali";
	  }
	  
	  
	  
	  @RequestMapping("/confermaModifica")
	  public String confermaModifica(Model model, HttpSession session, HttpServletRequest request)
	      throws InternalUnexpectedException
	  { 
	    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
          session);
      long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
	    CaratteristicheAziendali dati = quadroEJB.getCaratteristicheAziendali(idProcedimentoOggetto);
      boolean inserimento = dati == null;
	    //controllo campi obbligatori
	    Errors errors = new Errors();

	    String denominazione = IuffiUtils.STRING.trim(request.getParameter("denominazione"));
	    String descodc = IuffiUtils.STRING.trim(request.getParameter("descodchidden"));
	    String desccatattiva = IuffiUtils.STRING.trim(request.getParameter("desccatattivahidden"));
	    String [] filiere = request.getParameterValues("descfiliera");
	    String [] metodiCol = request.getParameterValues("descmetodocolt");
	    String [] descmulti = request.getParameterValues("descmulti");
      String descrizione = IuffiUtils.STRING.trim(request.getParameter("descrizione"));
      String altrotipoattivita = IuffiUtils.STRING.trim(request.getParameter("altrotipoattivita"));
      String altrafiliera = IuffiUtils.STRING.trim(request.getParameter("altrafiliera"));
      String altrafunz = IuffiUtils.STRING.trim(request.getParameter("altrafunz"));
      String desctrasformazione = IuffiUtils.STRING.trim(request.getParameter("desctrasformazione"));
      
	    errors.validateMandatory(denominazione, "denominazione");
	    
	    if(filiere != null && filiere.length == 1 && filiere[0].equalsIgnoreCase("")) {
	      filiere = null;
	      
	    }
	    if(metodiCol != null && metodiCol.length == 1 && metodiCol[0].equalsIgnoreCase("")) {
	      metodiCol = null;
	      
      }	
	    errors.validateMandatory(metodiCol, "descmetodocolt");
	    errors.validateMandatory(filiere, "descfiliera");
	    if (!errors.isEmpty())
	    {
	      List<DecodificaDTO<Integer>> metodiColt = quadroEJB.getListaMetodiColtivazione();
	      List<DecodificaDTO<Integer>> filiere2 = quadroEJB.getListaFiliere();
	      List<DecodificaDTO<Integer>> multi = quadroEJB.getListaMultifunzionalita();
	      if(dati == null) {
	        dati = new CaratteristicheAziendali();
	        dati.setDenominazione(quadroEJB.getDenominazioneAziendaById(procedimentoOggetto.getIdProcedimento()));
	      }
	      else {
	        // Se  trovo le caratteritiche aziendali vado a cercare tutte le info da visualizzare (metodi di coltivazione, filiere etc)
	        // costruisco le descrizioni per attivita, filiere, metodi coltivazione e multifunzionalita
	        List<DecodificaDTO<Long>> metodiColtAtt = quadroEJB.getListaMetodiAttivi(dati.getId());
	        List<Long> metodiColtAttInt = new ArrayList<Long>();
	        for (DecodificaDTO<Long> decodificaDTO : metodiColtAtt) {
	          metodiColtAttInt.add(decodificaDTO.getId());
	        }
	        List<DecodificaDTO<Long>> filiereAtt = quadroEJB.getListaFiliereAttivi(dati.getId());
	        List<Long> filiereAttInt = new ArrayList<Long>();
	        for (DecodificaDTO<Long> decodificaDTO : filiereAtt) {
	          filiereAttInt.add(decodificaDTO.getId());
	        }
	        
	        List<DecodificaDTO<Long>> multiAtt = quadroEJB.getListaMultiAttivi(dati.getId());
	        List<Long> multiAttInt = new ArrayList<Long>();
	        for (DecodificaDTO<Long> decodificaDTO : multiAtt) {
	          multiAttInt.add(decodificaDTO.getId());
	        }
	        model.addAttribute("metodiColtAtt", metodiColtAttInt);
	        model.addAttribute("filiereAtt", filiereAttInt);
	        model.addAttribute("multiAtt", multiAttInt);
	      }
	      OrganismoControllo oc = quadroEJB.getOrganismoControllo(procedimentoOggetto.getIdProcedimento());
	      if(oc != null) {
	        dati.setDescodc(oc.getCodueodc() + " - " + oc.getDescodc());
	      }   
	      List<String> tipiAttivita = quadroEJB.getListaTipoAttivita(procedimentoOggetto.getIdProcedimento());
	      if(tipiAttivita != null && tipiAttivita.size() > 0) {
	        dati.setDesccatattiva(tipiAttivita.get(0));
	      }
	      model.addAttribute("listMetodiColt", metodiColt);
        model.addAttribute("listFiliere", filiere2);
        model.addAttribute("listMulti", multi);
        model.addAttribute("errors", errors);
        model.addAttribute("prefReqValues", Boolean.TRUE);
        model.addAttribute("caratteristiche", dati);

	      return "caratteristicheAziendali/modificaCaratteristicheAziendali";
	    }else{
	    	//SALVO SU DB E RICARICO
	    	String [] codudcedesc = descodc.split(" -");
	    	String cod = codudcedesc[0];
	    	quadroIuffiEJB.insertOrUpdateCaratteristicheAziendali(dati, inserimento, quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto),
	    	    denominazione, cod, desccatattiva, descrizione, altrotipoattivita, altrafiliera, 
	    	    altrafunz, desctrasformazione, getLogOperationOggettoQuadroDTO(session), filiere, metodiCol, descmulti);
	    }
	    return "redirect:../cuiuffi313v/index.do";
	  }
	  
	  public boolean canUpdate(long idProcedimentoOggetto, HttpSession session)
	      throws InternalUnexpectedException
	  {
	    UpdatePermissionProcedimentoOggetto permission = null;
	    try
	    {
	      permission = quadroEJB.canUpdateProcedimentoOggetto(idProcedimentoOggetto,
	          true);
	    }
	    catch (IuffiPermissionException e)
	    {
	      return false;
	    }
	    String extCodAttore = permission.getExtCodAttore();
	    if (extCodAttore != null)
	    {
	      UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
	      return IuffiUtils.PAPUASERV.isAttoreAbilitato(utenteAbilitazioni,
	          extCodAttore);
	    }
	    return true;

	  }
}
