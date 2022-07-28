package it.csi.iuffi.iuffiweb.presentation.quadro.concessioni.gestionepratiche;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

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
import it.csi.iuffi.iuffiweb.integration.ws.regata.CredenzialiDTO;
import it.csi.iuffi.iuffiweb.integration.ws.regata.DatiAziendaDTO;
import it.csi.iuffi.iuffiweb.integration.ws.regata.EsitoRegataMassiveVO;
import it.csi.iuffi.iuffiweb.integration.ws.regata.EsitoRegataVO;
import it.csi.iuffi.iuffiweb.integration.ws.regata.PayloadMassiveDTO;
import it.csi.iuffi.iuffiweb.integration.ws.regata.RegataServiceFactory;
import it.csi.iuffi.iuffiweb.integration.ws.regata.exception.InternalException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-317-M", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi317m")
public class CUIUFFI317MInviaAziendeMassivo extends BaseController
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
    if(concessione.getIdStatoConcessione()!=IuffiConstants.CONCESSIONI.STATO.RICHIESTA_VERCOR )
    {
      return quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.RICHIESTA_VERCOR_NON_EFFETTUATA);
    }
    
    List<DatiAziendaDTO> elencoAziende = quadroIuffiEJB.getElencoAziendeConcessione(idConcessione);
    if(elencoAziende!=null)
    {
      if(elencoAziende.size()>=500)
      {
        return quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.TROPPE_AZIENDE);
      }
      
      PayloadMassiveDTO input = new PayloadMassiveDTO();
      CredenzialiDTO credenziali = new CredenzialiDTO();
      Long idUtenteLogin = 94770l;
      credenziali.setIdUtenteIride(idUtenteLogin);
      credenziali.setCodiceFruitore("IUFFI");
      credenziali.setIdRichiestaMassiva(quadroIuffiEJB.getIdRichiesta(idConcessione));
      input.setCredenziali(credenziali);
      input.setDatiAzienda(elencoAziende);
      
      EsitoRegataMassiveVO esiti = RegataServiceFactory.getRestServiceClient().inviaAziendeMassivo(input);
      quadroIuffiEJB.aggiornaFlagVisuraConcessione(idConcessione, idUtenteLogin);

      List<String> cuaaAziende = new ArrayList<String>();
      for(DatiAziendaDTO aziendaDTO : elencoAziende)
      {
        if(!cuaaAziende.contains(aziendaDTO.getCuaa()))
          cuaaAziende.add(aziendaDTO.getCuaa());
      }
      //Verificare che tutte le aziende siano state caricate correttamente accedendo alla vista REGATA_V_VISURE_RNA con CUAA_AZIENDA e ID_VIS_MASSIVA_RNA, 
      int nCuaaCaricati = quadroIuffiEJB.getNumeroAziendeInseriteCorrettamente(cuaaAziende, concessione.getExtIdVisMassivaRna());
      if(nCuaaCaricati==cuaaAziende.size())
      {
        return quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.RICHIESTA_INVIATA_CORRETTAMENTE);
      }
      else if(nCuaaCaricati==0)
      {
        return quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.ERRORE_RICHIESTA);        
      }
      else
      {
        String ret = quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.RICHIESTA_INVIATA_CON_ERRORI);        
        for( EsitoRegataVO esito : esiti.getLista_esiti())
        {
          if(esito.getIdControlliInterni()!=0)
          {
            ret += esito.getMsg() + "\n";
          }
        }
        return ret;
      }
      
    }

    return "Attenzione! Si è verificato un errore: non sono state trovate aziende per questa concessione.";
  }

}
