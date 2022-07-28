package it.csi.iuffi.iuffiweb.presentation;

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

import it.csi.iuffi.iuffiweb.dto.reportistica.CellReportVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ColReportVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.GraficoVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.RowsReportVO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ExcelBuilderReport extends AbstractExcelView
{
  @Override
  protected void buildExcelDocument(Map<String, Object> model,
      HSSFWorkbook workbook, HttpServletRequest request,
      HttpServletResponse response)
      throws Exception
  {

    GraficoVO graficoVO = (GraficoVO) model.get("graficoVO");

    String bando = (String) request.getSession()
        .getAttribute("denominazioneBando");
    String descrBreve = graficoVO.getDescrBreve();
    String descrEstesa = graficoVO.getDescrEstesa();

    // create a new Excel sheet
    HSSFSheet sheet = workbook.createSheet(graficoVO.getDescrBreve());
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

    palette.setColorAtIndex((short) 59, (byte) 217, (byte) 237, (byte) 247);// #D9EDF7
                                                                            // azzurrino

    style.setFillForegroundColor(palette.getColor(57).getIndex());
    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

    // CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setFontName("Arial");
    // style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
    // style.setFillPattern(CellStyle.SOLID_FOREGROUND);
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
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    styleVal.setBorderBottom(CellStyle.BORDER_THIN);
    styleVal.setBorderTop(CellStyle.BORDER_THIN);
    styleVal.setBorderLeft(CellStyle.BORDER_THIN);
    styleVal.setBorderRight(CellStyle.BORDER_THIN);
    font.setColor(HSSFColor.BLACK.index);
    styleVal.setFont(font);

    // CELL STYLE PER I VALORI
    HSSFCellStyle styleValNumber = ((HSSFWorkbook) workbook).createCellStyle();
    font = workbook.createFont();
    font.setFontName("Arial");
    styleValNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    styleValNumber.setBorderBottom(CellStyle.BORDER_THIN);
    styleValNumber.setBorderTop(CellStyle.BORDER_THIN);
    styleValNumber.setBorderLeft(CellStyle.BORDER_THIN);
    styleValNumber.setBorderRight(CellStyle.BORDER_THIN);
    font.setColor(HSSFColor.BLACK.index);
    styleValNumber.setFont(font);
    styleValNumber.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    styleValNumber.setDataFormat(
        workbook.createDataFormat().getFormat("#,###,###,###,#.####"));

    // CELL STYLE GENERICO, DI DEFAULT
    HSSFCellStyle styleValDef = ((HSSFWorkbook) workbook).createCellStyle();
    font = workbook.createFont();
    font.setFontName("Arial");
    styleValDef.setBorderBottom(CellStyle.BORDER_THIN);
    styleValDef.setBorderTop(CellStyle.BORDER_THIN);
    styleValDef.setBorderLeft(CellStyle.BORDER_THIN);
    styleValDef.setBorderRight(CellStyle.BORDER_THIN);
    font.setColor(HSSFColor.BLACK.index);
    styleValDef.setFont(font);

    int col = -1;
    int rowCount = 0;

    HSSFCellStyle styleTitle = ((HSSFWorkbook) workbook).createCellStyle();
    styleTitle.setWrapText(true);
    styleTitle.setBorderBottom(CellStyle.BORDER_THIN);
    styleTitle.setBorderTop(CellStyle.BORDER_THIN);
    styleTitle.setBorderLeft(CellStyle.BORDER_THIN);
    styleTitle.setBorderRight(CellStyle.BORDER_THIN);
    styleTitle.setFillForegroundColor(palette.getColor(59).getIndex());
    styleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    styleTitle.setFont(font);

    StringBuffer titleText = new StringBuffer("Bando: " + bando + " \n");
    titleText.append("Descrizione breve: " + descrBreve + " \n");
    titleText.append("Descrizione estesa: " + descrEstesa);

    // Create title block
    HSSFRow title = sheet.createRow(0);
    title.createCell(0).setCellValue(titleText.toString());
    title.getCell(0).setCellStyle(styleTitle);
    sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 12));
    rowCount = 6;
    // create header row
    HSSFRow header = sheet.createRow(rowCount);
    for (ColReportVO colonnaVO : graficoVO.getReportVO().getColsDefinitions())
    {
      col++;
      header.createCell(col).setCellValue(colonnaVO.getLabel());
      header.getCell(col).setCellStyle(style);
      sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, col, col));
    }
    // create values
    HSSFRow aRow;
    HSSFCellStyle styleWrap = ((HSSFWorkbook) workbook).createCellStyle();
    font = workbook.createFont();
    font.setFontName("Arial");
    styleWrap.setBorderBottom(CellStyle.BORDER_THIN);
    styleWrap.setBorderTop(CellStyle.BORDER_THIN);
    styleWrap.setBorderLeft(CellStyle.BORDER_THIN);
    styleWrap.setBorderRight(CellStyle.BORDER_THIN);
    font.setColor(HSSFColor.BLACK.index);
    styleWrap.setFont(font);
    styleWrap.setWrapText(true);

    for (RowsReportVO rigaVO : graficoVO.getReportVO().getRowValues())
    {
      col = -1;
      rowCount++;
      aRow = sheet.createRow(rowCount);

      for (CellReportVO cella : rigaVO.get("c"))
      {
        col++;
        createCell(aRow, col, cella);

        /*
         * if(cella.getV() instanceof Number &&
         * Double.parseDouble(cella.getV().toString()) % 1 != 0)
         * aRow.getCell(col).setCellStyle(styleValNumber); else
         */
        aRow.getCell(col).setCellStyle(styleValDef);

      }
    }
  }

  private void createCell(HSSFRow row, int col, CellReportVO cella)
  {
    if (cella.getV() instanceof Number)
    {
      row.createCell(col, HSSFCell.CELL_TYPE_NUMERIC)
          .setCellValue(new Double(String.valueOf(cella.getV())));
    }
    else
    {
      row.createCell(col, HSSFCell.CELL_TYPE_STRING)
          .setCellValue(IuffiUtils.STRING.nvl(cella.getV()));

    }
  }

}
