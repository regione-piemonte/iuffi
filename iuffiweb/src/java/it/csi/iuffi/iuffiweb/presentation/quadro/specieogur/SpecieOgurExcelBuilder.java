package it.csi.iuffi.iuffiweb.presentation.quadro.specieogur;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import it.csi.iuffi.iuffiweb.dto.AnnoCensitoDTO;
import it.csi.iuffi.iuffiweb.dto.DistrettoDTO;
import it.csi.iuffi.iuffiweb.dto.IpotesiPrelievoDTO;
import it.csi.iuffi.iuffiweb.dto.OgurDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class SpecieOgurExcelBuilder extends AbstractExcelView
{
	
	 
  @Override
  protected void buildExcelDocument(Map<String, Object> model,
      HSSFWorkbook workbook, HttpServletRequest request,
      HttpServletResponse response)
      throws Exception
  {




    // CELL STYLE GENERICO, DI DEFAULT
    HSSFCellStyle styleValDef = ((HSSFWorkbook) workbook).createCellStyle();
    Font font = workbook.createFont();
    font = workbook.createFont();
    font.setFontName("Arial");
    styleValDef.setWrapText(true);
    styleValDef.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    styleValDef.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    font.setColor(HSSFColor.BLACK.index);
    styleValDef.setFont(font);

    HSSFCellStyle styleCurrency = workbook.createCellStyle();
    styleCurrency.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    styleCurrency.setDataFormat((short) 8);
    styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
    styleCurrency.setFont(font);
    
    HSSFCellStyle styleNumber = workbook.createCellStyle();
    styleNumber.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    styleNumber.setDataFormat((short) 8);
    styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,####0.0000"));
    styleNumber.setFont(font);
    
    HSSFCellStyle styleNumberCenter = workbook.createCellStyle();
    styleNumberCenter.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    styleNumberCenter.setDataFormat((short) 8);
    styleNumberCenter.setDataFormat(workbook.createDataFormat().getFormat("#,####0.0000"));
    styleNumberCenter.setFont(font);
    styleNumberCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    
    HSSFCellStyle styleHeaderTab = ((HSSFWorkbook) workbook).createCellStyle();
    HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
    palette.setColorAtIndex((short) 57, (byte) 66, (byte) 139, (byte) 202); // color
                                                                            // #428BCA
    palette.setColorAtIndex((short) 58, (byte) 211, (byte) 211, (byte) 211); // color
                                                                             // #F9F9F9
    styleHeaderTab.setFillForegroundColor(palette.getColor(57).getIndex());
    styleHeaderTab.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    font = workbook.createFont();
    font.setFontName("Arial");
    styleHeaderTab.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    styleHeaderTab.setAlignment(CellStyle.ALIGN_CENTER);
    styleHeaderTab.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    styleHeaderTab.setBorderBottom(CellStyle.BORDER_THIN);
    styleHeaderTab.setBorderLeft(CellStyle.BORDER_THIN);
    styleHeaderTab.setBorderRight(CellStyle.BORDER_THIN);
    styleHeaderTab.setWrapText(true);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setColor(HSSFColor.WHITE.index);
    styleHeaderTab.setFont(font);
    
    
    HSSFCellStyle styleHeaderTabPreHeader = ((HSSFWorkbook) workbook).createCellStyle();
    palette.setColorAtIndex((short) 57, (byte) 66, (byte) 139, (byte) 202); // color                                                                      // #428BCA
    palette.setColorAtIndex((short) 58, (byte) 211, (byte) 211, (byte) 211); // color// #F9F9F9
    styleHeaderTabPreHeader.setFillForegroundColor(palette.getColor(57).getIndex());
    styleHeaderTabPreHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    font = workbook.createFont();
    font.setFontName("Arial");
    styleHeaderTabPreHeader.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    styleHeaderTabPreHeader.setAlignment(CellStyle.ALIGN_CENTER);
    styleHeaderTabPreHeader.setBorderTop(CellStyle.BORDER_MEDIUM);
    styleHeaderTabPreHeader.setBorderLeft(CellStyle.BORDER_MEDIUM);
    styleHeaderTabPreHeader.setBorderRight(CellStyle.BORDER_MEDIUM);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setColor(HSSFColor.WHITE.index);
    styleHeaderTabPreHeader.setFont(font);
    
    HSSFCellStyle styleHeaderTabPreHeaderL = ((HSSFWorkbook) workbook).createCellStyle();
    palette.setColorAtIndex((short) 57, (byte) 66, (byte) 139, (byte) 202); // color                                                                      // #428BCA
    palette.setColorAtIndex((short) 58, (byte) 211, (byte) 211, (byte) 211); // color// #F9F9F9
    styleHeaderTabPreHeaderL.setFillForegroundColor(palette.getColor(57).getIndex());
    styleHeaderTabPreHeaderL.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    font = workbook.createFont();
    font.setFontName("Arial");
    styleHeaderTabPreHeaderL.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    styleHeaderTabPreHeaderL.setAlignment(CellStyle.ALIGN_CENTER);
    styleHeaderTabPreHeaderL.setBorderTop(CellStyle.BORDER_MEDIUM);
    styleHeaderTabPreHeaderL.setBorderBottom(CellStyle.BORDER_MEDIUM);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setColor(HSSFColor.WHITE.index);
    styleHeaderTabPreHeaderL.setFont(font);

    OgurDTO ogur = (OgurDTO) model.get("ogur");

    String fileName = "SpecieOgur_" + ogur.getDescrizione() + ".xls";
    response.setContentType("application/vnd.ms-excel"); //Tell the browser to expect an excel file
    response.setHeader("Content-Disposition", "attachment; filename="+fileName); //Tell the browser it should be named as the custom file name

    
    // create a new Excel sheet
    HSSFSheet sheet = workbook.createSheet("Specie");
    sheet.setDefaultColumnWidth(15);
    
    int rowCount = 0;
    int countCol = -1;
    HSSFRow row;
    
    
    
    if(ogur.getDistretti().size()==0) //NO DISTRETTI
    {
      row = sheet.createRow(rowCount++);
      countCol=0;
      row.createCell(countCol).setCellValue("SPECIE");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      countCol++;
      row.createCell(countCol).setCellValue(ogur.getDescrizione());
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      
      row = sheet.createRow(rowCount++);
      countCol=0;
      row.createCell(countCol).setCellValue("SUPERFICIE TOTALE ATC/CA (HA)");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      countCol++;
      row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(ogur.getSuperficieTotaleAtcca()));
      row.getCell(countCol).setCellStyle(styleNumberCenter);
      row = sheet.createRow(rowCount++);
      row.createCell(countCol).setCellValue("Non sono ancora presenti distretti per questa specie.");
      row.getCell(countCol).setCellStyle(styleNumber);
      sheet.autoSizeColumn(0);
      sheet.autoSizeColumn(1);

      return;
    }
  
    row = sheet.createRow(rowCount++);
    row.setHeight((short)550);
    countCol=0;
    row.createCell(countCol).setCellValue("SPECIE");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    row.createCell(countCol).setCellValue("");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    CellRangeAddress ca = new CellRangeAddress(0, 0, 0, 1);
    sheet.addMergedRegion(ca);
    countCol++;
    row.createCell(countCol).setCellValue(ogur.getDescrizione());
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    
    
    for(int i=0; i<ogur.getDistretti().size()-1; i++)
    {
      countCol++;
      row.createCell(countCol).setCellValue("");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
    }
    ca = new CellRangeAddress(0, 0, 2, ogur.getDistretti().size()+1);
    sheet.addMergedRegion(ca);
    row = sheet.createRow(rowCount++);
    countCol=0;
    row.createCell(countCol).setCellValue("SUPERFICIE TOTALE ATC/CA (HA)");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    ca = new CellRangeAddress(rowCount-1, rowCount-1, 0, 1);
    sheet.addMergedRegion(ca);
    
    countCol++;
    row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(ogur.getSuperficieTotaleAtcca()));
    row.getCell(countCol).setCellStyle(styleNumberCenter);
    //INIZIO STRUTTURA DISTRETTI 
    countCol++;
    row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(ogur.getSuperficieTotaleAtcca()));
    row.getCell(countCol).setCellStyle(styleNumberCenter);
    ca = new CellRangeAddress(rowCount-1, rowCount-1, 2, ogur.getDistretti().size()+1);
    sheet.addMergedRegion(ca);
    
    row = sheet.createRow(rowCount++);
    countCol=0;
    row.createCell(countCol).setCellValue("NOMINATIVO DISTRETTO");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    ca = new CellRangeAddress(rowCount-1, rowCount-1, 0, 1);
    sheet.addMergedRegion(ca);
    /*for(DistrettoDTO distretto : ogur.getDistretti())
    {
      countCol++;
      row.createCell(countCol).setCellValue(distretto.getNominDistretto());
      row.getCell(countCol).setCellStyle(styleNumber);
    }*/
    
    row = sheet.createRow(rowCount++);
    countCol=0;
    row.createCell(countCol).setCellValue("SUPERFICIE DISTRETTO(HA)");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    row.createCell(countCol).setCellValue("");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    ca = new CellRangeAddress(rowCount-1, rowCount-1, 0, 1);
    sheet.addMergedRegion(ca);    
    /*for(DistrettoDTO distretto : ogur.getDistretti())
    {
      countCol++;
      row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(distretto.getSuperficieDistretto()));
      row.getCell(countCol).setCellStyle(styleNumber);
    }*/
    
    row = sheet.createRow(rowCount++);
    countCol=0;
    row.createCell(countCol).setCellValue("SUPERFICIE VENABILE DISTRETTO(HA)");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    row.createCell(countCol).setCellValue("");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    ca = new CellRangeAddress(rowCount-1, rowCount-1, 0, 1);
    sheet.addMergedRegion(ca);
    /*for(DistrettoDTO distretto : ogur.getDistretti())
    {
      countCol++;
      row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(distretto.getSuperfVenabDistretto()));
      row.getCell(countCol).setCellStyle(styleNumber);
    }*/
   
    row = sheet.createRow(rowCount++);
    countCol=0;
    row.createCell(countCol).setCellValue("SUS");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    row.createCell(countCol).setCellValue("");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    ca = new CellRangeAddress(rowCount-1, rowCount-1, 0, 1);
    sheet.addMergedRegion(ca);
    List<AnnoCensitoDTO> anni = ogur.getDistretti().get(0).getAnniCensiti();
   /* for(DistrettoDTO distretto : ogur.getDistretti())
    {
      countCol++;
      row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(distretto.getSus()));
      row.getCell(countCol).setCellStyle(styleNumber);
      anni = distretto.getAnniCensiti();
    }*/
    
    for(AnnoCensitoDTO a : anni)
    {
      int initRow = rowCount;
      countCol=0;
      row = sheet.createRow(rowCount++);
      row.createCell(countCol).setCellValue(a.getAnno());
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      countCol++;
      row.createCell(countCol).setCellValue("CENSITO");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      countCol=0;
      row = sheet.createRow(rowCount++);
      row.createCell(countCol).setCellValue(a.getAnno());
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      countCol++;
      row.createCell(countCol).setCellValue("SUPERFICIE CENSITA (HA)");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      countCol=0;
      row = sheet.createRow(rowCount++);
      row.createCell(countCol).setCellValue(a.getAnno());
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      countCol++;
      row.createCell(countCol).setCellValue("PIANO NUMERICO");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      countCol=0;
      row = sheet.createRow(rowCount++);
      row.createCell(countCol).setCellValue(a.getAnno());
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      countCol++;
      row.createCell(countCol).setCellValue("PRELEVATO  ");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      ca = new CellRangeAddress(initRow, initRow+3, 0, 0);
      sheet.addMergedRegion(ca);
    }

    countCol=0;
    row = sheet.createRow(rowCount++);
    row.createCell(countCol).setCellValue("CENSIMENTO " + (ogur.getDistretti().get(0).getCensimento().getAnno()));
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    int colInit = 0;
    for(int i=0; i<ogur.getDistretti().size()+1; i++)
    {
      countCol++;
      row.createCell(countCol).setCellValue("");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
    }
    ca = new CellRangeAddress(rowCount-1, rowCount-1, colInit, ogur.getDistretti().size()+1);
    sheet.addMergedRegion(ca);   
    
    row = sheet.createRow(rowCount++);
    countCol=0;
    row.createCell(countCol).setCellValue("DENSITA' " + (ogur.getDistretti().get(0).getCensimento().getAnno()) + ": CAPI/SUP CENSITA");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    row.createCell(countCol).setCellValue("");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    ca = new CellRangeAddress(rowCount-1, rowCount-1, 0, 1);
    sheet.addMergedRegion(ca);
    row = sheet.createRow(rowCount++);
    countCol=0;
    row.createCell(countCol).setCellValue("DENSITA' " + (ogur.getDistretti().get(0).getCensimento().getAnno()) + ": CAPI/SUS  ");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    row.createCell(countCol).setCellValue("");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    ca = new CellRangeAddress(rowCount-1, rowCount-1, 0, 1);
    sheet.addMergedRegion(ca);
    
    countCol=0;
    row = sheet.createRow(rowCount++);
    row.createCell(countCol).setCellValue("PREVISIONE");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    colInit=0;
    for(int i=0; i<ogur.getDistretti().size()+1; i++)
    {
      countCol++;
      row.createCell(countCol).setCellValue("");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
    }
    ca = new CellRangeAddress(rowCount-1, rowCount-1, colInit, ogur.getDistretti().size()+1);
    sheet.addMergedRegion(ca);   

    row = sheet.createRow(rowCount++);
    countCol=0;
    row.createCell(countCol).setCellValue("DENSITA' OBIETTIVO A FINE QUINQUENNIO");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    row.createCell(countCol).setCellValue("");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    ca = new CellRangeAddress(rowCount-1, rowCount-1, 0, 1);
    sheet.addMergedRegion(ca);
    row = sheet.createRow(rowCount++);
    countCol=0;
    row.createCell(countCol).setCellValue("CONSISTENZA POTENZIALE A FINE QUINQUENNIO");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    row.createCell(countCol).setCellValue("");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    ca = new CellRangeAddress(rowCount-1, rowCount-1, 0, 1);
    sheet.addMergedRegion(ca);
    
    countCol=0;
    row = sheet.createRow(rowCount++);
    row.createCell(countCol).setCellValue("IPOTESI DI PRELIEVO (max)");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    colInit=0;
    for(int i=0; i<ogur.getDistretti().size()+1; i++)
    {
      countCol++;
      row.createCell(countCol).setCellValue("");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
    }
    ca = new CellRangeAddress(rowCount-1, rowCount-1, colInit, ogur.getDistretti().size()+1);
    sheet.addMergedRegion(ca);   
    
    List<IpotesiPrelievoDTO> anniIpotesi = ogur.getDistretti().get(0).getIpotesiPrelievo();
    for(IpotesiPrelievoDTO a : anniIpotesi)
    {
      row = sheet.createRow(rowCount++);
      countCol=0;
      row.createCell(countCol).setCellValue(a.getAnno());
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      countCol++;
      row.createCell(countCol).setCellValue("");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      ca = new CellRangeAddress(rowCount-1, rowCount-1, 0, 1);
      sheet.addMergedRegion(ca);
    }
    
    
    countCol=0;
    row = sheet.createRow(rowCount++);
    row.createCell(countCol).setCellValue("DANNI CAUSATI");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    colInit=0;
    for(int i=0; i<ogur.getDistretti().size()+1; i++)
    {
      countCol++;
      row.createCell(countCol).setCellValue("");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
    }
    ca = new CellRangeAddress(rowCount-1, rowCount-1, colInit, ogur.getDistretti().size()+1);
    sheet.addMergedRegion(ca);   
    
    for(AnnoCensitoDTO a : anni)
    {
      row = sheet.createRow(rowCount++);
      countCol=0;
      row.createCell(countCol).setCellValue(a.getAnno());
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      countCol++;
      row.createCell(countCol).setCellValue("");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      ca = new CellRangeAddress(rowCount-1, rowCount-1, 0, 1);
      sheet.addMergedRegion(ca);
    }
    
    countCol=0;
    row = sheet.createRow(rowCount++);
    row.createCell(countCol).setCellValue("INCIDENTI STRADALI");
    row.getCell(countCol).setCellStyle(styleHeaderTab);
    colInit=0;
    for(int i=0; i<ogur.getDistretti().size()+1; i++)
    {
      countCol++;
      row.createCell(countCol).setCellValue("");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
    }
    ca = new CellRangeAddress(rowCount-1, rowCount-1, colInit, ogur.getDistretti().size()+1);
    sheet.addMergedRegion(ca);   
    
    for(AnnoCensitoDTO a : anni)
    {
      row = sheet.createRow(rowCount++);
      countCol=0;
      row.createCell(countCol).setCellValue(a.getAnno());
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      countCol++;
      row.createCell(countCol).setCellValue("");
      row.getCell(countCol).setCellStyle(styleHeaderTab);
      ca = new CellRangeAddress(rowCount-1, rowCount-1, 0, 1);
      sheet.addMergedRegion(ca);
    }
    
    
    //FINE STRUTTURA DISTRETTI
    
 
    countCol=1;

    //MO SCRIVO ID ATI 
    for(DistrettoDTO distretto : ogur.getDistretti())
    {
      countCol++;
      rowCount=2;
      row = sheet.getRow(rowCount++);
      row.createCell(countCol).setCellValue(distretto.getNominDistretto());
      row.getCell(countCol).setCellStyle(styleNumber);
      
      row = sheet.getRow(rowCount++);
      row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(distretto.getSuperficieDistretto()));
      row.getCell(countCol).setCellStyle(styleNumber);
      
      row = sheet.getRow(rowCount++);
      row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(distretto.getSuperfVenabDistretto()));
      row.getCell(countCol).setCellStyle(styleNumber);
      
      row = sheet.getRow(rowCount++);
      row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(distretto.getSus()));
      row.getCell(countCol).setCellStyle(styleNumber);
      
      for(AnnoCensitoDTO a : distretto.getAnniCensiti())
      {
        row = sheet.getRow(rowCount++);
        row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(a.getTotCensito()));
        row.getCell(countCol).setCellStyle(styleNumber);
        
        row = sheet.getRow(rowCount++);
        row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(a.getSuperfCensita()));
        row.getCell(countCol).setCellStyle(styleNumber);
        
        row = sheet.getRow(rowCount++);
        row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(a.getPianoNumerico()));
        row.getCell(countCol).setCellStyle(styleNumber);
        
        row = sheet.getRow(rowCount++);
        row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(a.getTotPrelevato()));
        row.getCell(countCol).setCellStyle(styleNumber);
      }
      rowCount++;
      
      row = sheet.getRow(rowCount++);
      row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(distretto.getCensimento().getDensitaSupCens()));
      row.getCell(countCol).setCellStyle(styleNumber);
      
      row = sheet.getRow(rowCount++);
      row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(distretto.getCensimento().getDensitaCapiSus()));
      row.getCell(countCol).setCellStyle(styleNumber);
      rowCount++;
      
      row = sheet.getRow(rowCount++);
      row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(distretto.getCensimento().getDensitaObiettivo()));
      row.getCell(countCol).setCellStyle(styleNumber);
      
      row = sheet.getRow(rowCount++);
      row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(distretto.getCensimento().getConsistenzaPotenz()));
      row.getCell(countCol).setCellStyle(styleNumber);
      
      rowCount++;
      for(IpotesiPrelievoDTO ipotesi : distretto.getIpotesiPrelievo())
      {
        row = sheet.getRow(rowCount++);
        row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(ipotesi.getPercentuale()) + " %");
        row.getCell(countCol).setCellStyle(styleNumber);
      }
      rowCount++;
      for(AnnoCensitoDTO a : distretto.getAnniCensiti())
      {
        row = sheet.getRow(rowCount++);
        row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(a.getDanniCausati()) + " €");
        row.getCell(countCol).setCellStyle(styleNumber);
      }
      rowCount++;
      for(AnnoCensitoDTO a : distretto.getAnniCensiti())
      {
        row = sheet.getRow(rowCount++);
        row.createCell(countCol).setCellValue(IuffiUtils.FORMAT.formatCurrency(a.getIncidentiStradali()) + " N°");
        row.getCell(countCol).setCellStyle(styleNumber);
      }

    }

    for(int i = 0; i< ogur.getDistretti().size()+3; i++)
      sheet.autoSizeColumn(i,true);

  }


}
