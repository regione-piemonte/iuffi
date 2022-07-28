package it.csi.iuffi.iuffiweb.presentation.quadro.dannifauna;

import java.util.ArrayList;
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
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.danniFauna.RiepilogoDannoFaunaDTO;

public class RiepilogoDanniFaunaExcelBuilder extends AbstractExcelView
{
	
	 @Autowired
	  IInterventiEJB interventiEJB = null;
	 
  @Override
  protected void buildExcelDocument(Map<String, Object> model,
      HSSFWorkbook workbook, HttpServletRequest request,
      HttpServletResponse response)
      throws Exception
  {

    boolean isIstruttoria = (Boolean)model.get("isIstruttoria");
    
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
    styleHeaderTab.setBorderBottom(CellStyle.BORDER_MEDIUM);
    styleHeaderTab.setBorderLeft(CellStyle.BORDER_MEDIUM);
    styleHeaderTab.setBorderRight(CellStyle.BORDER_MEDIUM);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setColor(HSSFColor.WHITE.index);
    styleHeaderTab.setFont(font);
    styleHeaderTab.setWrapText(true);
    
    
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

    @SuppressWarnings("unchecked")
    ArrayList<RiepilogoDannoFaunaDTO> arrayList = (ArrayList<RiepilogoDannoFaunaDTO>) model.get("elenco");
    List<RiepilogoDannoFaunaDTO> elenco = arrayList;

    // create a new Excel sheet
    HSSFSheet sheet = workbook.createSheet("Riepilogo Danni Fauna");
    sheet.setDefaultColumnWidth(30);
    
    HSSFRow aRow = null;
    int rowCount = 0;
    int countCol = -1;
    HSSFRow header;
    
    countCol = -1;
    header = sheet.createRow(rowCount++);
    
    countCol++;
    header.createCell(countCol).setCellValue("Progressivo");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Specie");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Tipologia danno");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Comune");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Utilizzo");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Superficie coinvolta (ha)");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Superficie accertata (ha)");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Importo danno effettivo (€)");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    
    setColumnsWidth(sheet);
    
    // Riepilogo danni fauna
    for (RiepilogoDannoFaunaDTO item : elenco)
    {
      countCol = -1;
      aRow = sheet.createRow(rowCount++);
      
      countCol++;
      aRow.createCell(countCol).setCellValue(item.getProgressivo());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDescSpecieFauna());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDescTipoDannoFauna());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDescComune());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDescTipoUtilizzo());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      if(item.getSuperficieCoinvolta()!=null)
      {
          aRow.createCell(countCol).setCellValue(item.getSuperficieCoinvoltaStr());
      }
      else
      {
          aRow.createCell(countCol).setCellValue("");
      }
      aRow.getCell(countCol).setCellStyle(styleNumber);
 
      countCol++;
      if(item.getSuperficieAccertata()!=null)
      {
    	  aRow.createCell(countCol).setCellValue(item.getSuperficieAccertataStr());
      }
      else
      {
    	  aRow.createCell(countCol).setCellValue("");
      }
      aRow.getCell(countCol).setCellStyle(styleNumber);
      
      countCol++;
      if(item.getImportoDannoEffettivo()!=null)
      {
          aRow.createCell(countCol).setCellValue(item.getImportoDannoEffettivoStr());
      }
      else
      {
          aRow.createCell(countCol).setCellValue("");
      }
      aRow.getCell(countCol).setCellStyle(styleCurrency);
    }
    
    
    List<DannoFaunaDTO> danniFauna = (List<DannoFaunaDTO>)model.get("danniFauna");
    
    
    //foglio con particellare
    sheet = workbook.createSheet("Particellare");
    sheet.setDefaultColumnWidth(15);
    
    aRow = null;
    rowCount = 0;
    countCol = -1;
    
    countCol = -1;
    header = sheet.createRow(rowCount++);
    header.setHeight((short)550);
    
    countCol++;
    header.createCell(countCol).setCellValue("Progressivo");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Specie");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Tipologia danno");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Descrizione");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Quantità");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Unità Misura");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    
    countCol++;
    header.createCell(countCol).setCellValue("Prov");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Comune");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Sez");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Fo");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Part");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Sub");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Sup. Catastale (ha)");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Sup. utilizzata (ha)");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Sup. coinvolta (ha)");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Utilizzo");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Sup. utilizzata secondaria (ha)");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    
    if(isIstruttoria)
    {
      countCol++;
      header.createCell(countCol).setCellValue("Utilizzo riscontrato");
      header.getCell(countCol).setCellStyle(styleHeaderTab);
    }
    
    countCol++;
    header.createCell(countCol).setCellValue("Utilizzo secondario");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Coltura Secondaria");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    
    Long previousIdDannoFauna = null;
    
    // Stampo gli interventi
    for (DannoFaunaDTO item : danniFauna)
    {
      countCol = -1;
      aRow = sheet.createRow(rowCount++);
      if(previousIdDannoFauna == null || previousIdDannoFauna.longValue() != item.getIdDannoFauna().longValue())
      {
    	  previousIdDannoFauna = item.getIdDannoFauna();
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getProgressivo());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getDescSpecieFauna());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getDescTipoDannoFauna());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getUlterioriInformazioni());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getQuantitaFrt());
    	  aRow.getCell(countCol).setCellStyle(styleNumber);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getDescUnitaMisura());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
      }
      else
      {
    	  while(countCol < 5)
    	  {
    		  countCol++;
    		  aRow.createCell(countCol).setCellValue(""); 
    	  }
      }
      
      if(item.getSiglaProvincia() != null && !item.getSiglaProvincia().equals(""))
      {
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getSiglaProvincia());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getDescComune());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getSezione());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getFoglio());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getParticella());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getSubalterno());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getSupCatastale() != null ? item.getSupCatastale().doubleValue() : 0.0);
    	  aRow.getCell(countCol).setCellStyle(styleNumber);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getSuperficieUtilizzata() != null ? item.getSuperficieUtilizzata().doubleValue() : 0.0);
    	  aRow.getCell(countCol).setCellStyle(styleNumber);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getSuperficieDanneggiata() != null ? item.getSuperficieDanneggiata().doubleValue() : 0.0);
    	  aRow.getCell(countCol).setCellStyle(styleNumber);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getUtilizzo());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getSupUtilizzataSecondaria() != null ? item.getSupUtilizzataSecondaria().doubleValue() : 0.0);
    	  aRow.getCell(countCol).setCellStyle(styleNumber);
    	  
    	  if(isIstruttoria)
    	  {
    	    countCol++;
    	    aRow.createCell(countCol).setCellValue(item.getDescrizioneUtilizzoRisc());
    	    aRow.getCell(countCol).setCellStyle(styleValDef);
    	  }
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getUtilizzoSecondario());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
    	  
    	  countCol++;
    	  aRow.createCell(countCol).setCellValue(item.getDecodeFlagUtilizzoSec());
    	  aRow.getCell(countCol).setCellStyle(styleValDef);
      }
      else
      {
    	  while(countCol < 18)
    	  {
        	  countCol++;
        	  aRow.createCell(countCol).setCellValue("");
    	  }
      }
    }
    
    
    
  }
  
  private void setColumnsWidth(HSSFSheet sheet)
  {
	  sheet.setColumnWidth(0, 20*255);
	  sheet.setColumnWidth(1, 25*255);
	  sheet.setColumnWidth(2, 25*255);
	  sheet.setColumnWidth(3, 25*255);
	  sheet.setColumnWidth(5, 25*255);
	  sheet.setColumnWidth(6, 25*255);
  }
}
