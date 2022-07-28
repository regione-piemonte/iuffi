package it.csi.iuffi.iuffiweb.presentation.quadro.coltureaziendali;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import it.csi.iuffi.iuffiweb.dto.coltureaziendali.ColtureAziendaliDettaglioDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class CultureAziendaliExcelBuilder extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

	List<ColtureAziendaliDettaglioDTO> elenco = (List<ColtureAziendaliDettaglioDTO>) model.get("elenco");

	// create a new Excel sheet
	HSSFSheet sheet = workbook.createSheet("Elenco culture aziendali");
	sheet.setDefaultColumnWidth(30);

	// create style for header cells
	HSSFCellStyle style = ((HSSFWorkbook) workbook).createCellStyle();
	HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
	// palette.setColorAtIndex((short)57, (byte)224, (byte)233, (byte)242); //
	// color #E0E9F2

	palette.setColorAtIndex((short) 57, (byte) 66, (byte) 139, (byte) 202); // color
	// #428BCA

	palette.setColorAtIndex((short) 58, (byte) 211, (byte) 211, (byte) 211); // color
	// #F9F9F9

	style.setFillForegroundColor(palette.getColor(57).getIndex());
	style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

	// CellStyle style = workbook.createCellStyle();
	Font font = workbook.createFont();
	font.setFontName("Arial");
	// style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
	// style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setBorderBottom(CellStyle.BORDER_MEDIUM);
	style.setBorderTop(CellStyle.BORDER_MEDIUM);
	style.setBorderLeft(CellStyle.BORDER_MEDIUM);
	style.setBorderRight(CellStyle.BORDER_MEDIUM);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	font.setColor(HSSFColor.WHITE.index);
	style.setFont(font);

	// CELL STYLE GENERICO, DI DEFAULT
	HSSFCellStyle styleValDef = ((HSSFWorkbook) workbook).createCellStyle();
	font = workbook.createFont();
	font.setFontName("Arial");
	styleValDef.setWrapText(true);
	styleValDef.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	styleValDef.setBorderBottom(CellStyle.BORDER_THIN);
	styleValDef.setBorderTop(CellStyle.BORDER_THIN);
	styleValDef.setBorderLeft(CellStyle.BORDER_THIN);
	styleValDef.setBorderRight(CellStyle.BORDER_THIN);
	font.setColor(HSSFColor.BLACK.index);
	styleValDef.setFont(font);

	// CELL STYLE NUMERI
	HSSFCellStyle styleValNumber = ((HSSFWorkbook) workbook).createCellStyle();
	font = workbook.createFont();
	font.setFontName("Arial");
	styleValNumber.setWrapText(true);
	styleValNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	styleValNumber.setBorderBottom(CellStyle.BORDER_THIN);
	styleValNumber.setBorderTop(CellStyle.BORDER_THIN);
	styleValNumber.setBorderLeft(CellStyle.BORDER_THIN);
	styleValNumber.setBorderRight(CellStyle.BORDER_THIN);
	font.setColor(HSSFColor.BLACK.index);
	styleValNumber.setFont(font);
	styleValNumber.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	styleValNumber.setDataFormat(workbook.createDataFormat().getFormat("#,###,###,###,#0.00"));

	// CELL STYLE NUMERI CON 4 DECIMALI DOPO LA VIRGOLA
	HSSFCellStyle styleValNumber4 = ((HSSFWorkbook) workbook).createCellStyle();
	font = workbook.createFont();
	font.setFontName("Arial");
	styleValNumber4.setWrapText(true);
	styleValNumber4.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	styleValNumber4.setBorderBottom(CellStyle.BORDER_THIN);
	styleValNumber4.setBorderTop(CellStyle.BORDER_THIN);
	styleValNumber4.setBorderLeft(CellStyle.BORDER_THIN);
	styleValNumber4.setBorderRight(CellStyle.BORDER_THIN);
	font.setColor(HSSFColor.BLACK.index);
	styleValNumber4.setFont(font);
	styleValNumber4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	styleValNumber4.setDataFormat(workbook.createDataFormat().getFormat("#,###,###,###,#0.0000"));

	// CELL STYLE NUMERI TOTALI
	HSSFCellStyle styleValNumberTotal = ((HSSFWorkbook) workbook).createCellStyle();
	font = workbook.createFont();
	font.setFontName("Arial");
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	styleValNumberTotal.setWrapText(true);
	styleValNumberTotal.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	styleValNumberTotal.setBorderBottom(CellStyle.BORDER_THIN);
	styleValNumberTotal.setBorderTop(CellStyle.BORDER_THIN);
	styleValNumberTotal.setBorderLeft(CellStyle.BORDER_THIN);
	styleValNumberTotal.setBorderRight(CellStyle.BORDER_THIN);
	font.setColor(HSSFColor.BLACK.index);
	styleValNumberTotal.setFont(font);
	styleValNumberTotal.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	styleValNumberTotal.setDataFormat(workbook.createDataFormat().getFormat("#,###,###,###,#0.00"));

	// CELL STYLE NUMERI TOTALI CON 4 DECIMALI DOPO LA VIRGOLA
	HSSFCellStyle styleValNumberTotal4 = ((HSSFWorkbook) workbook).createCellStyle();
	font = workbook.createFont();
	font.setFontName("Arial");
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	styleValNumberTotal4.setWrapText(true);
	styleValNumberTotal4.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	styleValNumberTotal4.setBorderBottom(CellStyle.BORDER_THIN);
	styleValNumberTotal4.setBorderTop(CellStyle.BORDER_THIN);
	styleValNumberTotal4.setBorderLeft(CellStyle.BORDER_THIN);
	styleValNumberTotal4.setBorderRight(CellStyle.BORDER_THIN);
	font.setColor(HSSFColor.BLACK.index);
	styleValNumberTotal4.setFont(font);
	styleValNumberTotal4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	styleValNumberTotal4.setDataFormat(workbook.createDataFormat().getFormat("#,###,###,###,#0.0000"));
	
	
	// create header row
	int col = -1;
	HSSFRow header0 = sheet.createRow(0);
	
	
	col++;
	header0.createCell(col).setCellValue("Ubicazione terreno");
	header0.getCell(col).setCellStyle(style);
	sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

	col++;
	header0.createCell(col).setCellValue("Utilizzo");
	header0.getCell(col).setCellStyle(style);
	sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

	col++;
	header0.createCell(col).setCellValue("Destinazione");
	header0.getCell(col).setCellStyle(style);
	sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

	col++;
	header0.createCell(col).setCellValue("Dettaglio uso");
	header0.getCell(col).setCellStyle(style);
	sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

	col++;
	header0.createCell(col).setCellValue("Qualità uso");
	header0.getCell(col).setCellStyle(style);
	sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

	col++;
	header0.createCell(col).setCellValue("Varietà");
	header0.getCell(col).setCellStyle(style);
	sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

	col++;
	header0.createCell(col).setCellValue("Sup. (ha)");
	header0.getCell(col).setCellStyle(style);
	sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

	col++;
	header0.createCell(col).setCellValue("Danneggiato");
	header0.getCell(col).setCellStyle(style);
	sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));
	
	
	col++;
	header0.createCell(col).setCellValue("P.L.V. ordinaria annuale (media del triennio precedente)");
	header0.getCell(col).setCellStyle(style);
	sheet.addMergedRegion(new CellRangeAddress(0,0,8,14));
	
	col = 14;
	col++;
	header0.createCell(col).setCellValue("P.L.V. effettiva (conseguita nell'annata agraria dell'evento)");
	header0.getCell(col).setCellStyle(style);
	sheet.addMergedRegion(new CellRangeAddress(0,0,15,20));
	
	col = 20;
	col++;
	header0.createCell(col).setCellValue("Danno");
	header0.getCell(col).setCellStyle(style);
	sheet.addMergedRegion(new CellRangeAddress(0,0,21,22));
	
	
	col = -1;
	HSSFRow header1 = sheet.createRow(1);

