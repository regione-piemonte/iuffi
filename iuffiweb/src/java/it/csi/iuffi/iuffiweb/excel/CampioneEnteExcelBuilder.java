package it.csi.iuffi.iuffiweb.excel;


import java.math.BigDecimal;
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

import it.csi.iuffi.iuffiweb.model.CampioneEnteDTO;
import it.csi.iuffi.iuffiweb.view.AbstractXlsView;


@SuppressWarnings("unchecked")
public class CampioneEnteExcelBuilder extends AbstractXlsView {


  private SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
  private SimpleDateFormat sdfOra = new SimpleDateFormat("HH:mm");

  @Override
  public void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
    // Auto-generated method stub

    String filename = "campioniEnte.xls";
    // change the file name
    response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");

    List<CampioneEnteDTO> campioniEnte = (List<CampioneEnteDTO>) model.get("elenco");
    //MissioneRequest mr = (MissioneRequest) model.get("missioneRequest");
      
    // create excel xls sheet
    Sheet sheet = workbook.createSheet("Lista Trappole Ente");
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
    
    titleRow.createCell(0).setCellValue("Lista Trappole Ente");
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
    header.createCell(0).setCellValue("Tipo Campione");
    header.getCell(0).setCellStyle(headerStyle);
    header.createCell(1).setCellValue("Ente");
    header.getCell(1).setCellStyle(headerStyle);
    header.createCell(2).setCellValue("Costo");
    header.getCell(2).setCellStyle(headerStyle);
    header.createCell(3).setCellValue("Data inizio validità");
    header.getCell(3).setCellStyle(headerStyle);
    header.createCell(4).setCellValue("Data fine validità");
    header.getCell(4).setCellStyle(headerStyle);


    int rowCount = sheet.getLastRowNum()+1;
    
    for(CampioneEnteDTO campioneEnte : campioniEnte) {
      Row row = sheet.createRow(rowCount++);
      row.createCell(0).setCellValue(campioneEnte.getDescrTipoCampione());
      row.createCell(1).setCellValue(campioneEnte.getDescrEnte());
      BigDecimal bd = campioneEnte.getCosto();
      double d = bd.doubleValue();
      row.createCell(2).setCellValue(d);
      if(campioneEnte.getDataInizioValidita() != null) {
        row.createCell(3).setCellValue(sdfData.format(campioneEnte.getDataInizioValidita()) + " " + sdfOra.format(campioneEnte.getDataInizioValidita()));
      }
      if(campioneEnte.getDataFineValidita() != null) {
        row.createCell(4).setCellValue(sdfData.format(campioneEnte.getDataFineValidita()) + " " + sdfOra.format(campioneEnte.getDataFineValidita()));
      }
    }
    sheet.shiftRows(1,sheet.getLastRowNum(),1);
    sheet.shiftRows(numRow+2,sheet.getLastRowNum(),1);
  }

}
