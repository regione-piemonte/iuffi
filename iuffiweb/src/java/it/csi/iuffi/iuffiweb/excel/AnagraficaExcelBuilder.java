package it.csi.iuffi.iuffiweb.excel;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.view.AbstractXlsView;


@SuppressWarnings("unchecked")
public class AnagraficaExcelBuilder extends AbstractXlsView {


  private SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
  private SimpleDateFormat sdfOra = new SimpleDateFormat("HH:mm");

  @Override
  public void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
    // Auto-generated method stub

    String filename = "anagrafica.xls";
    // change the file name
    response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");

    List<AnagraficaDTO> anagrafiche = (List<AnagraficaDTO>) model.get("elenco");
    //MissioneRequest mr = (MissioneRequest) model.get("missioneRequest");
      
    // create excel xls sheet
    Sheet sheet = workbook.createSheet("Lista Anagrafiche");
    sheet.setDefaultColumnWidth(20);

    CellStyle headerStyle = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setFontName("Arial");
    headerStyle.setFont(font);
    headerStyle.setBorderTop((short) 2);
    headerStyle.setBorderBottom((short) 2);

    // create style for header cells
    CellStyle style = workbook.createCellStyle();
    
    //style.setFillForegroundColor(HSSFColor.BLUE.index);
    //style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    font.setColor(HSSFColor.BLACK.index);
    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
    style.setFont(font);

    Row titleRow = sheet.createRow(0);
    String filter1 = "";
    int numRow = 0;
    
    titleRow.createCell(0).setCellValue("Lista Anagrafiche");
    titleRow.createCell(1).setCellValue("");
    titleRow.createCell(2).setCellValue("");
    titleRow.createCell(3).setCellValue("");
    titleRow.createCell(4).setCellValue("");
    titleRow.createCell(5).setCellValue("");
    titleRow.getCell(0).setCellStyle(headerStyle);
    titleRow.getCell(1).setCellStyle(headerStyle);
    titleRow.getCell(2).setCellStyle(headerStyle);
    titleRow.getCell(3).setCellStyle(headerStyle);
    titleRow.getCell(4).setCellStyle(headerStyle);
    sheet.addMergedRegion(new CellRangeAddress(numRow, numRow, (int) 0,(int) 2));
    //sheet.setDefaultColumnStyle(numRow, style);

    if (filter1.length() > 0) {
      Row subtitleRow2 = sheet.createRow(sheet.getLastRowNum()+1);
      subtitleRow2.createCell(0).setCellValue(filter1);
      subtitleRow2.createCell(1).setCellValue("");
      subtitleRow2.createCell(2).setCellValue("");
      subtitleRow2.createCell(3).setCellValue("");
      subtitleRow2.createCell(4).setCellValue("");
      subtitleRow2.createCell(5).setCellValue("");
      numRow++;
      sheet.addMergedRegion(new CellRangeAddress(numRow, numRow, (int) 0, (int) 5));
    }


    // create header row
    Row header = sheet.createRow(sheet.getLastRowNum()+1);
    header.createCell(0).setCellValue("Cognome");
    header.getCell(0).setCellStyle(headerStyle);
    header.createCell(1).setCellValue("Nome");
    header.getCell(1).setCellStyle(headerStyle);
    header.createCell(2).setCellValue("Codice Fiscale");
    header.getCell(2).setCellStyle(headerStyle);
    header.createCell(3).setCellValue("Ente Appartenenza");
    header.getCell(3).setCellStyle(headerStyle);
    header.createCell(4).setCellValue("Paga Oraria");
    header.getCell(4).setCellStyle(headerStyle);
    header.createCell(5).setCellValue("Collaboratore a Contratto");
    header.getCell(5).setCellStyle(headerStyle);
    header.createCell(6).setCellValue("Data Inizio Validità");
    header.getCell(6).setCellStyle(headerStyle);
    header.createCell(7).setCellValue("Data Fine Validità");
    header.getCell(7).setCellStyle(headerStyle);
   // header.createCell(8).setCellValue("Archiviato");
  //  header.getCell(8).setCellStyle(headerStyle);


    int rowCount = sheet.getLastRowNum()+1;
    
    for(AnagraficaDTO anagrafica : anagrafiche) {
      Row row = sheet.createRow(rowCount++);
      row.createCell(0).setCellValue(anagrafica.getCognome());
      row.createCell(1).setCellValue(anagrafica.getNome());
      row.createCell(2).setCellValue(anagrafica.getCfAnagraficaEst());
      row.createCell(3).setCellValue(anagrafica.getEnteAppartenenza());
      row.createCell(4).setCellValue(anagrafica.getPagaOraria());
      row.createCell(5).setCellValue(anagrafica.getSubcontractor());
      if(anagrafica.getDataInizioValidita() != null) {
        row.createCell(6).setCellValue(sdfData.format(anagrafica.getDataInizioValidita()) + " " + sdfOra.format(anagrafica.getDataInizioValidita()));
      }
      if(anagrafica.getDataFineValidita() != null) {
        row.createCell(7).setCellValue(sdfData.format(anagrafica.getDataFineValidita()) + " " + sdfOra.format(anagrafica.getDataFineValidita()));
      }
     // row.createCell(8).setCellValue(anagrafica.getFlagArchiviato());
    }
    sheet.shiftRows(1,sheet.getLastRowNum(),1);
    sheet.shiftRows(numRow+2,sheet.getLastRowNum(),1);
  }

}
