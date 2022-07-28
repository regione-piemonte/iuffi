package it.csi.iuffi.iuffiweb.presentation.quadro.caratteristicheAziendali;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.permission.UpdatePermissionProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati.FileAllegatiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.caratteristicheaziendali.CaratteristicheAziendali;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.GruppoInfoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi313v")
@IuffiSecurity(value = "CU-IUFFI-313-V", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI313VisualizzaCaratteristicheAziendali extends BaseController
{
  public static final String CU_NAME = "CU-IUFFI-313-V";
  @Autowired
  IQuadroEJB                 quadroEJB;

  @RequestMapping("/index")
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    
    //Cerco le caratteristiche aziendali salvate per il procedimento oggetto selezionato
    CaratteristicheAziendali dati = quadroEJB.getCaratteristicheAziendali(idProcedimentoOggetto);
    
    if(dati != null) {
      // Se  trovo le caratteritiche aziendali vado a cercare tutte le info da visualizzare (metodi di coltivazione, filiere etc)
      List<DecodificaDTO<Long>> metodiColt = quadroEJB.getListaMetodiAttivi(dati.getId());
      List<DecodificaDTO<Long>> filiere = quadroEJB.getListaFiliereAttivi(dati.getId());
      List<DecodificaDTO<Long>> multi = quadroEJB.getListaMultiAttivi(dati.getId());
      List<String> tipiAttivita = quadroEJB.getListaTipoAttivita(procedimentoOggetto.getIdProcedimento());
      
      // costruisco le descrizioni per attivita, filiere, metodi coltivazione e multifunzionalita
      if(tipiAttivita != null && tipiAttivita.size() > 0) {
        dati.setDesccatattiva(tipiAttivita.get(0));
      }
      
      String metodoCOltivazione = getStringaDescrizione(metodiColt);
      dati.setDescmetodocolt(metodoCOltivazione);
      
      String fil = getStringaDescrizione(filiere);
      dati.setDescfiliera(fil);
      
      String mult = getStringaDescrizione(multi);
      dati.setDescmulti(mult);
    }
    
    model.addAttribute("datiInseriti", dati != null);
    model.addAttribute("caratteristiche", dati);
    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB,
        procedimentoOggetto.getIdProcedimentoOggetto(),
        quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
    return "caratteristicheAziendali/dettaglioCaratteristicheAziendali";
  }
  
  private String getStringaDescrizione(List<DecodificaDTO<Long>> input) {
    String result = "";
    int i = 0;
    for (DecodificaDTO<Long> decodificaDTO : input)
    {
      if(i == 0) {
        result = decodificaDTO.getDescrizione();
      }
      else {
        result = result + ", " + decodificaDTO.getDescrizione();
      }
      i++;
    }
    return result;
    
  }

  public boolean canUpdate(long idProcedimentoOggetto, HttpSession session)
      throws InternalUnexpectedException
  {
    UpdatePermissionProcedimentoOggetto permission = null;
    try
    {
      permission = quadroEJB.canUpdateProcedimentoOggetto(idProcedimentoOggetto,
          true);
    }
    catch (IuffiPermissionException e)
    {
      return false;
    }
    String extCodAttore = permission.getExtCodAttore();
    if (extCodAttore != null)
    {
      UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
      return IuffiUtils.PAPUASERV.isAttoreAbilitato(utenteAbilitazioni,
          extCodAttore);
    }
    return true;

  }
}
