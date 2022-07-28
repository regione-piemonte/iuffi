package it.csi.iuffi.iuffiweb.presentation.quadro.trasformazioneprodotti;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.trasformazioneprodotti.ProdottoTrasformato;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.trasformazioneprodotti.ProdottoTrasformatoInserimento;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi321m")
@IuffiSecurity(value = "CU-IUFFI-321-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI320ModificaTrasformazioneProdotti extends BaseController {

	public static final String CU_NAME = "CU-IUFFI-321-M";
	
	  @Autowired
	  IQuadroIuffiEJB                 quadroIuffiEJB;
	  @Autowired
	  IQuadroEJB                 quadroEJB;

	  @RequestMapping("/index")
	  public String index(Model model, HttpSession session) throws InternalUnexpectedException { 
	    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(session);
	    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
	    long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);	    
	    List<ProdottoTrasformato> listaProdottoTrasformato = quadroEJB.getProdottoTrasformato(idDatiProcedimento);
	    int j = 1;
	    if(listaProdottoTrasformato != null) {
	      for (ProdottoTrasformato prodottoTrasformato : listaProdottoTrasformato) {
	        prodottoTrasformato.setIndex(j);
	        j++;
	      }
	    }
	   
	    List<DecodificaDTO<Integer>> prodotti = quadroEJB.getListaProdotti();
	    int i = 1;
	    for (DecodificaDTO<Integer> decodificaDTO : prodotti) {
	      decodificaDTO.setCodice(i + "");
	      i++;
      }
	    model.addAttribute("datiInseriti", listaProdottoTrasformato != null && listaProdottoTrasformato.size() > 0);
	    model.addAttribute("prodotti", prodotti);
	    model.addAttribute("listaProdottoTrasformato", listaProdottoTrasformato);
	    return "trasformazioneprodotti/modificaTrasformazioneProdotti";	  
    }
	  
	  
	  
	  @RequestMapping("/confermaModifica")
	  public String confermaModifica(Model model, HttpSession session, HttpServletRequest request) throws InternalUnexpectedException { 
	    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(session);
      long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
      long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);
      QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);
           
      Enumeration <String> iere = request.getParameterNames();
      List<ProdottoTrasformatoInserimento> insert = new ArrayList<ProdottoTrasformatoInserimento>();
      for (Object o : Collections.list(iere)) {
        String s = o.toString();      
          String [] tmp = s.split("_");
          if(tmp != null && tmp.length > 1) {
            ProdottoTrasformatoInserimento prodotto = new ProdottoTrasformatoInserimento();
            prodotto.setNomecampo("prodotti");
            prodotto.setId(IuffiUtils.STRING.trim(request.getParameter("prodotti_" + tmp[1])));
            prodotto.setDescrizione(IuffiUtils.STRING.trim(request.getParameter("desc_" + tmp[1])));
            if((prodotto.getId() != null && !prodotto.getId().equalsIgnoreCase("")) || prodotto.getDescrizione() != null && !prodotto.getDescrizione().equalsIgnoreCase("")) {
              insert.add(prodotto);
            }
            ProdottoTrasformatoInserimento prodotto2 = new ProdottoTrasformatoInserimento();
            prodotto2.setNomecampo("prodotti2_" + tmp[1]);
            prodotto2.setId(IuffiUtils.STRING.trim(request.getParameter("prodotti2_" + tmp[1])));
            prodotto2.setDescrizione(IuffiUtils.STRING.trim(request.getParameter("descNew_" + tmp[1])));
            if((prodotto2.getId() != null && !prodotto2.getId().equalsIgnoreCase("")) || prodotto2.getDescrizione() != null && !prodotto2.getDescrizione().equalsIgnoreCase("")) {
              insert.add(prodotto2);
            }     
          }
      }
      //controllo campi obbligatori
      Errors errors = new Errors();
      List<String> ids = new ArrayList<String>();
      List<ProdottoTrasformato> listaProdottoTrasformato = quadroEJB.getProdottoTrasformato(idDatiProcedimento);
      for (ProdottoTrasformatoInserimento prodottoTrasformatoInserimento : insert) {
        errors.validateMandatory(prodottoTrasformatoInserimento.getId(), prodottoTrasformatoInserimento.getNomecampo());
        if((prodottoTrasformatoInserimento.getId() == null || prodottoTrasformatoInserimento.getId().compareTo("") == 0) 
            && prodottoTrasformatoInserimento.getDescrizione() != null && !prodottoTrasformatoInserimento.getDescrizione().equalsIgnoreCase("")) {
          String s = prodottoTrasformatoInserimento.getNomecampo();
          String [] tmp = s.split("_");
          ids.add(tmp[1] + "," + prodottoTrasformatoInserimento.getDescrizione());
        }
      }
      if (!errors.isEmpty()) {
        boolean inErrore = true;
        model.addAttribute("errors", errors);
        List<DecodificaDTO<Integer>> prodotti = quadroEJB.getListaProdotti();
        List<DecodificaDTO<Integer>> prodottiE = new ArrayList<DecodificaDTO<Integer>>();
        List<String> listaInput = removeDuplicate(ids);
        for (String string : listaInput)         {
          DecodificaDTO<Integer> er = new DecodificaDTO<Integer>();
          String [] t = string.split(",");
          if(t[0] != null && !t[0].equalsIgnoreCase("")) {
            er.setId(Integer.parseInt(t[0]));
          }        
          er.setDescrizione(t[1]);
          prodottiE.add(er);
        }
        
        
        int i = 1;
        for (DecodificaDTO<Integer> decodificaDTO : prodotti) {
          decodificaDTO.setCodice(i + "");
          i++;
        }
        model.addAttribute("datiInseriti", listaProdottoTrasformato != null && listaProdottoTrasformato.size() > 0);
        model.addAttribute("inErrore", inErrore);
        model.addAttribute("prodotti", prodotti);
        model.addAttribute("prodottiE", prodottiE);
        model.addAttribute("listaProdottoTrasformato", listaProdottoTrasformato);
        
        return "trasformazioneprodotti/modificaTrasformazioneProdotti";
      }
      else {
        quadroEJB.eliminaProdottiTrasformati(idDatiProcedimento);
        List<ProdottoTrasformatoInserimento> listaInput = eliminaDupicati(insert);
        for (ProdottoTrasformatoInserimento prodottoTrasformatoInserimento : listaInput) {
          quadroIuffiEJB.insertProdottiTrasformati(getLogOperationOggettoQuadroDTO(session), idDatiProcedimento, 
              new Long(IuffiUtils.STRING.trim(prodottoTrasformatoInserimento.getId())), 
              IuffiUtils.STRING.trim(prodottoTrasformatoInserimento.getDescrizione()));
        }
        model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB,
            idProcedimentoOggetto,
            quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
       

        return "redirect:../cuiuffi321v/index.do";
      }
      
	    
	  }
	  
	  public List<String> removeDuplicate(List<String> sourceList){
	    java.util.Set setPmpListArticle=new HashSet(sourceList);
	    return new ArrayList(setPmpListArticle);
	  }
	  
	  public List<ProdottoTrasformatoInserimento> eliminaDupicati(List<ProdottoTrasformatoInserimento> lista) {
	    for (int i = 0 ; i < lista.size() ; i++) {
	      String id=(String) lista.get(i).getId();
	      String desc=(String) lista.get(i).getDescrizione();
	      for (int j=i+1 ; j< lista.size() ; j++) {
	        String tmpd=(String)lista.get(j).getDescrizione();
	        String tmpi=(String)lista.get(j).getId();
	        if (desc.equals(tmpd)) {
	          if (id.equals(tmpi)) {
	            lista.remove(j);
	            j--; //aggiunto a questa versione
	          }
	  
	        }
	      }
	    }
	    return lista;
	  }	  
}