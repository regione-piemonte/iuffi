package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.rendicontazionespese.saldo;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaRendicontazioneSpese;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-211-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi211m")
public class CUIUFFI211MRendicontazioneSpeseSaldoModifica
    extends CUIUFFI211RendicontazioneSpeseSaldoAbstract
{

  @RequestMapping(value = "/modifica", method = RequestMethod.POST)
  public String controllerModifica(Model model, HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    String[] idIntervento = request.getParameterValues("id");
    List<Long> ids = IuffiUtils.LIST.toListOfLong(idIntervento);

    HttpSession session = request.getSession();
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    List<RigaRendicontazioneSpese> list = rendicontazioneEAccertamentoSpeseEJB
        .getElencoRendicontazioneSpese(idProcedimentoOggetto, ids);
    if (request.getParameter("confermaModificaInterventi") != null)
    {
      if (validateAndUpdate(model, request, list))
      {
        return "redirect:../" + CU_BASE_NAME + "l/index.do";
      }
      else
      {
        model.addAttribute("preferRequest", Boolean.TRUE);
      }
    }
    model.addAttribute("interventi", list);
    model.addAttribute("action", "../" + CU_BASE_NAME + "m/modifica.do");
    return JSP_BASE_URL + "modificaMultipla";
  }

  private boolean validateAndUpdate(Model model, HttpServletRequest request,
      List<RigaRendicontazioneSpese> list)
      throws InternalUnexpectedException
  {
    HttpSession session = request.getSession();
    Errors errors = new Errors();
    BigDecimal totaleContributoRichiesto = BigDecimal.ZERO;
    for (RigaRendicontazioneSpese riga : list)
    {
      final long idIntervento = riga.getIdIntervento();
      final String nameNote = "note_" + idIntervento;
      String note = request.getParameter(nameNote);
      if (errors.validateFieldMaxLength(note, nameNote, 4000))
      {
        riga.setNote(note);
      }
      String nameFlagInterventoCompletato = "flagInterventoCompletato_"
          + idIntervento;
      if (request.getParameter(nameFlagInterventoCompletato) != null)
      {
        riga.setFlagInterventoCompletato(IuffiConstants.FLAGS.SI);
      }
      else
      {
        riga.setFlagInterventoCompletato(IuffiConstants.FLAGS.NO);
      }
      if (!IuffiConstants.FLAGS.SI.equals(riga.getUsaDocumentiSpesa()))
      {
        String nameSpesaSostenutaAttuale = "importoSpesa_" + idIntervento;
        BigDecimal spesaSostenutaAttuale = errors
            .validateMandatoryBigDecimalInRange(
                request.getParameter(nameSpesaSostenutaAttuale),
                nameSpesaSostenutaAttuale,
                2, BigDecimal.ZERO, IuffiConstants.MAX.IMPORTO_INTERVENTO);
        riga.setImportoSpesa(spesaSostenutaAttuale);
        if (spesaSostenutaAttuale != null)
        {
          BigDecimal contributoRichiesto = spesaSostenutaAttuale.multiply(
              riga.getPercentualeContributo(),
              MathContext.DECIMAL128);
          contributoRichiesto = contributoRichiesto.scaleByPowerOfTen(-2);
          contributoRichiesto = contributoRichiesto.setScale(2,
              RoundingMode.HALF_UP);
          totaleContributoRichiesto = totaleContributoRichiesto
              .add(contributoRichiesto);
          riga.setContributoRichiesto(contributoRichiesto);
        }
      }
    }
    if (!errors.addToModelIfNotEmpty(model))
    {
      rendicontazioneEAccertamentoSpeseEJB.updateRendicontazioneSpese(list,
          getLogOperationOggettoQuadroDTO(session));
      return true;
    }
    else
    {
      return false;
    }
  }

  @RequestMapping(value = "/modifica_multipla", method = RequestMethod.POST)
  public String modificaMultipla(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    String[] idIntervento = request.getParameterValues("idIntervento");
    List<Long> ids = IuffiUtils.LIST.toListOfLong(idIntervento);

    HttpSession session = request.getSession();
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    List<RigaRendicontazioneSpese> list = rendicontazioneEAccertamentoSpeseEJB
        .getElencoRendicontazioneSpese(idProcedimentoOggetto, ids);
    model.addAttribute("interventi", list);
    model.addAttribute("action", "../" + CU_BASE_NAME + "m/modifica.do");
    return JSP_BASE_URL + "modificaMultipla";
  }

  @RequestMapping(value = "/modifica_singola_{idIntervento}", method = RequestMethod.GET)
  public String modificaSingola(Model model, HttpServletRequest request,
      @PathVariable("idIntervento") long idIntervento)
      throws InternalUnexpectedException
  {
    HttpSession session = request.getSession();
    List<Long> ids = new ArrayList<Long>();
    ids.add(idIntervento);
    final long idProcedimentoOggetto = getIdProcedimentoOggetto(session);
    List<RigaRendicontazioneSpese> list = rendicontazioneEAccertamentoSpeseEJB
        .getElencoRendicontazioneSpese(idProcedimentoOggetto, ids);
    model.addAttribute("interventi", list);
    model.addAttribute("action", "../" + CU_BASE_NAME + "m/modifica.do");
    return JSP_BASE_URL + "modificaMultipla";
  }
}
