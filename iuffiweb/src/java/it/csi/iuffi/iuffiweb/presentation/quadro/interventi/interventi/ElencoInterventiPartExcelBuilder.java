package it.csi.iuffi.iuffiweb.presentation.quadro.interventi.interventi;

import java.math.BigDecimal;
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
import org.springframework.web.servlet.view.document.AbstractExcelView;

import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaElencoInterventi;

public class ElencoInterventiPartExcelBuilder extends AbstractExcelView
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

    @SuppressWarnings("unchecked")
    ArrayList<RigaElencoInterventi> arrayList = (ArrayList<RigaElencoInterventi>) model
        .get("elenco");
    List<RigaElencoInterventi> elenco = arrayList;

    // create a new Excel sheet
    HSSFSheet sheet = workbook.createSheet("Interventi");
    sheet.setDefaultColumnWidth(30);

    HSSFRow aRow = null;
    int rowCount = 0;
    int countCol = -1;

    HSSFRow header = sheet.createRow(rowCount++);

    countCol++;
    header.createCell(countCol).setCellValue("Progressivo");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Attività");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Partecipante");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Tipo Classificazione");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Tipo Intervento");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Intervento");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Ulteriori informazioni");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Comuni interessati");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Dato / Valore / UM");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Importo unitario");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Importo Investimento");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Operazione");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Spesa ammessa");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Percentuale contributo");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Contributo concesso");
    header.getCell(countCol).setCellStyle(styleHeaderTab);

    BigDecimal totImporto = new BigDecimal(0);
    BigDecimal totImportoUnitario = new BigDecimal(0);
    BigDecimal totSpesaAmmessa = new BigDecimal(0);
    BigDecimal totContributoConcesso = new BigDecimal(0);

    // Stampo gli interventi
    for (RigaElencoInterventi item : elenco)
    {
      countCol = -1;
      aRow = sheet.createRow(rowCount++);

      countCol++;
      if (item.getProgressivo() != null)
        aRow.createCell(countCol).setCellValue(item.getProgressivo());
      else
        aRow.createCell(countCol).setCellValue("");

      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDescTipoClassificazione());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDescTipoAggregazione());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDescIntervento());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getUlterioriInformazioni());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDescComuni());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getMisurazioniToString());
      aRow.getCell(countCol).setCellStyle(styleCurrency);

      countCol++;
      if (item.getImportoUnitario() != null)
        aRow.createCell(countCol)
            .setCellValue(item.getImportoUnitario().doubleValue());
      else
        aRow.createCell(countCol).setCellValue("");
      aRow.getCell(countCol).setCellStyle(styleCurrency);
      if (item.getImportoUnitario() != null)
        totImportoUnitario = totImportoUnitario.add(item.getImportoUnitario());

      countCol++;
      if (item.getImportoInvestimento() != null)
        aRow.createCell(countCol)
            .setCellValue(item.getImportoInvestimento().doubleValue());
      else
        aRow.createCell(countCol).setCellValue("");
      aRow.getCell(countCol).setCellStyle(styleCurrency);
      if (item.getImportoInvestimento() != null)
        totImporto = totImporto.add(item.getImportoInvestimento());

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getOperazione());
      aRow.getCell(countCol).setCellStyle(styleCurrency);

      countCol++;
      if (item.getSpesaAmmessa() != null)
        aRow.createCell(countCol)
            .setCellValue(item.getSpesaAmmessa().doubleValue());
      else
        aRow.createCell(countCol).setCellValue("");
      aRow.getCell(countCol).setCellStyle(styleCurrency);
      if (item.getSpesaAmmessa() != null)
        totSpesaAmmessa = totSpesaAmmessa.add(item.getSpesaAmmessa());

      countCol++;
      if (item.getPercentualeContributo() != null)
        aRow.createCell(countCol)
            .setCellValue(item.getPercentualeContributo().doubleValue());
      else
        aRow.createCell(countCol).setCellValue("");
      aRow.getCell(countCol).setCellStyle(styleCurrency);

      countCol++;
      if (item.getContributoConcesso() != null)
        aRow.createCell(countCol)
            .setCellValue(item.getContributoConcesso().doubleValue());
      else
        aRow.createCell(countCol).setCellValue("");
      aRow.getCell(countCol).setCellStyle(styleCurrency);
      if (item.getContributoConcesso() != null)
        totContributoConcesso = totContributoConcesso
            .add(item.getContributoConcesso());
    }

    rowCount++;
    aRow = sheet.createRow(rowCount++);

    countCol = 9;
    aRow.createCell(countCol).setCellValue(totImportoUnitario.doubleValue());
    aRow.getCell(countCol).setCellStyle(styleCurrency);

    countCol = 10;
    aRow.createCell(countCol).setCellValue(totImporto.doubleValue());
    aRow.getCell(countCol).setCellStyle(styleCurrency);

    countCol = 12;
    aRow.createCell(countCol).setCellValue(totSpesaAmmessa.doubleValue());
    aRow.getCell(countCol).setCellStyle(styleCurrency);

    countCol = 14;
    aRow.createCell(countCol).setCellValue(totContributoConcesso.doubleValue());
    aRow.getCell(countCol).setCellStyle(styleCurrency);
  }

}
