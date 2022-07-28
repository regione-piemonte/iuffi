package it.csi.iuffi.iuffiweb.presentation.quadro.allegati;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.permission.UpdatePermissionProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati.FileAllegatiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.GruppoInfoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/cuiuffi108")
@IuffiSecurity(value = "CU-IUFFI-108", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI108AllegatiOggetto extends BaseController
{
  public static final String CU_NAME = "CU-IUFFI-108";
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
    List<GruppoInfoDTO> allegati = quadroEJB.getDichiarazioniOggetto(
        idProcedimentoOggetto, quadro.getIdQuadroOggetto(),
        procedimentoOggetto.getIdBandoOggetto());
    Map<Long, List<FileAllegatiDTO>> fileMap = quadroEJB
        .getMapFileAllegati(idProcedimentoOggetto);

    model.addAttribute("allegati", allegati);
    model.addAttribute("fileMap", fileMap);
    model.addAttribute("canUpdate", canUpdate(idProcedimentoOggetto, session));
    return "allegati/allegatiOggetto";
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
