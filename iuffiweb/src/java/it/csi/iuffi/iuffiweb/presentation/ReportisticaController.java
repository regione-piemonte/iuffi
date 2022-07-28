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
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.reportistica.GraficoVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ParametriQueryReportVO;
import it.csi.iuffi.iuffiweb.exception.InternalServiceException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/reportistica")
@IuffiSecurity(value = "000", controllo = IuffiSecurity.Controllo.NESSUNO)
public class ReportisticaController extends BaseController
{

  private static final String MIME_TYPE_XLSX  = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  @Autowired
  private IReportisticaEJB    reportisticaEJB = null;

  @RequestMapping(value = "mainreport_{idBando}", method = RequestMethod.GET)
  public String reportGet(ModelMap model, HttpServletRequest request,
      HttpSession session, @PathVariable("idBando") long idBando)
      throws InternalUnexpectedException
  {
    addInfoBando(session, idBando);
    model.addAttribute("elencoQueryBando",
        session.getAttribute("elencoQueryBando_" + idBando));
    return "reportistica/reportistica";
  }

  private void addInfoBando(HttpSession session, long idBando)
      throws InternalUnexpectedException
  {
    BandoDTO bando = reportisticaEJB.getInformazioniBando(idBando);
    session.setAttribute("denominazioneBando", bando.getDenominazione());
    session.setAttribute("idBandoStatistiche", idBando);
    session.setAttribute("idBandoGrafici", idBando);

    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");

    List<DecodificaDTO<String>> elencoQueryBando = reportisticaEJB
        .elencoQueryBando(idBando, Boolean.FALSE,
            IuffiUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni));
    session.setAttribute("elencoQueryBando_" + idBando, elencoQueryBando);

  }

  @RequestMapping(value = "elencoreport_{idBando}", method = RequestMethod.GET)
  public String elencoReportTabellariGet(ModelMap model,
      HttpServletRequest request, HttpSession session,
      @PathVariable("idBando") long idBando) throws InternalUnexpectedException
  {
    addInfoBando(session, idBando);
    return "reportistica/elencoReport";
  }

  @RequestMapping(value = "visualizza_report_{idElencoQuery}", method = RequestMethod.GET)
  public String dettaglioGet(ModelMap model, HttpServletRequest request,
      HttpSession session, @PathVariable("idElencoQuery") long idElencoQuery)
      throws InternalUnexpectedException
  {

    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");
    ParametriQueryReportVO params = new ParametriQueryReportVO();
    params.setIdBando((Long) session.getAttribute("idBandoStatistiche"));
    params
        .setCodEnteCaa(IuffiUtils.PAPUASERV.extractInfoEnteBaseFromEnteLogin(
            utenteAbilitazioni.getEnteAppartenenza()).getCodiceEnte());
    params.setCodEntiAbilitati(IuffiUtils.PAPUASERV.getCodEntiAbilitati(utenteAbilitazioni));
    GraficoVO graficoVO = reportisticaEJB.getGrafico(idElencoQuery, params);
    model.addAttribute("tabella", graficoVO);
    session.setAttribute("reportSelzionatoSession", graficoVO);
    return "reportistica/dettaglioReport";
  }

  @RequestMapping(value = "getElencoEstrazioni", produces = "application/json")
  @ResponseBody
  public Object getElencoEstrazioni(HttpSession session,
      HttpServletRequest request) throws InternalUnexpectedException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");

    List<DecodificaDTO<String>> elenco = reportisticaEJB.elencoQueryBando(
        (Long) session.getAttribute("idBandoGrafici"), Boolean.TRUE,
        IuffiUtils.PAPUASERV.getFirstCodiceAttore(utenteAbilitazioni));
    return elenco;
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
    params.setIdBando((Long) session.getAttribute("idBandoStatistiche"));
    params
        .setCodEnteCaa(IuffiUtils.PAPUASERV.extractInfoEnteBaseFromEnteLogin(
            utenteAbilitazioni.getEnteAppartenenza()).getCodiceEnte());
    params.setCodEntiAbilitati(IuffiUtils.PAPUASERV.getCodEntiAbilitati(utenteAbilitazioni));
    GraficoVO graficoVO = reportisticaEJB.getGrafico(idElencoQuery, params);
    return graficoVO;
  }

  @RequestMapping(value = "dettaglio_{idElencoQuery}", method = RequestMethod.GET)
  public String dettaglio(
      @PathVariable(value = "idElencoQuery") long idElencoQuery, ModelMap model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    model.addAttribute("dettIdElencoQuery", idElencoQuery);
    return "reportistica/dettaglioGrafico";
  }

  @RequestMapping(value = "stampa_{idElencoQuery}", method = RequestMethod.GET)
  public String stampa(
      @PathVariable(value = "idElencoQuery") long idElencoQuery, ModelMap model,
      HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException
  {
    model.addAttribute("dettIdElencoQuery", idElencoQuery);
    return "reportistica/stampaGrafico";
  }

  @RequestMapping(value = "downloadExcelReport")
  public ModelAndView downloadExcel(HttpSession session, Model model)
      throws InternalServiceException, InternalUnexpectedException
  {
    GraficoVO graficoVO = (GraficoVO) session
        .getAttribute("reportSelzionatoSession");
    if (reportisticaEJB
        .hasExcelTemplateInElencoQuery(graficoVO.getIdElencoQuery()))
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
    httpHeaders.add("Content-type", MIME_TYPE_XLSX);
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"downloadExcelReportTemplate.xlsx\"");
    byte[] bytes = reportisticaEJB
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
