package it.csi.iuffi.iuffiweb.presentation.quadro.economico;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.dto.LogOperationOggettoQuadroDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InfoRiduzione;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico.RangePercentuale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.quadroeconomico.RigaJSONInterventoQuadroEconomicoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-163-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi163m")
public class CUIUFFI163MQuadroEconomicoModifica extends BaseController
{
  @Autowired
  IInterventiEJB interventiEJB;

  @RequestMapping(value = "/modifica", method = RequestMethod.POST)
  public String controllerModifica(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    String[] idIntervento = request.getParameterValues("idIntervento");
    List<Long> ids = IuffiUtils.LIST.toListOfLong(idIntervento);

    HttpSession session = request.getSession();
    List<RigaJSONInterventoQuadroEconomicoDTO> list = interventiEJB
        .getInterventiQuadroEconomicoPerModifica(
            getIdProcedimentoOggetto(session), ids);
    if (request.getParameter("confermaModificaInterventi") != null)
    {
      if (validateAndUpdate(model, request, list))
      {
        return "redirect:../cuiuffi163l/index.do";
      }
      else
      {
        model.addAttribute("preferRequest", Boolean.TRUE);
      }
    }
    model.addAttribute("interventi", list);
    model.addAttribute("action", "../cuiuffi163m/modifica.do");
    calcolaTotaliEVerificaColonnaProgressivo(model, list);
    return "quadroeconomico/modificaMultipla";
  }

  private void calcolaTotaliEVerificaColonnaProgressivo(Model model,
      List<RigaJSONInterventoQuadroEconomicoDTO> list)
  {
    BigDecimal bdTotaleInvestimento = BigDecimal.ZERO;
    BigDecimal bdTotaleAmmesso = BigDecimal.ZERO;
    BigDecimal bdTotaleContributo = BigDecimal.ZERO;
    boolean progressivo = false;
    if (list != null && !list.isEmpty())
    {
      for (RigaJSONInterventoQuadroEconomicoDTO rigaDTO : list)
      {
        bdTotaleInvestimento = IuffiUtils.NUMBERS.add(bdTotaleInvestimento,
            IuffiUtils.NUMBERS.nvl(rigaDTO.getImportoInvestimento()));
        bdTotaleAmmesso = IuffiUtils.NUMBERS.add(bdTotaleAmmesso,
            IuffiUtils.NUMBERS
                .nvl(rigaDTO.getImportoAmmessoOInvestimento()));
        bdTotaleContributo = IuffiUtils.NUMBERS.add(bdTotaleContributo,
            IuffiUtils.NUMBERS.nvl(rigaDTO.getImportoContributo()));
        if (rigaDTO.getProgressivo() != null)
        {
          progressivo = true;
        }
      }
    }
    bdTotaleInvestimento = bdTotaleInvestimento.setScale(2);
    bdTotaleAmmesso = bdTotaleAmmesso.setScale(2);
    bdTotaleContributo = bdTotaleContributo.setScale(2);
    model.addAttribute("totaleInvestimento", bdTotaleInvestimento);
    model.addAttribute("totaleAmmesso", bdTotaleAmmesso);
    model.addAttribute("totaleContributo", bdTotaleContributo);
    model.addAttribute("progressivo", Boolean.valueOf(progressivo));
    if (progressivo)
    {
      model.addAttribute("spanTotali", "3");
    }
    else
    {
      model.addAttribute("spanTotali", "2");
    }
  }

  @RequestMapping(value = "/modifica_multipla", method = RequestMethod.POST)
  public String modificaMultipla(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    String[] idIntervento = request.getParameterValues("idIntervento");
    List<Long> ids = IuffiUtils.LIST.toListOfLong(idIntervento);
    List<RigaJSONInterventoQuadroEconomicoDTO> list = interventiEJB
        .getInterventiQuadroEconomicoPerModifica(
            getIdProcedimentoOggetto(request.getSession()), ids);
    if (checkForDeleted(list, model))
    {
      return "errore/utenteNonAutorizzato";
    }
    model.addAttribute("interventi", list);
    model.addAttribute("action", "../cuiuffi163m/modifica.do");
    calcolaTotaliEVerificaColonnaProgressivo(model, list);
    return "quadroeconomico/modificaMultipla";
  }

