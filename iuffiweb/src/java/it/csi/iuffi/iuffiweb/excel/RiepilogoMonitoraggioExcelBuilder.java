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
import org.apache.poi.ss.util.CellUtil;

import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.RiepilogoMonitoraggioDTO;
import it.csi.iuffi.iuffiweb.view.AbstractXlsView;


@SuppressWarnings("unchecked")
public class RiepilogoMonitoraggioExcelBuilder extends AbstractXlsView {


  private SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
  private SimpleDateFormat sdfOra = new SimpleDateFormat("HH:mm");

  @Override
  public void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
    // Auto-generated method stub

    String filename = "tabellaCompatibilitaSpecie-ON.xls";
    // change the file name
    response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");

    List<RiepilogoMonitoraggioDTO> list = (List<RiepilogoMonitoraggioDTO>) model.get("elenco");
      
    // create excel xls sheet
    Sheet sheet = workbook.createSheet("Tabella di compatibilità Specie-ON");
    sheet.setDefaultColumnWidth(10);
    sheet.autoSizeColumn(0);
    sheet.autoSizeColumn(1);

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
    //style.setFont(font);

    Row titleRow = sheet.createRow(0);
    int numRow = 0;
    
    titleRow.createCell(0).setCellValue("Tabella di compatibilità Specie-Organismi nocivi nel periodo");
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
    sheet.addMergedRegion(new CellRangeAddress(numRow, numRow, (int) 0,(int) 4));
    sheet.setDefaultColumnStyle(numRow, style);

    // create header row
    Row header = sheet.createRow(sheet.getLastRowNum()+1);
    header.createCell(0).setCellValue("Pianta");
    header.getCell(0).setCellStyle(headerStyle);
    header.createCell(1).setCellValue("Matrice campione");
    header.getCell(1).setCellStyle(headerStyle);
    header.createCell(2).setCellValue("Gennaio");
    header.getCell(2).setCellStyle(headerStyle);
    CellUtil.setAlignment(header.getCell(2), workbook, CellStyle.ALIGN_CENTER);
    header.createCell(3).setCellValue("Febbraio");
    header.getCell(3).setCellStyle(headerStyle);
    CellUtil.setAlignment(header.getCell(3), workbook, CellStyle.ALIGN_CENTER);
    header.createCell(4).setCellValue("Marzo");
    header.getCell(4).setCellStyle(headerStyle);
    CellUtil.setAlignment(header.getCell(4), workbook, CellStyle.ALIGN_CENTER);
    header.createCell(5).setCellValue("Aprile");
    header.getCell(5).setCellStyle(headerStyle);
    CellUtil.setAlignment(header.getCell(5), workbook, CellStyle.ALIGN_CENTER);
    header.createCell(6).setCellValue("Maggio");
    header.getCell(6).setCellStyle(headerStyle);
    CellUtil.setAlignment(header.getCell(6), workbook, CellStyle.ALIGN_CENTER);
    header.createCell(7).setCellValue("Giugno");
    header.getCell(7).setCellStyle(headerStyle);
    CellUtil.setAlignment(header.getCell(7), workbook, CellStyle.ALIGN_CENTER);
    header.createCell(8).setCellValue("Luglio");
    header.getCell(8).setCellStyle(headerStyle);
    CellUtil.setAlignment(header.getCell(8), workbook, CellStyle.ALIGN_CENTER);
    header.createCell(9).setCellValue("Agosto");
    header.getCell(9).setCellStyle(headerStyle);
    CellUtil.setAlignment(header.getCell(9), workbook, CellStyle.ALIGN_CENTER);
    header.createCell(10).setCellValue("Settembre");
    header.getCell(10).setCellStyle(headerStyle);
    CellUtil.setAlignment(header.getCell(10), workbook, CellStyle.ALIGN_CENTER);
    header.createCell(11).setCellValue("Ottobre");
    header.getCell(11).setCellStyle(headerStyle);
    CellUtil.setAlignment(header.getCell(11), workbook, CellStyle.ALIGN_CENTER);
    header.createCell(12).setCellValue("Novembre");
    header.getCell(12).setCellStyle(headerStyle);
    CellUtil.setAlignment(header.getCell(12), workbook, CellStyle.ALIGN_CENTER);
    header.createCell(13).setCellValue("Dicembre");
    header.getCell(13).setCellStyle(headerStyle);
    CellUtil.setAlignment(header.getCell(13), workbook, CellStyle.ALIGN_CENTER);
    header.createCell(14).setCellValue("Organismi nocivi");
    header.getCell(14).setCellStyle(headerStyle);
    header.createCell(15).setCellValue("");
    header.getCell(15).setCellStyle(headerStyle);
    header.createCell(16).setCellValue("");
    header.getCell(16).setCellStyle(headerStyle);
    header.createCell(17).setCellValue("");
    header.getCell(17).setCellStyle(headerStyle);
    header.createCell(18).setCellValue("");
    header.getCell(18).setCellStyle(headerStyle);
    numRow++;
    sheet.addMergedRegion(new CellRangeAddress(numRow, numRow, (int) 14,(int) 18));
    CellUtil.setAlignment(header.getCell(14), workbook, CellStyle.ALIGN_CENTER);
    
