package it.csi.iuffi.iuffiweb.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping(value = "/datigeografici")
@IuffiSecurity(value = "", controllo = IuffiSecurity.Controllo.NESSUNO)
public class DatiGeograficiController extends BaseController
{
  @Autowired
  IInterventiEJB interventiEJB = null;

  @IsPopup
  @RequestMapping(value = "/elenco_comuni_attivi_{istatProvincia}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<DecodificaDTO<String>> elencoComuniAttiviPerProvincia(Model model,
      @PathVariable("istatProvincia") String istatProvincia,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> list = interventiEJB.getDecodificheComuni(null,
        istatProvincia, IuffiConstants.FLAGS.NO);
    if (list == null)
    {
      list = new ArrayList<DecodificaDTO<String>>();
    }
    return list;
  }

  @IsPopup
  @RequestMapping(value = "/elenco_comuni_prov_attivi_{istatProvincia}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<ComuneDTO> elencoComuniProvAttiviPerProvincia(Model model,
      @PathVariable("istatProvincia") String istatProvincia,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<ComuneDTO> list = interventiEJB.getDecodificheComuniWidthProv(null,
        istatProvincia, IuffiConstants.FLAGS.NO);
    if (list == null)
    {
      list = new ArrayList<ComuneDTO>();
    }
    return list;
  }

  @IsPopup
  @RequestMapping(value = "/elenco_comuni_attivi", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public List<ComuneDTO> elencoComuniAttiviByDescrizione(Model model,
      @RequestParam("descComune") String descComune,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<ComuneDTO> list = interventiEJB.getDecodificheComuniWidthProvByComune(
        descComune, IuffiConstants.FLAGS.NO);
    if (list == null)
    {
      list = new ArrayList<ComuneDTO>();
    }
    return list;
  }

  @IsPopup
  @RequestMapping(value = "/elenco_comuni_{istatProvincia}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<DecodificaDTO<String>> elencoComuniPerProvincia(Model model,
      @PathVariable("istatProvincia") String istatProvincia,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return interventiEJB.getDecodificheComuni(null, istatProvincia, null);
  }

  @IsPopup
  @RequestMapping(value = "/elenco_comuni_conduzioni_{istatProvincia}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<DecodificaDTO<String>> elencoComuniPerProvinciaConTerreniInConduzione(
      Model model,
      @PathVariable("istatProvincia") String istatProvincia,
      HttpSession session)
      throws InternalUnexpectedException
  {
    int idProcedimentoAgricoltura = getUtenteAbilitazioni(session).getIdProcedimento();
    return interventiEJB.getComuniPerProvinciaConTerreniInConduzione(
        getIdProcedimentoOggetto(session),
        istatProvincia, idProcedimentoAgricoltura);
  }

  @IsPopup
  @RequestMapping(value = "/elenco_province_{idRegione}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<DecodificaDTO<String>> elencoProvincePerRegione(Model model,
      @PathVariable("idRegione") String idRegione, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return interventiEJB.getProvincie(idRegione);
  }

  @IsPopup
  @RequestMapping(value = "/elenco_sezioni_condotte_{istatComune}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<DecodificaDTO<String>> elencoSezioniPerComuneConTerreniInConduzione(
      Model model, @PathVariable("istatComune") String istatComune,
      HttpSession session)
      throws InternalUnexpectedException
  {
	int idProcedimentoAgricoltura = getUtenteAbilitazioni(session).getIdProcedimento();
    return interventiEJB.getSezioniPerComuneConTerreniInConduzione(
        getIdProcedimentoOggetto(session), istatComune, idProcedimentoAgricoltura);
  }

  @IsPopup
  @RequestMapping(value = "/elenco_sezioni_{istatComune}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<DecodificaDTO<String>> elencoSezioniPerComune(Model model,
      @PathVariable("istatComune") String istatComune, HttpSession session)
      throws InternalUnexpectedException
  {
    return interventiEJB.getSezioniPerComune(istatComune);
  }

  @IsPopup
  @RequestMapping(value = "/elenco_comuni_italia", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<ComuneDTO> elencoComuniItaliani(Model model,
      @PathVariable("istatComune") String istatComune, HttpSession session)
      throws InternalUnexpectedException
  {
    return interventiEJB.getComuni(null, null, IuffiConstants.FLAGS.NO,
        IuffiConstants.FLAGS.NO);
  }
}
