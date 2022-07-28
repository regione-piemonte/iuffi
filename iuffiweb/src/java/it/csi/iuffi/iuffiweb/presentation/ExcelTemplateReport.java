package it.csi.iuffi.iuffiweb.presentation;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;

import it.csi.iuffi.iuffiweb.dto.reportistica.CellReportVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.GraficoVO;
import it.csi.iuffi.iuffiweb.dto.reportistica.RowsReportVO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ExcelTemplateReport
{

  public static void buildExcelDocument(Workbook workbook, GraficoVO graficoVO)
      throws Exception
  {
    Sheet sheet = workbook.getSheetAt(0);
    int col = -1;
    int rowCount = 0;
    Row aRow;

    List<RowsReportVO> rowValues = graficoVO.getReportVO().getRowValues();

    for (RowsReportVO rigaVO : rowValues)
    {
      col = -1;
      rowCount++;

      aRow = sheet.getRow(rowCount);
      if (aRow == null)
      {
        aRow = sheet.createRow(rowCount);
      }
      for (CellReportVO cella : rigaVO.get("c"))
      {
        col++;
        createCell(aRow, col, cella);
      }
    }
    if (rowValues.size() > 0)
    {
      XSSFSheet xssfSheet = (XSSFSheet) sheet;
      List<XSSFTable> tables = xssfSheet.getTables();
      if (tables != null && tables.size() > 0)
      {
        XSSFTable table = tables.get(0);
        String ref = getTableCellRange(rowValues);
        table.getCTTable().setRef(ref);
        CTAutoFilter ctAutoFilter = table.getCTTable().getAutoFilter();
        ctAutoFilter.setRef(ref);
      }
    }
  }

  private static String getTableCellRange(List<RowsReportVO> rowValues)
  {
    List<CellReportVO> columns = rowValues.get(0).get("c");
    int size = columns.size() - 1;
    StringBuilder last = new StringBuilder();
    last.insert(0, ((char) ('A' + size % 26)));
    size = size / 26;
    size--;
    if (size >= 0)
    {
      last.insert(0, ((char) ('A' + size % 26)));
    }
    last.append(rowValues.size() + 1);
    return "A1:" + last;
  }

  private static void createCell(Row row, int col, CellReportVO cella)
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
