package it.csi.iuffi.iuffiweb.presentation.quadro.concessioni.gestionepratiche;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoVO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-317-V", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping(value = "/cuiuffi317v")
public class CUIUFFI317VRecuperaDatiVisura extends BaseController
{

  @Autowired
  IQuadroIuffiEJB quadroIuffiEJB;
  @Autowired
  IQuadroEJB quadroEJB;
  
  @IsPopup
  @RequestMapping(value = "/index_{idConcessione}", method = RequestMethod.GET)
  @ResponseBody
  public String index(@PathVariable("idConcessione") long idConcessione, Model model, HttpServletRequest request) throws InternalUnexpectedException
  {
    
    ConcessioneDTO concessione = quadroIuffiEJB.getConcessione(idConcessione);
    if(concessione.getIdStatoConcessione()!=IuffiConstants.CONCESSIONI.STATO.RICHIESTA_VERCOR || concessione.getExtIdVisMassivaRna()==null)
    {
      return quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.RICHIESTA_VERCOR_NON_EFFETTUATA);
    }
    

    if(quadroIuffiEJB.esisteIdVisMassivaRnaSuVista(concessione.getExtIdVisMassivaRna()))
    {
      /*
       * E’ possibile recuperare i dati solo per le aziende lavorate, aziende
       * che hanno DATA_VISURA e VERCOR_RNA valorizzato, per ogni azienda
       * lavorata proseguire con l’aggiornamento dei dati di visura.
       */
      Map<String, String> parametri = quadroEJB.getParametri(new String[]
      { IuffiConstants.PARAMETRO.IMPORTO_DEMINIMIS });
      String importoDeminimisStr = parametri
          .get(IuffiConstants.PARAMETRO.IMPORTO_DEMINIMIS);
      
      BigDecimal importoDeminimis = new BigDecimal(importoDeminimisStr);
      List<ProcedimentoOggettoVO> aziende = quadroIuffiEJB.getAziendeDaVistaRegata(concessione.getExtIdVisMassivaRna());
      String identificativoOld = "";
      BigDecimal importoDisponibileAzienda = BigDecimal.ZERO;
      if(aziende!=null && !aziende.isEmpty())
      {
        for(ProcedimentoOggettoVO azienda : aziende)
        {
          //TODO calcolo sull'importo e se è ok e aggiorno importo e data visura
          if(!identificativoOld.equals(azienda.getIdentificativo()))
           {
            importoDisponibileAzienda = importoDeminimis.subtract(azienda.getImportoConcessoTotale()==null?BigDecimal.ZERO : azienda.getImportoConcessoTotale());
            identificativoOld=azienda.getIdentificativo();
           }
            
          if(importoDisponibileAzienda.compareTo(azienda.getImportoPerizia())>=0)
          {
            quadroIuffiEJB.updatePraticaConcessioneEVercorProcedimento(azienda.getIdProcedimento(), azienda.getIdPraticheConcessione(), azienda.getNumeroVercor(), azienda.getDataEsitoRna(), getIdUtenteLogin(request.getSession()));
            importoDisponibileAzienda = importoDisponibileAzienda.subtract(azienda.getImportoPerizia());
          }
        }
      }
      else{
         return "Richiesta effettuata ma ancora in elaborazione.";
      }
    }
    else
    {
      return quadroEJB.getMessaggioErrore(IuffiConstants.MESSAGGIO_ERRORE.RICHIESTA_DA_EFFETTUARE);
    }

    return "Operazione completata con successo.";
  }
  


}
