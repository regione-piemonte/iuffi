package it.csi.iuffi.iuffiweb.presentation.estrazionicampione;

import java.text.SimpleDateFormat;
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
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import it.csi.iuffi.iuffiweb.dto.ProcedimentoOggettoVO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RicercaProcedimentoEstrazioneExcelBuilder extends AbstractExcelView
{

  @SuppressWarnings("unchecked")
  @Override
  protected void buildExcelDocument(Map<String, Object> model,
      HSSFWorkbook workbook, HttpServletRequest request,
      HttpServletResponse response)
      throws Exception
  {

    List<ProcedimentoOggettoVO> elenco = (List<ProcedimentoOggettoVO>) model
        .get("elenco");

    // create a new Excel sheet
    HSSFSheet sheet = workbook.createSheet("Elenco Procedimenti");
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
    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    style.setAlignment(CellStyle.ALIGN_CENTER);
    style.setBorderBottom(CellStyle.BORDER_MEDIUM);
    style.setBorderTop(CellStyle.BORDER_MEDIUM);
    style.setBorderLeft(CellStyle.BORDER_MEDIUM);
    style.setBorderRight(CellStyle.BORDER_MEDIUM);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setColor(HSSFColor.WHITE.index);
    style.setFont(font);

    // CELL STYLE PER I VALORI
    HSSFCellStyle styleVal = ((HSSFWorkbook) workbook).createCellStyle();
    font = workbook.createFont();
    font.setFontName("Arial");
    // style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
    // style.setFillPattern(CellStyle.SOLID_FOREGROUND);
    styleVal.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    styleVal.setBorderBottom(CellStyle.BORDER_THIN);
    styleVal.setBorderTop(CellStyle.BORDER_THIN);
    styleVal.setBorderLeft(CellStyle.BORDER_THIN);
    styleVal.setBorderRight(CellStyle.BORDER_THIN);
    font.setColor(HSSFColor.BLACK.index);
    styleVal.setFont(font);

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

    // create header row
    int col = -1;
    HSSFRow header = sheet.createRow(0);

    col++;
    header.createCell(col).setCellValue("Identificativo");
    header.getCell(col).setCellStyle(style);
    // sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

    col++;
    header.createCell(col).setCellValue("Amm. competenza");
    header.getCell(col).setCellStyle(style);
    // sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

    col++;
    header.createCell(col).setCellValue("Anno campagna");
    header.getCell(col).setCellStyle(style);
    // sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

    col++;
    header.createCell(col).setCellValue("Operazione");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Bando");
    header.getCell(col).setCellStyle(style);
    // sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

    col++;
    header.createCell(col).setCellValue("Stato procedimento");
    header.getCell(col).setCellStyle(style);
    // sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

    if (elenco.get(0).getPO())
    {
      col++;
      header.createCell(col).setCellValue("Identificativo oggetto");
      header.getCell(col).setCellStyle(style);
      col++;
      header.createCell(col).setCellValue("Tipo oggetto");
      header.getCell(col).setCellStyle(style);
      col++;
      header.createCell(col).setCellValue("Stato oggetto");
      header.getCell(col).setCellStyle(style);

    }
    col++;
    header.createCell(col).setCellValue("Tipologia estrazione");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Data ultimo aggiornamento");
    header.getCell(col).setCellStyle(style);
    // sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

    col++;
    header.createCell(col).setCellValue("CUAA");
    header.getCell(col).setCellStyle(style);
    // sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

    col++;
    header.createCell(col).setCellValue("Denominazione");
    header.getCell(col).setCellStyle(style);
    // sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

    col++;
    header.createCell(col).setCellValue("Comune");
    header.getCell(col).setCellStyle(style);
    // sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

    col++;
    header.createCell(col).setCellValue("Provincia");
    header.getCell(col).setCellStyle(style);
    // sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

    col++;
    header.createCell(col).setCellValue("Sede legale");
    header.getCell(col).setCellStyle(style);
    // sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

    col++;
    header.createCell(col).setCellValue("Gestore fascicolo");
    header.getCell(col).setCellStyle(style);
    // sheet.addMergedRegion(new CellRangeAddress(0,1,col,col));

    CreationHelper createHelper = workbook.getCreationHelper();
    CellStyle dateCellStyle = workbook.createCellStyle();
    dateCellStyle
        .setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
    dateCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
    dateCellStyle.setBorderTop(CellStyle.BORDER_THIN);
    dateCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
    dateCellStyle.setBorderRight(CellStyle.BORDER_THIN);
    dateCellStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);

    CellStyle yearCellStyle = workbook.createCellStyle();
    yearCellStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
    yearCellStyle
        .setDataFormat(createHelper.createDataFormat().getFormat("yyyy"));
    yearCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
    yearCellStyle.setBorderTop(CellStyle.BORDER_THIN);
    yearCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
    yearCellStyle.setBorderRight(CellStyle.BORDER_THIN);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");

    // create data rows
    int rowCount = 1;
    int countCol = -1;
    for (ProcedimentoOggettoVO item : elenco)
    {
      countCol = -1;
      HSSFRow aRow = sheet.createRow(rowCount++);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getIdentificativo());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDescrAmmCompetenza());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol)
          .setCellValue(dateFormat.parse(item.getAnnoCampagna()));
      aRow.getCell(countCol).setCellStyle(yearCellStyle);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getElencoCodiciLivelliText());
      aRow.getCell(countCol).setCellStyle(styleValDef);
      aRow.setHeightInPoints(sheet.getDefaultRowHeightInPoints()
          * (item.getElencoCodiciLivelli().size() + 1));

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDenominazioneBando());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDescrizione());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      if (elenco.get(0).getPO())
      {
        countCol++;
        aRow.createCell(countCol).setCellValue(item.getIdentificativoOggetto());
        aRow.getCell(countCol).setCellStyle(styleValDef);
        countCol++;
        aRow.createCell(countCol).setCellValue(item.getTipoOggetto());
        aRow.getCell(countCol).setCellStyle(styleValDef);
        countCol++;
        aRow.createCell(countCol).setCellValue(item.getStatoOggetto());
        aRow.getCell(countCol).setCellStyle(styleValDef);

      }

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getFlagEstrattaStr());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(
          IuffiUtils.DATE.parseDate(item.getDataUltimoAggiornamento()));
      aRow.getCell(countCol).setCellStyle(dateCellStyle);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getCuaa());
      aRow.getCell(countCol).setCellStyle(styleVal);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDenominazioneAzienda());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDescrComune());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDescrProvincia());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getIndirizzoSedeLegale());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue(item.getDenominzioneDelega());
      aRow.getCell(countCol).setCellStyle(styleValDef);
    }

    CellRangeAddress address = new CellRangeAddress(0, rowCount - 1, 0,
        countCol);
    sheet.setAutoFilter(address);
  }

}
