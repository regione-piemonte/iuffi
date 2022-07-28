package it.csi.iuffi.iuffiweb.presentation.listeliquidazione;

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
import org.springframework.web.servlet.view.document.AbstractExcelView;

import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RiepilogoPraticheApprovazioneDTO;

public class ElencoPraticheListaLiquidazioneExcelBuilder
    extends AbstractExcelView
{
  @Override
  protected void buildExcelDocument(Map<String, Object> model,
      HSSFWorkbook workbook, HttpServletRequest request,
      HttpServletResponse response)
      throws Exception
  {

    @SuppressWarnings("unchecked")
    List<RiepilogoPraticheApprovazioneDTO> pratiche = (List<RiepilogoPraticheApprovazioneDTO>) model
        .get("pratiche");

    // CELL STYLE GENERICO, DI DEFAULT
    HSSFCellStyle styleValDef = ((HSSFWorkbook) workbook).createCellStyle();
    Font font = workbook.createFont();
    font = workbook.createFont();
    font.setFontName("Arial");
    styleValDef.setWrapText(true);
    styleValDef.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    font.setColor(HSSFColor.BLACK.index);
    styleValDef.setFont(font);

    HSSFCellStyle styleCurrency = workbook.createCellStyle();
    styleCurrency.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    styleCurrency.setDataFormat((short) 8);
    styleCurrency
        .setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
    styleCurrency.setFont(font);

    HSSFCellStyle styleHeaderTab = ((HSSFWorkbook) workbook).createCellStyle();
    HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
    palette.setColorAtIndex((short) 57, (byte) 66, (byte) 139, (byte) 202); // color
                                                                            // #428BCA
    palette.setColorAtIndex((short) 58, (byte) 211, (byte) 211, (byte) 211); // color
                                                                             // #F9F9F9
    styleHeaderTab.setFillForegroundColor(palette.getColor(57).getIndex());
    styleHeaderTab.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    // CellStyle style = workbook.createCellStyle();
    font = workbook.createFont();
    font.setFontName("Arial");
    // style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
    // style.setFillPattern(CellStyle.SOLID_FOREGROUND);
    styleHeaderTab.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    styleHeaderTab.setAlignment(CellStyle.ALIGN_CENTER);
    styleHeaderTab.setBorderBottom(CellStyle.BORDER_MEDIUM);
    styleHeaderTab.setBorderTop(CellStyle.BORDER_MEDIUM);
    styleHeaderTab.setBorderLeft(CellStyle.BORDER_MEDIUM);
    styleHeaderTab.setBorderRight(CellStyle.BORDER_MEDIUM);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setColor(HSSFColor.WHITE.index);
    styleHeaderTab.setFont(font);

    // create a new Excel sheet
    HSSFSheet sheet = workbook.createSheet("Interventi");
    sheet.setDefaultColumnWidth(30);

    HSSFRow aRow = null;
    int rowCount = 0;
    int countCol = -1;

    HSSFRow header = sheet.createRow(rowCount++);

    countCol++;
    header.createCell(countCol).setCellValue("CUAA");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Denominazione");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Numero procedimento");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Operazione");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Causale pagamento");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Importo liquidato");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Importo premio");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Anticipo erogato");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;

    // Stampo gli interventi
    for (RiepilogoPraticheApprovazioneDTO item : pratiche)
    {
      countCol = -1;
      aRow = sheet.createRow(rowCount++);

      countCol++;
      if (item.getCuaa() != null)
        aRow.createCell(countCol).setCellValue(item.getCuaa());
      else
        aRow.createCell(countCol).setCellValue("");

      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDenominazione());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getIdentificativo());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getCodiceLivello());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getCausalePagamento());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol)
          .setCellValue(item.getImportoLiquidato().doubleValue());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;

      Double val = (double) 0;
      if (item.getImportoPremio() != null)
        val = item.getImportoPremio().doubleValue();
      aRow.createCell(countCol).setCellValue(val);
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;

      val = (double) 0;
      if (item.getAnticipoErogato() != null)
        val = item.getAnticipoErogato().doubleValue();
      aRow.createCell(countCol).setCellValue(val);
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;

    }

    rowCount++;
    aRow = sheet.createRow(rowCount++);
  }

}
