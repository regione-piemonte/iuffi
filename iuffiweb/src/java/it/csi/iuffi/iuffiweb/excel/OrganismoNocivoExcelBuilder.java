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

import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;

import it.csi.iuffi.iuffiweb.view.AbstractXlsView;


@SuppressWarnings("unchecked")
public class OrganismoNocivoExcelBuilder extends AbstractXlsView {


  private SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
  private SimpleDateFormat sdfOra = new SimpleDateFormat("HH:mm");

  @Override
  public void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
    // Auto-generated method stub

    String filename = "organismiNocivi.xls";
    // change the file name
    response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");

    List<OrganismoNocivoDTO> organismiNocivi = (List<OrganismoNocivoDTO>) model.get("elenco");
    //MissioneRequest mr = (MissioneRequest) model.get("missioneRequest");
      
    // create excel xls sheet
    Sheet sheet = workbook.createSheet("Lista Organismi Nocivi");
    sheet.setDefaultColumnWidth(20);

    CellStyle headerStyle = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setFontName("Arial");
    headerStyle.setFont(font);
    headerStyle.setBorderTop((short) 2);
    headerStyle.setBorderBottom((short) 2);

    // create style for header cells
    CellStyle style = workbook.createCellStyle();
    
    style.setFillForegroundColor(HSSFColor.BLUE.index);
    //style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    font.setColor(HSSFColor.BLACK.index);
    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
    style.setFont(font);

    Row titleRow = sheet.createRow(0);
    String filter1 = "";
    int numRow = 0;
    
    titleRow.createCell(0).setCellValue("Lista Organismi Nocivi");
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
    sheet.addMergedRegion(new CellRangeAddress(numRow, numRow, (int) 0,(int) 5));
    sheet.setDefaultColumnStyle(numRow, style);
    


    if (filter1.length() > 0) {
      Row subtitleRow2 = sheet.createRow(sheet.getLastRowNum()+1);
      subtitleRow2.createCell(0).setCellValue(filter1);
      subtitleRow2.getCell(0).setCellStyle(style);
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
    header.createCell(0).setCellValue("Nome Latino");
    header.getCell(0).setCellStyle(headerStyle);
    header.createCell(1).setCellValue("Sigla");
    header.getCell(1).setCellStyle(headerStyle);
    header.createCell(2).setCellValue("Indagine Ufficiale");
    header.getCell(2).setCellStyle(headerStyle);
    header.createCell(3).setCellValue("Emergenza");
    header.getCell(3).setCellStyle(headerStyle);
    header.createCell(4).setCellValue("Data inizio validità");
    header.getCell(4).setCellStyle(headerStyle);
    header.createCell(5).setCellValue("Data fine validità");
    header.getCell(5).setCellStyle(headerStyle);


    int rowCount = sheet.getLastRowNum()+1;
    
    for(OrganismoNocivoDTO organismoNocivo : organismiNocivi) {
      Row row = sheet.createRow(rowCount++);
      row.createCell(0).setCellValue(organismoNocivo.getNomeLatino());
      row.createCell(1).setCellValue(organismoNocivo.getSigla());
      row.createCell(2).setCellValue(organismoNocivo.getEuro());
      row.createCell(3).setCellValue(organismoNocivo.getFlagEmergenza());
      if(organismoNocivo.getDataInizioValidita() != null) {
        row.createCell(4).setCellValue(sdfData.format(organismoNocivo.getDataInizioValidita()) + " " + sdfOra.format(organismoNocivo.getDataInizioValidita()));
      }
      if(organismoNocivo.getDataFineValidita() != null) {
        row.createCell(5).setCellValue(sdfData.format(organismoNocivo.getDataFineValidita()) + " " + sdfOra.format(organismoNocivo.getDataFineValidita()));
      }
    }
    sheet.shiftRows(1,sheet.getLastRowNum(),1);
    sheet.shiftRows(numRow+2,sheet.getLastRowNum(),1);
  }

}
