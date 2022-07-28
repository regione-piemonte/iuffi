package it.csi.iuffi.iuffiweb.presentation.registroantimafia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.RegistroAntimafiaDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.presentation.BaseController;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;

@Controller
@IuffiSecurity(value = "CU-IUFFI-287", controllo = IuffiSecurity.Controllo.NESSUNO)
@RequestMapping("/cuiuffi287")
public class CUIUFFI287RegistroAntimaifa extends BaseController
{

  @Autowired
  IQuadroEJB quadroEJB = null;

  @RequestMapping(value = "/index")
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    clearCommonInSession(session);
    return "registroantimafia/registroantimafia";
  }

  @RequestMapping(value = "cerca")
  @ResponseBody
  public String cerca(Model model, HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    String cuaa = request.getParameter("cuaa");
    String denominazione = request.getParameter("denominazione");

    Map<String, Object> common = getCommonFromSession("REGISTRO_ANTIMAFIA",
        session, false);
    common.put("cuaa", cuaa);
    common.put("denominazione", denominazione);
    saveCommonInSession(common, session);
    return "SUCCESS";
  }

  @RequestMapping(value = "getDatiCertificato", produces = "application/json")
  @ResponseBody
  public List<RegistroAntimafiaDTO> getDatiCertificato(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession("REGISTRO_ANTIMAFIA",
        session, false);
    String cuaa = (String) common.get("cuaa");
    String denominazione = (String) common.get("denominazione");

    if (cuaa == null && denominazione == null)
      return new ArrayList<>();
    List<RegistroAntimafiaDTO> dati = quadroEJB
        .getDatiCertificatoRegistroAntimafia(cuaa, denominazione);

    return dati != null ? dati : new ArrayList<>();
  }

  @RequestMapping(value = "getDatiAziendaCertificato", produces = "application/json")
  @ResponseBody
  public List<RegistroAntimafiaDTO> getDatiAziendaCertificato(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession("REGISTRO_ANTIMAFIA",
        session, false);
    String cuaa = (String) common.get("cuaa");
    String denominazione = (String) common.get("denominazione");

    if (cuaa == null && denominazione == null)
      return new ArrayList<>();
    List<RegistroAntimafiaDTO> dati = quadroEJB
        .getDatiAziendaCertificatoRegistroAntimafia(cuaa, denominazione);

    return dati != null ? dati : new ArrayList<>();
  }

  @RequestMapping(value = "getDatiTitolare", produces = "application/json")
  @ResponseBody
  public List<RegistroAntimafiaDTO> getDatiTitolare(Model model,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    Map<String, Object> common = getCommonFromSession("REGISTRO_ANTIMAFIA",
        session, false);
    String cuaa = (String) common.get("cuaa");
    String denominazione = (String) common.get("denominazione");

    if (cuaa == null && denominazione == null)
      return new ArrayList<>();
    List<RegistroAntimafiaDTO> dati = quadroEJB
        .getDatiTitolareRegistroAntimafia(cuaa, denominazione);

    return dati != null ? dati : new ArrayList<>();

  }
}