package it.csi.iuffi.iuffiweb.excel;


import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;

import it.csi.iuffi.iuffiweb.model.PianoMonitoraggioDTO;
import it.csi.iuffi.iuffiweb.model.PrevisioneMonitoraggioDTO;
import it.csi.iuffi.iuffiweb.view.AbstractXlsView;


@SuppressWarnings("unchecked")
public class PianoMonitoraggioDettaglioExcelBuilder extends AbstractXlsView {


  private SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
  //private SimpleDateFormat sdfOra = new SimpleDateFormat("HH:mm");

  @Override
  public void buildExcelDocument(Map<String, Object> model, Workbook workbook2, HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    // Auto-generated method stub

    HSSFWorkbook workbook = (HSSFWorkbook) workbook2;
    String filename = "pianoMonitoraggio_dettaglio.xls";
    // change the file name
    response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");

    List<PrevisioneMonitoraggioDTO> list = (List<PrevisioneMonitoraggioDTO>) model.get("list");
    PianoMonitoraggioDTO piano = (PianoMonitoraggioDTO) model.get("piano");
      
    // create excel xls sheet
    Sheet sheet = workbook.createSheet("Piano di monitoraggio");
    sheet.setDefaultColumnWidth(10);

    CellStyle headerStyle = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setFontName("Arial");
    headerStyle.setFont(font);
    //headerStyle.setBorderTop((short) 2);
    //headerStyle.setBorderBottom((short) 2);

    // create style for header cells
    CellStyle style = workbook.createCellStyle();
    
    style.setFillForegroundColor(HSSFColor.BLUE.index);
    font.setColor(HSSFColor.BLACK.index);
    style.setFont(font);

    // bordi
    style.setBorderBottom(CellStyle.BORDER_THIN);
    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderLeft(CellStyle.BORDER_THIN);
    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderRight(CellStyle.BORDER_THIN);
    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderTop(CellStyle.BORDER_THIN);
    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    
    Font titleFont = workbook.createFont();
    titleFont.setFontName("Arial");
    titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    headerStyle.setFont(titleFont);
    headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
    
    CellStyle avanzamentoStyle = workbook.createCellStyle();
    HSSFColor lightGray = setColor(workbook,(byte) 0xE0, (byte)0xE0,(byte) 0xE0);
    avanzamentoStyle.setFillForegroundColor(lightGray.getIndex());
    avanzamentoStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
    
    // bordi
    avanzamentoStyle.setBorderBottom(CellStyle.BORDER_THIN);
    avanzamentoStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    avanzamentoStyle.setBorderLeft(CellStyle.BORDER_THIN);
    avanzamentoStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    avanzamentoStyle.setBorderRight(CellStyle.BORDER_THIN);
    avanzamentoStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
    avanzamentoStyle.setBorderTop(CellStyle.BORDER_THIN);
    avanzamentoStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
    
    // bordi
    headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
    headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
    headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    headerStyle.setBorderRight(CellStyle.BORDER_THIN);
    headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
    headerStyle.setBorderTop(CellStyle.BORDER_THIN);
    headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
    
    Row titleRow = sheet.createRow(0);
    int numRow = 0;
    
    titleRow.createCell(0).setCellValue("Anno");
    titleRow.createCell(1).setCellValue(piano.getAnno());
    titleRow.createCell(2).setCellValue("");
    titleRow.getCell(0).setCellStyle(headerStyle);
    titleRow.getCell(1).setCellStyle(headerStyle);
    titleRow.getCell(2).setCellStyle(headerStyle);
    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), (int) 1,(int) 2));
    //sheet.setDefaultColumnStyle(numRow, style);
    CellUtil.setAlignment(titleRow.getCell(0), workbook, CellStyle.ALIGN_RIGHT);
    CellUtil.setAlignment(titleRow.getCell(1), workbook, CellStyle.ALIGN_LEFT);
    
    Row subtitleRow = sheet.createRow(1);
    subtitleRow.createCell(0).setCellValue("Versione");
    subtitleRow.createCell(1).setCellValue(piano.getVersione() + " del " + sdfData.format(piano.getDataInserimento()));
    subtitleRow.createCell(2).setCellValue("");
    subtitleRow.getCell(0).setCellStyle(headerStyle);
    subtitleRow.getCell(1).setCellStyle(headerStyle);
    subtitleRow.getCell(2).setCellStyle(headerStyle);
    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), (int) 1,(int) 2));
    CellUtil.setAlignment(subtitleRow.getCell(0), workbook, CellStyle.ALIGN_RIGHT);
    CellUtil.setAlignment(subtitleRow.getCell(1), workbook, CellStyle.ALIGN_LEFT);

    // create header row
    Row header = sheet.createRow(sheet.getLastRowNum()+1);
    header.createCell(0).setCellValue("ORGANISMO NOCIVO");
    header.getCell(0).setCellStyle(headerStyle);
    header.createCell(1).setCellValue("Stadio piano");
    header.getCell(1).setCellStyle(headerStyle);
    header.createCell(2).setCellValue("OFFICIAL");
    header.getCell(2).setCellStyle(headerStyle);
    header.createCell(3).setCellValue("");
    header.getCell(3).setCellStyle(headerStyle);
    header.createCell(4).setCellValue("");
    header.getCell(4).setCellStyle(headerStyle);
    header.createCell(5).setCellValue("");
    header.getCell(5).setCellStyle(headerStyle);
    header.createCell(6).setCellValue("");
    header.getCell(6).setCellStyle(headerStyle);
    header.createCell(7).setCellValue("");
    header.getCell(7).setCellStyle(headerStyle);
    header.createCell(8).setCellValue("");
    header.getCell(8).setCellStyle(headerStyle);
    header.createCell(9).setCellValue("");
    header.getCell(9).setCellStyle(headerStyle);
    header.createCell(10).setCellValue("");
    header.getCell(10).setCellStyle(headerStyle);
    header.createCell(11).setCellValue("");
    header.getCell(11).setCellStyle(headerStyle);
    header.createCell(12).setCellValue("");
    header.getCell(12).setCellStyle(headerStyle);
    header.createCell(13).setCellValue("");
    header.getCell(13).setCellStyle(headerStyle);
    header.createCell(14).setCellValue("");
    header.getCell(14).setCellStyle(headerStyle);
    header.createCell(15).setCellValue("");
    header.getCell(15).setCellStyle(headerStyle);
    
    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), (int) 2,(int) 8));
    header.createCell(9).setCellValue("SUBCONTRACTOR");
    header.getCell(9).setCellStyle(headerStyle);
    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), (int) 9,(int) 15));
    
    // create header row
    Row header2 = sheet.createRow(sheet.getLastRowNum()+1);
    header2.createCell(0).setCellValue("");
    header2.getCell(0).setCellStyle(headerStyle);
    header2.createCell(1).setCellValue("");
    header2.getCell(1).setCellStyle(headerStyle);
    header2.createCell(2).setCellValue("VISUAL");
    header2.getCell(2).setCellStyle(headerStyle);
    header2.createCell(3).setCellValue("");
    header2.getCell(3).setCellStyle(headerStyle);
    header2.createCell(4).setCellValue("CAMPIONI");
    header2.getCell(4).setCellStyle(headerStyle);
    header2.createCell(5).setCellValue("");
    header2.getCell(5).setCellStyle(headerStyle);
    header2.createCell(6).setCellValue("ANALISI");
    header2.getCell(6).setCellStyle(headerStyle);
    header2.createCell(7).setCellValue("TRAPPOLE");
    header2.getCell(7).setCellStyle(headerStyle);
    header2.createCell(8).setCellValue("");
    header2.getCell(8).setCellStyle(headerStyle);
    header2.createCell(9).setCellValue("VISUAL");
    header2.getCell(9).setCellStyle(headerStyle);
    header2.createCell(10).setCellValue("");
    header2.getCell(10).setCellStyle(headerStyle);
    header2.createCell(11).setCellValue("CAMPIONI");
    header2.getCell(11).setCellStyle(headerStyle);
    header2.createCell(12).setCellValue("");
    header2.getCell(12).setCellStyle(headerStyle);
    header2.createCell(13).setCellValue("ANALISI");
    header2.getCell(13).setCellStyle(headerStyle);
    header2.createCell(14).setCellValue("TRAPPOLE");
    header2.getCell(14).setCellStyle(headerStyle);
    header2.createCell(15).setCellValue("");
    header2.getCell(15).setCellStyle(headerStyle);

    // create header row
    Row header3 = sheet.createRow(sheet.getLastRowNum()+1);
    header3.createCell(0).setCellValue("");
    header3.getCell(0).setCellStyle(headerStyle);
    header3.createCell(1).setCellValue("");
    header3.getCell(1).setCellStyle(headerStyle);
    header3.createCell(2).setCellValue("n.");
    header3.getCell(2).setCellStyle(headerStyle);
    header3.createCell(3).setCellValue("ore");
    header3.getCell(3).setCellStyle(headerStyle);
    header3.createCell(4).setCellValue("n.");
    header3.getCell(4).setCellStyle(headerStyle);
    header3.createCell(5).setCellValue("ore");
    header3.getCell(5).setCellStyle(headerStyle);
    header3.createCell(6).setCellValue("n.");
    header3.getCell(6).setCellStyle(headerStyle);
    header3.createCell(7).setCellValue("n.");
    header3.getCell(7).setCellStyle(headerStyle);
    header3.createCell(8).setCellValue("ore");
    header3.getCell(8).setCellStyle(headerStyle);
    header3.createCell(9).setCellValue("n.");
    header3.getCell(9).setCellStyle(headerStyle);
    header3.createCell(10).setCellValue("ore");
    header3.getCell(10).setCellStyle(headerStyle);
    header3.createCell(11).setCellValue("n.");
    header3.getCell(11).setCellStyle(headerStyle);
    header3.createCell(12).setCellValue("ore");
    header3.getCell(12).setCellStyle(headerStyle);
    header3.createCell(13).setCellValue("n.");
    header3.getCell(13).setCellStyle(headerStyle);
    header3.createCell(14).setCellValue("n.");
    header3.getCell(14).setCellStyle(headerStyle);
    header3.createCell(15).setCellValue("ore");
    header3.getCell(15).setCellStyle(headerStyle);

    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum()-2, sheet.getLastRowNum(), (int) 0,(int) 0));
    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum()-2, sheet.getLastRowNum(), (int) 1,(int) 1));
    CellUtil.setAlignment(header.getCell(0), workbook, CellStyle.ALIGN_CENTER);
    CellUtil.setCellStyleProperty(header.getCell(0), workbook, CellUtil.VERTICAL_ALIGNMENT, CellStyle.VERTICAL_CENTER);   // ORGANISMO NOCIVO
    CellUtil.setCellStyleProperty(header.getCell(1), workbook, CellUtil.VERTICAL_ALIGNMENT, CellStyle.VERTICAL_CENTER);   // Stadio piano
    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum()-1, sheet.getLastRowNum()-1, (int) 2,(int) 3));   // VISUAL
    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum()-1, sheet.getLastRowNum()-1, (int) 4,(int) 5));   // CAMPIONI
    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum()-1, sheet.getLastRowNum()-1, (int) 7,(int) 8));   // TRAPPOLE
    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum()-1, sheet.getLastRowNum()-1, (int) 9,(int) 10));  // VISUAL
    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum()-1, sheet.getLastRowNum()-1, (int) 11,(int) 12)); // CAMPIONI
    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum()-1, sheet.getLastRowNum()-1, (int) 14,(int) 15)); // TRAPPOLE
    CellUtil.setAlignment(header.getCell(2), workbook, CellStyle.ALIGN_CENTER);     // OFFICIAL
    CellUtil.setAlignment(header.getCell(9), workbook, CellStyle.ALIGN_CENTER);     // SUBCONTRACTOR
    CellUtil.setAlignment(header2.getCell(2), workbook, CellStyle.ALIGN_CENTER);    // VISUAL
    CellUtil.setAlignment(header2.getCell(4), workbook, CellStyle.ALIGN_CENTER);    // CAMPIONI
    CellUtil.setAlignment(header2.getCell(7), workbook, CellStyle.ALIGN_CENTER);    // TRAPPOLE
    CellUtil.setAlignment(header2.getCell(9), workbook, CellStyle.ALIGN_CENTER);    // VISUAL
    CellUtil.setAlignment(header2.getCell(11), workbook, CellStyle.ALIGN_CENTER);   // CAMPIONI
    CellUtil.setAlignment(header2.getCell(14), workbook, CellStyle.ALIGN_CENTER);   // TRAPPOLE
    
    int rowCount = sheet.getLastRowNum()+1;
    
    int numVisualRegTot = 0;
    double oreVisualRegTot = 0;
    int numCampioniRegTot = 0;
    double oreCampioniRegTot = 0;
    int numAnalisiRegTot = 0;
    int numTrappoleRegTot = 0;
    double oreTrappoleRegTot = 0;
    int numVisualEstTot = 0;
    double oreVisualEstTot = 0;
    int numCampioniEstTot = 0;
    double oreCampioniEstTot = 0;
    int numAnalisiEstTot = 0;
    int numTrappoleEstTot = 0;
    double oreTrappoleEstTot = 0;

    int numVisualAvRegTot = 0;
    double oreVisualAvRegTot = 0;
    int numCampioniAvRegTot = 0;
    double oreCampioniAvRegTot = 0;
    int numAnalisiAvRegTot = 0;
    int numTrappoleAvRegTot = 0;
    double oreTrappoleAvRegTot = 0;
    int numVisualAvEstTot = 0;
    double oreVisualAvEstTot = 0;
    int numCampioniAvEstTot = 0;
    double oreCampioniAvEstTot = 0;
    int numAnalisiAvEstTot = 0;
    int numTrappoleAvEstTot = 0;
    double oreTrappoleAvEstTot = 0;

    for (PrevisioneMonitoraggioDTO previsione : list) {
      Row row = sheet.createRow(rowCount++);
      row.createCell(0).setCellValue(previsione.getNomeCompleto());
      row.getCell(0).setCellStyle(style);
      row.createCell(1).setCellValue("Preventivo");
      row.getCell(1).setCellStyle(style);
      row.createCell(2).setCellValue(previsione.getNumVisualReg());
      row.getCell(2).setCellStyle(style);
      row.createCell(3).setCellValue(previsione.getOreVisualReg().doubleValue());
      row.getCell(3).setCellStyle(style);
      row.createCell(4).setCellValue(previsione.getNumCampioniReg());
      row.getCell(4).setCellStyle(style);
      row.createCell(5).setCellValue(previsione.getOreCampioniReg().doubleValue());
      row.getCell(5).setCellStyle(style);
      row.createCell(6).setCellValue(previsione.getNumAnalisiReg());
      row.getCell(6).setCellStyle(style);
      row.createCell(7).setCellValue(previsione.getNumTrappoleReg());
      row.getCell(7).setCellStyle(style);
      row.createCell(8).setCellValue(previsione.getOreTrappoleReg().doubleValue());
      row.getCell(8).setCellStyle(style);
      row.createCell(9).setCellValue(previsione.getNumVisualEst());
      row.getCell(9).setCellStyle(style);
      row.createCell(10).setCellValue(previsione.getOreVisualEst().doubleValue());
      row.getCell(10).setCellStyle(style);
      row.createCell(11).setCellValue(previsione.getNumCampioniEst());
      row.getCell(11).setCellStyle(style);
      row.createCell(12).setCellValue(previsione.getOreCampioniEst().doubleValue());
      row.getCell(12).setCellStyle(style);
      row.createCell(13).setCellValue(previsione.getNumAnalisiEst());
      row.getCell(13).setCellStyle(style);
      row.createCell(14).setCellValue(previsione.getNumTrappoleEst());
      row.getCell(14).setCellStyle(style);
      row.createCell(15).setCellValue(previsione.getOreTrappoleEst().doubleValue());
      row.getCell(15).setCellStyle(style);
      // Sommatorie
      numVisualRegTot += previsione.getNumVisualReg();
      oreVisualRegTot += previsione.getOreVisualReg().doubleValue();
      numCampioniRegTot += previsione.getNumCampioniReg();
      oreCampioniRegTot += previsione.getOreCampioniReg().doubleValue();
      numAnalisiRegTot += previsione.getNumAnalisiReg();
      numTrappoleRegTot += previsione.getNumTrappoleReg();
      oreTrappoleRegTot += previsione.getOreTrappoleReg().doubleValue();
      numVisualEstTot += previsione.getNumVisualEst();
      oreVisualEstTot += previsione.getOreVisualEst().doubleValue();
      numCampioniEstTot += previsione.getNumCampioniEst();
      oreCampioniEstTot += previsione.getOreCampioniEst().doubleValue();
      numAnalisiEstTot += previsione.getNumAnalisiEst();
      numTrappoleEstTot += previsione.getNumTrappoleEst();
      oreTrappoleEstTot += previsione.getOreTrappoleEst().doubleValue();
      //
      Row row2 = sheet.createRow(rowCount++);
      row2.createCell(0).setCellValue("");
      row2.getCell(0).setCellStyle(style);
      row2.createCell(1).setCellValue("Avanzamento");
      row2.getCell(1).setCellStyle(style);
      
      row2.createCell(2).setCellValue(previsione.getNumVisualAvReg());
      row2.getCell(2).setCellStyle(avanzamentoStyle);
      row2.createCell(3).setCellValue(previsione.getOreVisualAvReg().doubleValue());
      row2.getCell(3).setCellStyle(avanzamentoStyle);
      row2.createCell(4).setCellValue(previsione.getNumCampioniAvReg());
      row2.getCell(4).setCellStyle(avanzamentoStyle);
      row2.createCell(5).setCellValue(previsione.getOreCampioniAvReg().doubleValue());
      row2.getCell(5).setCellStyle(avanzamentoStyle);
      row2.createCell(6).setCellValue(previsione.getNumAnalisiAvReg());
      row2.getCell(6).setCellStyle(avanzamentoStyle);
      row2.createCell(7).setCellValue(previsione.getNumTrappoleAvReg());
      row2.getCell(7).setCellStyle(avanzamentoStyle);
      row2.createCell(8).setCellValue(previsione.getOreTrappoleAvReg().doubleValue());
      row2.getCell(8).setCellStyle(avanzamentoStyle);
      row2.createCell(9).setCellValue(previsione.getNumVisualAvEst());
      row2.getCell(9).setCellStyle(avanzamentoStyle);
      row2.createCell(10).setCellValue(previsione.getOreVisualAvEst().doubleValue());
      row2.getCell(10).setCellStyle(avanzamentoStyle);
      row2.createCell(11).setCellValue(previsione.getNumCampioniAvEst());
      row2.getCell(11).setCellStyle(avanzamentoStyle);
      row2.createCell(12).setCellValue(previsione.getOreCampioniAvEst().doubleValue());
      row2.getCell(12).setCellStyle(avanzamentoStyle);
      row2.createCell(13).setCellValue(previsione.getNumAnalisiAvEst());
      row2.getCell(13).setCellStyle(avanzamentoStyle);
      row2.createCell(14).setCellValue(previsione.getNumTrappoleAvEst());
      row2.getCell(14).setCellStyle(avanzamentoStyle);
      row2.createCell(15).setCellValue(previsione.getOreTrappoleAvEst().doubleValue());
      row2.getCell(15).setCellStyle(avanzamentoStyle);
      // Sommatorie
      numVisualAvRegTot += previsione.getNumVisualAvReg();
      oreVisualAvRegTot += previsione.getOreVisualAvReg().doubleValue();
      numCampioniAvRegTot += previsione.getNumCampioniAvReg();
      oreCampioniAvRegTot += previsione.getOreCampioniAvReg().doubleValue();
      numAnalisiAvRegTot += previsione.getNumAnalisiAvReg();
      numTrappoleAvRegTot += previsione.getNumTrappoleAvReg();
      oreTrappoleAvRegTot += previsione.getOreTrappoleAvReg().doubleValue();
      numVisualAvEstTot += previsione.getNumVisualAvEst();
      oreVisualAvEstTot += previsione.getOreVisualAvEst().doubleValue();
      numCampioniAvEstTot += previsione.getNumCampioniAvEst();
      oreCampioniAvEstTot += previsione.getOreCampioniAvEst().doubleValue();
      numAnalisiAvEstTot += previsione.getNumAnalisiAvEst();
      numTrappoleAvEstTot += previsione.getNumTrappoleAvEst();
      oreTrappoleAvEstTot += previsione.getOreTrappoleAvEst().doubleValue();
      //
      sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum()-1, sheet.getLastRowNum(), (int) 0,(int) 0));     // merge organismo nocivo su 2 righe
      CellUtil.setCellStyleProperty(row.getCell(0), workbook, CellUtil.VERTICAL_ALIGNMENT, CellStyle.VERTICAL_CENTER);  // allineamento verticale organismo nocivo
    }

    // Prima riga dei totali (preventivo)
    Row rowTot = sheet.createRow(rowCount++);
    rowTot.createCell(0).setCellValue("TOTALE");
    rowTot.getCell(0).setCellStyle(headerStyle);
    rowTot.createCell(1).setCellValue("Preventivo");
    rowTot.getCell(1).setCellStyle(headerStyle);
    rowTot.createCell(2).setCellValue(numVisualRegTot);
    rowTot.getCell(2).setCellStyle(headerStyle);
    rowTot.createCell(3).setCellValue(oreVisualRegTot);
    rowTot.getCell(3).setCellStyle(headerStyle);
    rowTot.createCell(4).setCellValue(numCampioniRegTot);
    rowTot.getCell(4).setCellStyle(headerStyle);
    rowTot.createCell(5).setCellValue(oreCampioniRegTot);
    rowTot.getCell(5).setCellStyle(headerStyle);
    rowTot.createCell(6).setCellValue(numAnalisiRegTot);
    rowTot.getCell(6).setCellStyle(headerStyle);
    rowTot.createCell(7).setCellValue(numTrappoleRegTot);
    rowTot.getCell(7).setCellStyle(headerStyle);
    rowTot.createCell(8).setCellValue(oreTrappoleRegTot);
    rowTot.getCell(8).setCellStyle(headerStyle);
    rowTot.createCell(9).setCellValue(numVisualEstTot);
    rowTot.getCell(9).setCellStyle(headerStyle);
    rowTot.createCell(10).setCellValue(oreVisualEstTot);
    rowTot.getCell(10).setCellStyle(headerStyle);
    rowTot.createCell(11).setCellValue(numCampioniEstTot);
    rowTot.getCell(11).setCellStyle(headerStyle);
    rowTot.createCell(12).setCellValue(oreCampioniEstTot);
    rowTot.getCell(12).setCellStyle(headerStyle);
    rowTot.createCell(13).setCellValue(numAnalisiEstTot);
    rowTot.getCell(13).setCellStyle(headerStyle);
    rowTot.createCell(14).setCellValue(numTrappoleEstTot);
    rowTot.getCell(14).setCellStyle(headerStyle);
    rowTot.createCell(15).setCellValue(oreTrappoleEstTot);
    rowTot.getCell(15).setCellStyle(headerStyle);
    
    // seconda riga totali (avanzamento)
    Row rowTot2 = sheet.createRow(rowCount++);
    rowTot2.createCell(0).setCellValue("");
    rowTot2.getCell(0).setCellStyle(headerStyle);
    rowTot2.createCell(1).setCellValue("Avanzamento");
    rowTot2.getCell(1).setCellStyle(headerStyle);
    rowTot2.createCell(2).setCellValue(numVisualAvRegTot);
    rowTot2.getCell(2).setCellStyle(headerStyle);
    rowTot2.createCell(3).setCellValue(oreVisualAvRegTot);
    rowTot2.getCell(3).setCellStyle(headerStyle);
    rowTot2.createCell(4).setCellValue(numCampioniAvRegTot);
    rowTot2.getCell(4).setCellStyle(headerStyle);
    rowTot2.createCell(5).setCellValue(oreCampioniAvRegTot);
    rowTot2.getCell(5).setCellStyle(headerStyle);
    rowTot2.createCell(6).setCellValue(numAnalisiAvRegTot);
    rowTot2.getCell(6).setCellStyle(headerStyle);
    rowTot2.createCell(7).setCellValue(numTrappoleAvRegTot);
    rowTot2.getCell(7).setCellStyle(headerStyle);
    rowTot2.createCell(8).setCellValue(oreTrappoleAvRegTot);
    rowTot2.getCell(8).setCellStyle(headerStyle);
    rowTot2.createCell(9).setCellValue(numVisualAvEstTot);
    rowTot2.getCell(9).setCellStyle(headerStyle);
    rowTot2.createCell(10).setCellValue(oreVisualAvEstTot);
    rowTot2.getCell(10).setCellStyle(headerStyle);
    rowTot2.createCell(11).setCellValue(numCampioniAvEstTot);
    rowTot2.getCell(11).setCellStyle(headerStyle);
    rowTot2.createCell(12).setCellValue(oreCampioniAvEstTot);
    rowTot2.getCell(12).setCellStyle(headerStyle);
    rowTot2.createCell(13).setCellValue(numAnalisiAvEstTot);
    rowTot2.getCell(13).setCellStyle(headerStyle);
    rowTot2.createCell(14).setCellValue(numTrappoleAvEstTot);
    rowTot2.getCell(14).setCellStyle(headerStyle);
    rowTot2.createCell(15).setCellValue(oreTrappoleAvEstTot);
    rowTot2.getCell(15).setCellStyle(headerStyle);
    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum()-1, sheet.getLastRowNum(), (int) 0,(int) 0));       // merge totale su 2 righe
    CellUtil.setCellStyleProperty(rowTot.getCell(0), workbook, CellUtil.VERTICAL_ALIGNMENT, CellStyle.VERTICAL_CENTER); // allineamento verticale totale
    
    // Autosize all columns
    Row row = sheet.getRow(sheet.getFirstRowNum());
    Iterator<Cell> cellIterator = row.cellIterator();
    while (cellIterator.hasNext()) {
        Cell cell = cellIterator.next();
        int columnIndex = cell.getColumnIndex();
        sheet.autoSizeColumn(columnIndex, true);
    }

    sheet.shiftRows(2,sheet.getLastRowNum(),1);
    sheet.shiftRows(numRow+2,sheet.getLastRowNum(),1);
  }

  public HSSFColor setColor(HSSFWorkbook workbook, byte r,byte g, byte b){
    HSSFPalette palette = workbook.getCustomPalette();
    HSSFColor hssfColor = null;
    try {
        hssfColor= palette.findColor(r, g, b); 
        if (hssfColor == null ){
            palette.setColorAtIndex(HSSFColor.LAVENDER.index, r, g,b);
            hssfColor = palette.getColor(HSSFColor.LAVENDER.index);
        }
    } catch (Exception e) {
        
    }
    return hssfColor;
  }

}
