package it.csi.iuffi.iuffiweb.presentation.estrazionicampione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.csi.iuffi.iuffiweb.business.IEstrazioniEJB;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.DettaglioEstrazioneDTO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.ImportiAttualiPrecedentiDTO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.ImportiTotaliDTO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.NumeroLottoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.estrazionecampione.RigaSimulazioneEstrazioneDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-219", controllo = IuffiSecurity.Controllo.DEFAULT)
@RequestMapping("cuiuffi219")
public class CUIUFFI219DettaglioEstrazioneCampione extends BaseController
{

  @Autowired
  private IEstrazioniEJB estrazioniEjb;

  @RequestMapping(value = "/index_{idNumeroLotto}", method = RequestMethod.GET)
  public final String index(Model model, HttpSession session,
      @PathVariable(value = "idNumeroLotto") long idNumeroLotto)
      throws InternalUnexpectedException
  {

    ImportiTotaliDTO totali = estrazioniEjb.getImportiTotali(idNumeroLotto);
    model.addAttribute("totali", totali);

    ImportiAttualiPrecedentiDTO importiPA = estrazioniEjb
        .getImportiAttualiPrecedenti(idNumeroLotto);
    importiPA.calcPercentualiTotali(totali);
    model.addAttribute("importiPA", importiPA);
    model.addAttribute("idNumeroLotto", idNumeroLotto);

    NumeroLottoDTO numeroLotto = estrazioniEjb.getNumeroLottoDto(idNumeroLotto);
    if (numeroLotto != null)
    {
      long idStatoEstrazione = numeroLotto.getIdStatoEstrazione();
      long idTipoEstrazione = numeroLotto.getIdTipoEstrazione();
      if (IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.STATO_ESTRAZIONE.CARICATA == idStatoEstrazione
          || IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.STATO_ESTRAZIONE.ESTRATTA == idStatoEstrazione)
      {
        model.addAttribute("btnRegistraVisibile", Boolean.TRUE);
      }

      if (idStatoEstrazione == IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.STATO_ESTRAZIONE.REGISTRATA
          &&
          estrazioniEjb.isEstrazioneAnnullabile(idNumeroLotto,
              idTipoEstrazione))
      {
        model.addAttribute("btnAnnullaVisibile", Boolean.TRUE);
      }

      model.addAttribute("idStatoEstrazione", idStatoEstrazione);
      model.addAttribute("idTipoEstrazione", idTipoEstrazione);
      model.addAttribute("tipoEstrazione",
          numeroLotto.getDescrTipoEstrazione());
      model.addAttribute("numeroEstrazione", numeroLotto.getNumeroEstrazione());

      if (IuffiConstants.FLAGS.ESTRAZIONE_CAMPIONE.STATO_ESTRAZIONE.ANNULLATA == idStatoEstrazione)
      {
        model.addAttribute("statoEstrazione",
            numeroLotto.getDescrStatoEstrazione() + " "
                + numeroLotto.getMotivazione());
      }
      else
      {
        model.addAttribute("statoEstrazione",
            numeroLotto.getDescrStatoEstrazione());
      }
    }
    return "estrazioniacampione/dettaglio";
  }

  @RequestMapping(value = "getElencoRisultatiEstrazione_{idNumeroLotto}", produces = "application/json")
  @ResponseBody
  public List<RigaSimulazioneEstrazioneDTO> getElencoRisultati(
      HttpSession session,
      @PathVariable(value = "idNumeroLotto") long idNumeroLotto)
      throws InternalUnexpectedException
  {
    List<RigaSimulazioneEstrazioneDTO> elenco = estrazioniEjb
        .getElencoRisultati(idNumeroLotto);
    if (elenco == null)
    {
      elenco = new ArrayList<>();
    }
    return elenco;
  }

  @RequestMapping(value = "dettaglioEstrazioneExcel_{idNumeroLotto}")
  public ModelAndView downloadExcel(Model model, HttpSession session,
      @PathVariable(value = "idNumeroLotto") long idNumeroLotto)
      throws InternalUnexpectedException
  {
    DettaglioEstrazioneDTO estrazioneDTO = new DettaglioEstrazioneDTO();

    ImportiTotaliDTO totali = estrazioniEjb.getImportiTotali(idNumeroLotto);
    ImportiAttualiPrecedentiDTO importiPA = estrazioniEjb
        .getImportiAttualiPrecedenti(idNumeroLotto);
    importiPA.calcPercentualiTotali(totali);
    List<RigaSimulazioneEstrazioneDTO> elenco = estrazioniEjb
        .getElencoRisultati(idNumeroLotto);

    estrazioneDTO.setElenco(elenco);
    estrazioneDTO.setImportiPA(importiPA);
    estrazioneDTO.setTotali(totali);

    return new ModelAndView("excelDettaglioEstrazioneView", "estrazioneDTO",
        estrazioneDTO);
  }

  @RequestMapping(value = "getElencoFlagEstrazione")
  @ResponseBody
  public String getCustomFilterImpegni(HttpSession session)
      throws InternalUnexpectedException
  {
    StringBuffer html = new StringBuffer("<div style=\"min-width:250px\">");

    Map<String, Object> oggetto;
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    oggetto = new HashMap<String, Object>();
    oggetto.put("id", -1);
    oggetto.put("label", "Tutti");
    ret.add(oggetto);

    oggetto = new HashMap<String, Object>();
    oggetto.put("id", 1);
    oggetto.put("label", "Estratti a campione");
    ret.add(oggetto);

    html.append("<div class=\"container-fluid\" style=\"min-width:25em;\">");
    html.append(" <div class=\"radio\">");
    html.append(
        "<label><input class=\"filter-enabled\" type=\"radio\" data-val=\"0\" name=\"optradio\">Estratti a campione (tutti)</label>");
    html.append("</div>");
    html.append(" <div class=\"radio\">");
    html.append(
        "<label><input class=\"filter-enabled\" type=\"radio\" data-val=\"1\" name=\"optradio\">Estratti a campione (controllo in loco)</label>");
    html.append("</div>");
    html.append("<div class=\"radio\">");
    html.append(
        "<label><input  class=\"filter-enabled\" type=\"radio\" data-val=\"-1\" name=\"optradio\" checked>Tutti</label>");
    html.append("</div></div>");

    return html.toString();
  }

}