  public boolean checkForDeleted(
      List<RigaJSONInterventoQuadroEconomicoDTO> list, Model model)
  {
    if (list != null)
    {
      for (RigaJSONInterventoQuadroEconomicoDTO intervento : list)
      {
        if (IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_ELIMINAZIONE
            .equals(intervento.getFlagTipoOperazione()))
        {
          model.addAttribute("errore",
              "L'intervento \"" + intervento.getDescIntervento()
                  + "\" è cessato, impossibile modificarlo");
          return true;
        }
      }
    }
    return false;
  }

  @RequestMapping(value = "/modifica_singola_{idIntervento}", method = RequestMethod.GET)
  public String modificaSingola(Model model, HttpServletRequest request,
      @PathVariable("idIntervento") long idIntervento)
      throws InternalUnexpectedException
  {
    HttpSession session = request.getSession();
    List<Long> ids = new ArrayList<Long>();
    ids.add(idIntervento);
    List<RigaJSONInterventoQuadroEconomicoDTO> list = interventiEJB
        .getInterventiQuadroEconomicoPerModifica(
            getIdProcedimentoOggetto(session), ids);
    if (checkForDeleted(list, model))
    {
      return "errore/utenteNonAutorizzato";
    }
    model.addAttribute("interventi", list);
    model.addAttribute("action", "../cuiuffi163m/modifica.do");
    calcolaTotaliEVerificaColonnaProgressivo(model, list);
    return "quadroeconomico/modificaMultipla";
  }