//	col++;
//	header1.createCell(col).setCellValue("Identificativo");
//	header1.getCell(col).setCellStyle(style);
	// sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

	col++;
	header1.createCell(col).setCellValue("Ubicazione terreno");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Utilizzo");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Destinazione");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Dettaglio uso");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Qualità uso");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Varietà");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Sup. (ha)");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Danneggiato");
	header1.getCell(col).setCellStyle(style);

	
	//ordinaria
	col++;
	header1.createCell(col).setCellValue("Q.li / ha");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Tot. q.li");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Prezzo al q.le");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Totale euro");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Giornate lavorate a ha");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Giornate lavorate");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Note");
	header1.getCell(col).setCellStyle(style);

	
	//effettiva
	col++;
	header1.createCell(col).setCellValue("Q.li / ha");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Tot. q.li");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Prezzo al q.le");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Totale euro");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Rimborso assicurativo");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("Totale comprensivo dei rimborsi (Euro)");
	header1.getCell(col).setCellStyle(style);

	
	//danno
	col++;
	header1.createCell(col).setCellValue("Euro");
	header1.getCell(col).setCellStyle(style);

	col++;
	header1.createCell(col).setCellValue("%");
	header1.getCell(col).setCellStyle(style);
	
	
	
	
	// create data rows
	int rowCount = 2;
	int countCol = -1;
	for (ColtureAziendaliDettaglioDTO item : elenco) {
	    countCol = -1;
	    HSSFRow aRow = sheet.createRow(rowCount++);
	    
	    
	    countCol++;
	    aRow.createCell(countCol).setCellValue(item.getUbicazioneTerreno());
	    aRow.getCell(countCol).setCellStyle(styleValDef);

	    countCol++;
	    aRow.createCell(countCol).setCellValue(item.getTipoUtilizzoDescrizione());
	    aRow.getCell(countCol).setCellStyle(styleValDef);

	    countCol++;
	    aRow.createCell(countCol).setCellValue(item.getDestinazioneDescrizione());
	    aRow.getCell(countCol).setCellStyle(styleValDef);

	    countCol++;
	    aRow.createCell(countCol).setCellValue(item.getDettaglioUsoDescrizione());
	    aRow.getCell(countCol).setCellStyle(styleValDef);

	    countCol++;
	    aRow.createCell(countCol).setCellValue(item.getQualitaUsoDescrizione());
	    aRow.getCell(countCol).setCellStyle(styleValDef);

	    countCol++;
	    aRow.createCell(countCol).setCellValue(item.getVarietaDescrizione());
	    aRow.getCell(countCol).setCellStyle(styleValDef);

	    countCol++;
	    if (item.getSuperficieUtilizzata() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(item.getSuperficieUtilizzata().doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber4);
		
	    countCol++;
	    aRow.createCell(countCol).setCellValue(item.getFlagDanneggiato());
	    aRow.getCell(countCol).setCellStyle(styleValDef);

	    
	    //ordinaria
	    countCol++;
	    if (item.getProduzioneHa() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getProduzioneHa()).doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);
	  
	    countCol++;
	    if (item.getProduzioneDichiarata() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getProduzioneDichiarata()).doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);

	    countCol++;
	    if (item.getPrezzo() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getPrezzo()).doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);

	    countCol++;
	    if (item.getTotaleEuroPlvOrd() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getTotaleEuroPlvOrd()).doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);

	    countCol++;
	    if (item.getGiornateLavorateHa() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getGiornateLavorateHa()).intValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);
	    
	    countCol++;
	    if (item.getGiornateLavorate() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getGiornateLavorate()).doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);
	    
	    countCol++;
	    aRow.createCell(countCol).setCellValue(item.getNote());
	    aRow.getCell(countCol).setCellStyle(styleValDef);
	    
	    
	    //effettiva	    
	    countCol++;
	    if (item.getProduzioneHaDanno() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getProduzioneHaDanno()).doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);

	    countCol++;
	    if (item.getProduzioneTotaleDanno() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getProduzioneTotaleDanno()).doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);

	    countCol++;
	    if (item.getPrezzoDanneggiato() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getPrezzoDanneggiato()).doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);

	    countCol++;
	    if (item.getTotaleEuroPlvEff() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getTotaleEuroPlvEff()).doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);
	    
	    countCol++;
	    if (item.getImportoRimborso() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getImportoRimborso()).doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);
	    
	    countCol++;
	    if (item.getTotConRimborsi() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getTotConRimborsi()).doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);
	    
	    //danno
	    countCol++;
	    if ((item.getFlagDanneggiato() != null && item.getFlagDanneggiato().equalsIgnoreCase("N")) || item.getTotaleEuroPlvEff() == null || item.getEuroDanno() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getEuroDanno()).doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);
	    
	    countCol++;
	    if ((item.getFlagDanneggiato() != null && item.getFlagDanneggiato().equalsIgnoreCase("N")) || item.getTotaleEuroPlvEff() == null || item.getPercentualeDanno() == null) aRow.createCell(countCol).setCellValue("");
	    else aRow.createCell(countCol).setCellValue(IuffiUtils.NUMBERS.nvl(item.getPercentualeDanno()).doubleValue());
	    aRow.getCell(countCol).setCellStyle(styleValNumber);
	}
	

	
	
	// create data rows
	countCol = -1;
	HSSFRow aRow = sheet.createRow(rowCount++);


	countCol++;
	aRow.createCell(countCol).setCellValue("");
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);

	countCol++;
	aRow.createCell(countCol).setCellValue("");
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);

	countCol++;
	aRow.createCell(countCol).setCellValue("");
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);

	countCol++;
	aRow.createCell(countCol).setCellValue("");
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);

	countCol++;
	aRow.createCell(countCol).setCellValue("");
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);

	countCol++;
	aRow.createCell(countCol).setCellValue("");
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);

	countCol++;
	String strFormula= "SUM(G3:G"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);

	countCol++;
	aRow.createCell(countCol).setCellValue("");
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);	
	
	countCol++;
	strFormula= "SUM(I3:I"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	strFormula= "SUM(J3:J"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	strFormula= "SUM(K3:K"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	strFormula= "SUM(L3:L"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	strFormula= "SUM(M3:M"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	strFormula= "SUM(N3:N"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	aRow.createCell(countCol).setCellValue("");
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	strFormula= "SUM(P3:P"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	strFormula= "SUM(Q3:Q"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	strFormula= "SUM(R3:R"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	strFormula= "SUM(S3:S"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	strFormula= "SUM(T3:T"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	strFormula= "SUM(U3:U"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	strFormula= "SUM(V3:V"+(rowCount-1)+")";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	countCol++;
	strFormula= "(1-(U"+(rowCount)+"/L"+(rowCount)+"))*100";	    
	aRow.createCell(countCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
	aRow.getCell(countCol).setCellFormula(strFormula);
	aRow.getCell(countCol).setCellStyle(styleValNumberTotal);
	
	
	

	//elimino totali euroDanno e percentualeDanno se le rispettive colonne non contengono valori
	boolean isEuroDannoColumnEmpty = true;
	boolean isPercDannoColumnEmpty = true;
	try {
	    for(int i = 2; i < rowCount-1; i++){
		if (!sheet.getRow(i).getCell(countCol-1).getStringCellValue().isEmpty()) isEuroDannoColumnEmpty = false;
		if (!sheet.getRow(i).getCell(countCol).getStringCellValue().isEmpty()) isPercDannoColumnEmpty = false;
		if (!isEuroDannoColumnEmpty && !isPercDannoColumnEmpty) break;
	    }
	} catch (Exception ex) {
	    isEuroDannoColumnEmpty = false;
	    isPercDannoColumnEmpty = false;
	}
	if (isEuroDannoColumnEmpty) sheet.getRow(rowCount-1).getCell(countCol-1).setCellValue("");
	if (isPercDannoColumnEmpty) sheet.getRow(rowCount-1).getCell(countCol).setCellValue("");	
	
	
	
	
	//metto grassetto ultima riga, quella dei totali
	for(int i = 0; i <= countCol; i++){
	    if (i == 6) sheet.getRow(rowCount-1).getCell(i).setCellStyle(styleValNumberTotal4);
	    else sheet.getRow(rowCount-1).getCell(i).setCellStyle(styleValNumberTotal);
	}
	
	//filtro ricerca
	sheet.setAutoFilter(new CellRangeAddress(1, rowCount - 1, 0, countCol));
	
	//larghezza celle
//	for (int i = 0; i <= 22; i++) {
//	    sheet.autoSizeColumn(i);	    
//	}
	
    }

}
