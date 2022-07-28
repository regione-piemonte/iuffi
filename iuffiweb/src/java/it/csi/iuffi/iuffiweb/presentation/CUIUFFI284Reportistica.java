package it.csi.iuffi.iuffiweb.presentation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.csi.iuffi.iuffiweb.business.IReportisticaEJB;
import it.csi.iuffi.iuffiweb.dto.ElencoQueryBandoDTO;
import it.csi.iuffi.iuffiweb.dto.reportistica.GraficoVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ParametriQueryReportVO;
import it.csi.iuffi.iuffiweb.exception.InternalServiceException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@IuffiSecurity(value = "CU-IUFFI-284", controllo = IuffiSecurity.Controllo.DEFAULT)
@RequestMapping(value = "/cuiuffi284")
public class CUIUFFI284Reportistica extends BaseController
{

  @Autowired
  private IReportisticaEJB reportEJB;

  @RequestMapping(value = "index", method = RequestMethod.GET)
  public String index(HttpSession session, Model model)
      throws InternalUnexpectedException
  {

    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    List<ElencoQueryBandoDTO> report = reportEJB
        .getElencoReport(utenteAbilitazioni.getAttori()[0].getCodice());
    model.addAttribute("report", report);

    return "reportistica/index/elencoReport";
  }

  @RequestMapping(value = "indexGrafici", method = RequestMethod.GET)
  public String indexGrafici(HttpSession session, Model model)
      throws InternalUnexpectedException
  {

    UtenteAbilitazioni utenteAbilitazioni = getUtenteAbilitazioni(session);
    List<ElencoQueryBandoDTO> grafici = reportEJB
        .getElencoGrafici(utenteAbilitazioni.getAttori()[0].getCodice());
    model.addAttribute("grafici", grafici);

    return "reportistica/index/elencoGrafici";
  }

  @RequestMapping(value = "visualizza_report_{idElencoQuery}", method = RequestMethod.GET)
  public String dettaglioReport(
      @PathVariable(value = "idElencoQuery") long idElencoQuery,
      HttpSession session, Model model) throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    ParametriQueryReportVO params = new ParametriQueryReportVO();
    params
        .setCodEnteCaa(IuffiUtils.PAPUASERV.extractInfoEnteBaseFromEnteLogin(
            utenteAbilitazioni.getEnteAppartenenza()).getCodiceEnte());
    params.setCodEntiAbilitati(IuffiUtils.PAPUASERV.getCodEntiAbilitati(utenteAbilitazioni));
    GraficoVO graficoVO = reportEJB.getGrafico(idElencoQuery, params);

    model.addAttribute("tabella", graficoVO);
    session.setAttribute("reportSelzionatoSession", graficoVO);

    return "reportistica/index/dettaglioReport";
  }

  @RequestMapping(value = "visualizza_grafico_{idElencoQuery}", method = RequestMethod.GET)
  public Object dettaglioGrafico(
      @PathVariable(value = "idElencoQuery") long idElencoQuery,
      HttpSession session, Model model) throws InternalUnexpectedException
  {

    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    ParametriQueryReportVO params = new ParametriQueryReportVO();
    params
        .setCodEnteCaa(IuffiUtils.PAPUASERV.extractInfoEnteBaseFromEnteLogin(
            utenteAbilitazioni.getEnteAppartenenza()).getCodiceEnte());
    params.setCodEntiAbilitati(IuffiUtils.PAPUASERV.getCodEntiAbilitati(utenteAbilitazioni));
    GraficoVO graficoVO = reportEJB.getGrafico(idElencoQuery, params);

    return graficoVO;
  }

  @RequestMapping(value = "stampa_{idElencoQuery}", method = RequestMethod.GET)
  public String stampa(
      @PathVariable(value = "idElencoQuery") long idElencoQuery, ModelMap model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    model.addAttribute("dettIdElencoQuery", idElencoQuery);
    return "reportistica/index/stampaGrafico";
  }

  @RequestMapping(value = "dettaglio_{idElencoQuery}", method = RequestMethod.GET)
  public String dettaglio(
      @PathVariable(value = "idElencoQuery") long idElencoQuery, ModelMap model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    model.addAttribute("dettIdElencoQuery", idElencoQuery);
    return "reportistica/index/dettaglioGrafico";
  }

  @RequestMapping(value = "getDettaglioGrafico_{idElencoQuery}", produces = "application/json")
  @ResponseBody
  public Object getData(
      @PathVariable(value = "idElencoQuery") long idElencoQuery,
      HttpSession session, HttpServletRequest request)
      throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    ParametriQueryReportVO params = new ParametriQueryReportVO();
    params
        .setCodEnteCaa(IuffiUtils.PAPUASERV.extractInfoEnteBaseFromEnteLogin(
            utenteAbilitazioni.getEnteAppartenenza()).getCodiceEnte());
    params.setCodEntiAbilitati(IuffiUtils.PAPUASERV.getCodEntiAbilitati(utenteAbilitazioni));
    GraficoVO graficoVO = reportEJB.getGrafico(idElencoQuery, params);

    return graficoVO;
  }

  @RequestMapping(value = "downloadExcelReport")
  public ModelAndView downloadExcel(HttpSession session, Model model)
      throws InternalServiceException, InternalUnexpectedException
  {
    GraficoVO graficoVO = (GraficoVO) session
        .getAttribute("reportSelzionatoSession");
    if (reportEJB.hasExcelTemplateInElencoQuery(graficoVO.getIdElencoQuery()))
    {
      return new ModelAndView("redirect:downloadExcelReportTemplate.xls");
    }
    else
    {
      return new ModelAndView("excelReportView", "graficoVO", graficoVO);
    }
  }

  @RequestMapping(value = "downloadExcelReportTemplate")
  public ResponseEntity<byte[]> downloadExcelTemplate(
      HttpServletRequest request, HttpSession session, Model model)
      throws Exception
  {
    GraficoVO graficoVO = (GraficoVO) session
        .getAttribute("reportSelzionatoSession");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type", IuffiUtils.FILE.getMimeType(".xlsx"));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"downloadExcelReportTemplate.xlsx\"");
    byte[] bytes = reportEJB
        .getExcelParametroDiElencoQuery(graficoVO.getIdElencoQuery());
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    Workbook workbook = WorkbookFactory.create(is);
    is.close();
    bytes = null;
    ExcelTemplateReport.buildExcelDocument(workbook, graficoVO);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    workbook.write(baos);
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(
        baos.toByteArray(),
        httpHeaders, HttpStatus.OK);
    return responseEntity;
  }
}