    int rowCount = sheet.getLastRowNum()+1;
    
    for (RiepilogoMonitoraggioDTO riepilogo : list) {
      Row row = sheet.createRow(rowCount++);
      row.createCell(0).setCellValue(riepilogo.getNomeComuneSpecie());
      row.createCell(1).setCellValue(riepilogo.getDescTipoCampione());
      row.createCell(2).setCellValue(riepilogo.getGennaio());
      CellUtil.setAlignment(row.getCell(2), workbook, CellStyle.ALIGN_CENTER);
      row.createCell(3).setCellValue(riepilogo.getFebbraio());
      CellUtil.setAlignment(row.getCell(3), workbook, CellStyle.ALIGN_CENTER);
      row.createCell(4).setCellValue(riepilogo.getMarzo());
      CellUtil.setAlignment(row.getCell(4), workbook, CellStyle.ALIGN_CENTER);
      row.createCell(5).setCellValue(riepilogo.getAprile());
      CellUtil.setAlignment(row.getCell(5), workbook, CellStyle.ALIGN_CENTER);
      row.createCell(6).setCellValue(riepilogo.getMaggio());
      CellUtil.setAlignment(row.getCell(6), workbook, CellStyle.ALIGN_CENTER);
      row.createCell(7).setCellValue(riepilogo.getGiugno());
      CellUtil.setAlignment(row.getCell(7), workbook, CellStyle.ALIGN_CENTER);
      row.createCell(8).setCellValue(riepilogo.getLuglio());
      CellUtil.setAlignment(row.getCell(8), workbook, CellStyle.ALIGN_CENTER);
      row.createCell(9).setCellValue(riepilogo.getAgosto());
      CellUtil.setAlignment(row.getCell(9), workbook, CellStyle.ALIGN_CENTER);
      row.createCell(10).setCellValue(riepilogo.getSettembre());
      CellUtil.setAlignment(row.getCell(10), workbook, CellStyle.ALIGN_CENTER);
      row.createCell(11).setCellValue(riepilogo.getOttobre());
      CellUtil.setAlignment(row.getCell(11), workbook, CellStyle.ALIGN_CENTER);
      row.createCell(12).setCellValue(riepilogo.getNovembre());
      CellUtil.setAlignment(row.getCell(12), workbook, CellStyle.ALIGN_CENTER);
      row.createCell(13).setCellValue(riepilogo.getDicembre());
      CellUtil.setAlignment(row.getCell(13), workbook, CellStyle.ALIGN_CENTER);
      int col = 14;
      for (OrganismoNocivoDTO o : riepilogo.getOrganismiNocivi()) {
        row.createCell(col).setCellValue(o.getSigla());
        CellUtil.setAlignment(row.getCell(col), workbook, CellStyle.ALIGN_CENTER);
        col++;
      }
    }
    sheet.autoSizeColumn(0);
    sheet.autoSizeColumn(1);
    sheet.shiftRows(1,sheet.getLastRowNum(),1);
    //sheet.shiftRows(numRow+2,sheet.getLastRowNum(),1);
  }

}
