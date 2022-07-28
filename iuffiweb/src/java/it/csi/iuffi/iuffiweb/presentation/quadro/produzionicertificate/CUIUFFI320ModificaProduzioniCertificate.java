package it.csi.iuffi.iuffiweb.presentation.quadro.produzionicertificate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.SportelloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.produzionicertificate.ProduzioniCertificate;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.produzionicertificate.ProduzioniInserimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.produzionicertificate.ProduzioniTradizionali;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.trasformazioneprodotti.ProdottoTrasformato;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.trasformazioneprodotti.ProdottoTrasformatoInserimento;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi322m")
@IuffiSecurity(value = "CU-IUFFI-322-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI320ModificaProduzioniCertificate extends BaseController {

	public static final String CU_NAME = "CU-IUFFI-322-M";
	
	  @Autowired
	  IQuadroIuffiEJB                 quadroIuffiEJB;
	  @Autowired
	  IQuadroEJB                 quadroEJB;

	  @RequestMapping("/index")
	  public String index(Model model, HttpSession session) throws InternalUnexpectedException { 
	    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(session);
	    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
	    long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);	    
      QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);          

	    List<ProduzioniCertificate> listaProduzioniCertificate = quadroEJB.getProduzioniCertificate(idDatiProcedimento);
	    if(listaProduzioniCertificate != null) {
	      for (ProduzioniCertificate produzioniCertificate : listaProduzioniCertificate) {
	        List<DecodificaDTO<Integer>> produzioniCertificateCombo = quadroEJB.getListaTipiProduzioneCertificata(produzioniCertificate.getIdprod());
	        produzioniCertificate.setProduzioniCertificate(produzioniCertificateCombo);
        }
	    }
	    List<ProduzioniTradizionali> listaProduzioniTradizionali = quadroEJB.getProduzioniTradizionali(idDatiProcedimento);	
	    if(listaProduzioniTradizionali != null) {
        for (ProduzioniTradizionali produzioniTradizionali : listaProduzioniTradizionali) {
          List<DecodificaDTO<Integer>> produzioniTradizionaliCombo = quadroEJB.getListaTipiProduzioneTradizionale(produzioniTradizionali.getIdprod());
          produzioniTradizionali.setProduzioniTradizionali(produzioniTradizionaliCombo);
        }
      }
	    List<DecodificaDTO<Integer>> produzioniPerCertificate = quadroEJB.getListaTipiProduzionePerCertificate();
	    List<DecodificaDTO<Integer>> produzioniPerTradizionali = quadroEJB.getListaTipiProduzionePerTradizionali();
	   
	    int i = 1;
	    for (DecodificaDTO<Integer> decodificaDTO : produzioniPerCertificate) {
	      decodificaDTO.setCodice(i + "");
	      i++;
      }
	    int y = 1;
      for (DecodificaDTO<Integer> decodificaDTO : produzioniPerTradizionali) {
        decodificaDTO.setCodice(y + "");
        y++;
      }
	    model.addAttribute("datiInseriti", listaProduzioniCertificate != null && listaProduzioniTradizionali != null);
	    model.addAttribute("produzioniPerCertificate", produzioniPerCertificate);
	    model.addAttribute("produzioniPerTradizionali", produzioniPerTradizionali);
	    model.addAttribute("listaProduzioniCertificate", listaProduzioniCertificate);
	    model.addAttribute("listaProduzioniTradizionali", listaProduzioniTradizionali);
	    model.addAttribute("inErrore", false);
	    return "produzionicertificate/modificaProduzioniCertificate";	  
    }
	  
	  @RequestMapping("/confermaModifica")
	  public String confermaModifica(Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException { 
	    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(session);
      long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
      long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);
      Enumeration <String> iere = request.getParameterNames();
      List<ProduzioniInserimento> insertCertificate = new ArrayList<ProduzioniInserimento>();
      List<ProduzioniInserimento> insertTradizionali = new ArrayList<ProduzioniInserimento>();
      for (Object o : Collections.list(iere)) {
        String s = o.toString();      
          String [] tmp = s.split("_");
          if(tmp != null && tmp.length > 1) {    
            if(tmp[0].contains("CertificateNew")) {
              // Produzioni certificate
              ProduzioniInserimento produzioneCertificataNew = new ProduzioniInserimento();
              produzioneCertificataNew.setNomecampo("prodotti");
              produzioneCertificataNew.setTipo("CERTIFICATANEW");
              produzioneCertificataNew.setId(IuffiUtils.STRING.trim(request.getParameter("produzioni_" + tmp[1])));
              produzioneCertificataNew.setDescrizioneProduzione(IuffiUtils.STRING.trim(request.getParameter("produzioniNew_" + tmp[1])));
              produzioneCertificataNew.setDescrizioneProduzioneCertificata(IuffiUtils.STRING.trim(request.getParameter("produzioniCertificateNew_" + tmp[1])));
              produzioneCertificataNew.setDescrizioneQualita(IuffiUtils.STRING.trim(request.getParameter("descNew_" + tmp[1])));
              produzioneCertificataNew.setBio(IuffiUtils.STRING.trim(request.getParameter("bioNew_" + tmp[1])));
              if((produzioneCertificataNew.getDescrizioneProduzioneCertificata() != null && !produzioneCertificataNew.getDescrizioneProduzioneCertificata().equalsIgnoreCase("")) ||
                  (produzioneCertificataNew.getDescrizioneProduzione() != null && !produzioneCertificataNew.getDescrizioneProduzione().equalsIgnoreCase("")) 
                  || produzioneCertificataNew.getDescrizioneQualita() != null && !produzioneCertificataNew.getDescrizioneQualita().equalsIgnoreCase("")) {               
                insertCertificate.add(produzioneCertificataNew);
                if(produzioneCertificataNew.getBio() == null) {
                  produzioneCertificataNew.setBio("N");
                }
              }
            }
            else if(tmp[0].contains("Certificate")) {
              ProduzioniInserimento produzioneCertificataOld = new ProduzioniInserimento();
              produzioneCertificataOld.setNomecampo("prodotti");
              produzioneCertificataOld.setTipo("CERTIFICATA");
              produzioneCertificataOld.setId(IuffiUtils.STRING.trim(request.getParameter("produzioni_" + tmp[1])));
              produzioneCertificataOld.setDescrizioneProduzione(IuffiUtils.STRING.trim(request.getParameter("produzioni_" + tmp[1])));
              produzioneCertificataOld.setDescrizioneProduzioneCertificata(IuffiUtils.STRING.trim(request.getParameter("produzioniCertificate_" + tmp[1])));
              produzioneCertificataOld.setDescrizioneQualita(IuffiUtils.STRING.trim(request.getParameter("desc_" + tmp[1])));
              produzioneCertificataOld.setBio(IuffiUtils.STRING.trim(request.getParameter("bio_" + tmp[1])));
              if((produzioneCertificataOld.getDescrizioneProduzioneCertificata() != null && !produzioneCertificataOld.getDescrizioneProduzioneCertificata().equalsIgnoreCase("")) ||
                  (produzioneCertificataOld.getDescrizioneProduzione() != null && !produzioneCertificataOld.getDescrizioneProduzione().equalsIgnoreCase("")) 
                  || produzioneCertificataOld.getDescrizioneQualita() != null && !produzioneCertificataOld.getDescrizioneQualita().equalsIgnoreCase("")) {                
                insertCertificate.add(produzioneCertificataOld);
                if(produzioneCertificataOld.getBio() == null) {
                  produzioneCertificataOld.setBio("N");
                }
              }
            }
            else if(tmp[0].contains("TradizionaleNew")) {
              // Produzioni tradizionali
              ProduzioniInserimento produzioneTradizionalaNew = new ProduzioniInserimento();
              produzioneTradizionalaNew.setNomecampo("prodotti");
              produzioneTradizionalaNew.setTipo("TRADIZIONALENEW");
              produzioneTradizionalaNew.setId(IuffiUtils.STRING.trim(request.getParameter("produzioni_" + tmp[1])));
              produzioneTradizionalaNew.setDescrizioneProduzione(IuffiUtils.STRING.trim(request.getParameter("produzioniTradNew_" + tmp[1])));
              produzioneTradizionalaNew.setDescrizioneProduzioneCertificata(IuffiUtils.STRING.trim(request.getParameter("produzioniTradizionaleNew_" + tmp[1])));
              produzioneTradizionalaNew.setBio(IuffiUtils.STRING.trim(request.getParameter("bioTradNew_" + tmp[1])));
              if((produzioneTradizionalaNew.getDescrizioneProduzioneCertificata() != null && !produzioneTradizionalaNew.getDescrizioneProduzioneCertificata().equalsIgnoreCase("")) ||
                  (produzioneTradizionalaNew.getDescrizioneProduzione() != null && !produzioneTradizionalaNew.getDescrizioneProduzione().equalsIgnoreCase(""))) {
                insertTradizionali.add(produzioneTradizionalaNew);
                if(produzioneTradizionalaNew.getBio() == null) {
                  produzioneTradizionalaNew.setBio("N");
                }
              }
            }
            else if(tmp[0].contains("Tradizionali")) {
              ProduzioniInserimento produzioneTradizionaleOld = new ProduzioniInserimento();
              produzioneTradizionaleOld.setNomecampo("prodotti");
              produzioneTradizionaleOld.setTipo("TRADIZIONALE");
              produzioneTradizionaleOld.setId(IuffiUtils.STRING.trim(request.getParameter("produzioni_" + tmp[1])));
              produzioneTradizionaleOld.setDescrizioneProduzione(IuffiUtils.STRING.trim(request.getParameter("produzioniTrad_" + tmp[1])));
              produzioneTradizionaleOld.setDescrizioneProduzioneCertificata(IuffiUtils.STRING.trim(request.getParameter("produzioniTradizionali_" + tmp[1])));
              produzioneTradizionaleOld.setBio(IuffiUtils.STRING.trim(request.getParameter("bioTrad_" + tmp[1])));
              if((produzioneTradizionaleOld.getDescrizioneProduzioneCertificata() != null && !produzioneTradizionaleOld.getDescrizioneProduzioneCertificata().equalsIgnoreCase("")) ||
                  (produzioneTradizionaleOld.getDescrizioneProduzione() != null && !produzioneTradizionaleOld.getDescrizioneProduzione().equalsIgnoreCase(""))) {                
                insertTradizionali.add(produzioneTradizionaleOld);
                if(produzioneTradizionaleOld.getBio() == null) {
                  produzioneTradizionaleOld.setBio("N");
                }
              }   
            }
          }
        }
        //controllo campi obbligatori
        Errors errors = new Errors();
        int indexCertificateInErrore = 1;
        for (ProduzioniInserimento produzioniInserimentoCertificata : insertCertificate) {
          produzioniInserimentoCertificata.setId(String.valueOf(indexCertificateInErrore));
          if(produzioniInserimentoCertificata.getDescrizioneProduzione() != null && produzioniInserimentoCertificata.getDescrizioneProduzione().equalsIgnoreCase("")) {
            produzioniInserimentoCertificata.setDescrizioneProduzione(null);
          }
          else {
            List<DecodificaDTO<Integer>> produzioniCertificate = quadroEJB.getListaTipiProduzioneCertificata(Long.parseLong(produzioniInserimentoCertificata.getDescrizioneProduzione()));
            produzioniInserimentoCertificata.setProduzioniSecondarie(produzioniCertificate);
          }
          if(produzioniInserimentoCertificata.getDescrizioneProduzioneCertificata() != null && produzioniInserimentoCertificata.getDescrizioneProduzioneCertificata().equalsIgnoreCase("")) {
            produzioniInserimentoCertificata.setDescrizioneProduzioneCertificata(null);
          }
          if(produzioniInserimentoCertificata.getDescrizioneQualita() != null && produzioniInserimentoCertificata.getDescrizioneQualita().equalsIgnoreCase("")) {
            produzioniInserimentoCertificata.setDescrizioneQualita(null);
          }
          errors.validateMandatory(produzioniInserimentoCertificata.getDescrizioneProduzione(), "produzioni_" + indexCertificateInErrore);
          errors.validateMandatory(produzioniInserimentoCertificata.getDescrizioneProduzioneCertificata(), "produzioniCertificate_" + indexCertificateInErrore);
          errors.validateMandatory(produzioniInserimentoCertificata.getDescrizioneQualita(), "desc_" + indexCertificateInErrore);
          indexCertificateInErrore++;
        }
        int indexTradizionaleInErrore = 1;
        for (ProduzioniInserimento produzioniInserimentoTradizionale : insertTradizionali) {
          produzioniInserimentoTradizionale.setId(String.valueOf(indexTradizionaleInErrore));
          if(produzioniInserimentoTradizionale.getDescrizioneProduzione() != null && produzioniInserimentoTradizionale.getDescrizioneProduzione().equalsIgnoreCase("")) {
            produzioniInserimentoTradizionale.setDescrizioneProduzione(null);
          }
          else {
            List<DecodificaDTO<Integer>> produzioniTradizionali = quadroEJB.getListaTipiProduzioneTradizionale(Long.parseLong(produzioniInserimentoTradizionale.getDescrizioneProduzione()));
            produzioniInserimentoTradizionale.setProduzioniSecondarie(produzioniTradizionali);
          }
          if(produzioniInserimentoTradizionale.getDescrizioneProduzioneCertificata() != null && produzioniInserimentoTradizionale.getDescrizioneProduzioneCertificata().equalsIgnoreCase("")) {
            produzioniInserimentoTradizionale.setDescrizioneProduzioneCertificata(null);
          }
          errors.validateMandatory(produzioniInserimentoTradizionale.getDescrizioneProduzione(), "produzioniTrad_" + indexTradizionaleInErrore);
          errors.validateMandatory(produzioniInserimentoTradizionale.getDescrizioneProduzioneCertificata(), "produzioniTradizionali_" + indexTradizionaleInErrore);
          indexTradizionaleInErrore++;
        }
      if (!errors.isEmpty()) {
        model.addAttribute("inErrore", true);
        List<DecodificaDTO<Integer>> produzioniPerCertificate = quadroEJB.getListaTipiProduzionePerCertificate();
        List<DecodificaDTO<Integer>> produzioniPerTradizionali = quadroEJB.getListaTipiProduzionePerTradizionali();
        model.addAttribute("errors", errors);
        model.addAttribute("produzioniPerCertificate", produzioniPerCertificate);
        model.addAttribute("produzioniPerTradizionali", produzioniPerTradizionali);        
        model.addAttribute("listaErroriProduzioniCertificate", insertCertificate);
        model.addAttribute("listaErroriProduzioniTradizionali", insertTradizionali);        
        return "produzionicertificate/modificaProduzioniCertificate";
      }
      else {
        boolean esistonoDuplicatiCert = esistonoDuplicati(insertCertificate);
        boolean esistonoDuplicatiTrad = esistonoDuplicati(insertTradizionali);
        if(esistonoDuplicatiCert || esistonoDuplicatiTrad) {
          model.addAttribute("msgErrore",
              "Non è possibile inserire righe duplicate! Correggere i dati inseriti e riprovare.");
          model.addAttribute("inErrore", true);
          List<DecodificaDTO<Integer>> produzioniPerCertificate = quadroEJB.getListaTipiProduzionePerCertificate();
          List<DecodificaDTO<Integer>> produzioniPerTradizionali = quadroEJB.getListaTipiProduzionePerTradizionali();
          model.addAttribute("produzioniPerCertificate", produzioniPerCertificate);
          model.addAttribute("produzioniPerTradizionali", produzioniPerTradizionali);        
          model.addAttribute("listaErroriProduzioniCertificate", insertCertificate);
          model.addAttribute("listaErroriProduzioniTradizionali", insertTradizionali);        
          return "produzionicertificate/modificaProduzioniCertificate";
        }
        else {
          quadroEJB.eliminaProduzioni(idDatiProcedimento);
          for (ProduzioniInserimento produzioniInserimento : insertCertificate) {
            // Inserisco produzione certificata
            quadroIuffiEJB.insertProduzioniCertificate(getLogOperationOggettoQuadroDTO(session), idDatiProcedimento, 
                new Long(IuffiUtils.STRING.trim(produzioniInserimento.getDescrizioneProduzioneCertificata())),
                null,
                IuffiUtils.STRING.trim(produzioniInserimento.getBio()));
          }         
          for (ProduzioniInserimento produzioniInserimento : insertTradizionali) {
            // Inserisco produzione tradizionale
            quadroIuffiEJB.insertProduzioniCertificate(getLogOperationOggettoQuadroDTO(session), idDatiProcedimento, 
                null,
                new Long(IuffiUtils.STRING.trim(produzioniInserimento.getDescrizioneProduzioneCertificata())),
                IuffiUtils.STRING.trim(produzioniInserimento.getBio()));
          }                  
        }        
        return "redirect:../cuiuffi322v/index.do";
      }    
	  }
	  
	  public List<String> removeDuplicate(List<String> sourceList){
      java.util.Set setPmpListArticle=new HashSet(sourceList);
      return new ArrayList(setPmpListArticle);
    }

	  public boolean esistonoDuplicati(List<ProduzioniInserimento> lista) {
	    for (int i = 0 ; i < lista.size() ; i++) {
	      String descrizioneProduzioneCertificata=(String) lista.get(i).getDescrizioneProduzioneCertificata();
	      for (int j=i+1 ; j< lista.size() ; j++) {
	        String tmppc=(String)lista.get(j).getDescrizioneProduzioneCertificata();
	        if (descrizioneProduzioneCertificata.equals(tmppc)) {
	              return true;
	        }
	      }
	    }
	    return false;
	  }
	  
	  @RequestMapping(value = "/elenco_produzioni_certificate_{idProduzione}", method = RequestMethod.GET, produces = "application/json")
	  @ResponseBody
	  public List<DecodificaDTO<Integer>> elencoProduzioniCertificate(Model model,
	      @PathVariable("idProduzione") String idProduzione,
	      HttpServletRequest request)
	      throws Exception {
	    List<DecodificaDTO<Integer>> produzioniCertificate = quadroEJB.getListaTipiProduzioneCertificata(Long.parseLong(idProduzione));
	    
	    return produzioniCertificate;
	  }
	  
	  @RequestMapping(value = "/elenco_produzioni_tradizionali_{idProduzione}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<DecodificaDTO<Integer>> elencoProduzioniTradizionali(Model model,
        @PathVariable("idProduzione") String idProduzione,
        HttpServletRequest request)
        throws Exception {
      List<DecodificaDTO<Integer>> produzioniTradizionali = quadroEJB.getListaTipiProduzioneTradizionale(Long.parseLong(idProduzione));
      
      return produzioniTradizionali;
    }
	  
	  @RequestMapping(value = "/descrizione_produzionenew_{idProduzione}", method = RequestMethod.GET, produces = "application/text")
    @ResponseBody
    public String descProduzioniNew(Model model,
        @PathVariable("idProduzione") String idProduzione,
        HttpServletRequest request)
        throws Exception {
	    if(idProduzione != null && !idProduzione.equalsIgnoreCase("")) {
	      String result = quadroEJB.getDescProduzione(Long.parseLong(idProduzione));
	      return result;
	    }
	    else return "";
    }
	  
	   @RequestMapping(value = "/descrizione_produzione_{idProduzione}", method = RequestMethod.GET, produces = "application/text")
	    @ResponseBody
	    public String descProduzioni(Model model,
	        @PathVariable("idProduzione") String idProduzione,
	        HttpServletRequest request)
	        throws Exception {
	      if(idProduzione != null && !idProduzione.equalsIgnoreCase("")) {
	        String result = quadroEJB.getDescProduzione(Long.parseLong(idProduzione));
	        return result;
	      }
	      else return "";
	    }
	  
}