  private boolean validateAndUpdate(Model model, HttpServletRequest request,
      List<RigaJSONInterventoQuadroEconomicoDTO> list)
      throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    InfoRiduzione infoRiduzione = interventiEJB
        .getInfoRiduzione(getIdProcedimentoOggetto(request.getSession()));
    BigDecimal percentualeRiduzione = null;
    if (infoRiduzione != null)
    {
      percentualeRiduzione = infoRiduzione.getPercentuale();
    }
    for (RigaJSONInterventoQuadroEconomicoDTO riga : list)
    {
      final long idIntervento = riga.getIdIntervento();
      final String nameImportoAmmesso = "importoAmmesso_" + idIntervento;
      BigDecimal importoInvestimento = riga.getImportoInvestimento();
      if (percentualeRiduzione != null)
      {
        importoInvestimento = importoInvestimento
            .subtract(importoInvestimento
                .multiply(percentualeRiduzione, MathContext.DECIMAL128)
                .scaleByPowerOfTen(-2).setScale(2, BigDecimal.ROUND_HALF_UP),
                MathContext.DECIMAL128)
            .setScale(2, BigDecimal.ROUND_HALF_UP);
      }
      final BigDecimal importoAmmesso = errors
          .validateMandatoryBigDecimalInRange(
              request.getParameter(nameImportoAmmesso), nameImportoAmmesso, 2,
              BigDecimal.ZERO,
              importoInvestimento);
      riga.setImportoAmmesso(importoAmmesso);
      final String namePercentualeContributo = "percentualeContributo_"
          + idIntervento;
      BigDecimal percentualeContributo = null;
      if (!riga.isPercentualeFissa())
      {
        percentualeContributo = errors.validateMandatoryBigDecimalInRange(
            request.getParameter(namePercentualeContributo),
            namePercentualeContributo, 2,
            riga.getPercentualeContributoMinima(),
            riga.getPercentualeContributoMassima());
      }
      else
      {
        /*
         * Se l'importo è fisso non devo fare test, da html il campo è disabled,
         * quindi non ce l'ho in input ==> prendo il valore massimo ammesso (o
         * minimo, tanto sono uguali)
         */
        percentualeContributo = riga.getPercentualeContributoMassima();
      }
      riga.setPercentualeContributo(percentualeContributo);
      if (importoAmmesso != null && percentualeContributo != null)
      {
        BigDecimal importoContributo = importoAmmesso
            .multiply(percentualeContributo, MathContext.DECIMAL128)
            .divide(new BigDecimal(100, MathContext.DECIMAL128))
            .setScale(2, RoundingMode.HALF_UP);
        riga.setImportoContributo(importoContributo);
      }
    }
    if (!errors.addToModelIfNotEmpty(model))
    {
      interventiEJB.updateInterventiQuadroEconomico(list,
          getLogOperationOggettoQuadroDTO(request.getSession()));
      return true;
    }
    else
    {
      return false;
    }
  }

  @IsPopup
  @RequestMapping(value = "/conferma_modifica_percentuale_multipla", method = RequestMethod.GET)
  public String confermaModificaPercentualeMultipla(Model model,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    final String[] idIntervento = request.getParameterValues("idIntervento");
    List<RangePercentuale> rangePercentuali = interventiEJB
        .getRangePercentuali(IuffiUtils.ARRAY.toLong(idIntervento));
    model.addAttribute("percentualeRibasso",
        interventiEJB.isBandoConPercentualeRiduzione(
            getIdProcedimentoOggetto(request.getSession())));
    model.addAttribute("rangePercentuali", rangePercentuali);
    return "quadroeconomico/confermaModificaPercentualeMultipla";
  }

  @IsPopup
  @RequestMapping(value = "/modifica_percentuale_multipla", method = RequestMethod.POST)
  public String modificaPercentualeMultipla(Model model,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    final long[] idIntervento = IuffiUtils.ARRAY
        .toLong(request.getParameterValues("idIntervento"));
    List<RangePercentuale> rangePercentuali = interventiEJB
        .getRangePercentuali(idIntervento);
    model.addAttribute("rangePercentuali", rangePercentuali);
    Map<Long, BigDecimal> mapInterventiPercentuali = new HashMap<Long, BigDecimal>();
    for (RangePercentuale range : rangePercentuali)
    {
      String key = range.getKey();
      final BigDecimal percentualeContributoMinima = range
          .getPercentualeContributoMinima();
      final BigDecimal percentualeContributoMassima = range
          .getPercentualeContributoMassima();
      BigDecimal bdPercentuale = null;
      if (range.isFixed())
      {
        // Le percentuali max e min di sistema sono uguali ==> uso quindi questa
        // percentuale come valore obbligatorio
        bdPercentuale = percentualeContributoMassima; // va bene qualsiasi delle
                                                      // 2 tanto sono uguali
      }
      else
      {
        // Le percentuali min e max non sono uguali, valido che quanto inserito
        // dall'utente sia compreso nel range
        bdPercentuale = errors.validateMandatoryBigDecimalInRange(
            request.getParameter(key), key, 2, percentualeContributoMinima,
            percentualeContributoMassima);
      }
      if (bdPercentuale != null)
      {
        for (Long lIdIntervento : range.getIdInterventi())
        {
          mapInterventiPercentuali.put(lIdIntervento, bdPercentuale);
        }
      }
    }
    if (errors.addToModelIfNotEmpty(model))
    {
      model.addAttribute("preferRequest", Boolean.TRUE);
      return confermaModificaPercentualeMultipla(model, request);
    }
    HttpSession session = request.getSession();
    LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO = getLogOperationOggettoQuadroDTO(
        session);
    interventiEJB.modificaPercentualeInterventoQuadroEconomico(idIntervento,
        mapInterventiPercentuali, logOperationOggettoQuadroDTO);
    return "dialog/success";
  }

}