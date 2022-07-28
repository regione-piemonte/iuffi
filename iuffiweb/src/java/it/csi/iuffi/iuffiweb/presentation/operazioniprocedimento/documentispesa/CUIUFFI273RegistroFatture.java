package it.csi.iuffi.iuffiweb.presentation.operazioniprocedimento.documentispesa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRicercaEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.FornitoreDTO;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.LivelloDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;

@Controller
@RequestMapping("/cuiuffi273")
@IuffiSecurity(value = "CU-IUFFI-273", controllo = IuffiSecurity.Controllo.DEFAULT)
public class CUIUFFI273RegistroFatture extends BaseController
{
  @Autowired
  IQuadroEJB  quadroEJB  = null;

  @Autowired
  IRicercaEJB ricercaEJB = null;

  @RequestMapping(value = "/back", method = RequestMethod.GET)
  public String back(Model model, HttpSession session)
      throws InternalUnexpectedException
  {

    return "registrofatture/elencoProcedimenti";

  }

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String get(Model model, HttpSession session)
      throws InternalUnexpectedException
  {

    model.addAttribute("elencoTipiDocumenti",
        quadroEJB.geElencoTipiDocumenti());
    model.addAttribute("elencoModalitaPagamento",
        quadroEJB.geElencoModalitaPagamento());

    Map<String, Object> common = getCommonFromSession("RegistroFatture",
        session, false);
    if (common != null && !common.isEmpty())
    {
      if (common.containsKey("idsTipoDocumentoStr"))
        model.addAttribute("idsTipoDocumentoStr",
            (String) common.get("idsTipoDocumentoStr"));
      if (common.containsKey("idsModalitaPagamentoStr"))
        model.addAttribute("idsModalitaPagamentoStr",
            (String) common.get("idsModalitaPagamentoStr"));
      if (common.containsKey("dateFrom"))
        model.addAttribute("dataDa", (Date) common.get("dateFrom"));
      if (common.containsKey("dateTo"))
        model.addAttribute("dataA", (Date) common.get("dateTo"));
      Long idFornitore = null;
      if (common.containsKey("idFornitore"))
        idFornitore = (Long) common.get("idFornitore");
      model.addAttribute("idFornitore", idFornitore);
      if (idFornitore != null)
      {
        if (common.containsKey("codiceFornitore"))
          model.addAttribute("codiceFornitore",
              (String) common.get("codiceFornitore"));
      }

    }
    return "registrofatture/ricerca";
  }

  @RequestMapping(value = "/ricercaFornitorePopup", method = RequestMethod.GET)
  @IsPopup
  public String ricercaFornitorePopup(Model model, HttpSession session)
      throws InternalUnexpectedException
  {

    return "registrofatture/popupRicercaFornitore";
  }

  @RequestMapping(value = "/ricerca_fornitori", produces = "application/json", method = RequestMethod.POST)
  @ResponseBody
  public List<DecodificaDTO<Long>> popupRicercaFornitori(Model model,
      HttpSession session, HttpServletRequest request,
      @RequestParam(value = "cuaa") String CUAA,
      @RequestParam(value = "den") String denominazione)
      throws InternalUnexpectedException
  {
    List<DecodificaDTO<Long>> list = null;
    list = new ArrayList<DecodificaDTO<Long>>();
    list = (ArrayList<DecodificaDTO<Long>>) quadroEJB
        .geElencoFornitoriRicercaFatture(CUAA, denominazione);

    model.addAttribute("cuaa", CUAA);
    return list;
  }

