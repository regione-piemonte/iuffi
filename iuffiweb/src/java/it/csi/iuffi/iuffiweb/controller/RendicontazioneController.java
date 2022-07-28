package it.csi.iuffi.iuffiweb.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import it.csi.iuffi.iuffiweb.business.IOrganismoNocivoEJB;
import it.csi.iuffi.iuffiweb.business.IRendicontazioneFinanziariaEJB;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.RendicontazioneDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.annotation.IuffiSecurity;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;

@Controller
@IuffiSecurity(value = "TABELLE", controllo = IuffiSecurity.Controllo.DEFAULT)
@NoLoginRequired
@SuppressWarnings("unchecked")
public class RendicontazioneController  extends TabelleController
{
  ResourceBundle res = ResourceBundle.getBundle("config");
  @Autowired
  Validator validator;
  
  @Autowired
  private IOrganismoNocivoEJB organismoNocivoEJB;
  
  @Autowired
  private IRendicontazioneFinanziariaEJB rendicontazioneEJB;
  
  @InitBinder
  @Override
  public void initBinder(WebDataBinder binder) {
    super.initBinder(binder);
  }

  protected static final Logger        logger     = Logger
      .getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".integration");

  private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


  @RequestMapping(value = "/rendicontazione/showFilter")
  public String showFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    RendicontazioneDTO dto = new RendicontazioneDTO();
    if (dto.getAnno() == null) {
      Calendar now = Calendar.getInstance();
      int currentYear = now.get(Calendar.YEAR);
      dto.setAnno(currentYear);
    }
    model.addAttribute("rendicontazione", dto);
    setBreadcrumbs(model, request);
    session.removeAttribute("listaRendicontazione");
    session.removeAttribute("rendicontazione");
    return "gestionerendicontazione/ricerca";
  }

  @RequestMapping(value = "/rendicontazione/search")
  public String search(HttpSession session,Model model,@ModelAttribute("rendicontazione") RendicontazioneDTO rendicontazione,HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    Map<String,String> filtroInSessione = this.filtri(model, request, response, session);
    session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
    session.setAttribute("rendicontazione", rendicontazione);
    setBreadcrumbs(model, request);
    return "gestionerendicontazione/lista";
  }

  @RequestMapping(value = "/rendicontazione/getRendicontazioneJson", produces = "application/json", method = RequestMethod.GET)
  @ResponseBody
  public String getRendicontazioneJson(HttpServletRequest request, HttpSession session)
      throws InternalUnexpectedException, JsonGenerationException, JsonMappingException, IOException
  {
    RendicontazioneDTO dto = (RendicontazioneDTO) session.getAttribute("rendicontazione");
    //List<RendicontazioneDTO> lista = rendicontazioneEJB.findAll(); 
    List<RendicontazioneDTO> lista = rendicontazioneEJB.findByFilter(dto);
    session.setAttribute("listaRendicontazione", lista);
    
    if (lista == null) {
      lista = new ArrayList<>();
    }

    ObjectMapper mapper = new ObjectMapper().configure(
        org.codehaus.jackson.map.DeserializationConfig.Feature.USE_ANNOTATIONS, false)
        .configure(org.codehaus.jackson.map.SerializationConfig.Feature.USE_ANNOTATIONS, false);

    String obj = mapper.writeValueAsString(lista);
    return obj;
  }

  @RequestMapping(value = "/rendicontazione/clearFilter")
  public String clearFilter(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException
  {
    session.removeAttribute("listaRendicontazione");
    return showFilter(model, session, request, response);
  }

  @Lazy
  @RequestMapping(value = "/rendicontazione/filtri")
 public Map<String, String> filtri(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException
  {
    Map<String, String> filtroInSessione = (Map<String, String>) session.getAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER);
  
      if(filtroInSessione == null) {
        filtroInSessione = new HashMap<String, String>();
        session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER, filtroInSessione);
      }
      String filtroInit;
      if(filtroInSessione.get("tableRendicontazione") == null || "{}".equals(filtroInSessione.get("tableRendicontazione"))) {
        filtroInit = "";//"{\"flagArchiviato\":{\"_values\":[\"N\"]}}";
      } else {
        filtroInit=filtroInSessione.get("tableRendicontazione");
      } 
       // filtroInit =
           // if(anagrafica.getCognome()!=null &&)
          //  + ",\"cognome\":{\"cnt\":[\""+dto.getCognome().toUpperCase()+"\"]}"
          //  filtroInit += "}";  
       // value='{"annoCampagna":{"cnt":"2016"},"descrizione":{"_values":["Liquidato"]}}'
      filtroInSessione.put("tableRendicontazione", filtroInit);
      return filtroInSessione;
  }

  @Lazy
  @RequestMapping(value = "/rendicontazione/rendicontazioneExcel")
  public ModelAndView downloadExcel(Model model,HttpServletRequest request, HttpServletResponse response, HttpSession session)throws InternalUnexpectedException, FileNotFoundException, IOException
  {
    //response.setContentType("application/xls");
    //response.setHeader("Content-Disposition", "attachment; filename=\"rendicontazione.xls\"");
    HashMap<String, Object> modelForXls = new HashMap<String, Object>();
    List<RendicontazioneDTO> list = (List<RendicontazioneDTO>) session.getAttribute("listaRendicontazione");
    modelForXls.put("listaRendicontazione", list);
    RendicontazioneDTO rendicontazione = (RendicontazioneDTO) session.getAttribute("rendicontazione");
    modelForXls.put("rendicontazione", rendicontazione);
    return new ModelAndView("excelRendicontazioneView", modelForXls);
  }
  

  @RequestMapping(value = "/rendicontazione/generaExcel")
  public void generaExcelAllAreas(HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException, IOException
  {
    //response.setContentType("application/xls");
    //carico il template
    ServletContext servletContext = request.getSession().getServletContext();
    String root = servletContext.getRealPath("/");
    String templateFileAreeIndenni = "templateAreeIndenni.xlsx";
    String templateFileAreeDemarcate = "templateAreeDemarcate.xlsx";
    String templateAreeIndenni = root + res.getString("conf.path") + File.separator + templateFileAreeIndenni;
    String templateAreeDemarcate = root + res.getString("conf.path") + File.separator + templateFileAreeDemarcate;

    //logger.debug("PATH FILE template aree indenni :: "+templateFileAreeIndenni);
    //logger.debug("PATH FILE template aree demarcate :: "+templateFileAreeDemarcate);
    File fileAreeIndenni = null;
    File fileAreeDemarcate = null;

    RendicontazioneDTO dto = (RendicontazioneDTO) session.getAttribute("rendicontazione");

    if (StringUtils.isBlank(dto.getArea()) || dto.getArea().equals("N")) {
      fileAreeIndenni = new File(templateAreeIndenni);
      if (!fileAreeIndenni.exists()) {
        templateAreeIndenni = root + res.getString("conf.path") + File.separator + "web" + File.separator + "WEB-INF" + File.separator + "conf" + File.separator + templateFileAreeIndenni;
        fileAreeIndenni = new File(templateAreeIndenni);
      }
    }
    if (StringUtils.isBlank(dto.getArea()) || dto.getArea().equals("S")) {
      fileAreeDemarcate = new File(templateAreeDemarcate);
      if (!fileAreeDemarcate.exists()) {
        templateAreeDemarcate = root + res.getString("conf.path") + File.separator + "web" + File.separator + "WEB-INF" + File.separator + "conf" + File.separator + templateFileAreeDemarcate;
        fileAreeDemarcate = new File(templateAreeDemarcate);
      }
    }
    //creo la cartella di appoggio    
    String pathUserHome = System.getProperty("user.home");
    File dirPath = new File(pathUserHome+"/appo");
    dirPath.mkdirs();
    List<OrganismoNocivoDTO> listaOn = rendicontazioneEJB.findOrganismiNocivi(dto);
    List<RendicontazioneDTO> listaObj = (List<RendicontazioneDTO>)session.getAttribute("listaRendicontazione");

    if (listaObj == null) {
      logger.debug("Metodo generaExcel listaRendicontazione nulla (la ricarico)");
      listaObj = rendicontazioneEJB.findByFilter(dto);
    }

    FileInputStream fis = null;
    FileInputStream fisAD = null;

    XSSFWorkbook wb = null;
    XSSFSheet sheetSummary = null;
    XSSFSheet sheetTab1 = null;     // Official
    XSSFSheet sheetTab2 = null;     // Subcontractor
    XSSFRow row;
    XSSFRow rowTab2;

    XSSFWorkbook wbAD = null;
    XSSFSheet sheetSummaryAD = null;
    XSSFSheet sheetADTab1 = null;     // Official
    XSSFSheet sheetADTab2 = null;     // Subcontractor
    XSSFRow rowAD;
    XSSFRow rowADTab2;
    
    int conta = 0;
    
    if (listaOn != null) {
      for (OrganismoNocivoDTO onr: listaOn) {

        if (fileAreeIndenni != null) {
          fis = new FileInputStream(fileAreeIndenni);
          wb = new XSSFWorkbook(fis);
          fis.close();
          sheetSummary = wb.getSheetAt(0);
          sheetTab1 = wb.getSheetAt(4);     // Official
          sheetTab2 = wb.getSheetAt(5);     // Subcontractor
          row = null;
          rowTab2 = null;
          /*
          for (int i=0; i< wb.getNumberOfSheets(); i++) {
            logger.debug("i="+i+" - name="+wb.getSheetAt(i).getSheetName());
          }*/
          XSSFRow rowNomeOn = sheetSummary.getRow(2);
          XSSFRow rowLabel = sheetSummary.getRow(8);
          rowNomeOn.getCell(4).setCellValue(onr.getNomeCompleto());
          rowLabel.getCell(6).setCellValue("Data in Application "+ dto.getAnno()+" and after adjustment");
          rowLabel.getCell(8).setCellValue(" Annual budget "+ dto.getAnno()+" after adjustment");
        }

        if (fileAreeDemarcate != null) {
          fisAD = new FileInputStream(fileAreeDemarcate);
          wbAD = new XSSFWorkbook(fisAD);
          fisAD.close();
          sheetSummaryAD = wbAD.getSheetAt(0);
          sheetADTab1 = wbAD.getSheetAt(1);     // Official
          sheetADTab2 = wbAD.getSheetAt(2);       // Subcontractor
          rowAD = null;
          rowADTab2 = null;
          /*
          for (int i=0; i< wbAD.getNumberOfSheets(); i++) {
            logger.debug("i="+i+" - name="+wbAD.getSheetAt(i).getSheetName());
          }*/
          XSSFRow rowADNomeOn = sheetSummaryAD.getRow(2);
          XSSFRow rowADLabel = sheetSummaryAD.getRow(8);
          rowADNomeOn.getCell(4).setCellValue(onr.getNomeCompleto());
          rowADLabel.getCell(6).setCellValue("Data in Application "+ dto.getAnno()+" and after adjustment");
          rowADLabel.getCell(8).setCellValue(" Annual budget "+ dto.getAnno()+" after adjustment");
        }

        int count = 5;    // colonna start x foglio Official (file aree indenni)
        int count2 = 6;   // colonna start x foglio Subcontractor (file aree indenni)
        int countAD = 5;  // colonna start x foglio Official (file aree demarcate)
        int countAD2 = 5; // colonna start x foglio Subcontractor (file aree demarcate)

        for (RendicontazioneDTO obj : listaObj) {

          if (obj.getOnSpecie()!=null && obj.getOnSpecie().toLowerCase().indexOf(onr.getSigla().toLowerCase()) != -1 && obj.getTipo().equals("I") ||
              obj.getOrganismiCampione()!=null && obj.getOrganismiCampione().toLowerCase().indexOf(onr.getSigla().toLowerCase()) != -1 && obj.getTipo().equals("C") ||
              obj.getOrganismoTrappola()!=null && obj.getOrganismoTrappola().toLowerCase().indexOf(onr.getSigla().toLowerCase()) != -1 && obj.getTipo().equals("T")) {
/*
            if (onr.getSigla().equals("ANOLCN") && obj.getNumPiazzamento() != null && obj.getNumPiazzamento().intValue() > 0) {
              logger.debug("on trappola = " + obj.getOrganismoTrappola());
              conta += obj.getNumPiazzamento().intValue();
            }
*/
            if (obj.getFlagEmergenza().equalsIgnoreCase("N")) {     // Aree indenni
              row = sheetTab1.getRow(count);
              rowTab2 = sheetTab2.getRow(count2);
              if (obj.getSubcontractor().equals("N")) {
                writeAreeIndenniOfficial(obj, row, onr);
                count++;
              }
              else
              {
                //rowTab2.getCell(0).setCellValue("");
                /*
                logger.debug("organismo nocivo: " + onr.getSigla());
                logger.debug("data missione: " + obj.getDataMissioneS());
                logger.debug("row num: " + rowTab2.getRowNum());*/
                writeAreeIndenniSubcontractor(obj, rowTab2, onr);
                count2++;
              } // if subcontractor
              Row lastRow = sheetTab1.getRow(sheetTab1.getLastRowNum());
              lastRow.getCell(8).setCellFormula("SUM(I6:I"+sheetTab1.getLastRowNum()+")");
              lastRow.getCell(9).setCellFormula("SUM(J6:J"+sheetTab1.getLastRowNum()+")");
              lastRow.getCell(10).setCellFormula("SUM(K6:K"+sheetTab1.getLastRowNum()+")");
              lastRow.getCell(12).setCellFormula("SUM(M6:M"+sheetTab1.getLastRowNum()+")");
              lastRow.getCell(15).setCellFormula("SUM(P6:P"+sheetTab1.getLastRowNum()+")");
              lastRow.getCell(19).setCellFormula("SUM(T6:T"+sheetTab1.getLastRowNum()+")");
              lastRow.getCell(23).setCellFormula("SUM(X6:X"+sheetTab1.getLastRowNum()+")");
              lastRow.getCell(28).setCellFormula("SUM(AC6:AC"+sheetTab1.getLastRowNum()+")");
              lastRow.getCell(30).setCellFormula("SUM(AE6:AE"+sheetTab1.getLastRowNum()+")");
              lastRow.getCell(31).setCellFormula("SUM(AF6:AF"+sheetTab1.getLastRowNum()+")");
              lastRow.getCell(32).setCellFormula("SUM(AG6:AG"+sheetTab1.getLastRowNum()+")");
              lastRow.getCell(37).setCellFormula("SUM(AL6:AL"+sheetTab1.getLastRowNum()+")");
              Row lastRow2 = sheetTab2.getRow(sheetTab2.getLastRowNum());
              lastRow2.getCell(8).setCellFormula("SUM(I7:I"+sheetTab2.getLastRowNum()+")");
              lastRow2.getCell(9).setCellFormula("SUM(J7:J"+sheetTab2.getLastRowNum()+")");
              lastRow2.getCell(10).setCellFormula("SUM(K7:K"+sheetTab2.getLastRowNum()+")");
              lastRow2.getCell(12).setCellFormula("SUM(M7:M"+sheetTab2.getLastRowNum()+")");
              lastRow2.getCell(15).setCellFormula("SUM(P7:P"+sheetTab2.getLastRowNum()+")");
              lastRow2.getCell(19).setCellFormula("SUM(T7:T"+sheetTab2.getLastRowNum()+")");
              lastRow2.getCell(23).setCellFormula("SUM(X7:X"+sheetTab2.getLastRowNum()+")");
              lastRow2.getCell(28).setCellFormula("SUM(AC7:AC"+sheetTab2.getLastRowNum()+")");
              lastRow2.getCell(30).setCellFormula("SUM(AE7:AE"+sheetTab2.getLastRowNum()+")");
              lastRow2.getCell(31).setCellFormula("SUM(AF7:AF"+sheetTab2.getLastRowNum()+")");
              lastRow2.getCell(32).setCellFormula("SUM(AG7:AG"+sheetTab2.getLastRowNum()+")");
            } // end if flagEmergenza = 'N'
            else  // flagEmergenza = 'S'
            {
              rowAD = sheetADTab1.getRow(countAD);
              rowADTab2 = sheetADTab2.getRow(countAD2);
              if (obj.getSubcontractor().equals("N")) {
                writeAreeDemarcateOfficial(obj, rowAD);
                countAD++;
              }
              else {
                writeAreeDemarcateSubcontractor(obj, rowADTab2);
                countAD2++;
              }
              Row lastRow = sheetADTab1.getRow(sheetADTab1.getLastRowNum());
              lastRow.getCell(7).setCellFormula("SUM(H6:H"+sheetADTab1.getLastRowNum()+")/100");
              lastRow.getCell(8).setCellFormula("SUM(I6:I"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(9).setCellFormula("SUM(J6:J"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(10).setCellFormula("SUM(K6:K"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(12).setCellFormula("SUM(M6:M"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(13).setCellFormula("SUM(N6:N"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(16).setCellFormula("SUM(Q6:Q"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(17).setCellFormula("SUM(R6:R"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(19).setCellFormula("SUM(T6:T"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(20).setCellFormula("SUM(U6:U"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(23).setCellFormula("SUM(X6:X"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(25).setCellFormula("SUM(Z6:Z"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(26).setCellFormula("SUM(AA6:AA"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(28).setCellFormula("SUM(AC6:AC"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(29).setCellFormula("SUM(AD6:AD"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(30).setCellFormula("SUM(AE6:AE"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(32).setCellFormula("SUM(AG6:AG"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(33).setCellFormula("SUM(AH6:AH"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(35).setCellFormula("SUM(AJ6:AJ"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(36).setCellFormula("SUM(AK6:AK"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(38).setCellFormula("SUM(AM6:AM"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(39).setCellFormula("SUM(AN6:AN"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(43).setCellFormula("SUM(AR6:AR"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(45).setCellFormula("SUM(AT6:AT"+sheetADTab1.getLastRowNum()+")");
              lastRow.getCell(46).setCellFormula("SUM(AU6:AU"+sheetADTab1.getLastRowNum()+")");
              Row lastRow2 = sheetADTab2.getRow(sheetADTab2.getLastRowNum());
              lastRow2.getCell(8).setCellFormula("SUM(I6:I"+sheetADTab2.getLastRowNum()+")/100");
              lastRow2.getCell(9).setCellFormula("SUM(J6:J"+sheetADTab2.getLastRowNum()+")");
              lastRow2.getCell(10).setCellFormula("SUM(K6:K"+sheetADTab2.getLastRowNum()+")");
              lastRow2.getCell(11).setCellFormula("SUM(L6:L"+sheetADTab2.getLastRowNum()+")");
              lastRow2.getCell(13).setCellFormula("SUM(N6:N"+sheetADTab2.getLastRowNum()+")");
              lastRow2.getCell(14).setCellFormula("SUM(O6:O"+sheetADTab2.getLastRowNum()+")");
              lastRow2.getCell(18).setCellFormula("SUM(S6:S"+sheetADTab2.getLastRowNum()+")");
              lastRow2.getCell(21).setCellFormula("SUM(V6:V"+sheetADTab2.getLastRowNum()+")");
              lastRow2.getCell(27).setCellFormula("SUM(AB6:AB"+sheetADTab2.getLastRowNum()+")");
              lastRow2.getCell(31).setCellFormula("SUM(AF6:AF"+sheetADTab2.getLastRowNum()+")");
              lastRow2.getCell(33).setCellFormula("SUM(AH6:AH"+sheetADTab2.getLastRowNum()+")");
              lastRow2.getCell(34).setCellFormula("SUM(AI6:AI"+sheetADTab2.getLastRowNum()+")");
            }
          } // if organismo nocivo corrisponde
        } // for lista rendicontazione

        logger.debug("FOGLIO 1: " + count);
        logger.debug("FOGLIO 2: " + count2);
        logger.debug("FOGLIO 1 AD: " + countAD);
        logger.debug("FOGLIO 2 AD: " + countAD2);
        logger.debug("ON FILE: " + onr.getNomeCompleto());
/*
        if (onr.getSigla().equals("POPIJA")) {
          logger.debug("MONITORAGGIO POPIJA");
        }
*/
        if (wb != null)
          wb.getCreationHelper().createFormulaEvaluator().evaluateAll();
        if (wbAD != null)
          wbAD.getCreationHelper().createFormulaEvaluator().evaluateAll();

        if (fis != null) {
          FileOutputStream fileOut = new FileOutputStream(dirPath+"/PIEMONTE-"+onr.getNomeCompleto()+".xlsx");
          wb.write(fileOut);
          fileOut.close();
        }
        
        if (fisAD != null) {
          FileOutputStream fileOutAD = new FileOutputStream(dirPath+"/PIEMONTE-"+onr.getNomeCompleto()+"-AreeDemarcate.xlsx");
          wbAD.write(fileOutAD);
          fileOutAD.close();
        }
      } // for lista organismi nocivi
    }

    logger.debug("conta = " + conta);
    
    if (dirPath.exists()) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ZipOutputStream zos = new ZipOutputStream(baos);
      zip(dirPath, zos);
      zos.close();

      deleteDirectory(dirPath);

      // byte[] zip = zos.

      response.setContentType("application/zip");
      response.setHeader("Content-Disposition", "attachment; filename=output.zip");

      ServletOutputStream sos = response.getOutputStream();
      sos.write(baos.toByteArray());
      sos.flush();
      sos.close();
    }
    logger.debug("*********************** FINE GENERAZIONE FILE EXCEL ********************");
  }

  @RequestMapping(value = "/rendicontazione/generaExcelNew")
  public void generaExcelAllAreasProva(HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException, IOException
  {
    // *****************************************************************************************************************************
    // 09/02/2022 - non usato attualmente, serve se si vuole utilizzare SXSSFWorkbook al posto di XSSFWorkbook - da sistemare perchè
    // la libreria SXSSF funziona solo in write/append - soluzione da studiare
    // ******************************************************************************************************************************
    //response.setContentType("application/xls");
    //carico il template
    ServletContext servletContext = request.getSession().getServletContext();
    String root = servletContext.getRealPath("/");
    String templateFileAreeIndenni = "templateAreeIndenniVuoto.xlsx";
    String templateFileAreeDemarcate = "templateAreeDemarcateVuoto.xlsx";
    String templateAreeIndenni = root + res.getString("conf.path") + File.separator + templateFileAreeIndenni;
    String templateAreeDemarcate = root + res.getString("conf.path") + File.separator + templateFileAreeDemarcate;

    //logger.debug("PATH FILE template aree indenni :: "+templateFileAreeIndenni);
    //logger.debug("PATH FILE template aree demarcate :: "+templateFileAreeDemarcate);
    File fileAreeIndenni = null;
    File fileAreeDemarcate = null;
    
    RendicontazioneDTO dto = (RendicontazioneDTO) session.getAttribute("rendicontazione");

    if (StringUtils.isBlank(dto.getArea()) || dto.getArea().equals("N")) {
      fileAreeIndenni = new File(templateAreeIndenni);
      if (!fileAreeIndenni.exists()) {
        templateAreeIndenni = root + res.getString("conf.path") + File.separator + "web" + File.separator + "WEB-INF" + File.separator + "conf" + File.separator + templateFileAreeIndenni;
        fileAreeIndenni = new File(templateAreeIndenni);
      }
    }
    if (StringUtils.isBlank(dto.getArea()) || dto.getArea().equals("S")) {
      fileAreeDemarcate = new File(templateAreeDemarcate);
      if (!fileAreeDemarcate.exists()) {
        templateAreeDemarcate = root + res.getString("conf.path") + File.separator + "web" + File.separator + "WEB-INF" + File.separator + "conf" + File.separator + templateFileAreeDemarcate;
        fileAreeDemarcate = new File(templateAreeDemarcate);
      }
    }
    //creo la cartella di appoggio    
    String pathUserHome = System.getProperty("user.home");
    File dirPath = new File(pathUserHome+"/appo");
    dirPath.mkdirs();
    List<OrganismoNocivoDTO> on = rendicontazioneEJB.findOrganismiNocivi(dto);
    List<RendicontazioneDTO> listaObj = (List<RendicontazioneDTO>)session.getAttribute("listaRendicontazione");

    if (listaObj == null) {
      logger.debug("Metodo generaExcel listaRendicontazione nulla (la ricarico)");
      listaObj = rendicontazioneEJB.findByFilter(dto);
    }

    FileInputStream fis = null;
    FileInputStream fisAD = null;
    
    XSSFWorkbook wb_template = null;
    SXSSFSheet sheetSummary = null;
    SXSSFSheet sheetTab1 = null;     // Official
    SXSSFSheet sheetTab2 = null;     // Subcontractor
    Row row;
    Row rowTab2;

    XSSFWorkbook wbAD_template = null;
    SXSSFSheet sheetSummaryAD = null;
    SXSSFSheet sheetADTab1 = null;     // Official
    SXSSFSheet sheetADTab2 = null;     // Subcontractor
    Row rowAD;
    Row rowADTab2;
    
    SXSSFWorkbook wb = null;
    SXSSFWorkbook wbAD = null;
    
    for (OrganismoNocivoDTO onr: on) {

      if (fileAreeIndenni != null) {
        fis = new FileInputStream(fileAreeIndenni);
        wb_template = new XSSFWorkbook(fis);
        fis.close();
        wb = new SXSSFWorkbook(wb_template);
        wb.setCompressTempFiles(true);
        sheetSummary = (SXSSFSheet) wb.getSheetAt(0);
        sheetTab1 = (SXSSFSheet) wb.getSheetAt(4);      // Official
        sheetTab2 = (SXSSFSheet) wb.getSheetAt(5);      // Subcontractor
        sheetTab1.setRandomAccessWindowSize(100);       // keep 100 rows in memory, exceeding rows will be flushed to disk
        sheetTab2.setRandomAccessWindowSize(100);
        row = null;
        rowTab2 = null;
        /*
        for (int i=0; i< wb.getNumberOfSheets(); i++) {
          logger.debug("i="+i+" - name="+wb.getSheetAt(i).getSheetName());
        }*/
        /*
        Row rowNomeOn = sheetSummary.getRow(2);
        Row rowLabel = sheetSummary.getRow(8);
        rowNomeOn.getCell(4).setCellValue(onr.getNomeCompleto());
        rowLabel.getCell(6).setCellValue("Data in Application "+ dto.getAnno()+" and after adjustment");
        rowLabel.getCell(8).setCellValue(" Annual budget "+ dto.getAnno()+" after adjustment");
        */
      }

      if (fileAreeDemarcate != null) {
        fisAD = new FileInputStream(fileAreeDemarcate);
        wbAD_template = new XSSFWorkbook(fisAD);
        fisAD.close();
        wbAD = new SXSSFWorkbook(wbAD_template);
        wbAD.setCompressTempFiles(true);
        sheetSummaryAD = (SXSSFSheet) wbAD.getSheetAt(0);
        sheetADTab1 = (SXSSFSheet) wbAD.getSheetAt(1);      // Official
        sheetADTab2 = (SXSSFSheet) wbAD.getSheetAt(2);      // Subcontractor
        sheetADTab1.setRandomAccessWindowSize(100);         // keep 100 rows in memory, exceeding rows will be flushed to disk
        sheetADTab2.setRandomAccessWindowSize(100);
        rowAD = null;
        rowADTab2 = null;
        /*
        for (int i=0; i< wbAD.getNumberOfSheets(); i++) {
          logger.debug("i="+i+" - name="+wbAD.getSheetAt(i).getSheetName());
        }*/
        Row rowADNomeOn = sheetSummaryAD.getRow(2);
        Row rowADLabel = sheetSummaryAD.getRow(8);
        if (rowADNomeOn != null)
          rowADNomeOn.getCell(4).setCellValue(onr.getNomeCompleto());
        if (rowADLabel != null) {
          rowADLabel.getCell(6).setCellValue("Data in Application "+ dto.getAnno()+" and after adjustment");
          rowADLabel.getCell(8).setCellValue(" Annual budget "+ dto.getAnno()+" after adjustment");
        }
      }

      int count = 5;    // colonna start x foglio Official (file aree indenni)
      int count2 = 6;   // colonna start x foglio Subcontractor (file aree indenni)
      int countAD = 5;  // colonna start x foglio Official (file aree demarcate)
      int countAD2 = 5; // colonna start x foglio Subcontractor (file aree demarcate)

      for (RendicontazioneDTO obj : listaObj) {

        if (obj.getOnSpecie()!=null && obj.getOnSpecie().toLowerCase().indexOf(onr.getSigla().toLowerCase()) != -1) {

          if (obj.getFlagEmergenza().equalsIgnoreCase("N")) {     // Aree indenni
            row = sheetTab1.getRow(count);
            rowTab2 = sheetTab2.getRow(count2);
            if (obj.getSubcontractor().equals("N")) {
              //writeAreeIndenniOfficial(obj, row);
              count++;
            }
            else
            {
              //rowTab2.getCell(0).setCellValue("");
              /*
              logger.debug("organismo nocivo: " + onr.getSigla());
              logger.debug("data missione: " + obj.getDataMissioneS());
              logger.debug("row num: " + rowTab2.getRowNum());*/
              //writeAreeIndenniSubcontractor(obj, rowTab2);
              count2++;
            } // if subcontractor
            /*
            Row lastRow = sheetTab1.getRow(sheetTab1.getLastRowNum());
            lastRow.getCell(8).setCellFormula("SUM(I6:I"+sheetTab1.getLastRowNum()+")");
            lastRow.getCell(9).setCellFormula("SUM(J6:J"+sheetTab1.getLastRowNum()+")");
            lastRow.getCell(10).setCellFormula("SUM(K6:K"+sheetTab1.getLastRowNum()+")");
            lastRow.getCell(12).setCellFormula("SUM(M6:M"+sheetTab1.getLastRowNum()+")");
            lastRow.getCell(15).setCellFormula("SUM(P6:P"+sheetTab1.getLastRowNum()+")");
            lastRow.getCell(19).setCellFormula("SUM(T6:T"+sheetTab1.getLastRowNum()+")");
            lastRow.getCell(23).setCellFormula("SUM(X6:X"+sheetTab1.getLastRowNum()+")");
            lastRow.getCell(28).setCellFormula("SUM(AC6:AC"+sheetTab1.getLastRowNum()+")");
            lastRow.getCell(30).setCellFormula("SUM(AE6:AE"+sheetTab1.getLastRowNum()+")");
            lastRow.getCell(31).setCellFormula("SUM(AF6:AF"+sheetTab1.getLastRowNum()+")");
            lastRow.getCell(32).setCellFormula("SUM(AG6:AG"+sheetTab1.getLastRowNum()+")");
            lastRow.getCell(37).setCellFormula("SUM(AL6:AL"+sheetTab1.getLastRowNum()+")");
            Row lastRow2 = sheetTab2.getRow(sheetTab2.getLastRowNum());
            lastRow2.getCell(8).setCellFormula("SUM(I7:I"+sheetTab2.getLastRowNum()+")");
            lastRow2.getCell(9).setCellFormula("SUM(J7:J"+sheetTab2.getLastRowNum()+")");
            lastRow2.getCell(10).setCellFormula("SUM(K7:K"+sheetTab2.getLastRowNum()+")");
            lastRow2.getCell(12).setCellFormula("SUM(M7:M"+sheetTab2.getLastRowNum()+")");
            lastRow2.getCell(15).setCellFormula("SUM(P7:P"+sheetTab2.getLastRowNum()+")");
            lastRow2.getCell(19).setCellFormula("SUM(T7:T"+sheetTab2.getLastRowNum()+")");
            lastRow2.getCell(23).setCellFormula("SUM(X7:X"+sheetTab2.getLastRowNum()+")");
            lastRow2.getCell(28).setCellFormula("SUM(AC7:AC"+sheetTab2.getLastRowNum()+")");
            lastRow2.getCell(30).setCellFormula("SUM(AE7:AE"+sheetTab2.getLastRowNum()+")");
            lastRow2.getCell(31).setCellFormula("SUM(AF7:AF"+sheetTab2.getLastRowNum()+")");
            lastRow2.getCell(32).setCellFormula("SUM(AG7:AG"+sheetTab2.getLastRowNum()+")");
            */
          } // end if flagEmergenza = 'N'
          else  // flagEmergenza = 'S'
          {
            rowAD = sheetADTab1.getRow(countAD);
            rowADTab2 = sheetADTab2.getRow(countAD2);
            if (obj.getSubcontractor().equals("N")) {
              //writeAreeDemarcateOfficial(obj, rowAD);           metodo da implementare
              countAD++;
            }
            else {
              writeAreeDemarcateSubcontractor(obj, sheetADTab2, countAD2);
              countAD2++;
            }
          }
        } // if organismo nocivo corrisponde
      } // for lista rendicontazione

      logger.debug("FOGLIO 1: " + count);
      logger.debug("FOGLIO 2: " + count2);
      logger.debug("FOGLIO 1 AD: " + countAD);
      logger.debug("FOGLIO 2 AD: " + countAD2);
      logger.debug("ON FILE: " + onr.getNomeCompleto());
/*
      if (wb != null)
        wb.getCreationHelper().createFormulaEvaluator().evaluateAll();
      if (wbAD != null)
        wbAD.getCreationHelper().createFormulaEvaluator().evaluateAll();
*/
      if (fis != null) {
        FileOutputStream fileOut = new FileOutputStream(dirPath+"/PIEMONTE-"+onr.getNomeCompleto()+"_new.xlsx");
        wb.write(fileOut);
        fileOut.close();
      }

      if (fisAD != null) {
        FileOutputStream fileOutAD = new FileOutputStream(dirPath+"/PIEMONTE-"+onr.getNomeCompleto()+"-AreeDemarcate_new.xlsx");
        wbAD.setForceFormulaRecalculation(true);
        wbAD.write(fileOutAD);
        fileOutAD.close();
      }
    } // for lista organismi nocivi

    if (dirPath.exists()) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ZipOutputStream zos = new ZipOutputStream(baos);
      zip(dirPath, zos);
      zos.close();

      deleteDirectory(dirPath);

      // byte[] zip = zos.

      response.setContentType("application/zip");
      response.setHeader("Content-Disposition", "attachment; filename=output.zip");

      ServletOutputStream sos = response.getOutputStream();
      sos.write(baos.toByteArray());
      sos.flush();
    }
    logger.debug("*********************** FINE GENERAZIONE FILE EXCEL ********************");
  }

  @RequestMapping(value = "/rendicontazione/generaExcelAreeIndenni")
  public void generaExcelAreeIndenni(HttpSession session,Model model,HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException, IOException
  {
    // 09/02/2022 - non usato attualmente, serve se si vuole generare solo per aree indenni
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    //response.setContentType("application/xls");
    //carico il template
    ServletContext servletContext = request.getSession().getServletContext();
    String root = servletContext.getRealPath("/");
    String templateFileAreeIndenni = "templateAreeIndenni.xlsx";
    String templateAreeIndenni = root + res.getString("conf.path") + File.separator + templateFileAreeIndenni;
    logger.debug("PATH FILE template aree indenni :: "+templateFileAreeIndenni);

    File fileAreeIndenni = new File(templateAreeIndenni);
    if (!fileAreeIndenni.exists()) {
      templateAreeIndenni = root + res.getString("conf.path") + File.separator + "web" + File.separator + "WEB-INF" + File.separator + "conf" + File.separator + templateFileAreeIndenni;
      fileAreeIndenni = new File(templateAreeIndenni);
    }
    RendicontazioneDTO dto = (RendicontazioneDTO) session.getAttribute("rendicontazione");
    //creo la cartella di appoggio    
    String pathUserHome = System.getProperty("user.home");
    File dirPath = new File(pathUserHome+"/appo");
    dirPath.mkdirs();
    List<OrganismoNocivoDTO> on = rendicontazioneEJB.findOrganismiNocivi(dto);
    List<RendicontazioneDTO> listaObj = (List<RendicontazioneDTO>)session.getAttribute("listaRendicontazione");

    if (listaObj == null) {
      logger.debug("Metodo generaExcel listaRendicontazione nulla (la ricarico)");
      listaObj = rendicontazioneEJB.findByFilter(dto);
    }

    for (OrganismoNocivoDTO onr: on) {

      FileInputStream fis = new FileInputStream(fileAreeIndenni);
      XSSFWorkbook wb = new XSSFWorkbook(fis); 
      XSSFSheet sheetSummary = wb.getSheetAt(0);
      XSSFSheet sheetTab1 = wb.getSheetAt(4);     // Official
      XSSFSheet sheetTab2 = wb.getSheetAt(5);     // Subcontractor
      XSSFRow row;
      XSSFRow rowTab2;

      for (int i=0; i< wb.getNumberOfSheets(); i++) {
        logger.debug("i="+i+" - name="+wb.getSheetAt(i).getSheetName());
      }
      logger.debug(sheetTab1.getSheetName());
      logger.debug(sheetTab2.getSheetName());

      XSSFRow rowNomeOn = sheetSummary.getRow(2);
      XSSFRow rowLabel = sheetSummary.getRow(8);
      rowNomeOn.getCell(4).setCellValue(onr.getNomeCompleto());
      rowLabel.getCell(6).setCellValue("Data in Application "+ dto.getAnno()+" and after adjustment");
      rowLabel.getCell(8).setCellValue(" Annual budget "+ dto.getAnno()+" after adjustment");
      int count = 5;  // colonna start x foglio Official
      int count2 = 6; // colonna start x foglio Subcontractor
      // This trick ensures that we get the data properly even if it doesn't start from first few rows

      for (RendicontazioneDTO obj : listaObj) {

        if (obj == null)
          continue;

        row = sheetTab1.getRow(count);
        rowTab2 = sheetTab2.getRow(count2);

        if (obj.getOnSpecie()!=null && obj.getOnSpecie().toLowerCase().indexOf(onr.getSigla().toLowerCase()) != -1) {

          if (obj.getSubcontractor().equals("N")) {
            if (row.getRowNum() > sheetTab1.getLastRowNum()-1) {
              copyRow(wb, sheetTab1, 58, row.getRowNum());
              row = sheetTab1.getRow(sheetTab1.getLastRowNum()-1);
            }
            row.getCell(0).setCellValue(obj.getIspettore()!=null ? obj.getIspettore() : "");
            row.getCell(1).setCellValue(obj.getNumeroTrasferta()!=null ? obj.getNumeroTrasferta() : "");
            row.getCell(2).setCellValue((obj.getNumeroVerbale()!=null)?obj.getNumeroVerbale():"");
            row.getCell(3).setCellValue((obj.getDataInizioValidita()!=null)?df.format(obj.getDataInizioValidita()):"");
            row.getCell(4).setCellValue((obj.getTypologyOfLocation()!=null)?obj.getCodiceUfficiale().concat(" ").concat(obj.getTypologyOfLocation()):obj.getCodiceUfficiale()); // Evolutive gennaio 22
            // 5 other survey site ?
            row.getCell(6).setCellValue(obj.getLatitudine()!=null ? Double.valueOf(obj.getLatitudine()) : 0);
            row.getCell(7).setCellValue(obj.getLongitudine() != null ? Double.valueOf(obj.getLongitudine()) : 0); 
            if (obj.getTipo().equals("I")) {
              try {
                row.getCell(8).setCellValue(obj.getSuperfice()!=null ? obj.getSuperfice() : 0);
                //row.getCell(8).setCellValue(obj.getNumeroPiante()!=null ? obj.getNumeroPiante() : 0);
                row.getCell(9).setCellValue(1);
                row.getCell(10).setCellValue((obj.getNumOreIspezione()!=null ? obj.getNumOreIspezione() : 0)/(obj.getNumOnRimborso()!=null ? obj.getNumOnRimborso() : 0));
                // 11 RESULT OF EXAMINATION (menu a tendina)
                //row.getCell(11).setCellValue(obj.getCostoOrarioTecIspezione()!=null ? obj.getCostoOrarioTecIspezione() : 0);
                //se ho campioni
                row.getCell(12).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                row.getCell(13).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                row.getCell(14).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                //row.getCell(16).setCellValue("-");
                row.getCell(15).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
                //row.getCell(18).setCellValue(obj.getCostoOrarioTecCampionamento()!=null ? obj.getCostoOrarioTecCampionamento() : 0);

                // da valutare *****************************
                row.getCell(25).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                row.getCell(26).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                row.getCell(27).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
                row.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                row.getCell(29).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
                //
              }
              catch (Exception e) {
                logger.error("Errore: " + e.getMessage());
                logger.debug("righe template 1 non sufficienti?");
              }
            } // end if tipo = Ispezione visiva
            else
              if(obj.getTipo().equals("C")) { // campioni
                row.getCell(12).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                row.getCell(13).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                row.getCell(14).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                //row.getCell(16).setCellValue("-");
                row.getCell(15).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
                //row.getCell(18).setCellValue(obj.getCostoOrarioTecCampionamento()!=null ? obj.getCostoOrarioTecCampionamento() : 0);

                // da valutare *****************************
                row.getCell(25).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                row.getCell(26).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                row.getCell(27).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
                row.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                row.getCell(29).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
                //
              }
              else {  // tipo = Trappolaggio
                row.getCell(16).setCellValue((obj.getCodiceTrappola()!=null)?obj.getCodiceTrappola():"");
                row.getCell(17).setCellValue((obj.getTipologiaTrappola()!=null)?obj.getTipologiaTrappola():"");
                // 18 EU trap category ?
                row.getCell(19).setCellValue((obj.getNumPiazzamento()!=null)?obj.getNumPiazzamento():0);
                //row.getCell(24).setCellValue((obj.getCostoSingolaTrappola()!=null)?obj.getCostoSingolaTrappola():0);
                row.getCell(20).setCellValue((obj.getNumOrePiazzTrappole()!=null)?obj.getNumOrePiazzTrappole():0);
                //row.getCell(31).setCellValue((obj.getCostoOrarioTecTrappolaggio()!=null)?obj.getCostoOrarioTecTrappolaggio():0);
                row.getCell(21).setCellValue((obj.getNumOreRicSostTrappole()!=null)?obj.getNumOreRicSostTrappole():0);
                //row.getCell(34).setCellValue((obj.getCostoOrarioTecTrappolaggio()!=null)?obj.getCostoOrarioTecTrappolaggio():0);
                row.getCell(22).setCellValue((obj.getNumOreRimozioneTrappole()!=null)?obj.getNumOreRimozioneTrappole():0);
                //row.getCell(37).setCellValue((obj.getCostoOrarioTecTrappolaggio()!=null)?obj.getCostoOrarioTecTrappolaggio():0);
              } // end if tipo = trappolaggio

            // Campo aggiuntivo rispetto al template richiesto da Regione (G. Mason e C. Morone)
            row.createCell(38).setCellValue((obj.getNomeLatinoSpecie()!=null)?obj.getNomeLatinoSpecie():"");
            count++;
          }
          else
          {
            // Subcontractor
            //rowTab2.getCell(0).setCellValue("");
            logger.debug("organismo nocivo: " + onr.getSigla());
            logger.debug("data missione: " + obj.getDataMissioneS());
            logger.debug("row num: " + rowTab2.getRowNum());

            if (rowTab2.getRowNum() > 58) {
              copyRow(wb, sheetTab2, 58, rowTab2.getRowNum());
              rowTab2 = sheetTab2.getRow(sheetTab2.getLastRowNum()-1);
            }
            if (rowTab2.getCell(1) == null) {
              logger.debug("attenzione");
            }
            rowTab2.getCell(1).setCellValue(obj.getIspettore()!=null ? obj.getIspettore() : "");
            rowTab2.getCell(2).setCellValue((obj.getNumeroVerbale()!=null)?obj.getNumeroVerbale():"");
            rowTab2.getCell(3).setCellValue((obj.getDataMissione()!=null)?df.format(obj.getDataMissione()):"");
            rowTab2.getCell(4).setCellValue((obj.getTypologyOfLocation()!=null)?obj.getCodiceUfficiale().concat(" ").concat(obj.getTypologyOfLocation()):obj.getCodiceUfficiale()); // Evolutive gennaio 22
            // 5 - other survey site (2.5.6 and 3.4.4  specification)
            rowTab2.getCell(6).setCellValue(obj.getLatitudine()!=null ? Double.valueOf(obj.getLatitudine()) : 0);
            rowTab2.getCell(7).setCellValue(obj.getLongitudine() != null ? Double.valueOf(obj.getLongitudine()) : 0); 
            rowTab2.getCell(8).setCellValue(obj.getSuperfice()!=null ? obj.getSuperfice() : 0);
            //rowTab2.getCell(9).setCellValue(obj.getNumeroPiante()!=null ? obj.getNumeroPiante() : 0);
            rowTab2.getCell(9).setCellValue(1);

            if (obj.getTipo().equals("I")) {
              try {
                rowTab2.getCell(10).setCellValue((obj.getNumOreIspezione()!=null ? obj.getNumOreIspezione() : 0)/(obj.getNumOnRimborso()!=null ? obj.getNumOnRimborso() : 0));
                // 11 - RESULT OF VISUAL EXAMINATION
                //rowTab2.getCell(12).setCellValue(obj.getCostoOrarioTecIspezione()!=null ? obj.getCostoOrarioTecIspezione() : 0);
                //se ho campioni
                rowTab2.getCell(12).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                rowTab2.getCell(13).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                rowTab2.getCell(14).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                rowTab2.getCell(15).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
                //test
                rowTab2.getCell(25).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                rowTab2.getCell(26).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                rowTab2.getCell(27).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
                rowTab2.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni().intValue() : 0);
                rowTab2.getCell(29).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
              }
              catch (Exception e) {
                logger.error("Errore: " + e.getMessage());
                logger.debug("righe template 2 non sufficienti?");
              }
            } // end if tipo = Ispezione visiva
            else
              if (obj.getTipo().equals("C")) { // campioni
                rowTab2.getCell(12).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                rowTab2.getCell(13).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                rowTab2.getCell(14).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                //test
                rowTab2.getCell(25).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                rowTab2.getCell(26).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                rowTab2.getCell(27).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
                rowTab2.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni().intValue() : 0);
                rowTab2.getCell(29).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
              }
              else {  // tipo = Trappolaggio
                rowTab2.getCell(16).setCellValue((obj.getCodiceTrappola()!=null)?obj.getCodiceTrappola():"");
                rowTab2.getCell(17).setCellValue((obj.getTipologiaTrappola()!=null)?obj.getTipologiaTrappola():"");
                // 18 - EU trap category
                rowTab2.getCell(19).setCellValue((obj.getNumPiazzamento()!=null)?obj.getNumPiazzamento():0);
                Integer numTrap = 0;
                if (obj.getNumPiazzamento()!=null && obj.getNumPiazzamento()>0) {
                  rowTab2.getCell(20).setCellValue(obj.getNumOrePiazzTrappole()!=null ? obj.getNumOrePiazzTrappole():0);
                  numTrap += obj.getNumPiazzamento();
                }
                if (obj.getNumRicSostTrappole()!=null && obj.getNumRicSostTrappole()>0) {
                  rowTab2.getCell(21).setCellValue((obj.getNumOreRicSostTrappole()!=null)?obj.getNumOreRicSostTrappole():0);
                  numTrap += obj.getNumRicSostTrappole();
                }
                if (obj.getNumRimozioneTrappole()!=null && obj.getNumRimozioneTrappole()>0) {
                  rowTab2.getCell(22).setCellValue((obj.getNumOreRimozioneTrappole()!=null)?obj.getNumOreRimozioneTrappole():0);
                  numTrap += obj.getNumRimozioneTrappole();
                }
                // 23 - N° Total Hours
                // 24 - RESULT OF TRAPPING
                //rowTab2.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni().intValue() : 0);
              } // end if tipo = trappolaggio
            // Campo aggiuntivo rispetto al template richiesto da Regione (G. Mason e C. Morone)
            rowTab2.createCell(35).setCellValue((obj.getNomeLatinoSpecie()!=null)?obj.getNomeLatinoSpecie():"");
            count2++;
          }
        } // if organismo nocivo corrisponde
        Row lastRow = sheetTab1.getRow(sheetTab1.getLastRowNum());
        lastRow.getCell(8).setCellFormula("SUM(I6:I"+sheetTab1.getLastRowNum()+")");
        lastRow.getCell(9).setCellFormula("SUM(J6:J"+sheetTab1.getLastRowNum()+")");
        lastRow.getCell(10).setCellFormula("SUM(K6:K"+sheetTab1.getLastRowNum()+")");
        lastRow.getCell(12).setCellFormula("SUM(M6:M"+sheetTab1.getLastRowNum()+")");
        lastRow.getCell(15).setCellFormula("SUM(P6:P"+sheetTab1.getLastRowNum()+")");
        lastRow.getCell(19).setCellFormula("SUM(T6:T"+sheetTab1.getLastRowNum()+")");
        lastRow.getCell(23).setCellFormula("SUM(X6:X"+sheetTab1.getLastRowNum()+")");
        lastRow.getCell(28).setCellFormula("SUM(AC6:AC"+sheetTab1.getLastRowNum()+")");
        lastRow.getCell(30).setCellFormula("SUM(AE6:AE"+sheetTab1.getLastRowNum()+")");
        lastRow.getCell(31).setCellFormula("SUM(AF6:AF"+sheetTab1.getLastRowNum()+")");
        lastRow.getCell(32).setCellFormula("SUM(AG6:AG"+sheetTab1.getLastRowNum()+")");
        lastRow.getCell(37).setCellFormula("SUM(AL6:AL"+sheetTab1.getLastRowNum()+")");
        Row lastRow2 = sheetTab2.getRow(sheetTab2.getLastRowNum());
        lastRow2.getCell(8).setCellFormula("SUM(I7:I"+sheetTab2.getLastRowNum()+")");
        lastRow2.getCell(9).setCellFormula("SUM(J7:J"+sheetTab2.getLastRowNum()+")");
        lastRow2.getCell(10).setCellFormula("SUM(K7:K"+sheetTab2.getLastRowNum()+")");
        lastRow2.getCell(12).setCellFormula("SUM(M7:M"+sheetTab2.getLastRowNum()+")");
        lastRow2.getCell(15).setCellFormula("SUM(P7:P"+sheetTab2.getLastRowNum()+")");
        lastRow2.getCell(19).setCellFormula("SUM(T7:T"+sheetTab2.getLastRowNum()+")");
        lastRow2.getCell(23).setCellFormula("SUM(X7:X"+sheetTab2.getLastRowNum()+")");
        lastRow2.getCell(28).setCellFormula("SUM(AC7:AC"+sheetTab2.getLastRowNum()+")");
        lastRow2.getCell(30).setCellFormula("SUM(AE7:AE"+sheetTab2.getLastRowNum()+")");
        lastRow2.getCell(31).setCellFormula("SUM(AF7:AF"+sheetTab2.getLastRowNum()+")");
        lastRow2.getCell(32).setCellFormula("SUM(AG7:AG"+sheetTab2.getLastRowNum()+")");
      } // for lista rendicontazione

      logger.debug("FOGLIO 1: " + count);
      logger.debug("FOGLIO 2: " + count2);
      logger.debug("ON FILE: " + onr.getNomeCompleto());

      wb.getCreationHelper().createFormulaEvaluator().evaluateAll();

      //System.out.println("FOGLIO 1:" + count);
      // System.out.println("FOGLIO 2:" + count2);
      // System.out.println("ON:" + onr.getNomeCompleto());
      FileOutputStream fileOut = new FileOutputStream(dirPath+"/PIEMONTE-"+onr.getNomeCompleto()+".xlsx");
      wb.write(fileOut);
      fileOut.close();
    } // for lista organismi nocivi

    if(dirPath.exists()) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ZipOutputStream zos = new ZipOutputStream(baos);
      zip(dirPath, zos);
      zos.close();

      deleteDirectory(dirPath);

      // byte[] zip = zos.

      response.setContentType("application/zip");
      response.setHeader("Content-Disposition", "attachment; filename=output.zip");

      ServletOutputStream sos = response.getOutputStream();
      sos.write(baos.toByteArray());
      sos.flush();
    }
  }

  @RequestMapping(value = "/rendicontazione/generaExcelDemarcate")
  public void generaExcelAreeDemarcate(HttpSession session,Model model,HttpServletRequest request, HttpServletResponse response) throws InternalUnexpectedException, IOException
  {
    // 09/02/2022 - non usato attualmente, usa il vecchio template
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    //response.setContentType("application/xls");
    //carico il template
    ServletContext servletContext = request.getSession().getServletContext();
    String root = servletContext.getRealPath("/");
    String template = root + res.getString("conf.path") + File.separator + "template.xlsx";
    logger.debug("PATH FILE template ::"+template);
    File file = new File(template); 
    if(!file.exists()) {
      template = root + res.getString("conf.path") + File.separator + "web" + File.separator + "WEB-INF" + File.separator + "conf" + File.separator + "template.xlsx";
      file = new File(template);                      
    }
    RendicontazioneDTO dto = (RendicontazioneDTO) session.getAttribute("rendicontazione");
    //creo la cartella di appoggio    
    String pathUserHome = System.getProperty("user.home");
    File dirPath = new File(pathUserHome+"/appo");
    dirPath.mkdirs();
    List<OrganismoNocivoDTO> on = organismoNocivoEJB.findValidi();
    List<RendicontazioneDTO> listaObj = (List<RendicontazioneDTO>)session.getAttribute("listaRendicontazione");
    
    if (listaObj == null) {
      logger.debug("Metodo generaExcel listaRendicontazione nulla (la ricarico)");
      listaObj = rendicontazioneEJB.findByFilter(dto);
    }
    
   // if(dir) {
      for(OrganismoNocivoDTO onr: on) {
        if(onr.getEuro().equals("S")){
          FileInputStream fis = new FileInputStream(file);
          XSSFWorkbook wb = new XSSFWorkbook(fis); 
          XSSFSheet sheetSummary = wb.getSheetAt(0);
          XSSFSheet sheetTab1 = wb.getSheetAt(1);     // Official
          XSSFSheet sheetTab2 = wb.getSheetAt(2);     // Subcontractor
          XSSFRow row;
          XSSFRow rowTab2;
          //XSSFCell cell;
          //int rows; // No of rows
          //rows = sheetTab1.getPhysicalNumberOfRows();
          //int cols = 0; // No of columns
          //int tmp = 0;
          XSSFRow rowNomeOn = sheetSummary.getRow(2);
          XSSFRow rowLabel = sheetSummary.getRow(8);
          rowNomeOn.getCell(4).setCellValue(onr.getNomeCompleto());
          rowLabel.getCell(6).setCellValue("Data in Application "+ dto.getAnno()+" and after adjustment");
          rowLabel.getCell(8).setCellValue(" Annual budget "+ dto.getAnno()+" after adjustment");
          int count = 5;  // colonna start x foglio Official
          int count2 = 5; // colonna start x foglio Subcontractor
          // This trick ensures that we get the data properly even if it doesn't start from first few rows
          for (RendicontazioneDTO obj : listaObj) {
            
            if (obj == null)
              continue;
            
            row = sheetTab1.getRow(count);
            rowTab2 = sheetTab2.getRow(count2);
            
            if (obj.getOnSpecie()!=null && obj.getOnSpecie().toLowerCase().indexOf(onr.getSigla().toLowerCase()) != -1) {
              
              if (obj.getSubcontractor().equals("N")) {
                row.getCell(0).setCellValue(obj.getIspettore()!=null ? obj.getIspettore() : "");
                row.getCell(1).setCellValue(obj.getNumeroTrasferta()!=null ? obj.getNumeroTrasferta() : "");
                row.getCell(2).setCellValue((obj.getNumeroVerbale()!=null)?obj.getNumeroVerbale():"");
                row.getCell(3).setCellValue((obj.getDataInizioValidita()!=null)?df.format(obj.getDataInizioValidita()):"");
                row.getCell(4).setCellValue((obj.getDescTipoArea()!=null)?obj.getDescTipoArea():"");
                row.getCell(5).setCellValue(obj.getLatitudine()!=null ? Double.valueOf(obj.getLatitudine()) : 0);
                row.getCell(6).setCellValue(obj.getLongitudine() != null ? Double.valueOf(obj.getLongitudine()) : 0); 
                if (obj.getTipo().equals("I")) {
                  try {
                    row.getCell(7).setCellValue(obj.getSuperfice()!=null ? obj.getSuperfice() : 0);
                    row.getCell(8).setCellValue(obj.getNumeroPiante()!=null ? obj.getNumeroPiante() : 0);
                    row.getCell(9).setCellValue(1);
                    row.getCell(10).setCellValue((obj.getNumOreIspezione()!=null ? obj.getNumOreIspezione() : 0)/(obj.getNumOnRimborso()!=null ? obj.getNumOnRimborso() : 0));
                    row.getCell(11).setCellValue(obj.getCostoOrarioTecIspezione()!=null ? obj.getCostoOrarioTecIspezione() : 0);
                    //se ho campioni
                    row.getCell(13).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                    row.getCell(14).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                    row.getCell(15).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                    row.getCell(16).setCellValue("-");
                    row.getCell(17).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
                    row.getCell(18).setCellValue(obj.getCostoOrarioTecCampionamento()!=null ? obj.getCostoOrarioTecCampionamento() : 0);
                    //test
                    row.getCell(40).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                    row.getCell(41).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                    row.getCell(42).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
                    row.getCell(43).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                    row.getCell(44).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
                  }
                  catch (Exception e) {
                    logger.error("Errore: " + e.getMessage());
                    logger.debug("righe template 1 non sufficienti?");
                  }
                } // end if tipo = Ispezione visiva
                else
                if(obj.getTipo().equals("C")) { // campioni
                  row.getCell(13).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                  row.getCell(14).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                  row.getCell(15).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                  row.getCell(16).setCellValue("-");
                  row.getCell(17).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
                  row.getCell(18).setCellValue(obj.getCostoOrarioTecCampionamento()!=null ? obj.getCostoOrarioTecCampionamento() : 0);
                  //test
                  row.getCell(40).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                  row.getCell(41).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                  row.getCell(42).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
                  row.getCell(43).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                  row.getCell(44).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
                }
                else {  // tipo = Trappolaggio
                  row.getCell(21).setCellValue((obj.getCodiceSfr()!=null)?obj.getCodiceSfr():"");
                  row.getCell(22).setCellValue((obj.getTipologiaTrappola()!=null)?obj.getTipologiaTrappola():"");
                  row.getCell(23).setCellValue((obj.getNumPiazzamento()!=null)?obj.getNumPiazzamento():0);
                  row.getCell(24).setCellValue((obj.getCostoSingolaTrappola()!=null)?obj.getCostoSingolaTrappola():0);
                  row.getCell(30).setCellValue((obj.getNumOrePiazzTrappole()!=null)?obj.getNumOrePiazzTrappole():0);
                  row.getCell(31).setCellValue((obj.getCostoOrarioTecTrappolaggio()!=null)?obj.getCostoOrarioTecTrappolaggio():0);
                  row.getCell(33).setCellValue((obj.getNumOreRicSostTrappole()!=null)?obj.getNumOreRicSostTrappole():0);
                  row.getCell(34).setCellValue((obj.getCostoOrarioTecTrappolaggio()!=null)?obj.getCostoOrarioTecTrappolaggio():0);
                  row.getCell(36).setCellValue((obj.getNumOreRimozioneTrappole()!=null)?obj.getNumOreRimozioneTrappole():0);
                  row.getCell(37).setCellValue((obj.getCostoOrarioTecTrappolaggio()!=null)?obj.getCostoOrarioTecTrappolaggio():0);
                } // end if tipo = trappolaggio
                // Campo aggiuntivo rispetto al template richiesto da Regione (G. Mason e C. Morone)
                row.createCell(49).setCellValue((obj.getNomeLatinoSpecie()!=null)?obj.getNomeLatinoSpecie():"");
                count++;
              }
              else {
                // Subcontractor
                rowTab2.getCell(0).setCellValue(obj.getIspettore()!=null ? obj.getIspettore() : "");
                rowTab2.getCell(5).setCellValue(obj.getDescTipoArea()!=null ? obj.getDescTipoArea() : "");
                rowTab2.getCell(6).setCellValue(obj.getLatitudine()!=null ? Double.valueOf(obj.getLatitudine()) : 0);
                rowTab2.getCell(7).setCellValue(obj.getLongitudine() != null ? Double.valueOf(obj.getLongitudine()) : 0); 
                rowTab2.getCell(8).setCellValue(obj.getSuperfice()!=null ? obj.getSuperfice() : 0);
                rowTab2.getCell(9).setCellValue(obj.getNumeroPiante()!=null ? obj.getNumeroPiante() : 0);
                rowTab2.getCell(10).setCellValue(1);
                
                if (obj.getTipo().equals("I")) {
                  try {
                    rowTab2.getCell(11).setCellValue((obj.getNumOreIspezione()!=null ? obj.getNumOreIspezione() : 0)/(obj.getNumOnRimborso()!=null ? obj.getNumOnRimborso() : 0));
                    rowTab2.getCell(12).setCellValue(obj.getCostoOrarioTecIspezione()!=null ? obj.getCostoOrarioTecIspezione() : 0);
                    //se ho campioni
                    rowTab2.getCell(14).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                    rowTab2.getCell(15).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                    rowTab2.getCell(16).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                    //test
                    rowTab2.getCell(28).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                    rowTab2.getCell(29).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                    rowTab2.getCell(30).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
                    rowTab2.getCell(31).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                    rowTab2.getCell(32).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
                  }
                  catch (Exception e) {
                    logger.error("Errore: " + e.getMessage());
                    logger.debug("righe template 2 non sufficienti?");
                  }
                } // end if tipo = Ispezione visiva
                else
                if (obj.getTipo().equals("C")) { // campioni
                  rowTab2.getCell(14).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                  rowTab2.getCell(15).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                  rowTab2.getCell(16).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                  //test
                  rowTab2.getCell(28).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
                  rowTab2.getCell(29).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
                  rowTab2.getCell(30).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
                  rowTab2.getCell(31).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
                  rowTab2.getCell(32).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
                }
                else {  // tipo = Trappolaggio
                  rowTab2.getCell(19).setCellValue((obj.getCodiceSfr()!=null)?obj.getCodiceSfr():"");
                  rowTab2.getCell(20).setCellValue((obj.getTipologiaTrappola()!=null)?obj.getTipologiaTrappola():"");
                  //rowTab2.getCell(21).setCellValue((obj.getNumPiazzamento()!=null)?obj.getNumPiazzamento():0);
                  Integer numTrap = 0;
                  rowTab2.getCell(24).setCellValue("no");
                  rowTab2.getCell(25).setCellValue("no");
                  if (obj.getNumPiazzamento()!=null && obj.getNumPiazzamento()>0) {
                    rowTab2.getCell(11).setCellValue(obj.getNumOrePiazzTrappole()!=null ? obj.getNumOrePiazzTrappole():0);
                    numTrap += obj.getNumPiazzamento();
                    rowTab2.getCell(24).setCellValue("si");
                  }
                  if (obj.getNumRicSostTrappole()!=null && obj.getNumRicSostTrappole()>0) {
                    rowTab2.getCell(11).setCellValue((obj.getNumOreRicSostTrappole()!=null)?obj.getNumOreRicSostTrappole():0);
                    numTrap += obj.getNumRicSostTrappole();
                    rowTab2.getCell(25).setCellValue("si");
                  }
                  if (obj.getNumRimozioneTrappole()!=null && obj.getNumRimozioneTrappole()>0) {
                    rowTab2.getCell(11).setCellValue((obj.getNumOreRimozioneTrappole()!=null)?obj.getNumOreRimozioneTrappole():0);
                    numTrap += obj.getNumRimozioneTrappole();
                    rowTab2.getCell(25).setCellValue("si");
                  }
                  rowTab2.getCell(19).setCellValue("-");
                  rowTab2.getCell(20).setCellValue(obj.getTipologiaTrappola()!=null ? obj.getTipologiaTrappola() : "");
                  rowTab2.getCell(21).setCellValue(numTrap!=null ? numTrap : 0);
                  rowTab2.getCell(22).setCellValue("si");
                } // end if tipo = trappolaggio
                // Campo aggiuntivo rispetto al template richiesto da Regione (G. Mason e C. Morone)
                rowTab2.createCell(37).setCellValue((obj.getNomeLatinoSpecie()!=null)?obj.getNomeLatinoSpecie():"");
                count2++;
              }
            } // if organismo nocivo corrisponde
          } // for lista rendicontazione
          
          logger.debug("FOGLIO 1: " + count);
          logger.debug("FOGLIO 2: " + count2);
          logger.debug("ON FILE: " + onr.getNomeCompleto());

          wb.getCreationHelper().createFormulaEvaluator().evaluateAll(); 
          //System.out.println("FOGLIO 1:" + count);
         // System.out.println("FOGLIO 2:" + count2);
         // System.out.println("ON:" + onr.getNomeCompleto());
          FileOutputStream fileOut = new FileOutputStream(dirPath+"/PIEMONTE-"+onr.getNomeCompleto()+".xlsx");
          wb.write(fileOut);
          fileOut.close();
        } // if organismo nocivo con flag EURO = S
      } // for lista organismi nocivi
      if(dirPath.exists()) {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ZipOutputStream zos = new ZipOutputStream(baos);
          zip(dirPath, zos);
          zos.close();
          
          deleteDirectory(dirPath);
      
         // byte[] zip = zos.
              
          response.setContentType("application/zip");
          response.setHeader("Content-Disposition", "attachment; filename=output.zip");
           
          ServletOutputStream sos = response.getOutputStream();
          sos.write(baos.toByteArray());
          sos.flush();
      }
  }

  /**
   * per zippare la cartella
   * @param dirPath
   * @param zos
   * @throws IOException
   */
  private static final void zip(File dirPath, ZipOutputStream zos) throws IOException {
      File[] files = dirPath.listFiles();
      byte[] buffer = new byte[8192];
      int read = 0;
      for (int i = 0 ; i < files.length; i++) {
        FileInputStream in = new FileInputStream(files[i]);
        ZipEntry entry = new ZipEntry(files[i].getPath().substring(dirPath.getPath().length() + 1));
        zos.putNextEntry(entry);
          while (-1 != (read = in.read(buffer))) {
              zos.write(buffer, 0, read);
          }
        in.close();
      }
  }  

  public static boolean deleteDirectory(File path) {
    if(path.exists()) {
     File[] files = path.listFiles();
     for(int i=0; i<files.length; i++) {
           if(files[i].isDirectory()) {
               deleteDirectory(files[i]);
           }
           else {
               files[i].delete();
           }
     }
    }
    return(path.delete());
  }

  private static void copyRow(Workbook workbook, Sheet worksheet, int sourceRowNum, int destinationRowNum) {
    // Get the source / new row
    Row newRow = worksheet.getRow(destinationRowNum);
    Row sourceRow = worksheet.getRow(sourceRowNum);

    // If the row exist in destination, push down all rows by 1 else create a new row
    //if (newRow != null) {
        //worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
      worksheet.shiftRows(destinationRowNum, destinationRowNum, 1);
    //} else {
      newRow = worksheet.createRow(destinationRowNum);
    //}

    //CellStyle newCellStyle = null;
    // Loop through source columns to add to new row
    for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
        // Grab a copy of the old/new cell
        Cell oldCell = sourceRow.getCell(i);
        Cell newCell = newRow.createCell(i);

        // If the old cell is null jump to next cell
        if (oldCell == null) {
            newCell = null;
            continue;
        }
/*
 *  Modificato il 25/02/2022 (S.D.) - Non è necessario creare un nuovo stile, basta applicare quello della cella che si sta duplicando
 *  
        CellStyle newCellStyle = null;
        // Copy style from old cell and apply to new cell
        if (oldCell.getCellType() == Cell.CELL_TYPE_NUMERIC || oldCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
          newCellStyle = workbook.createCellStyle();
          newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
        }
        else
        {
            if (worksheet.getSheetName().toLowerCase().indexOf("contract") > -1) {
              if (i > 10 && i < 14) {
                newCellStyle = workbook.createCellStyle();
                newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
              }
            }
        }
        //logger.debug(oldCell.getCellStyle());
        if (newCellStyle == null)
          newCellStyle = oldCell.getCellStyle();

        newCell.setCellStyle(newCellStyle);
*/
        newCell.setCellStyle(oldCell.getCellStyle());
        
        if (oldCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
          newCell.setCellFormula(oldCell.getCellFormula().replaceAll("59", String.valueOf(newCell.getRowIndex()+1)));
          //logger.debug(oldCell.getCellFormula());
        }
        // If there is a cell comment, copy
        if (oldCell.getCellComment() != null) {
            newCell.setCellComment(oldCell.getCellComment());
        }

        // If there is a cell hyperlink, copy
        if (oldCell.getHyperlink() != null) {
            newCell.setHyperlink(oldCell.getHyperlink());
        }

        // Set the cell data type
        newCell.setCellType(oldCell.getCellType());
/*
        // Set the cell data value
        switch (oldCell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                newCell.setCellValue(oldCell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                newCell.setCellValue(oldCell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                newCell.setCellErrorValue(oldCell.getErrorCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                newCell.setCellFormula(oldCell.getCellFormula());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                newCell.setCellValue(oldCell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                newCell.setCellValue(oldCell.getRichStringCellValue());
                break;
        }
*/
    }

    // If there are are any merged regions in the source row, copy to new row
    for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
        CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
        if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
            CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                    (newRow.getRowNum() +
                            (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
                                    )),
                    cellRangeAddress.getFirstColumn(),
                    cellRangeAddress.getLastColumn());
            worksheet.addMergedRegion(newCellRangeAddress);
        }
    }
  }

  private void writeAreeIndenniOfficial(RendicontazioneDTO obj, Row row) {
    // 09/02/2022 - Metodo non usato attualmente, serve se si vuole utilizzare SXSSFWorkbook al posto di XSSFWorkbook
    if (row.getRowNum() > row.getSheet().getLastRowNum()-1) {
      copyRow(row.getSheet().getWorkbook(), row.getSheet(), 58, row.getRowNum());
      row = row.getSheet().getRow(row.getSheet().getLastRowNum()-1);
    }
    row.getCell(0).setCellValue(obj.getIspettore()!=null ? obj.getIspettore() : "");
    row.getCell(1).setCellValue(obj.getNumeroTrasferta()!=null ? obj.getNumeroTrasferta() : "");
    row.getCell(2).setCellValue((obj.getNumeroVerbale()!=null)?obj.getNumeroVerbale():"");
    row.getCell(3).setCellValue((obj.getDataInizioValidita()!=null)?df.format(obj.getDataInizioValidita()):"");
    row.getCell(4).setCellValue((obj.getTypologyOfLocation()!=null)?obj.getCodiceUfficiale().concat(" ").concat(obj.getTypologyOfLocation()):obj.getCodiceUfficiale()); // Evolutive gennaio 22
    // 5 other survey site ?
    row.getCell(6).setCellValue(obj.getLatitudine()!=null ? Double.valueOf(obj.getLatitudine()) : 0);
    row.getCell(7).setCellValue(obj.getLongitudine() != null ? Double.valueOf(obj.getLongitudine()) : 0); 
    if (obj.getTipo().equals("I")) {
      try {
        row.getCell(8).setCellValue(obj.getSuperfice()!=null ? obj.getSuperfice() : 0);
        //row.getCell(8).setCellValue(obj.getNumeroPiante()!=null ? obj.getNumeroPiante() : 0);
        row.getCell(9).setCellValue(1);
        row.getCell(10).setCellValue((obj.getNumOreIspezione()!=null ? obj.getNumOreIspezione() : 0)/(obj.getNumOnRimborso()!=null ? obj.getNumOnRimborso() : 0));
        // 11 RESULT OF EXAMINATION (menu a tendina)
        //row.getCell(11).setCellValue(obj.getCostoOrarioTecIspezione()!=null ? obj.getCostoOrarioTecIspezione() : 0);
        //se ho campioni
        row.getCell(12).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        row.getCell(13).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        row.getCell(14).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        //row.getCell(16).setCellValue("-");
        row.getCell(15).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
        //row.getCell(18).setCellValue(obj.getCostoOrarioTecCampionamento()!=null ? obj.getCostoOrarioTecCampionamento() : 0);

        // da valutare *****************************
        row.getCell(25).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        row.getCell(26).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        row.getCell(27).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
        row.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        row.getCell(29).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
        //
      }
      catch (Exception e) {
        logger.error("Errore: " + e.getMessage());
      }
    } // end if tipo = Ispezione visiva
    else
      if(obj.getTipo().equals("C")) { // campioni
        row.getCell(12).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        row.getCell(13).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        row.getCell(14).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        //row.getCell(16).setCellValue("-");
        row.getCell(15).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
        //row.getCell(18).setCellValue(obj.getCostoOrarioTecCampionamento()!=null ? obj.getCostoOrarioTecCampionamento() : 0);

        // da valutare *****************************
        row.getCell(25).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        row.getCell(26).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        row.getCell(27).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
        row.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        row.getCell(29).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
        //
      }
      else {  // tipo = Trappolaggio
        row.getCell(16).setCellValue((obj.getCodiceTrappola()!=null)?obj.getCodiceTrappola():"");
        row.getCell(17).setCellValue((obj.getTipologiaTrappola()!=null)?obj.getTipologiaTrappola():"");
        // 18 EU trap category ?
        row.getCell(19).setCellValue((obj.getNumPiazzamento()!=null)?obj.getNumPiazzamento():0);
        //row.getCell(24).setCellValue((obj.getCostoSingolaTrappola()!=null)?obj.getCostoSingolaTrappola():0);
        row.getCell(20).setCellValue((obj.getNumOrePiazzTrappole()!=null)?obj.getNumOrePiazzTrappole():0);
        //row.getCell(31).setCellValue((obj.getCostoOrarioTecTrappolaggio()!=null)?obj.getCostoOrarioTecTrappolaggio():0);
        row.getCell(21).setCellValue((obj.getNumOreRicSostTrappole()!=null)?obj.getNumOreRicSostTrappole():0);
        //row.getCell(34).setCellValue((obj.getCostoOrarioTecTrappolaggio()!=null)?obj.getCostoOrarioTecTrappolaggio():0);
        row.getCell(22).setCellValue((obj.getNumOreRimozioneTrappole()!=null)?obj.getNumOreRimozioneTrappole():0);
        //row.getCell(37).setCellValue((obj.getCostoOrarioTecTrappolaggio()!=null)?obj.getCostoOrarioTecTrappolaggio():0);
      } // end if tipo = trappolaggio

    // Campo aggiuntivo rispetto al template richiesto da Regione (G. Mason e C. Morone)
    row.createCell(38).setCellValue((obj.getNomeLatinoSpecie()!=null)?obj.getNomeLatinoSpecie():"");
  }

  //private void writeAreeIndenniSubcontractor(RendicontazioneDTO obj, SXSSFSheet sheet, int i) {
  private void writeAreeIndenniSubcontractor(RendicontazioneDTO obj, Row rowTab2) {
    // 09/02/2022 - Metodo non usato attualmente, serve se si vuole utilizzare SXSSFWorkbook al posto di XSSFWorkbook
    // Subcontractor
    //Row rowTab2 = sheet.createRow(i);
    rowTab2.getCell(1).setCellValue(obj.getIspettore()!=null ? obj.getIspettore() : "");
    rowTab2.getCell(2).setCellValue((obj.getNumeroVerbale()!=null)?obj.getNumeroVerbale():"");
    rowTab2.getCell(3).setCellValue((obj.getDataMissione()!=null)?df.format(obj.getDataMissione()):"");
    rowTab2.getCell(4).setCellValue((obj.getTypologyOfLocation()!=null)?obj.getCodiceUfficiale().concat(" ").concat(obj.getTypologyOfLocation()):obj.getCodiceUfficiale()); // Evolutive gennaio 22
    // 5 - other survey site (2.5.6 and 3.4.4  specification)
    rowTab2.getCell(6).setCellValue(obj.getLatitudine()!=null ? Double.valueOf(obj.getLatitudine()) : 0);
    rowTab2.getCell(7).setCellValue(obj.getLongitudine() != null ? Double.valueOf(obj.getLongitudine()) : 0); 
    rowTab2.getCell(8).setCellValue(obj.getSuperfice()!=null ? obj.getSuperfice() : 0);
    //rowTab2.getCell(9).setCellValue(obj.getNumeroPiante()!=null ? obj.getNumeroPiante() : 0);
    rowTab2.getCell(9).setCellValue(1);

    if (obj.getTipo().equals("I")) {
      try {
        rowTab2.getCell(10).setCellValue((obj.getNumOreIspezione()!=null ? obj.getNumOreIspezione() : 0)/(obj.getNumOnRimborso()!=null ? obj.getNumOnRimborso() : 0));
        // 11 - RESULT OF VISUAL EXAMINATION
        //rowTab2.getCell(12).setCellValue(obj.getCostoOrarioTecIspezione()!=null ? obj.getCostoOrarioTecIspezione() : 0);
        //se ho campioni
        rowTab2.getCell(12).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        rowTab2.getCell(13).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        rowTab2.getCell(14).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        rowTab2.getCell(15).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
        //test
        rowTab2.getCell(25).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        rowTab2.getCell(26).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        rowTab2.getCell(27).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
        rowTab2.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni().intValue() : 0);
        rowTab2.getCell(29).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
      }
      catch (Exception e) {
        logger.error("Errore: " + e.getMessage());
      }
    } // end if tipo = Ispezione visiva
    else
      if (obj.getTipo().equals("C")) { // campioni
        rowTab2.getCell(12).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        rowTab2.getCell(13).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        rowTab2.getCell(14).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        //test
        rowTab2.getCell(25).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        rowTab2.getCell(26).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        rowTab2.getCell(27).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
        rowTab2.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni().intValue() : 0);
        rowTab2.getCell(29).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
      }
      else {  // tipo = Trappolaggio
        rowTab2.getCell(16).setCellValue((obj.getCodiceTrappola()!=null)?obj.getCodiceTrappola():"");
        rowTab2.getCell(17).setCellValue((obj.getTipologiaTrappola()!=null)?obj.getTipologiaTrappola():"");
        // 18 - EU trap category
        rowTab2.getCell(19).setCellValue((obj.getNumPiazzamento()!=null)?obj.getNumPiazzamento():0);
        Integer numTrap = 0;
        if (obj.getNumPiazzamento()!=null && obj.getNumPiazzamento()>0) {
          rowTab2.getCell(20).setCellValue(obj.getNumOrePiazzTrappole()!=null ? obj.getNumOrePiazzTrappole():0);
          numTrap += obj.getNumPiazzamento();
        }
        if (obj.getNumRicSostTrappole()!=null && obj.getNumRicSostTrappole()>0) {
          rowTab2.getCell(21).setCellValue((obj.getNumOreRicSostTrappole()!=null)?obj.getNumOreRicSostTrappole():0);
          numTrap += obj.getNumRicSostTrappole();
        }
        if (obj.getNumRimozioneTrappole()!=null && obj.getNumRimozioneTrappole()>0) {
          rowTab2.getCell(22).setCellValue((obj.getNumOreRimozioneTrappole()!=null)?obj.getNumOreRimozioneTrappole():0);
          numTrap += obj.getNumRimozioneTrappole();
        }
        // 23 - N° Total Hours
        // 24 - RESULT OF TRAPPING
        //rowTab2.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni().intValue() : 0);
      } // end if tipo = trappolaggio
    // Campo aggiuntivo rispetto al template richiesto da Regione (G. Mason e C. Morone)
    rowTab2.createCell(35).setCellValue((obj.getNomeLatinoSpecie()!=null)?obj.getNomeLatinoSpecie():"");
  }

  private void writeAreeIndenniOfficial(RendicontazioneDTO obj, XSSFRow row, OrganismoNocivoDTO on) {

    if (row.getRowNum() > row.getSheet().getLastRowNum()-1) {
      copyRow(row.getSheet().getWorkbook(), row.getSheet(), 58, row.getRowNum());
      row = row.getSheet().getRow(row.getSheet().getLastRowNum()-1);
    }
    row.getCell(0).setCellValue(obj.getIspettore()!=null ? obj.getIspettore() : "");
    row.getCell(1).setCellValue(obj.getNumeroTrasferta()!=null ? obj.getNumeroTrasferta() : "");
    row.getCell(2).setCellValue((obj.getNumeroVerbale()!=null)?obj.getNumeroVerbale():"");
    row.getCell(3).setCellValue((obj.getDataInizioValidita()!=null)?df.format(obj.getDataInizioValidita()):"");
    row.getCell(4).setCellValue((obj.getTypologyOfLocation()!=null)?obj.getCodiceUfficiale().concat(" ").concat(obj.getTypologyOfLocation()):obj.getCodiceUfficiale()); // Evolutive gennaio 22
    // 5 other survey site ?
    row.getCell(6).setCellValue(obj.getLatitudine()!=null ? Double.valueOf(obj.getLatitudine()) : 0);
    row.getCell(7).setCellValue(obj.getLongitudine() != null ? Double.valueOf(obj.getLongitudine()) : 0); 
    if (obj.getTipo().equals("I")) {
      try {
        row.getCell(8).setCellValue(obj.getSuperfice()!=null ? obj.getSuperfice() : 0);
        //row.getCell(8).setCellValue(obj.getNumeroPiante()!=null ? obj.getNumeroPiante() : 0);
        row.getCell(9).setCellValue(1);
        row.getCell(10).setCellValue((obj.getNumOreIspezione()!=null ? obj.getNumOreIspezione() : 0)/(obj.getNumOnRimborso()!=null ? obj.getNumOnRimborso() : 0));
        // 11 RESULT OF EXAMINATION (menu a tendina)
        //row.getCell(11).setCellValue(obj.getCostoOrarioTecIspezione()!=null ? obj.getCostoOrarioTecIspezione() : 0);
        //se ho campioni
        row.getCell(12).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        row.getCell(13).setCellValue(obj.getIdCampionamento()!=null ? obj.getIdCampionamento().toString() : "");
        row.getCell(14).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        row.getCell(15).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
        if (obj.getOrganismoTrappola() != null && obj.getOrganismoTrappola().equals(on.getSigla())) {
          // trappole
          row.getCell(16).setCellValue((obj.getCodiceTrappola()!=null)?obj.getCodiceTrappola():"");
          row.getCell(17).setCellValue((obj.getTipologiaTrappola()!=null)?obj.getTipologiaTrappola():"");
          // 18 EU trap category ?
          row.getCell(19).setCellValue((obj.getNumPiazzamento()!=null)?obj.getNumPiazzamento():0);
          row.getCell(20).setCellValue((obj.getNumOrePiazzTrappole()!=null)?obj.getNumOrePiazzTrappole():0D);
          row.getCell(21).setCellValue((obj.getNumOreRicSostTrappole()!=null)?obj.getNumOreRicSostTrappole():0D);
          row.getCell(22).setCellValue((obj.getNumOreRimozioneTrappole()!=null)?obj.getNumOreRimozioneTrappole():0D);
          double totOreTrap = row.getCell(20).getNumericCellValue()+row.getCell(21).getNumericCellValue()+row.getCell(22).getNumericCellValue();
          row.getCell(23).setCellValue(totOreTrap);
        }
        if (obj.getOrganismiCampione() != null && obj.getOrganismiCampione().indexOf(on.getSigla()) != -1) {
          // test
          row.getCell(25).setCellValue(obj.getIdCampionamento()!=null ? obj.getIdCampionamento().toString() : "");
          row.getCell(26).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
          row.getCell(27).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
          row.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
          row.getCell(29).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
        }
      }
      catch (Exception e) {
        logger.error("Errore: " + e.getMessage());
      }
    } // end if tipo = Ispezione visiva
    else
      if(obj.getTipo().equals("C")) { // campioni
        row.getCell(12).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        row.getCell(13).setCellValue(obj.getIdCampionamento()!=null ? obj.getIdCampionamento().toString() : "");
        row.getCell(14).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        row.getCell(15).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
        //
        row.getCell(25).setCellValue(obj.getIdCampionamento()!=null ? obj.getIdCampionamento().toString() : "");
        row.getCell(26).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        row.getCell(27).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
        row.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        row.getCell(29).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
        //
      }
      else {  // tipo = Trappolaggio
        row.getCell(16).setCellValue((obj.getCodiceTrappola()!=null)?obj.getCodiceTrappola():"");
        row.getCell(17).setCellValue((obj.getTipologiaTrappola()!=null)?obj.getTipologiaTrappola():"");
        // 18 EU trap category ?
        row.getCell(19).setCellValue((obj.getNumPiazzamento()!=null)?obj.getNumPiazzamento():0);
        row.getCell(20).setCellValue((obj.getNumOrePiazzTrappole()!=null)?obj.getNumOrePiazzTrappole():0D);
        row.getCell(21).setCellValue((obj.getNumOreRicSostTrappole()!=null)?obj.getNumOreRicSostTrappole():0D);
        row.getCell(22).setCellValue((obj.getNumOreRimozioneTrappole()!=null)?obj.getNumOreRimozioneTrappole():0D);
        double totOreTrap = row.getCell(20).getNumericCellValue()+row.getCell(21).getNumericCellValue()+row.getCell(22).getNumericCellValue();
        row.getCell(23).setCellValue(totOreTrap);
      } // end if tipo = trappolaggio

    // Campo aggiuntivo rispetto al template richiesto da Regione (G. Mason e C. Morone)
    row.createCell(38).setCellValue((obj.getNomeLatinoSpecie()!=null)?obj.getNomeLatinoSpecie():"");
  }

  private void writeAreeIndenniSubcontractor(RendicontazioneDTO obj, XSSFRow rowTab2, OrganismoNocivoDTO on) {
    // Subcontractor
    if (rowTab2.getRowNum() > 58) {
      copyRow(rowTab2.getSheet().getWorkbook(), rowTab2.getSheet(), 58, rowTab2.getRowNum());
      rowTab2 = rowTab2.getSheet().getRow(rowTab2.getSheet().getLastRowNum()-1);
    }
    rowTab2.getCell(1).setCellValue(obj.getIspettore()!=null ? obj.getIspettore() : "");
    rowTab2.getCell(2).setCellValue((obj.getNumeroVerbale()!=null)?obj.getNumeroVerbale():"");
    rowTab2.getCell(3).setCellValue((obj.getDataMissione()!=null)?df.format(obj.getDataMissione()):"");
    rowTab2.getCell(4).setCellValue((obj.getTypologyOfLocation()!=null)?obj.getCodiceUfficiale().concat(" ").concat(obj.getTypologyOfLocation()):obj.getCodiceUfficiale()); // Evolutive gennaio 22
    // 5 - other survey site (2.5.6 and 3.4.4  specification)
    rowTab2.getCell(6).setCellValue(obj.getLatitudine()!=null ? Double.valueOf(obj.getLatitudine()) : 0);
    rowTab2.getCell(7).setCellValue(obj.getLongitudine() != null ? Double.valueOf(obj.getLongitudine()) : 0); 
    rowTab2.getCell(8).setCellValue(obj.getSuperfice()!=null ? obj.getSuperfice() : 0);
    //rowTab2.getCell(9).setCellValue(obj.getNumeroPiante()!=null ? obj.getNumeroPiante() : 0);
    rowTab2.getCell(9).setCellValue(1);

    if (obj.getTipo().equals("I")) {
      try {
        rowTab2.getCell(10).setCellValue((obj.getNumOreIspezione()!=null ? obj.getNumOreIspezione() : 0)/(obj.getNumOnRimborso()!=null ? obj.getNumOnRimborso() : 0));
        // 11 - RESULT OF VISUAL EXAMINATION
        //rowTab2.getCell(12).setCellValue(obj.getCostoOrarioTecIspezione()!=null ? obj.getCostoOrarioTecIspezione() : 0);
        //se ho campioni
        rowTab2.getCell(12).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        rowTab2.getCell(13).setCellValue(obj.getIdCampionamento()!=null ? obj.getIdCampionamento().toString() : "");
        rowTab2.getCell(14).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        rowTab2.getCell(15).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
        
        if (obj.getOrganismoTrappola() != null && obj.getOrganismoTrappola().equals(on.getSigla())) {
          // trappolaggi
          rowTab2.getCell(16).setCellValue((obj.getCodiceTrappola()!=null)?obj.getCodiceTrappola():"");
          rowTab2.getCell(17).setCellValue((obj.getTipologiaTrappola()!=null)?obj.getTipologiaTrappola():"");
          // 18 - EU trap category
          rowTab2.getCell(19).setCellValue((obj.getNumPiazzamento()!=null)?obj.getNumPiazzamento():0);
          Integer numTrap = 0;
          double totOreTrap = 0D;
          if (obj.getNumPiazzamento()!=null && obj.getNumPiazzamento()>0) {
            rowTab2.getCell(20).setCellValue(obj.getNumOrePiazzTrappole()!=null ? obj.getNumOrePiazzTrappole():0D);
            numTrap += obj.getNumPiazzamento();
            totOreTrap += rowTab2.getCell(20).getNumericCellValue();
          }
          if (obj.getNumRicSostTrappole()!=null && obj.getNumRicSostTrappole()>0) {
            rowTab2.getCell(21).setCellValue((obj.getNumOreRicSostTrappole()!=null)?obj.getNumOreRicSostTrappole():0D);
            numTrap += obj.getNumRicSostTrappole();
            totOreTrap += rowTab2.getCell(21).getNumericCellValue();
          }
          if (obj.getNumRimozioneTrappole()!=null && obj.getNumRimozioneTrappole()>0) {
            rowTab2.getCell(22).setCellValue((obj.getNumOreRimozioneTrappole()!=null)?obj.getNumOreRimozioneTrappole():0D);
            numTrap += obj.getNumRimozioneTrappole();
            totOreTrap += rowTab2.getCell(22).getNumericCellValue();
          }
          rowTab2.getCell(23).setCellValue(totOreTrap);
        } else {
          
        }
        
        //test
        if (obj.getOrganismiCampione() != null && obj.getOrganismiCampione().indexOf(on.getSigla()) != -1) {
          rowTab2.getCell(25).setCellValue(obj.getIdCampionamento()!=null ? obj.getIdCampionamento().toString() : "");
          rowTab2.getCell(26).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
          rowTab2.getCell(27).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
          rowTab2.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni().intValue() : 0);
          rowTab2.getCell(29).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
        }
      }
      catch (Exception e) {
        logger.error("Errore: " + e.getMessage());
      }
    } // end if tipo = Ispezione visiva
    else
      if (obj.getTipo().equals("C")) { // campioni
        rowTab2.getCell(12).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        rowTab2.getCell(13).setCellValue(obj.getIdCampionamento()!=null ? obj.getIdCampionamento().toString() : "");    // N
        rowTab2.getCell(14).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        rowTab2.getCell(15).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
        //test
        rowTab2.getCell(25).setCellValue(obj.getIdCampionamento()!=null ? obj.getIdCampionamento().toString() : "");     // Z
        rowTab2.getCell(26).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        rowTab2.getCell(27).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
        rowTab2.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni().intValue() : 0);
        rowTab2.getCell(29).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
      }
      else {  // tipo = Trappolaggio
        rowTab2.getCell(16).setCellValue((obj.getCodiceTrappola()!=null)?obj.getCodiceTrappola():"");
        rowTab2.getCell(17).setCellValue((obj.getTipologiaTrappola()!=null)?obj.getTipologiaTrappola():"");
        // 18 - EU trap category
        rowTab2.getCell(19).setCellValue((obj.getNumPiazzamento()!=null)?obj.getNumPiazzamento():0);
        Integer numTrap = 0;
        double totOreTrap = 0D;
        if (obj.getNumPiazzamento()!=null && obj.getNumPiazzamento()>0) {
          rowTab2.getCell(20).setCellValue(obj.getNumOrePiazzTrappole()!=null ? obj.getNumOrePiazzTrappole():0D);
          numTrap += obj.getNumPiazzamento();
          totOreTrap += rowTab2.getCell(20).getNumericCellValue();
        }
        if (obj.getNumRicSostTrappole()!=null && obj.getNumRicSostTrappole()>0) {
          rowTab2.getCell(21).setCellValue((obj.getNumOreRicSostTrappole()!=null)?obj.getNumOreRicSostTrappole():0D);
          numTrap += obj.getNumRicSostTrappole();
          totOreTrap += rowTab2.getCell(21).getNumericCellValue();
        }
        if (obj.getNumRimozioneTrappole()!=null && obj.getNumRimozioneTrappole()>0) {
          rowTab2.getCell(22).setCellValue((obj.getNumOreRimozioneTrappole()!=null)?obj.getNumOreRimozioneTrappole():0D);
          numTrap += obj.getNumRimozioneTrappole();
          totOreTrap += rowTab2.getCell(22).getNumericCellValue();
        }
        rowTab2.getCell(23).setCellValue(totOreTrap);
        // 23 - N° Total Hours
        // 24 - RESULT OF TRAPPING
        //rowTab2.getCell(28).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni().intValue() : 0);
      } // end if tipo = trappolaggio
    // Campo aggiuntivo rispetto al template richiesto da Regione (G. Mason e C. Morone)
    rowTab2.createCell(35).setCellValue((obj.getNomeLatinoSpecie()!=null)?obj.getNomeLatinoSpecie():"");
  }

  private void writeAreeDemarcateOfficial(RendicontazioneDTO obj, XSSFRow row) {
    
    if (row.getRowNum() > 58) {
      copyRow(row.getSheet().getWorkbook(), row.getSheet(), 58, row.getRowNum());
      row = row.getSheet().getRow(row.getSheet().getLastRowNum()-1);
    }
    row.getCell(0).setCellValue(obj.getIspettore()!=null ? obj.getIspettore() : "");
    row.getCell(1).setCellValue(obj.getNumeroTrasferta()!=null ? obj.getNumeroTrasferta() : "");
    row.getCell(2).setCellValue((obj.getNumeroVerbale()!=null)?obj.getNumeroVerbale():"");
    row.getCell(3).setCellValue((obj.getDataInizioValidita()!=null)?df.format(obj.getDataInizioValidita()):"");
    row.getCell(4).setCellValue((obj.getDescTipoArea()!=null)?obj.getDescTipoArea():"");
    row.getCell(5).setCellValue(obj.getLatitudine()!=null ? Double.valueOf(obj.getLatitudine()) : 0);
    row.getCell(6).setCellValue(obj.getLongitudine() != null ? Double.valueOf(obj.getLongitudine()) : 0); 

    if (obj.getTipo().equals("I")) {
      try {
        row.getCell(7).setCellValue(obj.getSuperfice()!=null ? obj.getSuperfice() : 0);
        row.getCell(8).setCellValue(obj.getNumeroPiante()!=null ? obj.getNumeroPiante() : 0);
        row.getCell(9).setCellValue(1);
        row.getCell(10).setCellValue((obj.getNumOreIspezione()!=null ? obj.getNumOreIspezione() : 0)/(obj.getNumOnRimborso()!=null ? obj.getNumOnRimborso() : 0));
        row.getCell(11).setCellValue(obj.getCostoOrarioTecIspezione()!=null ? obj.getCostoOrarioTecIspezione() : 0);
        //se ho campioni
        row.getCell(13).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        row.getCell(14).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        row.getCell(15).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        row.getCell(16).setCellValue("-");
        row.getCell(17).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
        row.getCell(18).setCellValue(obj.getCostoOrarioTecCampionamento()!=null ? obj.getCostoOrarioTecCampionamento() : 0);
        //test
        row.getCell(40).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        row.getCell(41).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        row.getCell(42).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
        row.getCell(43).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        row.getCell(44).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
      }
      catch (Exception e) {
        logger.error("Errore: " + e.getMessage());
      }
    } // end if tipo = Ispezione visiva
    else
    if(obj.getTipo().equals("C")) { // campioni
      row.getCell(13).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
      row.getCell(14).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
      row.getCell(15).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
      row.getCell(16).setCellValue("-");
      row.getCell(17).setCellValue(obj.getNumOreRaccoltaCampioni()!=null ? obj.getNumOreRaccoltaCampioni() : 0);
      row.getCell(18).setCellValue(obj.getCostoOrarioTecCampionamento()!=null ? obj.getCostoOrarioTecCampionamento() : 0);
      //test
      row.getCell(40).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
      row.getCell(41).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
      row.getCell(42).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
      row.getCell(43).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
      row.getCell(44).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
    }
    else {  // tipo = Trappolaggio
      row.getCell(21).setCellValue((obj.getCodiceSfr()!=null)?obj.getCodiceSfr():"");
      row.getCell(22).setCellValue((obj.getTipologiaTrappola()!=null)?obj.getTipologiaTrappola():"");
      row.getCell(23).setCellValue((obj.getNumPiazzamento()!=null)?obj.getNumPiazzamento():0);
      row.getCell(24).setCellValue((obj.getCostoSingolaTrappola()!=null)?obj.getCostoSingolaTrappola():0);
      row.getCell(30).setCellValue((obj.getNumOrePiazzTrappole()!=null)?obj.getNumOrePiazzTrappole():0);
      row.getCell(31).setCellValue((obj.getCostoOrarioTecTrappolaggio()!=null)?obj.getCostoOrarioTecTrappolaggio():0);
      row.getCell(33).setCellValue((obj.getNumOreRicSostTrappole()!=null)?obj.getNumOreRicSostTrappole():0);
      row.getCell(34).setCellValue((obj.getCostoOrarioTecTrappolaggio()!=null)?obj.getCostoOrarioTecTrappolaggio():0);
      row.getCell(36).setCellValue((obj.getNumOreRimozioneTrappole()!=null)?obj.getNumOreRimozioneTrappole():0);
      row.getCell(37).setCellValue((obj.getCostoOrarioTecTrappolaggio()!=null)?obj.getCostoOrarioTecTrappolaggio():0);
    } // end if tipo = trappolaggio
    // Campo aggiuntivo rispetto al template richiesto da Regione (G. Mason e C. Morone)
    row.createCell(49).setCellValue((obj.getNomeLatinoSpecie()!=null)?obj.getNomeLatinoSpecie():"");
  }

  private void writeAreeDemarcateSubcontractor(RendicontazioneDTO obj, XSSFRow rowTab2) {
    
    // Subcontractor
    if (rowTab2.getRowNum() > 58) {
      copyRow(rowTab2.getSheet().getWorkbook(), rowTab2.getSheet(), 58, rowTab2.getRowNum());
      rowTab2 = rowTab2.getSheet().getRow(rowTab2.getSheet().getLastRowNum()-1);
    }
    rowTab2.getCell(0).setCellValue(obj.getIspettore()!=null ? obj.getIspettore() : "");
    rowTab2.getCell(5).setCellValue(obj.getDescTipoArea()!=null ? obj.getDescTipoArea() : "");
    rowTab2.getCell(6).setCellValue(obj.getLatitudine()!=null ? Double.valueOf(obj.getLatitudine()) : 0);
    rowTab2.getCell(7).setCellValue(obj.getLongitudine() != null ? Double.valueOf(obj.getLongitudine()) : 0); 
    rowTab2.getCell(8).setCellValue(obj.getSuperfice()!=null ? obj.getSuperfice() : 0);
    rowTab2.getCell(9).setCellValue(obj.getNumeroPiante()!=null ? obj.getNumeroPiante() : 0);
    rowTab2.getCell(10).setCellValue(1);
    
    if (obj.getTipo().equals("I")) {
      try {
        rowTab2.getCell(11).setCellValue((obj.getNumOreIspezione()!=null ? obj.getNumOreIspezione() : 0)/(obj.getNumOnRimborso()!=null ? obj.getNumOnRimborso() : 0));
        rowTab2.getCell(12).setCellValue(obj.getCostoOrarioTecIspezione()!=null ? obj.getCostoOrarioTecIspezione() : 0);
        //se ho campioni
        rowTab2.getCell(14).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        rowTab2.getCell(15).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        rowTab2.getCell(16).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        //test
        rowTab2.getCell(28).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        rowTab2.getCell(29).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        rowTab2.getCell(30).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
        rowTab2.getCell(31).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        rowTab2.getCell(32).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
      }
      catch (Exception e) {
        logger.error("Errore: " + e.getMessage());
      }
    } // end if tipo = Ispezione visiva
    else
    if (obj.getTipo().equals("C")) { // campioni
      rowTab2.getCell(14).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
      rowTab2.getCell(15).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
      rowTab2.getCell(16).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
      //test
      rowTab2.getCell(28).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
      rowTab2.getCell(29).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
      rowTab2.getCell(30).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
      rowTab2.getCell(31).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
      rowTab2.getCell(32).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
    }
    else {  // tipo = Trappolaggio
      rowTab2.getCell(19).setCellValue((obj.getCodiceSfr()!=null)?obj.getCodiceSfr():"");
      rowTab2.getCell(20).setCellValue((obj.getTipologiaTrappola()!=null)?obj.getTipologiaTrappola():"");
      //rowTab2.getCell(21).setCellValue((obj.getNumPiazzamento()!=null)?obj.getNumPiazzamento():0);
      Integer numTrap = 0;
      rowTab2.getCell(24).setCellValue("no");
      rowTab2.getCell(25).setCellValue("no");
      if (obj.getNumPiazzamento()!=null && obj.getNumPiazzamento()>0) {
        rowTab2.getCell(11).setCellValue(obj.getNumOrePiazzTrappole()!=null ? obj.getNumOrePiazzTrappole():0);
        numTrap += obj.getNumPiazzamento();
        rowTab2.getCell(24).setCellValue("si");
      }
      if (obj.getNumRicSostTrappole()!=null && obj.getNumRicSostTrappole()>0) {
        rowTab2.getCell(11).setCellValue((obj.getNumOreRicSostTrappole()!=null)?obj.getNumOreRicSostTrappole():0);
        numTrap += obj.getNumRicSostTrappole();
        rowTab2.getCell(25).setCellValue("si");
      }
      if (obj.getNumRimozioneTrappole()!=null && obj.getNumRimozioneTrappole()>0) {
        rowTab2.getCell(11).setCellValue((obj.getNumOreRimozioneTrappole()!=null)?obj.getNumOreRimozioneTrappole():0);
        numTrap += obj.getNumRimozioneTrappole();
        rowTab2.getCell(25).setCellValue("si");
      }
      rowTab2.getCell(19).setCellValue("-");
      rowTab2.getCell(20).setCellValue(obj.getTipologiaTrappola()!=null ? obj.getTipologiaTrappola() : "");
      rowTab2.getCell(21).setCellValue(numTrap!=null ? numTrap : 0);
      rowTab2.getCell(22).setCellValue("si");
    } // end if tipo = trappolaggio
    // Campo aggiuntivo rispetto al template richiesto da Regione (G. Mason e C. Morone)
    rowTab2.createCell(37).setCellValue((obj.getNomeLatinoSpecie()!=null)?obj.getNomeLatinoSpecie():"");
  }

  private void writeAreeDemarcateSubcontractor(RendicontazioneDTO obj, SXSSFSheet sheet, int i) {
    
    CellStyle style2decimals = sheet.getWorkbook().createCellStyle();
    style2decimals.setDataFormat(sheet.getWorkbook().getCreationHelper().createDataFormat().getFormat("0.00"));
    CellStyle style3decimals = sheet.getWorkbook().createCellStyle();
    style3decimals.setDataFormat(sheet.getWorkbook().getCreationHelper().createDataFormat().getFormat("0.000"));

    // Subcontractor
    Row rowTab2 = sheet.createRow(i+1);
    rowTab2.createCell(0).setCellValue(obj.getIspettore()!=null ? obj.getIspettore() : "");
    rowTab2.createCell(5).setCellValue(obj.getDescTipoArea()!=null ? obj.getDescTipoArea() : "");
    rowTab2.createCell(6).setCellValue(obj.getLatitudine()!=null ? Double.valueOf(obj.getLatitudine()) : 0);
    rowTab2.createCell(7).setCellValue(obj.getLongitudine() != null ? Double.valueOf(obj.getLongitudine()) : 0); 
    rowTab2.createCell(8).setCellValue(obj.getSuperfice()!=null ? obj.getSuperfice() : 0);
    rowTab2.getCell(8).setCellStyle(style3decimals);
    rowTab2.createCell(9).setCellValue(obj.getNumeroPiante()!=null ? obj.getNumeroPiante() : 0);
    rowTab2.createCell(10).setCellValue(1);
    
    if (obj.getTipo().equals("I")) {
      try {
        rowTab2.createCell(11).setCellValue((obj.getNumOreIspezione()!=null ? obj.getNumOreIspezione() : 0)/(obj.getNumOnRimborso()!=null ? obj.getNumOnRimborso() : 0));
        rowTab2.getCell(11).setCellStyle(style2decimals);
        rowTab2.createCell(12).setCellValue(obj.getCostoOrarioTecIspezione()!=null ? obj.getCostoOrarioTecIspezione() : 0);
        rowTab2.getCell(12).setCellStyle(style2decimals);
        rowTab2.createCell(13).setCellFormula("L"+(i+1)+"*M"+(i+1));
        //rowTab2.createCell(13).setCellStyle(style2decimals);
        //rowTab2.getCell(13).setCellFormula("L"+(i+1)+"*M"+(i+1));

        //se ho campioni
        rowTab2.createCell(14).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        rowTab2.createCell(15).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        rowTab2.createCell(16).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        //test
        rowTab2.createCell(28).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
        rowTab2.createCell(29).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
        rowTab2.createCell(30).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
        rowTab2.createCell(31).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
        rowTab2.createCell(32).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
      }
      catch (Exception e) {
        logger.error("Errore: " + e.getMessage());
      }
    } // end if tipo = Ispezione visiva
    else
    if (obj.getTipo().equals("C")) { // campioni
      rowTab2.createCell(14).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
      rowTab2.createCell(15).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
      rowTab2.createCell(16).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
      //test
      rowTab2.createCell(28).setCellValue(obj.getCodCampione()!=null ? obj.getCodCampione() : "");
      rowTab2.createCell(29).setCellValue(obj.getTipoCampione()!=null ? obj.getTipoCampione() : "");
      rowTab2.createCell(30).setCellValue(obj.getTestLab()!=null ? obj.getTestLab() : "");
      rowTab2.createCell(31).setCellValue(obj.getNumCampioni()!=null ? obj.getNumCampioni() : 0);
      rowTab2.createCell(32).setCellValue(obj.getCostoLaboratorio()!=null ? obj.getCostoLaboratorio() : 0);
    }
    else {  // tipo = Trappolaggio
      rowTab2.createCell(19).setCellValue((obj.getCodiceSfr()!=null)?obj.getCodiceSfr():"");
      rowTab2.createCell(20).setCellValue((obj.getTipologiaTrappola()!=null)?obj.getTipologiaTrappola():"");
      //rowTab2.getCell(21).setCellValue((obj.getNumPiazzamento()!=null)?obj.getNumPiazzamento():0);
      Integer numTrap = 0;
      rowTab2.createCell(24).setCellValue("no");
      rowTab2.createCell(25).setCellValue("no");
      if (obj.getNumPiazzamento()!=null && obj.getNumPiazzamento()>0) {
        rowTab2.createCell(11).setCellValue(obj.getNumOrePiazzTrappole()!=null ? obj.getNumOrePiazzTrappole():0);
        numTrap += obj.getNumPiazzamento();
        rowTab2.createCell(24).setCellValue("si");
      }
      if (obj.getNumRicSostTrappole()!=null && obj.getNumRicSostTrappole()>0) {
        rowTab2.createCell(11).setCellValue((obj.getNumOreRicSostTrappole()!=null)?obj.getNumOreRicSostTrappole():0);
        numTrap += obj.getNumRicSostTrappole();
        rowTab2.createCell(25).setCellValue("si");
      }
      if (obj.getNumRimozioneTrappole()!=null && obj.getNumRimozioneTrappole()>0) {
        rowTab2.createCell(11).setCellValue((obj.getNumOreRimozioneTrappole()!=null)?obj.getNumOreRimozioneTrappole():0);
        numTrap += obj.getNumRimozioneTrappole();
        rowTab2.createCell(25).setCellValue("si");
      }
      rowTab2.createCell(19).setCellValue("-");
      rowTab2.createCell(20).setCellValue(obj.getTipologiaTrappola()!=null ? obj.getTipologiaTrappola() : "");
      rowTab2.createCell(21).setCellValue(numTrap!=null ? numTrap : 0);
      rowTab2.createCell(22).setCellValue("si");
    } // end if tipo = trappolaggio
    // Campo aggiuntivo rispetto al template richiesto da Regione (G. Mason e C. Morone)
    rowTab2.createCell(37).setCellValue((obj.getNomeLatinoSpecie()!=null)?obj.getNomeLatinoSpecie():"");
  }

}
