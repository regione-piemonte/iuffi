package it.csi.iuffi.iuffiweb.excel;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import it.csi.iuffi.iuffiweb.model.RendicontazioneDTO;
import it.csi.iuffi.iuffiweb.view.AbstractXlsView;


@SuppressWarnings("unchecked")
public class RendicontazioneExcelBuilder extends AbstractXlsView {

  //private SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
  //private SimpleDateFormat sdfOra = new SimpleDateFormat("HH:mm");

  @Override
  public void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
    // Auto-generated method stub
  
    String filename = "rendicontazione.xls";
    // change the file name
    response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");
  
    List<RendicontazioneDTO> lista = (List<RendicontazioneDTO>) model.get("listaRendicontazione");
    RendicontazioneDTO rendicontazione = (RendicontazioneDTO) model.get("rendicontazione");
  
    // create excel xls sheet
    Sheet sheet = workbook.createSheet("Lista Rendicontazione");
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
  
    titleRow.createCell(0).setCellValue("Lista Rendicontazione");
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
  
    filter1 += "Anno " + rendicontazione.getAnno();

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
    
    int index = -1;
    // create header row
    Row header = sheet.createRow(sheet.getLastRowNum()+1);
    header.createCell(++index).setCellValue("Numero Verbale");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Data Missione");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Id Missione");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Id Rilevazione");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Numero Trasferta");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Ispettore");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Num.Ispettori");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Ore Totali Siti");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Ore Operazione");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Peso Verbale");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Peso Riga");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Latitudine");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Longitudine");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Tipo Area");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N. Ore Ispezione");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Costo Orario Tecnico Ispezione");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Totale Costo Ispezione");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N.Ha");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N.Piante");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Nome Latino Specie");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("ON Specie");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("ON no UE Specie");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N. ON a rimborso");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N.Campioni");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("ON Campioni");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N.Ore Raccolta Campioni");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Costo orario tecnico campionamento");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Totale costo raccolta campioni");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Tipologia campione");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("ON Trappola");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Codice SFR");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("ID Campionamento");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Codice Trappola");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Costo orario tecnico trappolaggio");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N.Piazzamenti");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N.Sostituzioni/Ricariche");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N.Rimozioni");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N.Ore piazzamenti trappole");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N.Ore Sostituzione/Ricarica trappole");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N.Ore rimozione trappole");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Costo totale piazzamento");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Costo totale sostituzioni/ricariche");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Costo totale ritiro");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Test Laboratorio");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("N.Registro Lab.");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Costo laboratorio per esame/reagenti");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Totale costo esami");
    header.getCell(index).setCellStyle(headerStyle);
    
    int rowCount = sheet.getLastRowNum()+1;

    CellStyle currencyStyle = workbook.createCellStyle();
    currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
    CellStyle haStyle = workbook.createCellStyle();
    haStyle.setDataFormat(workbook.createDataFormat().getFormat("0.0000"));
    CellStyle hourStyle = workbook.createCellStyle();
    hourStyle.setDataFormat(workbook.createDataFormat().getFormat("[HH]:MM"));
    
    for (RendicontazioneDTO rec : lista) {
      Row row = sheet.createRow(rowCount++);
      index = -1;
      row.createCell(++index).setCellValue((rec.getNumeroVerbale()!=null && rec.getNumeroVerbale().startsWith("VERB"))?rec.getNumeroVerbale():"");
      row.createCell(++index).setCellValue(rec.getDataMissioneS());
      row.createCell(++index).setCellValue(rec.getIdMissione());
      row.createCell(++index).setCellValue(rec.getIdRilevazione());
      row.createCell(++index).setCellValue((rec.getNumeroTrasferta()!=null)?rec.getNumeroTrasferta():"");
      row.createCell(++index).setCellValue(rec.getIspettore());
      row.createCell(++index).setCellValue(rec.getNumIspettori());
      row.createCell(++index).setCellValue((rec.getOreTotaliSitiS()!=null)?rec.getOreTotaliSitiS():"0");
      //row.getCell(index).setCellStyle(hourStyle);
      row.createCell(++index).setCellValue((rec.getOreOperazioni()!=null)?rec.getOreOperazioni():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue((rec.getPesoVerbale()!=null)?rec.getPesoVerbale():0);
      row.createCell(++index).setCellValue((rec.getPesoRiga()!=null)?rec.getPesoRiga():0);
      row.createCell(++index).setCellValue(rec.getLatitudine());
      row.createCell(++index).setCellValue(rec.getLongitudine());
      row.createCell(++index).setCellValue(rec.getDescTipoArea());
      row.createCell(++index).setCellValue((rec.getNumOreIspezione()!=null)?rec.getNumOreIspezione():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue((rec.getCostoOrarioTecIspezione()!=null)?rec.getCostoOrarioTecIspezione():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue((rec.getTotaleCostoIspezione()!=null)?rec.getTotaleCostoIspezione():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue((rec.getSuperfice()!=null)?rec.getSuperfice():0);
      row.getCell(index).setCellStyle(haStyle);
      row.createCell(++index).setCellValue((rec.getNumeroPiante()!=null)?rec.getNumeroPiante():0);
      row.createCell(++index).setCellValue(rec.getNomeLatinoSpecie());
      row.createCell(++index).setCellValue(rec.getOnSpecie());
      row.createCell(++index).setCellValue(rec.getOnNoUeSpecie());
      row.createCell(++index).setCellValue((rec.getNumOnRimborso()!=null)?rec.getNumOnRimborso():0);
      row.createCell(++index).setCellValue((rec.getNumCampioni()!=null)?rec.getNumCampioni():0);
      row.createCell(++index).setCellValue(rec.getOrganismiCampione());
      row.createCell(++index).setCellValue((rec.getNumOreRaccoltaCampioni()!=null)?rec.getNumOreRaccoltaCampioni():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue((rec.getCostoOrarioTecCampionamento()!=null)?rec.getCostoOrarioTecCampionamento():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue((rec.getTotaleCostoRaccCampioni()!=null)?rec.getTotaleCostoRaccCampioni():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue(rec.getTipoCampione());
      row.createCell(++index).setCellValue(rec.getOrganismoTrappola());
      row.createCell(++index).setCellValue(rec.getCodiceSfr());
      row.createCell(++index).setCellValue((rec.getIdCampionamento()!=null)?rec.getIdCampionamento().toString():"");
      row.createCell(++index).setCellValue(rec.getCodiceTrappola());
      row.createCell(++index).setCellValue((rec.getCostoOrarioTecTrappolaggio()!=null)?rec.getCostoOrarioTecTrappolaggio():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue((rec.getNumPiazzamento()!=null)?rec.getNumPiazzamento():0);
      row.createCell(++index).setCellValue((rec.getNumRicSostTrappole()!=null)?rec.getNumRicSostTrappole():0);
      row.createCell(++index).setCellValue((rec.getNumRimozioneTrappole()!=null)?rec.getNumRimozioneTrappole():0);
      row.createCell(++index).setCellValue((rec.getNumOrePiazzTrappole()!=null)?rec.getNumOrePiazzTrappole():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue((rec.getNumOreRicSostTrappole()!=null)?rec.getNumOreRicSostTrappole():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue((rec.getNumOreRimozioneTrappole()!=null)?rec.getNumOreRimozioneTrappole():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue((rec.getCostoTotalePiazzTrappole()!=null)?rec.getCostoTotalePiazzTrappole():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue((rec.getCostoTotaleRicSostTrappole()!=null)?rec.getCostoTotaleRicSostTrappole():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue((rec.getCostoTotaleRitiroTrappole()!=null)?rec.getCostoTotaleRitiroTrappole():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue(rec.getTestLab());
      row.createCell(++index).setCellValue(rec.getNumRegistroLab());
      row.createCell(++index).setCellValue((rec.getCostoLaboratorio()!=null)?rec.getCostoLaboratorio():0);
      row.getCell(index).setCellStyle(currencyStyle);
      row.createCell(++index).setCellValue((rec.getTotaleCostoEsami()!=null)?rec.getTotaleCostoEsami():0);
      row.getCell(index).setCellStyle(currencyStyle);
    }
    
    for (int i=0; i<42; i++) {
      sheet.autoSizeColumn(i);
    }
    sheet.shiftRows(1,sheet.getLastRowNum(),1);
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
