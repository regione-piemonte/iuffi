package it.csi.iuffi.iuffiweb.presentation.quadro.canalivendita;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.permission.UpdatePermissionProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati.ContenutoFileAllegatiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati.FileAllegatiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.canalidivendita.CanaliDiVendita;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.canalidivendita.ImmaginiCanaliDiVendita;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.caratteristicheaziendali.CaratteristicheAziendali;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.GruppoInfoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi323v")
@IuffiSecurity(value = "CU-IUFFI-323-V", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI323VisualizzaCanaliDiVendita extends BaseController
{
  public static final String CU_NAME = "CU-IUFFI-323-V";
  @Autowired
  IQuadroEJB                 quadroEJB;

  @RequestMapping("/index")
  public String index(Model model, HttpSession session) throws InternalUnexpectedException {
    ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        session);
    QuadroOggettoDTO quadro = procedimentoOggetto.findQuadroByCU(CU_NAME);
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);  
    //Cerco i canali salvati per il procedimento oggetto selezionato
    CanaliDiVendita dati = quadroEJB.getCanaliDiVendita(idDatiProcedimento);
    if(dati != null) {
      // Se  trovo le caratteritiche aziendali vado a cercare tutte le info da visualizzare (metodi di coltivazione, filiere etc)
      List<DecodificaDTO<Long>> listaCanali = quadroEJB.getListaCanaliDiVenditaAttivi(dati.getId());      
      // costruisco le descrizioni per attivita, filiere, metodi coltivazione e multifunzionalita
      String canali = getStringaDescrizione(listaCanali);
      dati.setCanale(canali);
    }
    
    model.addAttribute("datiInseriti", dati != null);
    model.addAttribute("canali", dati);
    model.addAttribute("ultimaModifica", getUltimaModifica(quadroEJB,
        procedimentoOggetto.getIdProcedimentoOggetto(),
        quadro.getIdQuadroOggetto(), procedimentoOggetto.getIdBandoOggetto()));
    return "canalidivendita/dettaglioCanaliVendita";
  }
  
  private String getStringaDescrizione(List<DecodificaDTO<Long>> input) {
    String result = "";
    int i = 0;
    for (DecodificaDTO<Long> decodificaDTO : input) {
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
  
  @RequestMapping(value = "visualizza_immagine_{idImmagine}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> visualizzaImmagine(HttpSession session,
      @PathVariable("idImmagine") String idImmagine)
      throws InternalUnexpectedException
  {
    Long id = null;
    if(idImmagine != null) {
      id = new Long(idImmagine);
      ImmaginiCanaliDiVendita immagine = null;
      List<ImmaginiCanaliDiVendita> listaImg = quadroEJB.getListaImmagini();
      for (ImmaginiCanaliDiVendita immaginiCanaliDiVendita : listaImg) {
        if(id.compareTo(immaginiCanaliDiVendita.getId()) == 0) {
          immagine = immaginiCanaliDiVendita;
        }
      }
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add("Content-type",
          IuffiUtils.FILE.getMimeType(".jpg"));
      httpHeaders.add("Content-Disposition",
          "attachment; filename=\"\"");

      if(immagine!=null && immagine.getImmagine() != null){
         ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
             immagine.getImmagine(), httpHeaders, HttpStatus.OK);
              return response;
      }
    }
    
   return null;
  }

}