  @RequestMapping(value = "/confermaDatiRicerca", method = RequestMethod.POST)
  public String confermaDatiRicerca(Model model, HttpSession session,
      HttpServletRequest request,
      @RequestParam(value = "dataInizio", required = false) String dataInizio,
      @RequestParam(value = "dataFine", required = false) String dataFine)
      throws InternalUnexpectedException
  {

    Errors errors = new Errors();
    String idsTipiPagamento = request.getParameter("idsTipi");
    String idsModalitaPagamento = request.getParameter("idsModPag");

    errors.validateMandatory(idsTipiPagamento, "idTipoDocumento");
    errors.validateMandatory(idsModalitaPagamento, "idModalitaPagamento");

    Long idFornitore = null;
    if (request.getParameter("idFornitore") != null
        && !request.getParameter("idFornitore").equals(""))
      idFornitore = errors.validateLong(request.getParameter("idFornitore"),
          "idFornitore");
    Date dateFrom = errors.validateOptionalDate(dataInizio, "dataInizio", true);
    Date dateTo = errors.validateOptionalDate(dataFine, "dataFine", true);
    if (dateFrom != null && dateTo != null)
      if (dateFrom.compareTo(dateTo) > 0)
      {
        errors.addError("dataInizio",
            "La data di inizio deve essere minore della data fine");
        errors.addError("dataFine",
            "La data di fine deve essere maggiore della data di inizio");
      }

    if (errors.isEmpty())
    {

      String[] arrayTipiStr = idsTipiPagamento.split("&&&");
      long[] idsTipi = new long[arrayTipiStr.length];
      for (int i = 0; i < arrayTipiStr.length; i++)
        idsTipi[i] = Long.valueOf(arrayTipiStr[i]);

      String[] arrayModStr = idsModalitaPagamento.split("&&&");
      long[] idsMod = new long[arrayModStr.length];
      for (int i = 0; i < arrayModStr.length; i++)
        idsMod[i] = Long.valueOf(arrayModStr[i]);

      List<ProcedimentoDTO> procs = null;
      if (errors.isEmpty())
      {
        procs = ricercaEJB.getElencoProcedimentiRegistroFatture(idsTipi, idsMod,
            idFornitore, dateFrom, dateTo);
        if (procs == null || procs.isEmpty())
          model.addAttribute("msgWarning", "Nessun risultato trovato.");
      }

      if (procs != null && !procs.isEmpty())
      {

        Map<String, Object> common = getCommonFromSession("RegistroFatture",
            session, false);
        common.put("idsTipoPagamento", idsTipi);
        common.put("idsModalitaPagamento", idsMod);
        common.put("idsTipoDocumentoStr", idsTipiPagamento);
        common.put("idsModalitaPagamentoStr", idsModalitaPagamento);
        common.put("idFornitore", idFornitore);
        if (idFornitore != null)
        {
          FornitoreDTO fornitore = quadroEJB.getDettaglioFornitore(idFornitore);
          if (fornitore != null)
            common.put("codiceFornitore", fornitore.getCodiceFornitore() + " - "
                + fornitore.getRagioneSociale());
        }

        common.put("dateFrom", dateFrom);
        common.put("dateTo", dateTo);
        saveCommonInSession(common, session);

        return "registrofatture/elencoProcedimenti";
      }
    }

    model.addAttribute("errors", errors);
    model.addAttribute("idsTipoDocumentoStr", idsTipiPagamento);
    model.addAttribute("idsModalitaPagamentoStr", idsModalitaPagamento);

    model.addAttribute("prfReqValues", true);
    if (idFornitore != null)
    {
      FornitoreDTO fornitore = quadroEJB.getDettaglioFornitore(idFornitore);
      if (fornitore != null)
      {
        String s = fornitore.getCodiceFornitore() + " - "
            + fornitore.getRagioneSociale();
        if (fornitore.getIndirizzoSedeLegale() != null
            && fornitore.getIndirizzoSedeLegale().compareTo("null") != 0
            && fornitore.getIndirizzoSedeLegale().compareTo("") != 0)
          s += " - " + fornitore.getIndirizzoSedeLegale();
        model.addAttribute("codiceFornitore", s);
      }
      model.addAttribute("idFornitore", idFornitore);
    }

    model.addAttribute("elencoTipiDocumenti",
        quadroEJB.geElencoTipiDocumenti());
    model.addAttribute("elencoModalitaPagamento",
        quadroEJB.geElencoModalitaPagamento());
    return "registrofatture/ricerca";
  }

  @RequestMapping(value = "getElencoProcedimenti", produces = "application/json")
  @ResponseBody
  public List<ProcedimentoDTO> getElencoProcedimenti(HttpSession session)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession("RegistroFatture",
        session, false);
    return ricercaEJB.getElencoProcedimentiRegistroFatture(
        (long[]) common.get("idsTipoPagamento"),
        (long[]) common.get("idsModalitaPagamento"),
        (Long) common.get("idFornitore"), (Date) common.get("dateFrom"),
        (Date) common.get("dateTo"));
  }

  @RequestMapping(value = "getElencoCodiciOperazioneJson", produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getElencoCodiciOperazioneJson(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    List<LivelloDTO> livelli = ricercaEJB.getElencoLivelli();
    List<LivelloDTO> liv = new LinkedList<LivelloDTO>();
    boolean aggiungi = true;

    for (LivelloDTO item : livelli)
    {
      for (LivelloDTO d : liv)
      {
        if (d.getCodiceLivello().compareTo(item.getCodiceLivello()) == 0)
        {
          aggiungi = false;
        }
      }

      if (aggiungi)
        liv.add(item);
      aggiungi = true;
    }

    Map<String, Object> stato;
    // al bootstrap-table-filter devo passare una map di questo tipo
    List<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
    ;
    for (LivelloDTO item : liv)
    {
      stato = new HashMap<String, Object>();
      stato.put("id", item.getCodice());
      stato.put("label", item.getCodice());
      ret.add(stato);
    }

    return ret;
  }

}