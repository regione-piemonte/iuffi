package it.csi.iuffi.iuffiweb.presentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@RequestMapping(value = "/datiamministrativi")
@IuffiSecurity(value = "", controllo = IuffiSecurity.Controllo.NESSUNO)
public class ComuniProvinceController extends BaseController
{
  @Autowired
  IQuadroEJB quadroEJB = null;

  @IsPopup
  @RequestMapping(value = "/elenco_comuni_attivi_{istatProvincia}", method = RequestMethod.GET, produces = "application/json")
  public Map<String, List<DecodificaDTO<String>>> elencoComuniAttiviPerProvincia(
      Model model, @PathVariable("istatProvincia") String istatProvincia,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<String>> list = quadroEJB.getDecodificheComuni(null,
        istatProvincia, IuffiConstants.FLAGS.NO);
    Map<String, List<DecodificaDTO<String>>> result = new HashMap<String, List<DecodificaDTO<String>>>();
    result.put("comuni", list);
    return result;
  }

  @IsPopup
  @RequestMapping(value = "/elenco_comuni_{istatProvincia}", method = RequestMethod.GET, produces = "application/json")
  public List<DecodificaDTO<String>> elencoComuniPerProvincia(Model model,
      @PathVariable("istatProvincia") String istatProvincia,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return quadroEJB.getDecodificheComuni(null, istatProvincia, null);
  }

  @IsPopup
  @RequestMapping(value = "/elenco_province_{idRegione}", method = RequestMethod.GET, produces = "application/json")
  public List<DecodificaDTO<String>> elencoProvincePerRegione(Model model,
      @PathVariable("idRegione") String idRegione, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return quadroEJB.getProvincie(idRegione);
  }
}
