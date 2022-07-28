package it.csi.iuffi.iuffiweb.presentation.quadro.premioprestitoconduzione;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.SportelloDTO;
import it.csi.iuffi.iuffiweb.dto.plsql.MainPunteggioDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.premioprestitoconduzione.PremiAllevamento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.premioprestitoconduzione.PremiColture;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.regimeaiuto.RegimeAiuto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi2002e")
@IuffiSecurity(value = "CU-IUFFI-2002-E", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI2002VisualizzaPremioPrestitoConduzione extends BaseController {
  public static final String CU_NAME = "CU-IUFFI-2002-E";
  @Autowired
  IQuadroEJB                 quadroEJB;

  @RequestMapping("/index")
  public String index(Model model, HttpSession session) throws InternalUnexpectedException {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);    
    BigDecimal totaleColture = new BigDecimal(0);
    BigDecimal totaleAllevamento = new BigDecimal(0);
    BigDecimal totale = new BigDecimal(0);
    List<PremiColture> listaPremiColture = quadroEJB.getPremiColture(idProcedimentoOggetto, procedimentoOggetto.getExtIdDichiarazioneConsistenza());
    List<PremiAllevamento> listaPremiAllevamento = quadroEJB.getPremiAllevamento(idProcedimentoOggetto, procedimentoOggetto.getExtIdDichiarazioneConsistenza());
    if(listaPremiColture != null) {
      for (PremiColture premiColture : listaPremiColture){
        BigDecimal tmp = premiColture.getImportopremio();
        totaleColture = totaleColture.add(tmp.setScale(2,BigDecimal.ROUND_HALF_UP));
        String tmp2 = premiColture.getSuperficie().substring(0, 1);
        if(tmp2.equalsIgnoreCase(".")) {
          premiColture.setSuperficie("0" + premiColture.getSuperficie());
        }
        premiColture.setSuperficie(premiColture.getSuperficie().replace(".", ","));
        if(premiColture.getSuperficie().equalsIgnoreCase("0")) {
          premiColture.setSuperficie("0,00");
        }
        premiColture.setImportopremio(premiColture.getImportopremio().setScale(2,BigDecimal.ROUND_HALF_UP));
        premiColture.setImportounitario(premiColture.getImportounitario().setScale(2,BigDecimal.ROUND_HALF_UP));
        premiColture.setImportopremioS(premiColture.getImportopremio().toString().replace(".", ","));
        premiColture.setImportounitarioS(premiColture.getImportounitario().toString().replace(".", ","));
      }
    }
    if(listaPremiAllevamento != null) {
      for (PremiAllevamento premiAllevamento : listaPremiAllevamento){
        premiAllevamento.setUbaS(premiAllevamento.getUba().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        premiAllevamento.setCoefficienteS(premiAllevamento.getCoefficiente().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        BigDecimal tmp = premiAllevamento.getImportopremio();
        totaleAllevamento = totaleAllevamento.add(tmp.setScale(2,BigDecimal.ROUND_HALF_UP));
        String tmp2 = premiAllevamento.getUbaS().substring(0, 1);
        if(tmp2.equalsIgnoreCase(".")) {
          premiAllevamento.setUbaS("0" + premiAllevamento.getUba());
        }
        
        String tmp3 = premiAllevamento.getCoefficienteS().substring(0, 1);
        if(tmp3.equalsIgnoreCase(".")) {
          premiAllevamento.setCoefficienteS("0" + premiAllevamento.getCoefficiente());
        }
        premiAllevamento.setUbaS(premiAllevamento.getUbaS().replace(".", ","));
        premiAllevamento.setCoefficienteS(premiAllevamento.getCoefficienteS().replace(".", ","));
        if(premiAllevamento.getUbaS().equalsIgnoreCase("0")) {
          premiAllevamento.setUbaS("0,00");
        }
        if(premiAllevamento.getCoefficienteS().equalsIgnoreCase("0")) {
          premiAllevamento.setCoefficienteS("0,00");
        }
//        if(!premiAllevamento.getUba().contains(",")) {
//          premiAllevamento.setUba(premiAllevamento.getUba() + ",00");
//        }
//        if(!premiAllevamento.getCoefficiente().contains(",")) {
//          premiAllevamento.setCoefficiente(premiAllevamento.getCoefficiente() + ",00");
//        }
        premiAllevamento.setImportopremio(premiAllevamento.getImportopremio().setScale(2,BigDecimal.ROUND_HALF_UP));
        premiAllevamento.setImportounitario(premiAllevamento.getImportounitario().setScale(2,BigDecimal.ROUND_HALF_UP));
        premiAllevamento.setImportopremioS(premiAllevamento.getImportopremio().toString().replace(".", ","));
        premiAllevamento.setImportounitarioS(premiAllevamento.getImportounitario().toString().replace(".", ","));
      }
    }
    
    totale = totaleAllevamento.add(totaleColture).setScale(2,BigDecimal.ROUND_HALF_UP);
    model.addAttribute("datiPremiColture", listaPremiColture != null && listaPremiColture.size() > 0);
    model.addAttribute("datiPremiAllevamento", listaPremiAllevamento != null && listaPremiAllevamento.size() > 0);
    model.addAttribute("listaPremiColture", listaPremiColture);
    model.addAttribute("listaPremiAllevamento", listaPremiAllevamento);
    
    model.addAttribute("totaleColture", totaleColture.toString().replace(".", ","));
    model.addAttribute("tot", totale.toString().replace(".", ","));
    model.addAttribute("totaleAllevamento", totaleAllevamento.toString().replace(".", ","));
    
    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB,
        idProcedimentoOggetto,
        quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
    return "premioprestitoconduzione/dettaglioPremioPrestitoConduzione";
  }
  
  @RequestMapping("/confermaModifica")
  public String confermaModifica(Model model, HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  { 
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    MainPunteggioDTO result = quadroEJB.calcolaPremio(idProcedimentoOggetto, getLogOperationOggettoQuadroDTO(session).getExtIdUtenteAggiornamento());
    if(result != null && result.getRisultato() == 1) {
      model.addAttribute("msg", result.getMessaggio());
      model.addAttribute("errore", true);
      return "premioprestitoconduzione/dettaglioPremioPrestitoConduzione";
    }  
    return "redirect:../cuiuffi2002e/index.do";
  }
  
}
