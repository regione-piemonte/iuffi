package it.csi.iuffi.iuffiweb.presentation.quadro.anticipo;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
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
import it.csi.iuffi.iuffiweb.dto.ComuneDTO;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.anticipo.DatiAnticipo;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.anticipo.DatiAnticipoModificaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.anticipo.SportelloBancaDTO;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@IuffiSecurity(value = "CU-IUFFI-169-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi169m")
public class CUIUFFI169MModificaDatiAnticipo extends BaseController
{
  private static final String BENEFICIARIO = "ARPEA";
  @Autowired
  IQuadroEJB                  quadroEJB;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String visualizzaModifica(Model model, HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException
  {
    final DatiAnticipo datiAnticipo = quadroEJB
        .getDatiAnticipo(getIdProcedimentoOggetto(request.getSession()));
    if (BigDecimal.ZERO
        .compareTo(datiAnticipo.getImportoContributoAnticipabile()) == 0)
    {
      throw new ApplicationException(
          "Per nessuna delle operazioni a cui fa riferimento la domanda è prevista l'erogazione di un anticipo. Non è possibile proseguire con la modifica dei dati di anticipo");
    }
    if (datiAnticipo.getExtIdSportello() != null)
    {
      model.addAttribute("checkedBanca", IuffiConstants.HTML.CHECKED);
    }
    else
    {
      if (datiAnticipo.getExtIstatComune() != null)
      {
        model.addAttribute("checkedAltroIstituto",
            IuffiConstants.HTML.CHECKED);
      }
    }
    return loadModel(model, datiAnticipo);
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public String confermaModifica(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    Errors errors = new Errors();
    DatiAnticipo datiAnticipo = quadroEJB
        .getDatiAnticipo(getIdProcedimentoOggetto(request.getSession()));
    Map<String, String> parametri = quadroEJB.getParametri(new String[]
    { IuffiConstants.PARAMETRO.MASSIMA_PERCENTUALE_ANTICIPO });
    final String parametroMaxPercentualeAnticipo = parametri
        .get(IuffiConstants.PARAMETRO.MASSIMA_PERCENTUALE_ANTICIPO);
    IuffiUtils.ASSERT.notNull(parametroMaxPercentualeAnticipo, "Parametro "
        + IuffiConstants.PARAMETRO.MASSIMA_PERCENTUALE_ANTICIPO);
    BigDecimal MAX_PERCENTUALE_ANTICIPO = IuffiUtils.NUMBERS
        .getBigDecimal(parametroMaxPercentualeAnticipo);
    final String namePercentualeAnticipo = "percentualeAnticipo";
    final BigDecimal percentualeAnticipo = errors.validateMandatoryBigDecimalInRange(
        request.getParameter(namePercentualeAnticipo), namePercentualeAnticipo,
        2,
        IuffiConstants.MIN.PERCENTUALE_NON_ZERO,
        MAX_PERCENTUALE_ANTICIPO);
    final BigDecimal importoContributo = datiAnticipo
        .getImportoContributoAnticipabile();
    // Arrotondamento per difetto alla seconda cifra decimale in modo da non
    // superare MAI la percentuale massima
    final BigDecimal MAX_IMPORTO_ANTICIPO = IuffiUtils.NUMBERS.multiply(
        MAX_PERCENTUALE_ANTICIPO.scaleByPowerOfTen(-2), importoContributo)
        .setScale(2, RoundingMode.FLOOR);
    final String nameImportoAnticipo = "importoAnticipo";
    BigDecimal importoAnticipo = errors.validateMandatoryBigDecimalInRange(
        request.getParameter(nameImportoAnticipo), nameImportoAnticipo, 2,
        IuffiConstants.MIN.IMPORTO_NON_ZERO,
        MAX_IMPORTO_ANTICIPO);
    if (percentualeAnticipo != null && importoAnticipo != null)
    {
      BigDecimal percentualeCalcolata = importoAnticipo
          .multiply(IuffiConstants.MAX.PERCENTUALE, MathContext.DECIMAL128)
          .divide(importoContributo, MathContext.DECIMAL128)
          .setScale(3, RoundingMode.HALF_UP);
      BigDecimal delta = percentualeAnticipo
          .subtract(percentualeCalcolata, MathContext.DECIMAL128)
          .setScale(3, RoundingMode.HALF_UP).abs();
      if (delta.compareTo(IuffiConstants.MIN.PERCENTUALE_NON_ZERO) > 1) // Errore
                                                                           // massimo
                                                                           // ammesso
                                                                           // (ossia
                                                                           // delta
                                                                           // massimo
                                                                           // ammesso)
                                                                           // è
                                                                           // 0.01
      {
        final String ERROR_MESSAGE = "L'importo e la percentuale non sono coerenti tra di loro, la differenza è superiore a 1%";
        errors.addError(nameImportoAnticipo, ERROR_MESSAGE);
      }
    }
    final String nameNumeroFideiussione = "numeroFideiussione";
    final String numeroFideiussione = request
        .getParameter(nameNumeroFideiussione);
    errors.validateMandatoryFieldLength(numeroFideiussione, 0, 30,
        nameNumeroFideiussione);
    final String nameDataStipulaFideiussione = "dataStipulaFideiussione";
    Date dataStipulaFideiussione = errors.validateMandatoryDateInRange(
        request.getParameter(nameDataStipulaFideiussione),
        nameDataStipulaFideiussione, null,
        new Date(), true, true);
    final String nameDataScadenzaFideiussionee = "dataScadenzaFideiussione";
    Date dataScadenzaFideiussione = errors.validateMandatoryDateInRange(
        request.getParameter(nameDataScadenzaFideiussionee),
        nameDataScadenzaFideiussionee,
        new Date(), null, false, true);
    final String nameTipoIstituto = "tipoIstituto";
    String tipoIstituto = errors.validateMandatoryValueList(
        request.getParameter(nameTipoIstituto), nameTipoIstituto, new String[]
        { "B", "A" });
    SportelloBancaDTO sportelloBancaDTO = null;
    ComuneDTO comuneDTO = null;
    final String nameAltroIstituto = "altroIstituto";
    final String nameIndirizzoAltroIstituto = "indirizzoAltroIstituto";
    final String altroIstituto = request.getParameter(nameAltroIstituto);
    final String indirizzoAltroIstituto = request
        .getParameter(nameIndirizzoAltroIstituto);
    if ("B".equals(tipoIstituto))
    {
      final String nameExtIdSportello = "extIdSportello";
      Long extIdSportello = errors.validateMandatoryID(
          request.getParameter(nameExtIdSportello), nameExtIdSportello);
      if (extIdSportello != null)
      {
        sportelloBancaDTO = quadroEJB.getSportelloBancaById(extIdSportello);
        if (sportelloBancaDTO == null)
        {
          errors.addError("extIdSportello", "Sportello non trovato");
        }
      }
    }
    else
    {
      if ("A".equals(tipoIstituto))
      {
        errors.validateMandatoryFieldLength(altroIstituto, 0, 100,
            nameAltroIstituto);
        errors.validateMandatoryFieldLength(indirizzoAltroIstituto, 0, 200,
            nameIndirizzoAltroIstituto);
        final String nameExtIstatComune = "extIstatComune";
        final String istatComune = request.getParameter(nameExtIstatComune);
        if (errors.validateMandatory(istatComune, "comuneAltroIstituto"))
        {
          comuneDTO = quadroEJB.getComune(istatComune);
          if (comuneDTO == null)
          {
            errors.addError("comuneAltroIstituto", "Comune non trovato");
          }
        }
      }
    }

    if (errors.addToModelIfNotEmpty(model))
    {
      if ("B".equals(tipoIstituto))
      {
        model.addAttribute("checkedBanca", IuffiConstants.HTML.CHECKED);
        if (sportelloBancaDTO != null)
        {
          datiAnticipo.setAbi(sportelloBancaDTO.getAbi());
          datiAnticipo.setCab(sportelloBancaDTO.getCab());
          datiAnticipo.setCapSportello(sportelloBancaDTO.getCapSportello());
          datiAnticipo
              .setDenominazioneBanca(sportelloBancaDTO.getDenominazioneBanca());
          datiAnticipo.setDenominazioneSportello(
              sportelloBancaDTO.getDenominazioneSportello());
          datiAnticipo.setDescrizioneComuneSportello(
              sportelloBancaDTO.getDescrizioneComuneSportello());
          datiAnticipo.setExtIdSportello(sportelloBancaDTO.getIdSportello());
          datiAnticipo
              .setIndirizzoSportello(sportelloBancaDTO.getIndirizzoSportello());
          datiAnticipo.setSiglaProvinciaSportello(
              sportelloBancaDTO.getSiglaProvinciaSportello());
        }
      }
      else
      {
        if ("A".equals(tipoIstituto))
        {
          model.addAttribute("checkedAltroIstituto",
              IuffiConstants.HTML.CHECKED);
          datiAnticipo.setAltroIstituto(altroIstituto);
          datiAnticipo.setIndirizzoAltroIstituto(indirizzoAltroIstituto);
          if (comuneDTO != null)
          {
            datiAnticipo.setCapAltroIstituto(comuneDTO.getCap());
            datiAnticipo
                .setDescComuneAltroIstituto(comuneDTO.getDescrizioneComune());
            datiAnticipo.setExtIstatComune(comuneDTO.getIstatComune());
            datiAnticipo
                .setSiglaProvinciaAltroIstituto(comuneDTO.getSiglaProvincia());
          }
        }
      }
      model.addAttribute("preferRequest", Boolean.TRUE);
      return loadModel(model, datiAnticipo);
    }
    DatiAnticipoModificaDTO updateDTO = new DatiAnticipoModificaDTO();
    updateDTO.setBeneficiarioFideiussione(BENEFICIARIO);
    updateDTO.setDataScadenza(dataScadenzaFideiussione);
    updateDTO.setDataStipula(dataStipulaFideiussione);
    if (sportelloBancaDTO != null)
    {
      updateDTO.setExtIdSportello(sportelloBancaDTO.getIdSportello());
    }
    else
    {
      if (comuneDTO != null)
      {
        updateDTO.setAltroIstituto(altroIstituto);
        updateDTO.setExtIstatComune(comuneDTO.getIstatComune());
        updateDTO.setIndirizzoAltroIstituto(indirizzoAltroIstituto);
      }
    }
    updateDTO.setImportoAnticipo(importoAnticipo);
    // L'importo della fideiussione viene impostato == all'importo dell'anticipo
    updateDTO.setImportoFideiussione(importoAnticipo);
    updateDTO.setNumeroFideiussione(numeroFideiussione);
    updateDTO.setPercentualeAnticipo(percentualeAnticipo);
    updateDTO.setIdAnticipo(datiAnticipo.getIdAnticipo());
    quadroEJB.updateAnticipo(updateDTO,
        getLogOperationOggettoQuadroDTO(request.getSession()));
    return "redirect:../cuiuffi169v/index.do";
  }

  protected String loadModel(Model model, final DatiAnticipo datiAnticipo)
  {
    datiAnticipo.setBeneficiarioFideiussione(BENEFICIARIO);
    model.addAttribute("datiAnticipo", datiAnticipo);
    model.addAttribute("azioneBanca",
        datiAnticipo.getExtIdSportello() != null ? "cambia" : "seleziona");
    model.addAttribute("azioneAltro",
        datiAnticipo.getExtIstatComune() != null ? "cambia" : "seleziona");
    return "anticipo/modificaDati";
  }

  @IsPopup
  @RequestMapping(value = "/popup_ricerca_comune_altro_istituto", method = RequestMethod.GET)
  public String popupRicercaComuniAltroistituto(Model model,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    model.addAttribute("province", quadroEJB.getProvincie(null));
    return "anticipo/popupRicercaComuneAltroIstituto";
  }

  @IsPopup
  @RequestMapping(value = "/popup_ricerca_sportello", method = RequestMethod.GET)
  public String popupRicercaSportello(Model model, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    model.addAttribute("banche", quadroEJB.getDecodificheBanche());
    return "anticipo/popupRicercaSportello";
  }

  @IsPopup
  @RequestMapping(value = "/elenco_sportelli_{idBanca}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<DecodificaDTO<Integer>> elencoComuniAttiviPerProvincia(
      Model model, @PathVariable("idBanca") int idBanca,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<Integer>> list = quadroEJB
        .getDecodificheSportelli(idBanca);
    if (list == null)
    {
      list = new ArrayList<DecodificaDTO<Integer>>();
    }
    return list;
  }

  @IsPopup
  @RequestMapping(value = "/dati_sportello_{idSportello}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public SportelloBancaDTO datiSportello(Model model,
      @PathVariable("idSportello") int idSportello,
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return quadroEJB.getSportelloBancaById(idSportello);
  }

}