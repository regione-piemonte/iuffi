package it.csi.iuffi.iuffiweb.presentation.quadro.pianoselettivomufloni;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.Distretto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.RicercaDistretto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;;

@Controller
@IuffiSecurity(value = "CU-IUFFI-2005-E", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "cuiuffi2005e")
public class CUIUFFI2005EElencoDistretti extends BaseController
{
  public static final long idSpecie= 5;
  public static final String CU_NAME = "CU-IUFFI-2005-E";
  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;

  @Autowired
  IQuadroEJB quadroEJB;
  
  @RequestMapping(value = "/index")
  public String index(Model model, HttpSession session) throws InternalUnexpectedException {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    List<RicercaDistretto> idDistretto = quadroIuffiEJB.getIdDistrettoOgur(idProcedimentoOggetto);    
    List<Distretto> distretti = new ArrayList<Distretto>();
    List<Distretto> distrettiRegione = new ArrayList<Distretto>();
    String idDistrettoS = "";
    String idDistrettoRegioneS = "";
    if(idDistretto != null) {
      for (RicercaDistretto ricercaDistretto : idDistretto) {
        if(ricercaDistretto.getIddistretto() != null) {
          if(idDistrettoS.equalsIgnoreCase("")) {
            idDistrettoS = idDistrettoS + ricercaDistretto.getIddistretto();
          }
          else {
            idDistrettoS = idDistrettoS + ", " + ricercaDistretto.getIddistretto();
          }
        }
        else if(ricercaDistretto.getIddistrettoregione() != null) {
          if(idDistrettoRegioneS.equalsIgnoreCase("")) {
            idDistrettoRegioneS = idDistrettoRegioneS + ricercaDistretto.getIddistrettoregione();
          }
          else {
            idDistrettoRegioneS = idDistrettoRegioneS + ", " + ricercaDistretto.getIddistrettoregione();
          }
        }
        
      }
    }
    
    if(!idDistrettoS.equalsIgnoreCase("")) {
      distretti = quadroIuffiEJB.getElencoDistrettiOgur(false, "("+ idDistrettoS + ")", idSpecie, null, idProcedimentoOggetto);
    }
    else if(!idDistrettoRegioneS.equalsIgnoreCase("")) {
      distrettiRegione = quadroIuffiEJB.getElencoDistrettiOgur(true, "("+ idDistrettoRegioneS + ")", idSpecie, null, idProcedimentoOggetto);
    }
    for (Distretto distretto : distretti){
      distretto.setTipo("distretto");
    }
    for (Distretto distretto : distrettiRegione){
      distretto.setTipo("regione");
    }
    distretti.addAll(distrettiRegione);
    
    model.addAttribute("distretti", eliminaDupicati(distretti));
    return "pianoselettivomufloni/elencodistretti";
  }
  
  public List<Distretto> eliminaDupicati(List<Distretto> lista) {
    for (int i = 0 ; i < lista.size() ; i++) {
      String id=(String) lista.get(i).getNominDistretto();
     
      for (int j=i+1 ; j< lista.size() ; j++) {
        String tmpd=(String)lista.get(j).getNominDistretto();
       
        if (id.equals(tmpd)) {
          
            lista.remove(j);
            j--; //aggiunto a questa versione
          
  
        }
      }
    }
    return lista;
  }   
  
}
