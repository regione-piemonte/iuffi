package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.rendicontazionesuperfici;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IRendicontazioneEAccertamentoSpeseEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaJSONRendicontazioneSuperficiDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-255-DB", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi255db")
public class CUIUFFI255DBDettaglioRendicontazioneSuperficiBeneficiario
    extends BaseController
{
  private static final String                     BASE_JSP_PATH = "rendicontazionesuperifici/beneficiario/";
  @Autowired
  protected IRendicontazioneEAccertamentoSpeseEJB rendicontazioneEAccertamentoSpeseEJB;

  @RequestMapping(value = "/dettaglio_{idIntervento}", method = RequestMethod.GET)
  protected String modifica(Model model,
      @PathVariable("idIntervento") long idIntervento,
      HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    final List<RigaJSONRendicontazioneSuperficiDTO> elenco = rendicontazioneEAccertamentoSpeseEJB
        .getRendicontazioniSuperficiJSON(
            getIdProcedimentoOggetto(request.getSession()), idIntervento);
    model.addAttribute("elenco", elenco);
    calcolaTotali(model, elenco);
    return BASE_JSP_PATH + "dettaglio";
  }

  public static void calcolaTotali(Model model,
      List<RigaJSONRendicontazioneSuperficiDTO> elenco)
  {
    if (elenco != null)
    {
      BigDecimal totaleSupUtilizzata = BigDecimal.ZERO;
      BigDecimal totaleSuperficieImpegno = BigDecimal.ZERO;
      BigDecimal totaleSupEffettiva = BigDecimal.ZERO;
      for (RigaJSONRendicontazioneSuperficiDTO riga : elenco)
      {
        totaleSupUtilizzata = add(totaleSupUtilizzata,
            riga.getSuperficieUtilizzata());
        totaleSuperficieImpegno = add(totaleSuperficieImpegno,
            riga.getSuperficieImpegno());
        totaleSupEffettiva = add(totaleSupEffettiva,
            riga.getSuperficieEffettiva());
      }
      model.addAttribute("totaleSupUtilizzata", totaleSupUtilizzata);
      model.addAttribute("totaleSuperficieImpegno", totaleSuperficieImpegno);
      model.addAttribute("totaleSupEffettiva", totaleSupEffettiva);
    }
  }

  private static BigDecimal add(BigDecimal bd, String bdString)
  {
    bdString = IuffiUtils.STRING.nvl(bdString);
    return IuffiUtils.NUMBERS.nvl(bd).add(
        IuffiUtils.NUMBERS.nvl(IuffiUtils.NUMBERS
            .getBigDecimal(bdString.replace(".", "").replace(",", "."))),
        MathContext.DECIMAL128);
  }
}