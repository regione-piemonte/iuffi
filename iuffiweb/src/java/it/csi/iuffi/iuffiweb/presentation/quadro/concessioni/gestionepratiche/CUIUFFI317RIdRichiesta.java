package it.csi.iuffi.iuffiweb.presentation.quadro.concessioni.gestionepratiche;

import java.security.InvalidParameterException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.ConcessioneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.integration.ws.regata.EsitoRegataVO;
import it.csi.iuffi.iuffiweb.integration.ws.regata.RegataServiceFactory;
import it.csi.iuffi.iuffiweb.integration.ws.regata.exception.InternalException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "CU-IUFFI-317-R", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi317r")
public class CUIUFFI317RIdRichiesta extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;
  @Autowired
  IQuadroEJB quadroEJB;
  
  @IsPopup
  @RequestMapping(value = "/index_{idConcessione}", method = RequestMethod.GET)
  @ResponseBody
  public String index(@PathVariable("idConcessione") long idConcessione, Model model, HttpServletRequest request) throws InternalUnexpectedException, InvalidParameterException, InternalException
  {
    ConcessioneDTO concessione = quadroIuffiEJB.getConcessione(idConcessione);
    if(concessione.getIdStatoConcessione()!=IuffiConstants.CONCESSIONI.STATO.BOZZA && concessione.getIdStatoConcessione()!=IuffiConstants.CONCESSIONI.STATO.RICHIESTA_VERCOR)
    {
      return quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.PRATICA_NON_IN_BOZZA_O_VERCOR);
    }

    UtenteAbilitazioni ua = getUtenteAbilitazioni(request.getSession());
    Long idUtenteLogin = ua.getIdUtenteLogin();
    idUtenteLogin=94770l;
    EsitoRegataVO esito = RegataServiceFactory.getRestServiceClient().chiediIdRichiesta(idUtenteLogin);
    if(esito.getIdControlliInterni()==0l)
    {
      Long idVisuraRitornataDalWs =  esito.getIdRichiesta();
      quadroIuffiEJB.aggiornaVisuraConcessione(idConcessione, idVisuraRitornataDalWs, getIdUtenteLogin(request.getSession()));
      model.addAttribute("visuraRitornataDalWs", idVisuraRitornataDalWs);
      return quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.IDENTIFICATIVO_ACQUISITO) + " Identificativo: " + idVisuraRitornataDalWs;
    }
    else
    {
      return quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.ERRORE_ATTIVAZIONE) + " - Errore: " + esito.getMsg();
    }
    
  }

}
