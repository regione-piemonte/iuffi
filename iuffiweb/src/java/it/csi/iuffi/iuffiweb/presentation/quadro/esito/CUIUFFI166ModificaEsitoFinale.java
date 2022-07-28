package it.csi.iuffi.iuffiweb.presentation.quadro.esito;

import java.util.ArrayList;
import java.util.Date;
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

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi166m")
@IuffiSecurity(value = "CU-IUFFI-166-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUIUFFI166ModificaEsitoFinale extends BaseController
{
  @Autowired
  IQuadroEJB quadroEJB = null;

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String get(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);
    QuadroOggettoDTO quadro = po.findQuadroByCU("CU-IUFFI-166-M");

    List<DecodificaDTO<Long>> elencoTecnici = quadroEJB
        .getElencoTecniciDisponibiliPerAmmCompetenza(
            po.getIdProcedimentoOggetto(),
            getUtenteAbilitazioni(session).getIdProcedimento());
    String tipoEsitoRichiesto = IuffiConstants.ESITO.TIPO.FINALE;
    if (IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_DOMANDA_ANNULLAMENTO
        .equals(po.getCodOggetto()) ||
        IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_RINUNCIA
            .equals(po.getCodOggetto())
        ||
        IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_VOLTURA
            .equals(po.getCodOggetto())
        ||
        IuffiConstants.OGGETTO.CODICE.REVOCA.equals(po.getCodOggetto()))
    {
      tipoEsitoRichiesto = IuffiConstants.ESITO.TIPO.RINUNCIATA;
    }
    List<DecodificaDTO<Long>> elencoEsiti = quadroEJB
        .getElencoEsiti(tipoEsitoRichiesto);

    model.addAttribute("isDanniFauna", Boolean.FALSE);

    List<DecodificaDTO<Long>> elencoAtti = quadroEJB.getElencoAtti();

    EsitoFinaleDTO esito = quadroEJB.getEsitoFinale(
        po.getIdProcedimentoOggetto(), quadro.getIdQuadroOggetto());
    List<DecodificaDTO<String>> ufficiZona = getListUfficiZonaFunzionari();
    model.addAttribute("esito", esito);
    model.addAttribute("elencoEsiti", elencoEsiti);
    model.addAttribute("elencoTecnici", elencoTecnici);
    model.addAttribute("elencoTecniciSup", elencoTecnici);
    model.addAttribute("elencoAtti", elencoAtti);
    model.addAttribute("flagAmmissione", po.getFlagAmmissione());
    model.addAttribute("ufficiZona", ufficiZona);

    return "esitofinale/modificaDati";
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public String post(Model model, HttpSession session,
      @RequestParam(value = "idEsito") Long idEsito,
      @RequestParam(value = "prescrizioni") String prescrizioni,
      @RequestParam(value = "note") String note,
      @RequestParam(value = "motivazione") String motivazione,
      @RequestParam(value = "idTecnico") Long idTecnico,
      @RequestParam(value = "idTipoAtto", required = false) Long idTipoAtto,
      @RequestParam(value = "numeroAtto", required = false) String numeroAtto,
      @RequestParam(value = "dataAtto", required = false) String dataAtto,
      @RequestParam(value = "idGradoSup", required = false) Long idGradoSup)
      throws Exception
  {

    ProcedimentoOggetto po = getProcedimentoOggettoFromSession(session);

    boolean isDAnniFauna = false;

    model.addAttribute("preferRequestValueEsito", Boolean.TRUE);
    List<DecodificaDTO<Long>> elencoAtti = quadroEJB.getElencoAtti();

    motivazione = IuffiUtils.STRING.escapeSpecialsChar(motivazione);
    prescrizioni = IuffiUtils.STRING.escapeSpecialsChar(prescrizioni);
    note = IuffiUtils.STRING.escapeSpecialsChar(note);
    numeroAtto = IuffiUtils.STRING.escapeSpecialsChar(numeroAtto);

    EsitoFinaleDTO esito = new EsitoFinaleDTO();
    Errors errors = new Errors();

    boolean isEsitoNegativo = false;
    List<DecodificaDTO<Long>> elencoEsiti = quadroEJB
        .getElencoEsiti(IuffiConstants.ESITO.TIPO.FINALE);
    if (idEsito != null)
    {
      for (DecodificaDTO<Long> item : elencoEsiti)
      {
        if (item.getId().longValue() == idEsito.longValue())
        {
          isEsitoNegativo = "N".equals(item.getCodice());
          break;
        }
      }
    }
    if (!isEsitoNegativo)
    {
      // errors.validateFieldLength(prescrizioni, "prescrizioni", 0, 4000);
      esito.setPrescrizioni(prescrizioni);
    }

    Date dataAttoD = new Date();

    if (!isDAnniFauna)
    {
      errors.validateMandatory(idGradoSup, "idGradoSup");
    }

    errors.validateMandatory(idTecnico, "idTecnico");

    if (elencoAtti != null && !elencoAtti.isEmpty()
        && "S".equals(po.getFlagAmmissione()))
      errors.validateMandatory(idTipoAtto, "idTipoAtto");

    if (idTipoAtto != null)
      if (checkIfAltro(idTipoAtto, model, session).compareTo("S") == 0
          && "S".equals(po.getFlagAmmissione()))
      {
        errors.validateMandatory(numeroAtto, "numeroAtto");
        errors.validateFieldLength(numeroAtto, "numeroAtto", 0, 200);
        dataAttoD = errors.validateMandatoryDate(dataAtto, "dataAtto", true);
      }

    if (errors.validateMandatory(idEsito, "idEsito"))
    {
      esito.setIdEsito(idEsito);
    }

    if (errors.validateMandatory(idTecnico, "idTecnico"))
    {
      esito.setIdTecnico(idTecnico);
    }

    if (!isDAnniFauna)
    {
      if (errors.validateMandatory(idGradoSup, "idGradoSup"))
      {
        esito.setIdGradoSup(idGradoSup);
      }
    }

    errors.validateFieldLength(note, "note", 0, 4000);
    esito.setNote(note);

    // errors.validateFieldLength(motivazione, "motivazione",0, 4000);
    esito.setMotivazione(motivazione);

    esito.setIdTipoAtto(idTipoAtto);

    if (idTipoAtto != null)
      if (checkIfAltro(idTipoAtto, model, session).compareTo("S") != 0)
      {
        esito.setDataAtto(null);
        esito.setNumeroAtto(null);
      }
      else
      {
        esito.setDataAtto(dataAttoD);
        esito.setNumeroAtto(numeroAtto);
      }

    if (!errors.isEmpty())
    {
      model.addAttribute("errors", errors);
      return get(model, session);
    }

    QuadroOggettoDTO quadro = po.findQuadroByCU("CU-IUFFI-166-M");
    quadroEJB.updateEsitoFinale(po.getIdProcedimentoOggetto(),
        quadro.getIdQuadroOggetto(), po.getIdBandoOggetto(), esito,
        getIdUtenteLogin(session));

    return "redirect:../cuiuffi166v/index.do";
  }

  @RequestMapping(value = "confermaelimina", method = RequestMethod.GET)
  @IsPopup
  public String popupIndex(Model model, HttpSession session)
      throws InternalUnexpectedException
  {

    setModelDialogWarning(model,
        "Avendo scelto esito Negativo verranno cancellate le eventuali prescrizioni inserite, vuoi continuare?",
        "confermaelimina.do");
    return "esitofinale/confermaElimina";
  }

  @IsPopup
  @RequestMapping(value = "confermaelimina", method = RequestMethod.POST)
  public String popupIndexPost(Model model, HttpSession session,
      HttpServletRequest request,
      @RequestParam(value = "idEsito") Long idEsito,
      @RequestParam(value = "prescrizioni") String prescrizioni,
      @RequestParam(value = "note") String note,
      @RequestParam(value = "motivazione") String motivazione,
      @RequestParam(value = "idTecnico") Long idTecnico,
      @RequestParam(value = "idTipoAtto", required = false) Long idTipoAtto,
      @RequestParam(value = "numeroAtto", required = false) String numeroAtto,
      @RequestParam(value = "dataAtto", required = false) String dataAtto,
      @RequestParam(value = "idGradoSup") Long idGradoSup) throws Exception
  {
    return post(model, session, idEsito, prescrizioni, note, motivazione,
        idTecnico, idTipoAtto, numeroAtto, dataAtto, idGradoSup);
  }

  @RequestMapping(value = "/checkIfFlagAltreInfo_{idTipoAtto}", produces = "application/json")
  @ResponseBody
  public String checkIfAltro(@PathVariable("idTipoAtto") Long idTipoAtto,
      Model model, HttpSession session) throws Exception
  {
    String s = quadroEJB.checkFlagAltreInfoAtto(idTipoAtto);
    return s != null ? s : "";
  }
}