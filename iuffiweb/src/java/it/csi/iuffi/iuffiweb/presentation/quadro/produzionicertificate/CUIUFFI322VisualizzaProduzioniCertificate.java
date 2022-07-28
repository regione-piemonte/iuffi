package it.csi.iuffi.iuffiweb.presentation.quadro.produzionicertificate;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.produzionicertificate.ProduzioniCertificate;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.produzionicertificate.ProduzioniTradizionali;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/cuiuffi322v")
@IuffiSecurity(value = "CU-IUFFI-322-V", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI322VisualizzaProduzioniCertificate extends BaseController {
  public static final String CU_NAME = "CU-IUFFI-322-V";
  
  @Autowired
  IQuadroEJB                 quadroEJB;

  @RequestMapping("/index")
  public String index(Model model, HttpSession session) throws InternalUnexpectedException {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);    
    long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);
    List<ProduzioniCertificate> listaProduzioniCertificate = quadroEJB.getProduzioniCertificate(idDatiProcedimento);
    List<ProduzioniTradizionali> listaProduzioniTradizionali = quadroEJB.getProduzioniTradizionali(idDatiProcedimento);

    model.addAttribute("listaProduzioniCertificate", listaProduzioniCertificate);
    model.addAttribute("listaProduzioniTradizionali", listaProduzioniTradizionali);

    model.addAttribute("datiInseritiCertificate", listaProduzioniCertificate != null);
    model.addAttribute("datiInseritiTradizionali", listaProduzioniTradizionali != null);
    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB,
        idProcedimentoOggetto,
        quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
    return "produzionicertificate/dettaglioProduzioniCertificate";
  }
  

}
