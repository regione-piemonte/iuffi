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

import it.csi.iuffi.iuffiweb.model.IspezioneVisivaDTO;
import it.csi.iuffi.iuffiweb.view.AbstractXlsView;

@SuppressWarnings("unchecked")
public class GestioneVisualExcelBuilder extends AbstractXlsView {


  private SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
  //private SimpleDateFormat sdfOra = new SimpleDateFormat("HH:mm");
  


  @Override
  public void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
    // Auto-generated method stub

    String filename = "visual.xls";
    // change the file name
    response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");

    List<IspezioneVisivaDTO> ispezioniVisive = (List<IspezioneVisivaDTO>) model.get("elenco");
    //MissioneRequest mr = (MissioneRequest) model.get("missioneRequest");
      
    // create excel xls sheet
    Sheet sheet = workbook.createSheet("Elenco visual");
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
    
    titleRow.createCell(0).setCellValue("Elenco visual");
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
    int index = 0;
    header.createCell(index).setCellValue("Id Ispezione");
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
    header.createCell(++index).setCellValue("Cod. Aut. Azienda");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Superficie(mq)");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N° piante");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Note");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Id Rilevazione");
    header.getCell(index).setCellStyle(headerStyle);
    
    int rowCount = sheet.getLastRowNum()+1;
    
    for(IspezioneVisivaDTO ispezioneVisiva : ispezioniVisive) {
      Row row = sheet.createRow(rowCount++);
      index = -1;
      row.createCell(++index).setCellValue(ispezioneVisiva.getIdIspezione());
      row.getCell(index).setCellStyle(rowStyle);
      if (ispezioneVisiva.getDataOraInizio() != null) {
        row.createCell(++index).setCellValue(sdfData.format(ispezioneVisiva.getDataOraInizio())); 
      } else {
        row.createCell(++index).setCellValue(""); 
      }
      row.getCell(index).setCellStyle(rowStyle);
      row.createCell(++index).setCellValue(ispezioneVisiva.getComune());
      row.getCell(index).setCellStyle(rowStyle);
      row.createCell(++index).setCellValue(ispezioneVisiva.getDettaglioArea());
      row.getCell(index).setCellStyle(rowStyle);
      row.createCell(++index).setCellValue(ispezioneVisiva.getIspettore());
      row.getCell(index).setCellStyle(rowStyle);
      row.createCell(++index).setCellValue(ispezioneVisiva.getNomeVolgareSpecie());
      row.getCell(index).setCellStyle(rowStyle);
      row.createCell(++index).setCellValue(ispezioneVisiva.getOnIspezionati());
      row.getCell(index).setCellStyle(rowStyle);
      row.createCell(++index).setCellValue(ispezioneVisiva.getCuaa());
      row.getCell(index).setCellStyle(rowStyle);
      if(ispezioneVisiva.getSuperficie() != null && ispezioneVisiva.getSuperficie() != 0) {
        row.createCell(++index).setCellValue(ispezioneVisiva.getSuperficie().toString());
      } else {
        row.createCell(++index).setCellValue("");
      }
      row.getCell(index).setCellStyle(rowStyle);
      if(ispezioneVisiva.getNumeroPiante() != null && ispezioneVisiva.getNumeroPiante() != 0) {
        row.createCell(++index).setCellValue(ispezioneVisiva.getNumeroPiante().toString());
      } else {
        row.createCell(++index).setCellValue("");
      }
      row.getCell(index).setCellStyle(rowStyle);
      row.createCell(++index).setCellValue(ispezioneVisiva.getNote());
      row.getCell(index).setCellStyle(rowStyle);
      row.createCell(++index).setCellValue(ispezioneVisiva.getIdRilevazione());
      row.getCell(index).setCellStyle(rowStyle);
    }
    sheet.shiftRows(1,sheet.getLastRowNum(),1);
    sheet.shiftRows(numRow+2,sheet.getLastRowNum(),1);
  }

}
