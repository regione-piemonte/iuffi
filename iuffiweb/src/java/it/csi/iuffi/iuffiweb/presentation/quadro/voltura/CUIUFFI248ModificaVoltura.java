package it.csi.iuffi.iuffiweb.presentation.quadro.voltura;

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
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.AziendaDTO;
import it.csi.iuffi.iuffiweb.dto.VolturaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.exception.InternalServiceException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IsPopup;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.validator.Errors;
import it.csi.smranags.gaaserv.exception.GaaservInternalException;
import it.csi.smranags.gaaserv.exception.MaxRangeException;
import it.csi.smranags.gaaserv.exception.QueryTimeOutException;

@Controller
@IuffiSecurity(value = "CU-IUFFI-248-M", controllo = IuffiSecurity.Controllo.PROCEDIMENTO_OGGETTO)
@RequestMapping(value = "/cuiuffi248m")
public class CUIUFFI248ModificaVoltura extends BaseController
{
  @Autowired
  protected IQuadroEJB quadroEJB;

  @RequestMapping(value = "/index", method =
  { RequestMethod.GET })
  public String modifica(Model model, HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException,
      QueryTimeOutException,
      MaxRangeException, GaaservInternalException, InternalServiceException,
      UnrecoverableException
  {
    final ProcedimentoOggetto procedimentoOggetto = getProcedimentoOggettoFromSession(
        request.getSession());
    VolturaDTO voltura = quadroEJB
        .getVoltura(procedimentoOggetto.getIdProcedimentoOggetto());
    model.addAttribute("voltura", voltura);

    return "voltura/modifica";
  }

  @RequestMapping(value = "/confermaModifica", method =
  { RequestMethod.POST })
  public String confermaModifica(Model model, HttpServletRequest request,
      HttpSession session)
      throws InternalUnexpectedException, ApplicationException,
      QueryTimeOutException, MaxRangeException, GaaservInternalException,
      InternalServiceException, UnrecoverableException
  {
    Errors errors = new Errors();
    ProcedimentoOggetto po = getProcedimentoOggettoFromRequest(request);
    model.addAttribute("preferRequest", Boolean.TRUE);

    VolturaDTO voltura = new VolturaDTO();

    String idAziendaStr = request.getParameter("idAzienda");
    if (idAziendaStr != null && idAziendaStr.compareTo("") != 0)
      voltura.setIdAzienda(Long.parseLong(idAziendaStr));

    String denominazioneAzienda = request.getParameter("denominazioneHidden");
    errors.validateMandatory(denominazioneAzienda, "azienda");
    voltura.setDenominazioneAzienda(denominazioneAzienda);
    voltura.setRappresentanteLegale(
        request.getParameter("rappresentanteLegaleHidden"));
    voltura.setSedeLegale(request.getParameter("sedeLegaleHidden"));
    String note = request.getParameter("note");
    voltura.setNote(note);
    errors.validateFieldLength(note, "note", 0, 4000);

    if (!errors.isEmpty())
    {
      voltura = quadroEJB.getVoltura(po.getIdProcedimentoOggetto());
      model.addAttribute("voltura", voltura);
      model.addAttribute("errors", errors);
      return "voltura/modifica";
    }

    quadroEJB.updateVoltura(voltura, po.getIdProcedimentoOggetto(),
        getLogOperationOggettoQuadroDTO(request.getSession()));

    return "redirect:../cuiuffi248l/index.do";
  }

  private List<AziendaDTO> caricaAziende(HttpServletRequest request,
      Model model) throws QueryTimeOutException, MaxRangeException,
      GaaservInternalException, InternalServiceException,
      InternalUnexpectedException, ApplicationException, UnrecoverableException
  {

    List<AziendaDTO> aziende = new ArrayList<>();
    String cuaa = request.getParameter("capofila");

    // VALIDATE CUAA
    if (cuaa != null && cuaa.compareTo("") != 0)
    {
      return getInfoAziendaByCuaa(cuaa, model, request);
    }
    else
    {
      String denominazioneAzienda = request
          .getParameter("denominazioneAzienda");
      // VALIDATE DENOMINAZIONE_AZIENDA SOLO SE IL CUAA NON E' STATO
      // INSERITO
      if (denominazioneAzienda != null
          && denominazioneAzienda.trim().length() >= 4)
      {
        return quadroEJB.getInfoAziendaByDenominazione(denominazioneAzienda);
      }
    }
    return aziende;
  }

  // Info aziende by CUAA (possono essere più di una)
  public List<AziendaDTO> getInfoAziendaByCuaa(
      @PathVariable("cuaa") String cuaa, Model model,
      HttpServletRequest request)
      throws InternalUnexpectedException, ApplicationException,
      QueryTimeOutException, MaxRangeException, GaaservInternalException,
      InternalServiceException, UnrecoverableException
  {
    List<AziendaDTO> ret = quadroEJB.getAziendeByCuaa(cuaa.toUpperCase());
    return ret;
  }

  @RequestMapping(value = "/selezioneAziendePopup", method =
  { RequestMethod.GET })
  @IsPopup
  public String selezioneAziendePopup(Model model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {

    return "voltura/selezioneAziendePopup";
  }

  @RequestMapping(value = "/selezioneAzienda_{idAzienda}", method =
  { RequestMethod.GET })
  @ResponseBody
  public String selezioneAzienda(@PathVariable("idAzienda") Long idAzienda,
      Model model, HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {

    if (idAzienda != null)
    {
      AziendaDTO azienda = quadroEJB.getAziendaById(idAzienda);
      return azienda.getIdAzienda() + "&&&" + azienda.getDenominazione() + "&&&"
          + azienda.getSedeLegale() + "&&&"
          + azienda.getRappresentanteLegale();
    }

    return "error";
  }

  @RequestMapping(value = "/elenco", produces = "application/json")
  @IsPopup
  @ResponseBody
  public List<AziendaDTO> elenco(Model model, HttpServletRequest request,
      HttpSession session) throws Exception
  {
    List<AziendaDTO> aziende = new ArrayList<>();
    aziende = caricaAziende(request, model);
    return aziende == null ? new ArrayList<AziendaDTO>() : aziende;
  }
}