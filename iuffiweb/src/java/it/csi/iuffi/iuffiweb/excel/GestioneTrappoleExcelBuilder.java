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

import it.csi.iuffi.iuffiweb.model.TrappolaggioDTO;
import it.csi.iuffi.iuffiweb.view.AbstractXlsView;

@SuppressWarnings("unchecked")
public class GestioneTrappoleExcelBuilder extends AbstractXlsView {


  private SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
  //private SimpleDateFormat sdfOra = new SimpleDateFormat("HH:mm");
  


  @Override
  public void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
    // Auto-generated method stub

    String filename = "trappole.xls";
    // change the file name
    response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");

    List<TrappolaggioDTO> trappolaggi = (List<TrappolaggioDTO>) model.get("elenco");
    //MissioneRequest mr = (MissioneRequest) model.get("missioneRequest");
      
    // create excel xls sheet
    Sheet sheet = workbook.createSheet("Elenco trappole");
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

    CellStyle rowStyle = workbook.createCellStyle();
    Font rowFont = workbook.createFont();
    rowFont.setFontName("Arial");
    rowStyle.setFont(rowFont);
    
    Row titleRow = sheet.createRow(0);
    String filter1 = "";
    int numRow = 0;
    
    titleRow.createCell(0).setCellValue("Elenco trappole");
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
    int index = -1;
    header.createCell(++index).setCellValue("Id Trappolaggio");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Data");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Comune");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Tipologia area");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Ispettore");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Specie vegetale");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("ON");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Tipo Trappola");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Codice Trappola");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Tipo Operazione");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Id Visual");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Id Rilevazione");
    header.getCell(index).setCellStyle(headerStyle);

    int rowCount = sheet.getLastRowNum()+1;
    
    for(TrappolaggioDTO trappolaggio : trappolaggi) {
      Row row = sheet.createRow(rowCount++);
      index = -1;
      row.createCell(++index).setCellValue(trappolaggio.getIdTrappolaggio());
      row.getCell(index).setCellStyle(rowStyle);
      if (trappolaggio.getDataOraInizio() != null)
        row.createCell(++index).setCellValue(sdfData.format(trappolaggio.getDataOraInizio())); 
      else
        row.createCell(++index).setCellValue("");
      row.createCell(++index).setCellValue(trappolaggio.getDescrComune());
      row.createCell(++index).setCellValue(trappolaggio.getDettaglioTipoArea());
      row.createCell(++index).setCellValue(trappolaggio.getIspettore());
      row.createCell(++index).setCellValue(trappolaggio.getPianta());
      row.createCell(++index).setCellValue(trappolaggio.getNomeLatino());
      row.createCell(++index).setCellValue(trappolaggio.getDescrComune());
      row.createCell(++index).setCellValue(trappolaggio.getCodiceSfr());
      row.createCell(++index).setCellValue(trappolaggio.getDescrOperazione());
      row.createCell(++index).setCellValue(trappolaggio.getIdIspezioneVisiva());
      row.createCell(++index).setCellValue(trappolaggio.getIdRilevazione());
    }
    sheet.shiftRows(1,sheet.getLastRowNum(),1);
    sheet.shiftRows(numRow+2,sheet.getLastRowNum(),1);
  }

}
