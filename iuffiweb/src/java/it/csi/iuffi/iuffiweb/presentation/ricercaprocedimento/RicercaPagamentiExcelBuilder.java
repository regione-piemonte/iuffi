package it.csi.iuffi.iuffiweb.presentation.ricercaprocedimento;

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
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import it.csi.iuffi.iuffiweb.dto.reportistica.CellReportVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ColReportVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.ReportVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.RowsReportVO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RicercaPagamentiExcelBuilder extends AbstractExcelView
{

  @Override
  protected void buildExcelDocument(Map<String, Object> model,
      HSSFWorkbook workbook, HttpServletRequest request,
      HttpServletResponse response)
      throws Exception
  {

    ReportVO reportPagamenti = (ReportVO) model.get("reportPagamenti");

    // create a new Excel sheet
    HSSFSheet sheet = workbook.createSheet("Elenco Pagamenti");
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
    styleValNumber.setDataFormat(
        workbook.createDataFormat().getFormat("#,###,###,###,#0.00"));

    // create header row
    int col = -1;
    HSSFRow header = sheet.createRow(0);

    if (reportPagamenti != null && reportPagamenti.getColsDefinitions() != null)
    {
      for (ColReportVO colonna : reportPagamenti.getColsDefinitions())
      {
        col++;
        header.createCell(col).setCellValue(colonna.getLabel());
        header.getCell(col).setCellStyle(style);
      }
    }

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
    // create data rows
    int rowCount = 1;
    int countCol = -1;

    if (reportPagamenti != null && reportPagamenti.getRowValues() != null)
    {
      HSSFRow aRow = null;

      for (RowsReportVO rigaVO : reportPagamenti.getRowValues())
      {
        col = -1;
        rowCount++;
        aRow = sheet.createRow(rowCount);

        for (CellReportVO cella : rigaVO.get("c"))
        {
          col++;
          createCell(aRow, col, cella);

          if (cella.getV() instanceof Number)
          {
            aRow.getCell(col).setCellStyle(styleValNumber);
          }
          else
          {
            aRow.getCell(col).setCellStyle(styleValDef);
          }
        }
      }
    }

    CellRangeAddress address = new CellRangeAddress(0, rowCount - 1, 0,
        countCol);
    sheet.setAutoFilter(address);
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
