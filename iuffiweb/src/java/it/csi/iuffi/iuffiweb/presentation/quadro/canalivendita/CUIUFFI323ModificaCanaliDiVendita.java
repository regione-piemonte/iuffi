package it.csi.iuffi.iuffiweb.presentation.quadro.canalivendita;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.permission.UpdatePermissionProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati.ContenutoFileAllegatiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.canalidivendita.CanaliDiVendita;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.canalidivendita.ImmaginiCanaliDiVendita;
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
@RequestMapping("/cuiuffi323m")
@IuffiSecurity(value = "CU-IUFFI-323-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI323ModificaCanaliDiVendita extends BaseController{

	public static final String CU_NAME = "CU-IUFFI-323-M";
	
	  @Autowired
	  IQuadroIuffiEJB                 quadroIuffiEJB;
	  @Autowired
	  IQuadroEJB                 quadroEJB;
	  @Autowired
	  IInterventiEJB interventiEJB;

	  @RequestMapping("/index")
	  public String index(Model model, HttpSession session) throws InternalUnexpectedException { 
	    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
	        session);
	    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);
	    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
	    long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);  	    
	    //Cerco i canali salvati per il procedimento oggetto selezionato
	    CanaliDiVendita dati = quadroEJB.getCanaliDiVendita(idDatiProcedimento);
	    List<DecodificaDTO<Long>> listaCanali = quadroEJB.getListaCanaliDiVendita();
	    List<Long> listaCanaliAttiviPerCombo = new ArrayList<Long>();
	    List<ImmaginiCanaliDiVendita> listaImg = quadroEJB.getListaImmagini();    
	    if(dati != null) {
	      //Se  trovo le caratteritiche aziendali vado a cercare tutte le info da visualizzare (metodi di coltivazione, filiere etc)
        List<DecodificaDTO<Long>> listaCanaliAttivi = quadroEJB.getListaCanaliDiVenditaAttivi(dati.getId());   
        for (DecodificaDTO<Long> decodificaDTO : listaCanaliAttivi) {
          listaCanaliAttiviPerCombo.add(decodificaDTO.getId());
        }
	      dati.setListCanali(listaCanaliAttivi);
	      model.addAttribute("listaCanaliAttiviPerCombo", listaCanaliAttiviPerCombo);
	      for (ImmaginiCanaliDiVendita immaginiCanaliDiVendita : listaImg) {
	        if(immaginiCanaliDiVendita.getId().toString().equalsIgnoreCase(dati.getIdimmagine())) {
	          immaginiCanaliDiVendita.setSelezionata(true);
	        }
	      }
	      model.addAttribute("immagine", dati.getIdimmagine());
	    }
	    
	    model.addAttribute("listaImg", listaImg);
	    model.addAttribute("listaCanali", listaCanali);
	    model.addAttribute("datiInseriti", dati != null);
	    model.addAttribute("canali", dati);
	    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB,
	        procedimentoOggetto.getIdProcedimentoOggetto(),
	        quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
	    return "canalidivendita/modificaCanaliVendita";
	  }
	  
	  
	  
	  @RequestMapping("/confermaModifica")
	  public String confermaModifica(Model model, HttpSession session, HttpServletRequest request)
	      throws InternalUnexpectedException
	  { 
	    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
          session);
      long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
      long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);       
      CanaliDiVendita dati = quadroEJB.getCanaliDiVendita(idDatiProcedimento);
	    Errors errors = new Errors();
	    String [] canaliSelzionati = request.getParameterValues("canali");
      String altriCanali = IuffiUtils.STRING.trim(request.getParameter("altriCanali"));
      String sitoWeb = IuffiUtils.STRING.trim(request.getParameter("sitoWeb"));
      String amazon = IuffiUtils.STRING.trim(request.getParameter("amazon"));
      String orari = IuffiUtils.STRING.trim(request.getParameter("orari"));
      String indirizzo = IuffiUtils.STRING.trim(request.getParameter("indirizzo"));
      String telefono = IuffiUtils.STRING.trim(request.getParameter("telefono"));
      String email = IuffiUtils.STRING.trim(request.getParameter("email"));
      String luogo = IuffiUtils.STRING.trim(request.getParameter("luogo"));
      String info = IuffiUtils.STRING.trim(request.getParameter("info"));
      String facebook = IuffiUtils.STRING.trim(request.getParameter("facebook"));
      String instagram = IuffiUtils.STRING.trim(request.getParameter("instagram"));
      String note = IuffiUtils.STRING.trim(request.getParameter("note"));  
      String idImmagineVetrina = IuffiUtils.STRING.trim(request.getParameter("immagine"));
      List<ImmaginiCanaliDiVendita> listaImg = quadroEJB.getListaImmagini();             
      if(idImmagineVetrina == null || idImmagineVetrina.equalsIgnoreCase("") || idImmagineVetrina.equalsIgnoreCase("undefined")) {
        model.addAttribute("msgErrore",
            "Selezionare immagine di vetrina, campo obbligatorio");
        errors.validateMandatory(idImmagineVetrina, "");	    
	      List<DecodificaDTO<Long>> listaCanali = quadroEJB.getListaCanaliDiVendita();
	      model.addAttribute("listaImg", listaImg);
	      model.addAttribute("listaCanali", listaCanali);
	      model.addAttribute("canali", dati);
	      List<Long> listaCanaliAttiviPerCombo = new ArrayList<Long>();
	      for(int i = 0; i < canaliSelzionati.length; i++) {
	        if(canaliSelzionati[i] != null && !canaliSelzionati[i].equalsIgnoreCase("") && !canaliSelzionati[i].equalsIgnoreCase("undefined")) {
	          listaCanaliAttiviPerCombo.add(new Long(canaliSelzionati[i]));
	        }	        
	      }
	      model.addAttribute("listaCanaliAttiviPerCombo", listaCanaliAttiviPerCombo);
	      return "canalidivendita/modificaCanaliVendita";
	    }else{
	      if(dati != null) {
	        quadroEJB.eliminaCanaliVenditaContatti(dati.getId());
	        quadroEJB.eliminaCanaliVendita(idDatiProcedimento);  	        
	      }
	      long index = quadroIuffiEJB.insertCanaliVendita(getLogOperationOggettoQuadroDTO(session), idDatiProcedimento, 
            altriCanali, sitoWeb, amazon, orari, indirizzo, telefono, email, luogo, info, facebook, instagram, note, new Long(idImmagineVetrina));
	      if(canaliSelzionati != null) {
	        for (int j = 0; j <canaliSelzionati.length; j++) {
	          if(canaliSelzionati[j] != null && !canaliSelzionati[j].equalsIgnoreCase("")) {
	            quadroIuffiEJB.insertCanaliContatti(index, new Long(canaliSelzionati[j]));
	          }
	        }
	      }
	      
	      
	    }
	    return "redirect:../cuiuffi323v/index.do";
	  }
	  
	  @RequestMapping(value = "visualizza_immagine_{idImmagine}", method = RequestMethod.GET)
	  public ResponseEntity<byte[]> visualizzaImmagine(HttpSession session,
	      @PathVariable("idImmagine") String idImmagine)
	      throws InternalUnexpectedException
	  {
	    Long id = null;
	    if(idImmagine != null && !idImmagine.equalsIgnoreCase("") || !idImmagine.equalsIgnoreCase("undefined")) {
	      id = new Long(idImmagine);
	      ImmaginiCanaliDiVendita immagine = null;
	      List<ImmaginiCanaliDiVendita> listaImg = quadroEJB.getListaImmagini();
	      for (ImmaginiCanaliDiVendita immaginiCanaliDiVendita : listaImg) {
	        if(id.compareTo(immaginiCanaliDiVendita.getId()) == 0) {
	          immagine = immaginiCanaliDiVendita;
	        }
	      }
	      HttpHeaders httpHeaders = new HttpHeaders();
	      httpHeaders.add("Content-type",
	          IuffiUtils.FILE.getMimeType(".jpg"));
	      httpHeaders.add("Content-Disposition",
	          "attachment; filename=\"\"");

	      if(immagine!=null && immagine.getImmagine() != null){
	         ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
	             immagine.getImmagine(), httpHeaders, HttpStatus.OK);
	              return response;
	      }
	    }
	    
	   return null;
	  }
	  
	 
}
