package it.csi.iuffi.iuffiweb.excel;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.MissioneDTO;
import it.csi.iuffi.iuffiweb.model.OrganismoNocivoDTO;
import it.csi.iuffi.iuffiweb.model.SpecieVegetaleDTO;
import it.csi.iuffi.iuffiweb.model.StatoEnum;
import it.csi.iuffi.iuffiweb.model.request.MissioneRequest;
import it.csi.iuffi.iuffiweb.view.AbstractXlsView;


@SuppressWarnings("unchecked")
public class MissioneExcelBuilder extends AbstractXlsView {


  private SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
  private SimpleDateFormat sdfOra = new SimpleDateFormat("HH:mm");

	@Override
	public void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Auto-generated method stub

		String filename = "missioni.xls";
		// change the file name
		response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");

		List<MissioneDTO> missioni = (List<MissioneDTO>) model.get("missioni");
		MissioneRequest mr = (MissioneRequest) model.get("missioneRequest");
			
		// create excel xls sheet
		Sheet sheet = workbook.createSheet("Lista Missioni");
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
		
    titleRow.createCell(0).setCellValue("Lista Missioni");
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
		
    if (StringUtils.isNotBlank(mr.getDallaData()))
      filter1 += "Dal " + mr.getDallaData();
    if (StringUtils.isNotBlank(mr.getAllaData()))
      filter1 += " al " + mr.getAllaData();

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

    if (model.get("ispettore") != null) {
      AnagraficaDTO ispettore = (AnagraficaDTO) model.get("ispettore");
      Row subtitleRow2 = sheet.createRow(sheet.getLastRowNum()+1);
      subtitleRow2.createCell(0).setCellValue("Ispettore: " + ispettore.getCognomeNome());
      subtitleRow2.getCell(0).setCellStyle(style);
      subtitleRow2.createCell(1).setCellValue("");
      subtitleRow2.createCell(2).setCellValue("");
      subtitleRow2.createCell(3).setCellValue("");
      subtitleRow2.createCell(4).setCellValue("");
      subtitleRow2.createCell(5).setCellValue("");
      numRow++;
      sheet.addMergedRegion(new CellRangeAddress(numRow, numRow, (int) 0, (int) 5));
    }

		if (model.get("specie") != null) {
		  SpecieVegetaleDTO specie = (SpecieVegetaleDTO) model.get("specie");
	    Row subtitleRow2 = sheet.createRow(sheet.getLastRowNum()+1);
	    subtitleRow2.createCell(0).setCellValue("Specie vegetale: " + specie.getGenereSpecie());
	    subtitleRow2.getCell(0).setCellStyle(style);
	    subtitleRow2.createCell(1).setCellValue("");
	    subtitleRow2.createCell(2).setCellValue("");
	    subtitleRow2.createCell(3).setCellValue("");
	    subtitleRow2.createCell(4).setCellValue("");
	    subtitleRow2.createCell(5).setCellValue("");
	    numRow++;
	    sheet.addMergedRegion(new CellRangeAddress(numRow, numRow, (int) 0, (int) 5));
		}

    if (model.get("organismoNocivo") != null) {
      OrganismoNocivoDTO organismoNocivo = (OrganismoNocivoDTO) model.get("organismoNocivo");
      Row subtitleRow2 = sheet.createRow(sheet.getLastRowNum()+1);
      subtitleRow2.createCell(0).setCellValue("Organismo nocivo: " + organismoNocivo.getNomeLatino());
      subtitleRow2.getCell(0).setCellStyle(style);
      subtitleRow2.createCell(1).setCellValue("");
      subtitleRow2.createCell(2).setCellValue("");
      subtitleRow2.createCell(3).setCellValue("");
      subtitleRow2.createCell(4).setCellValue("");
      subtitleRow2.createCell(5).setCellValue("");
      numRow++;
      sheet.addMergedRegion(new CellRangeAddress(numRow, numRow, (int) 0, (int) 5));
    }

    if (mr.getCampionamento() != null && mr.getCampionamento() || mr.getTrappolaggio() != null && mr.getTrappolaggio()) {
      String filter2 = "";
      if (mr.getCampionamento() != null && mr.getCampionamento()) {
        filter2 += "Missioni con Campionamenti";
        if (mr.getTrappolaggio() != null && mr.getTrappolaggio())
          filter2 +=" e Trappolaggi";
      }
      else {
        if (mr.getTrappolaggio() != null && mr.getTrappolaggio())
          filter2 = "Missioni con Trappolaggi";
      }
      Row subtitleRow2 = sheet.createRow(sheet.getLastRowNum()+1);
      subtitleRow2.createCell(0).setCellValue(filter2);
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
    header.createCell(++index).setCellValue("Id Missione");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Data Missione");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Ora Inizio");
    header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Ora Fine");
    header.getCell(index).setCellStyle(headerStyle);
		header.createCell(++index).setCellValue("Ispettori");
		header.getCell(index).setCellStyle(headerStyle);
    header.createCell(++index).setCellValue("Numero Trasferta");
    header.getCell(index).setCellStyle(headerStyle);
		header.createCell(++index).setCellValue("PDF Trasferta");
		header.getCell(index).setCellStyle(headerStyle);
		header.createCell(++index).setCellValue("Stato");
		header.getCell(index).setCellStyle(headerStyle);

		int rowCount = sheet.getLastRowNum()+1;
		
		for(MissioneDTO missione : missioni) {
			Row row = sheet.createRow(rowCount++);
			index = -1;
			row.createCell(++index).setCellValue(missione.getIdMissione());
			row.getCell(index).setCellStyle(rowStyle);
			row.createCell(++index).setCellValue(sdfData.format(missione.getDataOraInizioMissione()));
			row.createCell(++index).setCellValue(sdfOra.format(missione.getDataOraInizioMissione()));
			row.createCell(++index).setCellValue((missione.getDataOraFineMissione()!=null)?sdfOra.format(missione.getDataOraFineMissione()):"");
			row.createCell(++index).setCellValue(missione.getIspettore());
      row.createCell(++index).setCellValue((missione.getNumeroTrasferta()!=null)?String.valueOf(missione.getNumeroTrasferta()):"");
      row.createCell(++index).setCellValue((missione.getNomeFile()!=null)?missione.getNomeFile():"");
			row.createCell(++index).setCellValue(StatoEnum.valueOf(missione.getStato()).getDescrizione());
		}
		sheet.shiftRows(1,sheet.getLastRowNum(),1);
		sheet.shiftRows(numRow+2,sheet.getLastRowNum(),1);
	}

}
