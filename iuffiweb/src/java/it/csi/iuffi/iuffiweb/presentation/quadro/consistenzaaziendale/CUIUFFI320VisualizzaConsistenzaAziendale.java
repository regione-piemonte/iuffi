package it.csi.iuffi.iuffiweb.presentation.quadro.consistenzaaziendale;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ConsistenzaAziendale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ProduzioneAnimale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ProduzioneVegetale;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/cuiuffi320v")
@IuffiSecurity(value = "CU-IUFFI-320-V", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI320VisualizzaConsistenzaAziendale extends BaseController
{
  public static final String CU_NAME = "CU-IUFFI-320-V";
  @Autowired
  IQuadroEJB                 quadroEJB;

  @RequestMapping("/index")
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);    
    long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);
    ConsistenzaAziendale datiConsistenza = new ConsistenzaAziendale();
    List<ProduzioneAnimale> listaProduzioniAnimali = quadroEJB.getProduzioniAnimali(idDatiProcedimento);
    List<ProduzioneVegetale> listaProduzioniVegetali = quadroEJB.getProduzioniVegetali(idDatiProcedimento);
    if(listaProduzioniAnimali != null) {
      for (ProduzioneAnimale produzioneAnimale : listaProduzioniAnimali) {
        produzioneAnimale.setIdallevamento(produzioneAnimale.getId());
      }
    }
    if(listaProduzioniVegetali != null) {
      for (ProduzioneVegetale produzioneVegetale : listaProduzioniVegetali) {
        produzioneVegetale.setIdconduzione(produzioneVegetale.getId());
      } 
    }
    
    boolean datiInseritiA = false;
    boolean datiInseritiV = false;
    if(listaProduzioniVegetali != null && !listaProduzioniVegetali.isEmpty()) {
      datiInseritiV = true;
      datiConsistenza.setProduzioniVegetali(listaProduzioniVegetali);
    }
    if(listaProduzioniAnimali != null && !listaProduzioniAnimali.isEmpty()) {
      datiInseritiA = true;
      datiConsistenza.setProduzioniAnimali(listaProduzioniAnimali);
    }
    model.addAttribute("datiConsistenza", datiConsistenza);

    model.addAttribute("datiInseriti", datiInseritiA || datiInseritiV);
    model.addAttribute("datiInseritiA", datiInseritiA);
    model.addAttribute("datiInseritiV", datiInseritiV);
    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB,
        idProcedimentoOggetto,
        quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
    return "consistenzaaziendale/dettaglioConsistenzaAziendale";
  }
  

}
