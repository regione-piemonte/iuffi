package it.csi.iuffi.iuffiweb.presentation.listeliquidazione;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IListeLiquidazioneEJB;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONElencoListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping("/cuiuffi229")
@IuffiSecurity(value = "CU-IUFFI-229", controllo = IuffiSecurity.Controllo.DEFAULT)
public class CUIUFFI229EliminaListaLiquidazione extends BaseController
{

  @Autowired
  IListeLiquidazioneEJB listeLiquidazioneEJB = null;

  @IsPopup
  @RequestMapping(value = "/confermaElimina_{idListaLiquidazione}", method = RequestMethod.GET)
  public String confermaElimina(
      @PathVariable("idListaLiquidazione") Long idListaLiquidazione,
      HttpServletRequest request, Model model)
      throws InternalUnexpectedException
  {
    model.addAttribute("idListaLiquidazione", idListaLiquidazione);
    return "listeliquidazione/confermaElimina";
  }

  @RequestMapping(value = "/elimina_{idListaLiquidazione}", method = RequestMethod.GET)
  public String elimina(
      @PathVariable("idListaLiquidazione") Long idListaLiquidazione,
      HttpServletRequest request, Model model)
      throws InternalUnexpectedException
  {
    RigaJSONElencoListaLiquidazioneDTO lista = listeLiquidazioneEJB
        .getListaLiquidazioneById(idListaLiquidazione);

    boolean canUpdate = IuffiUtils.PAPUASERV.hasAmministrazioneCompetenza(
        getUtenteAbilitazioni(request.getSession()),
        lista.getExtIdAmmCompetenza());

    if (canUpdate && !lista.getFlagStatoLista().equals("A"))
    {
      listeLiquidazioneEJB.deleteListaLiquidazione(idListaLiquidazione);
      return "dialog/success";
    }
    model.addAttribute("error",
        "L'utente corrente non è abilitato a questa operazione.");
    return "dialog/error";
  }

}
