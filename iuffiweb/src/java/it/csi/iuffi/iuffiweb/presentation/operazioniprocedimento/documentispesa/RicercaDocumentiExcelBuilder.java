package it.csi.iuffi.iuffiweb.presentation.operazioniprocedimento.documentispesa;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import it.csi.iuffi.iuffiweb.dto.ExcelRicevutePagInterventoDTO;
import it.csi.iuffi.iuffiweb.dto.ExcelRigaDocumentoSpesaDTO;
import it.csi.iuffi.iuffiweb.dto.ExcelRigaDomandaDTO;
import it.csi.iuffi.iuffiweb.dto.ExcelRigaRicevutaPagDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.DocumentoSpesaVO;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class RicercaDocumentiExcelBuilder extends AbstractExcelView
{

  @SuppressWarnings("unchecked")
  @Override
  protected void buildExcelDocument(Map<String, Object> model,
      HSSFWorkbook workbook, HttpServletRequest request,
      HttpServletResponse response)
      throws Exception
  {

    List<ExcelRicevutePagInterventoDTO> elenco = (List<ExcelRicevutePagInterventoDTO>) model
        .get("elencoInterventi");
    List<ExcelRigaDocumentoSpesaDTO> elencoRicevute = (List<ExcelRigaDocumentoSpesaDTO>) request
        .getSession().getAttribute("elencoRicevute");
    request.getSession().removeAttribute("elencoRicevute");
    TestataProcedimento testataProcedimento = (TestataProcedimento) request
        .getSession().getAttribute(TestataProcedimento.SESSION_NAME);
    String idProcediemnto = IuffiFactory.getProcedimento(request)
        .getIdentificativo();

    // create a new Excel sheet
    HSSFSheet sheet2 = workbook.createSheet("Elenco Ricevute");
    sheet2.setDefaultColumnWidth(40);

    HSSFSheet sheet = workbook.createSheet("Elenco Interventi");
    sheet.setDefaultColumnWidth(40);

    CellRangeAddress cellRangeAddressProgressivo = null;

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

    // create style for header cells
    HSSFCellStyle styleCenter = ((HSSFWorkbook) workbook).createCellStyle();
    styleCenter.setFillForegroundColor(palette.getColor(57).getIndex());
    styleCenter.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    styleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    styleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

    // font riga totali arancio
    HSSFCellStyle styleValTotaleParziale = ((HSSFWorkbook) workbook)
        .createCellStyle();
    Font font34 = workbook.createFont();
    font34.setFontName("Century Gothic");
    font34.setFontHeight((short) (9 * 20));
    font34.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    styleValTotaleParziale.setWrapText(true);
    font34.setColor(HSSFColor.BLACK.index);
    styleValTotaleParziale.setFont(font34);
    styleValTotaleParziale.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    styleValTotaleParziale.setDataFormat(
        workbook.createDataFormat().getFormat("#,###,###,###,#0.00"));
    styleValTotaleParziale.setBorderBottom(CellStyle.BORDER_THIN);
    styleValTotaleParziale.setBorderTop(CellStyle.BORDER_THIN);
    styleValTotaleParziale.setBorderLeft(CellStyle.BORDER_THIN);
    styleValTotaleParziale.setBorderRight(CellStyle.BORDER_THIN);

    // create style for header cells
    HSSFCellStyle styleCenterProgr = ((HSSFWorkbook) workbook)
        .createCellStyle();
    styleCenterProgr.setFillForegroundColor(HSSFColor.AQUA.index);
    styleCenterProgr.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    styleCenterProgr.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    styleCenterProgr.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

    // create style for header cells
    HSSFCellStyle styleLeft = ((HSSFWorkbook) workbook).createCellStyle();
    styleLeft.setFillForegroundColor(palette.getColor(57).getIndex());
    styleLeft.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    styleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);

    // create style for header cells
    HSSFCellStyle styleLeftInterv = ((HSSFWorkbook) workbook).createCellStyle();
    styleLeftInterv.setFillForegroundColor(HSSFColor.AQUA.index);
    styleLeftInterv.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    styleLeftInterv.setAlignment(HSSFCellStyle.ALIGN_LEFT);

    // font riga totali
    HSSFCellStyle styleValTotale = ((HSSFWorkbook) workbook).createCellStyle();
    Font font33 = workbook.createFont();
    font33.setFontName("Century Gothic");
    font33.setFontHeight((short) (9 * 20));
    font33.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    styleValTotale.setWrapText(true);
    font33.setColor(HSSFColor.BLACK.index);
    styleValTotale.setFont(font33);
    styleValTotale.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    styleValTotale.setDataFormat(
        workbook.createDataFormat().getFormat("#,###,###,###,#0.00"));
    styleValTotale.setBorderBottom(CellStyle.BORDER_THIN);
    styleValTotale.setBorderTop(CellStyle.BORDER_THIN);
    styleValTotale.setBorderLeft(CellStyle.BORDER_THIN);
    styleValTotale.setBorderRight(CellStyle.BORDER_THIN);
    // font intestazione
    HSSFCellStyle styleValDefIntestazione = ((HSSFWorkbook) workbook)
        .createCellStyle();
    Font font31 = workbook.createFont();
    font31.setFontName("Century Gothic");
    font31.setFontHeight((short) (9 * 20));
    styleValDefIntestazione.setWrapText(true);
    font31.setColor(HSSFColor.BLACK.index);
    styleValDefIntestazione.setFont(font31);

    // font intestazione rosso italico
    HSSFCellStyle styleValDefIntestazioneRed = ((HSSFWorkbook) workbook)
        .createCellStyle();
    Font font32 = workbook.createFont();

    font32.setFontName("Century Gothic");
    font32.setFontHeight((short) (9 * 20));
    font32.setItalic(true);
    styleValDefIntestazioneRed.setWrapText(true);
    styleValDefIntestazioneRed
        .setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    styleValDefIntestazioneRed.setFillPattern(CellStyle.SOLID_FOREGROUND);
    font32.setColor(HSSFColor.BLACK.index);
    styleValDefIntestazioneRed.setFont(font32);

    // CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setFontName("Century Gothic");
    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    style.setAlignment(CellStyle.ALIGN_CENTER);
    style.setBorderBottom(CellStyle.BORDER_MEDIUM);
    style.setBorderTop(CellStyle.BORDER_MEDIUM);
    style.setBorderLeft(CellStyle.BORDER_MEDIUM);
    style.setBorderRight(CellStyle.BORDER_MEDIUM);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setColor(HSSFColor.WHITE.index);
    style.setFont(font);
    styleLeft.setFont(font);
    styleCenter.setFont(font);

    // CELL STYLE PER I VALORI
    HSSFCellStyle styleVal = ((HSSFWorkbook) workbook).createCellStyle();
    Font font2 = workbook.createFont();
    font2.setFontName("Century Gothic");
    styleVal.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    styleVal.setBorderBottom(CellStyle.BORDER_THIN);
    styleVal.setBorderTop(CellStyle.BORDER_THIN);
    styleVal.setBorderLeft(CellStyle.BORDER_THIN);
    styleVal.setBorderRight(CellStyle.BORDER_THIN);
    font2.setColor(HSSFColor.BLACK.index);
    styleVal.setFont(font2);

    // CELL STYLE GENERICO, DI DEFAULT
    HSSFCellStyle styleValDef = ((HSSFWorkbook) workbook).createCellStyle();
    Font font3 = workbook.createFont();
    font3.setFontName("Century Gothic");
    styleValDef.setWrapText(true);
    font3.setFontHeight((short) (9 * 20));
    styleValDef.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    styleValDef.setBorderBottom(CellStyle.BORDER_THIN);
    styleValDef.setBorderTop(CellStyle.BORDER_THIN);
    styleValDef.setBorderLeft(CellStyle.BORDER_THIN);
    styleValDef.setBorderRight(CellStyle.BORDER_THIN);
    font3.setColor(HSSFColor.BLACK.index);
    styleValDef.setFont(font3);

    HSSFCellStyle styleValDefCenter = ((HSSFWorkbook) workbook)
        .createCellStyle();
    font3 = workbook.createFont();
    font3.setFontName("Century Gothic");
    styleValDef.setWrapText(true);
    font3.setFontHeight((short) (9 * 20));
    styleValDefCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    styleValDefCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    font3.setColor(HSSFColor.BLACK.index);
    styleValDefCenter.setFont(font3);

    HSSFCellStyle styleValDefNoBorder = ((HSSFWorkbook) workbook)
        .createCellStyle();
    styleValDefNoBorder.setWrapText(true);
    styleValDefNoBorder.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    styleValDefNoBorder.setFont(font3);

    HSSFCellStyle styleValRedNoBorder = ((HSSFWorkbook) workbook)
        .createCellStyle();
    Font font4 = workbook.createFont();
    font4.setFontName("Century Gothic");
    styleValRedNoBorder.setWrapText(true);
    styleValRedNoBorder.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    font4.setColor(HSSFColor.RED.index);
    styleValRedNoBorder.setFont(font4);

    // CELL STYLE NUMERI
    HSSFCellStyle styleValNumber = ((HSSFWorkbook) workbook).createCellStyle();
    Font font5 = workbook.createFont();
    font5.setFontName("Century Gothic");
    styleValNumber.setWrapText(true);
    font3.setFontHeight((short) (9 * 20));
    styleValNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
    styleValNumber.setBorderBottom(CellStyle.BORDER_THIN);
    styleValNumber.setBorderTop(CellStyle.BORDER_THIN);
    styleValNumber.setBorderLeft(CellStyle.BORDER_THIN);
    styleValNumber.setBorderRight(CellStyle.BORDER_THIN);
    font5.setColor(HSSFColor.BLACK.index);
    styleValNumber.setFont(font5);
    styleValNumber.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    styleValNumber.setDataFormat(
        workbook.createDataFormat().getFormat("#,###,###,###,#0.00"));

    // CELL STYLE NUMERI
    HSSFCellStyle styleValNumberCenter = ((HSSFWorkbook) workbook)
        .createCellStyle();
    font5 = workbook.createFont();
    font5.setFontName("Century Gothic");
    styleValNumberCenter.setWrapText(true);
    font3.setFontHeight((short) (9 * 20));
    styleValNumberCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    font5.setColor(HSSFColor.BLACK.index);
    styleValNumberCenter.setFont(font5);
    styleValNumberCenter.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    styleValNumberCenter.setDataFormat(
        workbook.createDataFormat().getFormat("#,###,###,###,#0.00"));

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

    int col = -1;
    int rowCount = 0;

    HSSFRow aRow = sheet.createRow(rowCount);
    aRow.createCell(0).setCellValue("Procedimento");
    aRow.getCell(0).setCellStyle(styleValDefIntestazione);
    aRow.createCell(2).setCellValue(idProcediemnto + "");
    aRow.getCell(2).setCellStyle(styleValDefIntestazione);
    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowCount, rowCount,
        0, 1);
    sheet.addMergedRegion(cellRangeAddress);
    rowCount++;
    aRow = sheet.createRow(rowCount);
    aRow.createCell(0).setCellValue("CUAA");
    aRow.getCell(0).setCellStyle(styleValDefIntestazione);
    aRow.createCell(2).setCellValue(testataProcedimento.getCuaa());
    aRow.getCell(2).setCellStyle(styleValDefIntestazione);
    cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 0, 1);
    sheet.addMergedRegion(cellRangeAddress);
    rowCount++;
    aRow = sheet.createRow(rowCount);
    aRow.createCell(0).setCellValue("Denominazione");
    aRow.getCell(0).setCellStyle(styleValDefIntestazione);
    aRow.createCell(2)
        .setCellValue(testataProcedimento.getDenominazioneAzienda());
    aRow.getCell(2).setCellStyle(styleValDefIntestazione);
    cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 0, 1);
    sheet.addMergedRegion(cellRangeAddress);
    sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 10));
    rowCount++;
    rowCount++;
    HSSFRow header = sheet.createRow(rowCount);

    aRow = sheet.createRow(rowCount);
    aRow.createCell(0).setCellValue("");
    aRow.getCell(0).setCellStyle(style);
    aRow.createCell(1).setCellValue("Documento di spesa");
    aRow.getCell(1).setCellStyle(style);
    cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 1, 4);
    HSSFRegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    sheet.addMergedRegion(cellRangeAddress);

    cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 5, 9);
    aRow.createCell(5).setCellValue("Ricevuta di pagamento");
    aRow.getCell(5).setCellStyle(style);
    HSSFRegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    sheet.addMergedRegion(cellRangeAddress);

    rowCount++;

    header = sheet.createRow(rowCount);
    col = -1;
    col++;
    header.createCell(col).setCellValue("Progr.");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Importo lordo");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Importo netto");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col)
        .setCellValue("Importo associato per la rendicontazione");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Importo rendicontato");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Estremi Pagamento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Data Pagamento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Modalita Pagamento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Importo Pagamento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col)
        .setCellValue("Importo Associato per la rendicontazione");
    header.getCell(col).setCellStyle(style);
    rowCount++;
    // create header row
    int countCol = -1;

    BigDecimal totaleRicevuteDocumentiAssBdGLOBALE = BigDecimal.ZERO;
    BigDecimal totaleImportoRendicontatoInterventoGLOBALE = BigDecimal.ZERO;
    BigDecimal totaleImportoAssociatoInterventoGLOBALE = BigDecimal.ZERO;
    BigDecimal totaleImportoLordoIntervento = BigDecimal.ZERO;
    BigDecimal totaleImportoNettoIntervento = BigDecimal.ZERO;
    BigDecimal totaleImportoAssociatoIntervento = BigDecimal.ZERO;
    BigDecimal totaleImportoRendicontatoIntervento = BigDecimal.ZERO;
    BigDecimal totaleRicevuteDocumentiBd = BigDecimal.ZERO;
    BigDecimal totaleRicevuteDocumentiAssBd = BigDecimal.ZERO;

    if (elenco != null)
    {
      String oldDescrIntervento = "";
      int nInt = 0;
      for (ExcelRicevutePagInterventoDTO intervento : elenco)
      {
        // una per ogni fornitore + una sola per l'intervento
        int rowspan = 0; // riga intervento

        int nInterv = 0;

        for (ExcelRicevutePagInterventoDTO intervento2 : elenco)
        {
          if (intervento.getProgressivo().equals(intervento2.getProgressivo()))
          {
            nInterv++;// conto interventi uguali -> cosi so a che linea fare i
                      // totali
          }
        }
        nInt++; // incremento ad ogni intervento -> quando è uguale a nInterv ->
                // è l'ultimo record dello stesso intervento, quindi poi faccio
                // i totali

        String oldFornitore = "";

        if (!intervento.getProgressivo().equals(oldDescrIntervento))
        {
          if (!oldFornitore.equals(intervento.getDescrFornitore()))
          {
            rowspan = 0;

            for (ExcelRicevutePagInterventoDTO intervento2 : elenco)
              for (ExcelRigaDocumentoSpesaDTO documento : intervento2
                  .getDettaglioDocumento())
              {
                if (intervento.getProgressivo()
                    .equals(intervento2.getProgressivo()))
                {
                  rowspan += documento.getRicevute().size() + 2;
                }
              }
            aRow = sheet.createRow(rowCount);

            aRow.createCell(0).setCellValue(intervento.getProgressivo());
            aRow.getCell(0).setCellStyle(styleCenterProgr);
            cellRangeAddressProgressivo = new CellRangeAddress(rowCount,
                rowCount + rowspan, 0, 0);

          }
          oldDescrIntervento = intervento.getProgressivo();

          countCol = 1;
          aRow.createCell(countCol)
              .setCellValue(intervento.getDescrInterventoUF());
          aRow.getCell(countCol).setCellStyle(styleLeftInterv);

          cellRangeAddress = new CellRangeAddress(rowCount, rowCount, countCol,
              countCol + 8);
          HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress,
              sheet, workbook);
          HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress,
              sheet, workbook);
          HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress,
              sheet, workbook);
          HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
              cellRangeAddress, sheet, workbook);
          sheet.addMergedRegion(cellRangeAddress);
          rowCount++;

          totaleImportoLordoIntervento = BigDecimal.ZERO;
          totaleImportoNettoIntervento = BigDecimal.ZERO;
          totaleImportoAssociatoIntervento = BigDecimal.ZERO;
          totaleImportoRendicontatoIntervento = BigDecimal.ZERO;
          totaleRicevuteDocumentiBd = BigDecimal.ZERO;
          totaleRicevuteDocumentiAssBd = BigDecimal.ZERO;
        }

        for (ExcelRigaDocumentoSpesaDTO documento : intervento
            .getDettaglioDocumento())
        {
          if (!oldFornitore.equals(documento.getFornitore()))
          {
            oldFornitore = documento.getFornitore();
            countCol = 1;
            aRow = sheet.createRow(rowCount);
            aRow.createCell(countCol).setCellValue(documento.getFornitore());
            aRow.getCell(countCol).setCellStyle(styleValDefIntestazioneRed);

            cellRangeAddress = new CellRangeAddress(rowCount, rowCount,
                countCol, countCol + 8);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress,
                sheet, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet, workbook);
            sheet.addMergedRegion(cellRangeAddress);
            rowCount++;
          }

          int countricevute = 0;
          int totaleRicevute = documento.getRicevute().size();
          BigDecimal totaleRicevuteBd = BigDecimal.ZERO;
          BigDecimal totaleRicevuteAssBd = BigDecimal.ZERO;

          // sommo i valori del documento
          totaleImportoLordoIntervento = totaleImportoLordoIntervento
              .add(IuffiUtils.NUMBERS.nvl(documento.getImportoLordo()));
          totaleImportoNettoIntervento = totaleImportoNettoIntervento
              .add(IuffiUtils.NUMBERS.nvl(documento.getImportoNetto()));
          totaleImportoAssociatoIntervento = totaleImportoAssociatoIntervento
              .add(IuffiUtils.NUMBERS.nvl(documento.getImportoAssociato()));
          totaleImportoRendicontatoIntervento = totaleImportoRendicontatoIntervento
              .add(IuffiUtils.NUMBERS
                  .nvl(documento.getImportoRendicontato()));

          for (ExcelRigaRicevutaPagDTO ricevuta : documento.getRicevute())
          {
            totaleRicevuteBd = totaleRicevuteBd
                .add(IuffiUtils.NUMBERS.nvl(ricevuta.getImportoPagamento()));
            totaleRicevuteAssBd = totaleRicevuteAssBd.add(IuffiUtils.NUMBERS
                .nvl(ricevuta.getImportoPagamentoAssociato()));
            totaleRicevuteDocumentiBd = totaleRicevuteDocumentiBd
                .add(IuffiUtils.NUMBERS.nvl(ricevuta.getImportoPagamento()));
            totaleRicevuteDocumentiAssBd = totaleRicevuteDocumentiAssBd
                .add(IuffiUtils.NUMBERS
                    .nvl(ricevuta.getImportoPagamentoAssociato()));

            aRow = sheet.createRow(rowCount);
            rowCount++;
            if (countricevute == 0)
            {
              cellRangeAddress = null;

              countCol = 1;
              aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                  .formatCurrency(documento.getImportoLordo()));
              aRow.getCell(countCol).setCellStyle(styleValDefCenter);
              cellRangeAddress = new CellRangeAddress(rowCount - 1,
                  rowCount + totaleRicevute - 1, countCol, countCol);
              sheet.addMergedRegion(cellRangeAddress);
              countCol++;

              aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                  .formatCurrency(documento.getImportoNetto()));
              cellRangeAddress = new CellRangeAddress(rowCount - 1,
                  rowCount + totaleRicevute - 1, countCol, countCol);
              aRow.getCell(countCol).setCellStyle(styleValDefCenter);
              sheet.addMergedRegion(cellRangeAddress);
              countCol++;

              aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                  .formatCurrency(documento.getImportoAssociato()));
              aRow.getCell(countCol).setCellStyle(styleValNumberCenter);
              cellRangeAddress = new CellRangeAddress(rowCount - 1,
                  rowCount + totaleRicevute - 1, countCol, countCol);
              sheet.addMergedRegion(cellRangeAddress);
              countCol++;

              aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                  .formatCurrency(documento.getImportoRendicontato()));
              aRow.getCell(countCol).setCellStyle(styleValNumberCenter);
              cellRangeAddress = new CellRangeAddress(rowCount - 1,
                  rowCount + totaleRicevute - 1, countCol, countCol);
              sheet.addMergedRegion(cellRangeAddress);
              countCol++;

            }
            else
            {
              countCol = 5;
            }
            countricevute++;

            aRow.createCell(countCol)
                .setCellValue(ricevuta.getNumeroPagamento());
            aRow.getCell(countCol).setCellStyle(styleValNumber);
            countCol++;
            aRow.createCell(countCol)
                .setCellValue(ricevuta.getDataPagamentoStr());
            aRow.getCell(countCol).setCellStyle(styleValDef);
            countCol++;
            aRow.createCell(countCol)
                .setCellValue(ricevuta.getModalitaPagamento());
            aRow.getCell(countCol).setCellStyle(styleValDef);
            countCol++;
            aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                .formatCurrency(ricevuta.getImportoPagamento()));
            aRow.getCell(countCol).setCellStyle(styleValNumber);
            countCol++;
            aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                .formatCurrency(ricevuta.getImportoPagamentoAssociato()));
            aRow.getCell(countCol).setCellStyle(styleValNumber);
            countCol++;
          }

          // riga per totali ricevute
          aRow = sheet.createRow(rowCount);
          rowCount++;
          aRow.createCell(5).setCellValue("");
          aRow.getCell(5).setCellStyle(styleValTotaleParziale);
          aRow.createCell(6).setCellValue("");
          aRow.getCell(6).setCellStyle(styleValTotaleParziale);
          aRow.createCell(7).setCellValue("SUBTOTALE: ");
          aRow.getCell(7).setCellStyle(styleValTotaleParziale);
          aRow.createCell(8).setCellValue(
              IuffiUtils.FORMAT.formatCurrency(totaleRicevuteBd));
          aRow.getCell(8).setCellStyle(styleValTotaleParziale);
          aRow.createCell(9).setCellValue(
              IuffiUtils.FORMAT.formatCurrency(totaleRicevuteAssBd));
          aRow.getCell(9).setCellStyle(styleValTotaleParziale);
        }

        // totale per interventi

        if (nInt == nInterv)
        {
          nInt = 0;
          aRow = sheet.createRow(rowCount);
          rowCount++;
          int colTot = 0;
          aRow.createCell(colTot).setCellValue("TOTALE: ");
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
              .formatCurrency(totaleImportoLordoIntervento));
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
              .formatCurrency(totaleImportoNettoIntervento));
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
              .formatCurrency(totaleImportoAssociatoIntervento));
          totaleImportoAssociatoInterventoGLOBALE = totaleImportoAssociatoInterventoGLOBALE
              .add(totaleImportoAssociatoIntervento);
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
              .formatCurrency(totaleImportoRendicontatoIntervento));
          totaleImportoRendicontatoInterventoGLOBALE = totaleImportoRendicontatoInterventoGLOBALE
              .add(totaleImportoRendicontatoIntervento);
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue("");
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue("");
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue("");
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue(
              IuffiUtils.FORMAT.formatCurrency(totaleRicevuteDocumentiBd));
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
              .formatCurrency(totaleRicevuteDocumentiAssBd));
          totaleRicevuteDocumentiAssBdGLOBALE = totaleRicevuteDocumentiAssBdGLOBALE
              .add(totaleRicevuteDocumentiAssBd);
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
        }

        sheet.addMergedRegion(cellRangeAddressProgressivo);

        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN,
            cellRangeAddressProgressivo, sheet, workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
            cellRangeAddressProgressivo, sheet, workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN,
            cellRangeAddressProgressivo, sheet, workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
            cellRangeAddressProgressivo, sheet, workbook);
      }

      // CALCOLO TOTALI FINALI DOC DA CONTARE UNA VOLTA SOLA
      Map<Long, BigDecimal> mapImportiLordiDocContatiSoloUnaVolta = new HashMap<>();
      Map<Long, BigDecimal> mapImportiNettiDocContatiSoloUnaVolta = new HashMap<>();
      Map<String, BigDecimal> mapImportiRicevuteContatiSoloUnaVolta = new HashMap<>();

      for (ExcelRicevutePagInterventoDTO intervento : elenco)
        for (ExcelRigaDocumentoSpesaDTO documento : intervento
            .getDettaglioDocumento())
        {
          mapImportiLordiDocContatiSoloUnaVolta.put(documento.getIdDocumento(),
              documento.getImportoLordo());
          mapImportiNettiDocContatiSoloUnaVolta.put(documento.getIdDocumento(),
              documento.getImportoNetto());
          for (ExcelRigaRicevutaPagDTO ricev : documento.getRicevute())
          {
            mapImportiRicevuteContatiSoloUnaVolta.put(
                documento.getIdDocumento() + " " + ricev.getNumeroPagamento(),
                ricev.getImportoPagamento());
          }
        }
      BigDecimal totImportoLordoDocContatiSoloUnaVolta = BigDecimal.ZERO;
      BigDecimal totImportoNettoDocContatiSoloUnaVolta = BigDecimal.ZERO;
      BigDecimal totImportoRicevuteContateSoloUnaVolta = BigDecimal.ZERO;

      Iterator<Entry<Long, BigDecimal>> it = mapImportiLordiDocContatiSoloUnaVolta
          .entrySet().iterator();
      while (it.hasNext())
      {
        Entry<Long, BigDecimal> pair = it.next();
        totImportoLordoDocContatiSoloUnaVolta = totImportoLordoDocContatiSoloUnaVolta
            .add((BigDecimal) pair.getValue());
      }

      it = mapImportiNettiDocContatiSoloUnaVolta.entrySet().iterator();
      while (it.hasNext())
      {
        Entry<Long, BigDecimal> pair = it.next();
        totImportoNettoDocContatiSoloUnaVolta = totImportoNettoDocContatiSoloUnaVolta
            .add((BigDecimal) pair.getValue());
      }

      Iterator<Entry<String, BigDecimal>> it2 = mapImportiRicevuteContatiSoloUnaVolta
          .entrySet().iterator();
      while (it2.hasNext())
      {
        Entry<String, BigDecimal> pair = it2.next();
        totImportoRicevuteContateSoloUnaVolta = totImportoRicevuteContateSoloUnaVolta
            .add((BigDecimal) pair.getValue());
      }

      // totale per TOTALI GLOBALI
      rowCount++;
      aRow = sheet.createRow(rowCount);

      int colTot = 0;
      aRow.createCell(colTot).setCellValue("TOTALE: ");
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
          .formatCurrency(totImportoLordoDocContatiSoloUnaVolta));
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
          .formatCurrency(totImportoNettoDocContatiSoloUnaVolta));
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
          .formatCurrency(totaleImportoAssociatoInterventoGLOBALE));
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
          .formatCurrency(totaleImportoRendicontatoInterventoGLOBALE));
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue("");
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue("");
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue("");
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
          .formatCurrency(totImportoRicevuteContateSoloUnaVolta));
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
          .formatCurrency(totaleRicevuteDocumentiAssBdGLOBALE));
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
    }

    /*
     * =========================================================================
     * ====================================
     * 
     * 
     * FOGLIO DUE ELENCO RICEVUTE
     * 
     * 
     * =========================================================================
     * ====================================
     */

    col = -1;
    rowCount = 0;

    aRow = sheet2.createRow(rowCount);
    aRow.createCell(0).setCellValue("Procedimento");
    aRow.getCell(0).setCellStyle(styleValDefIntestazione);
    aRow.createCell(1).setCellValue(idProcediemnto + "");
    aRow.getCell(1).setCellStyle(styleValDefIntestazione);
    rowCount++;
    aRow = sheet2.createRow(rowCount);
    aRow.createCell(0).setCellValue("CUAA");
    aRow.getCell(0).setCellStyle(styleValDefIntestazione);
    aRow.createCell(1).setCellValue(testataProcedimento.getCuaa());
    aRow.getCell(1).setCellStyle(styleValDefIntestazione);
    rowCount++;
    aRow = sheet2.createRow(rowCount);
    aRow.createCell(0).setCellValue("Denominazione");
    aRow.getCell(0).setCellStyle(styleValDefIntestazione);
    aRow.createCell(1)
        .setCellValue(testataProcedimento.getDenominazioneAzienda());
    aRow.getCell(1).setCellStyle(styleValDefIntestazione);
    sheet2.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 10));
    rowCount++;
    rowCount++;

    header = sheet2.createRow(rowCount);

    col++;
    header.createCell(col).setCellValue("Data Documento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Numero Documento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Tipo Documento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Note Documento Spesa");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Fornitore");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Importo Netto documento spesa");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Importo IVA");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Importo Lordo");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col)
        .setCellValue("Importo Associato per la rendicontazione");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Numero Pagamento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Data Pagamento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Modalita Pagamento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Note");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Importo Pagamento Lordo");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col)
        .setCellValue("Pagamento Associato per la rendicontazione");
    header.getCell(col).setCellStyle(style);

    rowCount++;
    if (elencoRicevute != null)
    {
      for (ExcelRigaDocumentoSpesaDTO documento : elencoRicevute)
      {
        int countricevute = 0;
        int totaleRicevute = documento.getRicevute().size();
        BigDecimal totaleRicevuteBd = BigDecimal.ZERO;
        BigDecimal totaleRicevuteAssociateBd = BigDecimal.ZERO;
        for (ExcelRigaRicevutaPagDTO ricevuta : documento.getRicevute())
        {
          totaleRicevuteAssociateBd = totaleRicevuteAssociateBd
              .add(IuffiUtils.NUMBERS
                  .nvl(ricevuta.getImportoPagamentoAssociato()));
          totaleRicevuteBd = totaleRicevuteBd
              .add(IuffiUtils.NUMBERS.nvl(ricevuta.getImportoPagamento()));
          aRow = sheet2.createRow(rowCount);
          rowCount++;
          if (countricevute == 0)
          {
            cellRangeAddress = null;

            countCol = 0;
            aRow.createCell(countCol)
                .setCellValue(documento.getDataDocumentoStr());
            aRow.getCell(countCol).setCellStyle(styleValDef);
            cellRangeAddress = new CellRangeAddress(rowCount,
                rowCount + totaleRicevute - 1, countCol, countCol);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress,
                sheet2, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            sheet2.addMergedRegion(cellRangeAddress);
            countCol++;

            aRow.createCell(countCol)
                .setCellValue(documento.getNumeroDocumento());
            aRow.getCell(countCol).setCellStyle(styleValDef);
            cellRangeAddress = new CellRangeAddress(rowCount,
                rowCount + totaleRicevute - 1, countCol, countCol);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress,
                sheet2, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            sheet2.addMergedRegion(cellRangeAddress);
            countCol++;

            aRow.createCell(countCol)
                .setCellValue(documento.getTipoDocumento());
            cellRangeAddress = new CellRangeAddress(rowCount,
                rowCount + totaleRicevute - 1, countCol, countCol);
            aRow.getCell(countCol).setCellStyle(styleValDef);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress,
                sheet2, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            sheet2.addMergedRegion(cellRangeAddress);
            countCol++;

            aRow.createCell(countCol)
                .setCellValue(documento.getNoteDocumentoSpesa());
            aRow.getCell(countCol).setCellStyle(styleValDef);
            cellRangeAddress = new CellRangeAddress(rowCount,
                rowCount + totaleRicevute - 1, countCol, countCol);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress,
                sheet2, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            sheet2.addMergedRegion(cellRangeAddress);
            countCol++;

            aRow.createCell(countCol).setCellValue(documento.getFornitore());
            aRow.getCell(countCol).setCellStyle(styleValDef);
            cellRangeAddress = new CellRangeAddress(rowCount,
                rowCount + totaleRicevute - 1, countCol, countCol);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress,
                sheet2, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            sheet2.addMergedRegion(cellRangeAddress);
            countCol++;

            aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                .formatCurrency(documento.getImportoNetto()));
            aRow.getCell(countCol).setCellStyle(styleValNumber);
            cellRangeAddress = new CellRangeAddress(rowCount,
                rowCount + totaleRicevute - 1, countCol, countCol);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress,
                sheet2, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            sheet2.addMergedRegion(cellRangeAddress);
            countCol++;

            aRow.createCell(countCol).setCellValue(
                IuffiUtils.FORMAT.formatCurrency(documento.getImportoIva()));
            aRow.getCell(countCol).setCellStyle(styleValNumber);
            cellRangeAddress = new CellRangeAddress(rowCount,
                rowCount + totaleRicevute - 1, countCol, countCol);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress,
                sheet2, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            sheet2.addMergedRegion(cellRangeAddress);
            countCol++;

            aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                .formatCurrency(documento.getImportoLordo()));
            aRow.getCell(countCol).setCellStyle(styleValNumber);
            cellRangeAddress = new CellRangeAddress(rowCount,
                rowCount + totaleRicevute - 1, countCol, countCol);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress,
                sheet2, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            sheet2.addMergedRegion(cellRangeAddress);
            countCol++;

            aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                .formatCurrency(documento.getImportoAssociato()));
            aRow.getCell(countCol).setCellStyle(styleValNumber);
            cellRangeAddress = new CellRangeAddress(rowCount,
                rowCount + totaleRicevute - 1, countCol, countCol);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress,
                sheet2, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet2, workbook);
            sheet2.addMergedRegion(cellRangeAddress);
            countCol++;

          }
          else
          {
            countCol = 9;
          }
          countricevute++;

          aRow.createCell(countCol).setCellValue(ricevuta.getNumeroPagamento());
          aRow.getCell(countCol).setCellStyle(styleValNumber);
          countCol++;
          aRow.createCell(countCol)
              .setCellValue(ricevuta.getDataPagamentoStr());
          aRow.getCell(countCol).setCellStyle(styleValDef);
          countCol++;
          aRow.createCell(countCol)
              .setCellValue(ricevuta.getModalitaPagamento());
          aRow.getCell(countCol).setCellStyle(styleValDef);
          countCol++;
          aRow.createCell(countCol).setCellValue(ricevuta.getNote());
          aRow.getCell(countCol).setCellStyle(styleValDef);
          countCol++;
          aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
              .formatCurrency(ricevuta.getImportoPagamento()));
          aRow.getCell(countCol).setCellStyle(styleValNumber);
          countCol++;
          aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
              .formatCurrency(ricevuta.getImportoPagamentoAssociato()));
          aRow.getCell(countCol).setCellStyle(styleValNumber);
          countCol++;
        }

        // riga per totali ricevute
        aRow = sheet2.createRow(rowCount);
        rowCount++;
        aRow.createCell(12).setCellValue("totale");
        aRow.getCell(12).setCellStyle(styleValTotaleParziale);
        aRow.createCell(13).setCellValue(
            IuffiUtils.FORMAT.formatCurrency(totaleRicevuteBd));
        aRow.getCell(13).setCellStyle(styleValTotaleParziale);
        aRow.createCell(14).setCellValue(
            IuffiUtils.FORMAT.formatCurrency(totaleRicevuteAssociateBd));
        aRow.getCell(14).setCellStyle(styleValTotaleParziale);
      }
    }

    /*
     * =========================================================================
     * ====================================
     * 
     * 
     * FOGLIO TRE RIEPILOGO PER DOMANDA
     * 
     * 
     * =========================================================================
     * ====================================
     */

    // Preparo l'elenco di procedimenti oggetti a partire da un normale elenco
    // di documenti spesa. Tutti quelli che devo visualizzare.
    List<DocumentoSpesaVO> documentiConAllegatiEDomande = (List<DocumentoSpesaVO>) request
        .getSession().getAttribute("documentiConAllegatiEDomande");
    request.getSession().removeAttribute("documentiConAllegatiEDomande");

    List<ExcelRigaDomandaDTO> elencoDomande = (List<ExcelRigaDomandaDTO>) request
        .getSession().getAttribute("elencoDomande");
    if (elencoDomande != null && !elencoDomande.isEmpty())
    {
      // Creo nuovo foglio
      HSSFSheet sheet3 = workbook.createSheet("Elenco Domande Pagamento");
      sheet3.setDefaultColumnWidth(30);
      sheet3.autoSizeColumn(0);

      // Scrivo intestazione
      col = -1;
      rowCount = 0;

      aRow = sheet3.createRow(rowCount);
      aRow.createCell(0).setCellValue("Procedimento");
      aRow.getCell(0).setCellStyle(styleValDefIntestazione);
      aRow.createCell(2).setCellValue(idProcediemnto + "");
      aRow.getCell(2).setCellStyle(styleValDefIntestazione);
      cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 0, 1);
      sheet3.addMergedRegion(cellRangeAddress);

      rowCount++;
      aRow = sheet3.createRow(rowCount);
      aRow.createCell(0).setCellValue("CUAA");
      aRow.getCell(0).setCellStyle(styleValDefIntestazione);
      aRow.createCell(2).setCellValue(testataProcedimento.getCuaa());
      aRow.getCell(2).setCellStyle(styleValDefIntestazione);
      cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 0, 1);
      sheet3.addMergedRegion(cellRangeAddress);

      rowCount++;
      aRow = sheet3.createRow(rowCount);
      aRow.createCell(0).setCellValue("Denominazione");
      aRow.getCell(0).setCellStyle(styleValDefIntestazione);
      aRow.createCell(2)
          .setCellValue(testataProcedimento.getDenominazioneAzienda());
      aRow.getCell(2).setCellStyle(styleValDefIntestazione);
      cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 0, 1);
      sheet3.addMergedRegion(cellRangeAddress);
      sheet3.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 10));
      rowCount++;
      rowCount++;

      Collections.sort(elencoDomande, new Comparator<ExcelRigaDomandaDTO>()
      {
        public int compare(ExcelRigaDomandaDTO o1, ExcelRigaDomandaDTO o2)
        {
          if (o1.getPrefix() != null && o2.getPrefix() != null)
            return o1.getPrefix().compareTo(o2.getPrefix());
          else
            return 0;
        }
      });

      for (ExcelRigaDomandaDTO domanda : elencoDomande)
      {
        Collections.sort(domanda.getInterventi(),
            new Comparator<ExcelRicevutePagInterventoDTO>()
            {
              public int compare(ExcelRicevutePagInterventoDTO o1,
                  ExcelRicevutePagInterventoDTO o2)
              {
                return Integer.parseInt(o1.getProgressivo())
                    - Integer.parseInt(o2.getProgressivo());
              }
            });

        if (domanda.getPrefix() != null)
          rowCount = createTablePerDomanda(documentiConAllegatiEDomande,
              domanda, workbook, sheet3, domanda.getInterventi(), col, rowCount,
              style, styleValDefIntestazione, styleValDefIntestazioneRed,
              idProcediemnto, testataProcedimento,
              styleLeftInterv, styleValTotale, styleValNumber,
              styleValTotaleParziale, styleValDefCenter, styleValDef,
              styleCenterProgr, styleValNumberCenter);

      }
      sheet3.autoSizeColumn(0);
    }
    sheet.autoSizeColumn(0);

  }

  // TODO funz
  private int createTablePerDomanda(List<DocumentoSpesaVO> elencoDoc,
      ExcelRigaDomandaDTO p, HSSFWorkbook workbook, HSSFSheet sheet,
      List<ExcelRicevutePagInterventoDTO> elenco, int col, int rowCount,
      HSSFCellStyle style, HSSFCellStyle styleValDefIntestazione,
      HSSFCellStyle styleValDefIntestazioneRed, String idProcediemnto,
      TestataProcedimento testataProcedimento, HSSFCellStyle styleLeftInterv,
      HSSFCellStyle styleValTotale, HSSFCellStyle styleValNumber,
      HSSFCellStyle styleValTotaleParziale, HSSFCellStyle styleValDefCenter,
      HSSFCellStyle styleValDef, HSSFCellStyle styleCenterProgr,
      HSSFCellStyle styleValNumberCenter)
  {
    CellRangeAddress cellRangeAddressProgressivo = null;

    HSSFRow aRow = sheet.createRow(rowCount);
    aRow.createCell(0)
        .setCellValue(p.getDescrOggetto() + " - " + p.getPrefix());
    aRow.getCell(0).setCellStyle(style);
    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowCount, rowCount,
        0, 1);
    HSSFRegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    sheet.addMergedRegion(cellRangeAddress);
    rowCount++;

    HSSFRow header = sheet.createRow(rowCount);

    aRow = sheet.createRow(rowCount);
    aRow.createCell(0).setCellValue("");
    aRow.getCell(0).setCellStyle(style);
    aRow.createCell(1).setCellValue("Documento di spesa");
    aRow.getCell(1).setCellStyle(style);
    cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 1, 4);
    HSSFRegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    sheet.addMergedRegion(cellRangeAddress);

    cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 5, 9);
    aRow.createCell(5).setCellValue("Ricevuta di pagamento");
    aRow.getCell(5).setCellStyle(style);
    HSSFRegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress,
        sheet, workbook);
    sheet.addMergedRegion(cellRangeAddress);

    rowCount++;

    header = sheet.createRow(rowCount);
    col = -1;
    col++;
    header.createCell(col).setCellValue("Progr.");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Importo lordo");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Importo netto");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col)
        .setCellValue("Importo associato per la rendicontazione");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Importo rendicontato");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Estremi Pagamento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Data Pagamento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Modalita Pagamento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col).setCellValue("Importo Pagamento");
    header.getCell(col).setCellStyle(style);

    col++;
    header.createCell(col)
        .setCellValue("Importo Associato per la rendicontazione");
    header.getCell(col).setCellStyle(style);
    rowCount++;
    // create header row
    int countCol = -1;

    BigDecimal totaleRicevuteDocumentiAssBdGLOBALE = BigDecimal.ZERO;
    BigDecimal totaleImportoRendicontatoInterventoGLOBALE = BigDecimal.ZERO;
    BigDecimal totaleImportoAssociatoInterventoGLOBALE = BigDecimal.ZERO;
    BigDecimal totaleImportoLordoIntervento = BigDecimal.ZERO;
    BigDecimal totaleImportoNettoIntervento = BigDecimal.ZERO;
    BigDecimal totaleImportoAssociatoIntervento = BigDecimal.ZERO;
    BigDecimal totaleImportoRendicontatoIntervento = BigDecimal.ZERO;
    BigDecimal totaleRicevuteDocumentiBd = BigDecimal.ZERO;
    BigDecimal totaleRicevuteDocumentiAssBd = BigDecimal.ZERO;
    if (elenco != null)
    {

      String oldDescrIntervento = "";
      int nInt = 0;
      for (ExcelRicevutePagInterventoDTO intervento : elenco)
      {
        // una per ogni fornitore + una sola per l'intervento
        int rowspan = 0; // riga intervento

        int nInterv = 0;

        for (ExcelRicevutePagInterventoDTO intervento2 : elenco)
        {
          if (intervento.getProgressivo().equals(intervento2.getProgressivo()))
          {
            nInterv++;// conto interventi uguali -> cosi so a che linea fare i
                      // totali
          }
        }
        nInt++; // incremento ad ogni intervento -> quando è uguale a nInterv ->
                // è l'ultimo record dello stesso intervento, quindi poi faccio
                // i totali

        String oldFornitore = "";

        if (!intervento.getProgressivo().equals(oldDescrIntervento))
        {
          if (!oldFornitore.equals(intervento.getDescrFornitore()))
          {
            rowspan = 0;

            for (ExcelRicevutePagInterventoDTO intervento2 : elenco)
              for (ExcelRigaDocumentoSpesaDTO documento : intervento2
                  .getDettaglioDocumento())
              {
                if (intervento.getProgressivo()
                    .equals(intervento2.getProgressivo()))
                {
                  rowspan += documento.getRicevute().size() + 2;
                }
              }
            aRow = sheet.createRow(rowCount);

            aRow.createCell(0).setCellValue(intervento.getProgressivo());
            aRow.getCell(0).setCellStyle(styleCenterProgr);
            cellRangeAddressProgressivo = new CellRangeAddress(rowCount,
                rowCount + rowspan, 0, 0);

            for (int i = rowCount; i < rowCount + rowspan; i++)
            {
              if (sheet.getRow(i) != null)
                sheet.getRow(i).getCell(0).setCellStyle(styleCenterProgr);
            }

          }
          oldDescrIntervento = intervento.getProgressivo();

          countCol = 1;
          aRow.createCell(countCol)
              .setCellValue(intervento.getDescrInterventoUF());
          aRow.getCell(countCol).setCellStyle(styleLeftInterv);

          cellRangeAddress = new CellRangeAddress(rowCount, rowCount, countCol,
              countCol + 8);
          sheet.addMergedRegion(cellRangeAddress);

          HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress,
              sheet, workbook);
          HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress,
              sheet, workbook);
          HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress,
              sheet, workbook);
          HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
              cellRangeAddress, sheet, workbook);
          rowCount++;

          totaleImportoLordoIntervento = BigDecimal.ZERO;
          totaleImportoNettoIntervento = BigDecimal.ZERO;
          totaleImportoAssociatoIntervento = BigDecimal.ZERO;
          totaleImportoRendicontatoIntervento = BigDecimal.ZERO;
          totaleRicevuteDocumentiBd = BigDecimal.ZERO;
          totaleRicevuteDocumentiAssBd = BigDecimal.ZERO;
        }

        for (ExcelRigaDocumentoSpesaDTO documento : intervento
            .getDettaglioDocumento())
        {
          if (!oldFornitore.equals(documento.getFornitore()))
          {
            oldFornitore = documento.getFornitore();
            countCol = 1;
            aRow = sheet.createRow(rowCount);
            aRow.createCell(countCol).setCellValue(documento.getFornitore());
            aRow.getCell(countCol).setCellStyle(styleValDefIntestazioneRed);

            cellRangeAddress = new CellRangeAddress(rowCount, rowCount,
                countCol, countCol + 8);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress,
                sheet, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
                cellRangeAddress, sheet, workbook);
            sheet.addMergedRegion(cellRangeAddress);
            rowCount++;
          }

          int countricevute = 0;
          int totaleRicevute = documento.getRicevute().size();
          BigDecimal totaleRicevuteBd = BigDecimal.ZERO;
          BigDecimal totaleRicevuteAssBd = BigDecimal.ZERO;

          // sommo i valori del documento
          totaleImportoLordoIntervento = totaleImportoLordoIntervento
              .add(IuffiUtils.NUMBERS.nvl(documento.getImportoLordo()));
          totaleImportoNettoIntervento = totaleImportoNettoIntervento
              .add(IuffiUtils.NUMBERS.nvl(documento.getImportoNetto()));
          totaleImportoAssociatoIntervento = totaleImportoAssociatoIntervento
              .add(IuffiUtils.NUMBERS.nvl(documento.getImportoAssociato()));
          totaleImportoRendicontatoIntervento = totaleImportoRendicontatoIntervento
              .add(IuffiUtils.NUMBERS
                  .nvl(documento.getImportoRendicontato()));

          for (ExcelRigaRicevutaPagDTO ricevuta : documento.getRicevute())
          {
            totaleRicevuteBd = totaleRicevuteBd
                .add(IuffiUtils.NUMBERS.nvl(ricevuta.getImportoPagamento()));
            totaleRicevuteAssBd = totaleRicevuteAssBd.add(IuffiUtils.NUMBERS
                .nvl(ricevuta.getImportoPagamentoAssociato()));
            totaleRicevuteDocumentiBd = totaleRicevuteDocumentiBd
                .add(IuffiUtils.NUMBERS.nvl(ricevuta.getImportoPagamento()));
            totaleRicevuteDocumentiAssBd = totaleRicevuteDocumentiAssBd
                .add(IuffiUtils.NUMBERS
                    .nvl(ricevuta.getImportoPagamentoAssociato()));

            aRow = sheet.createRow(rowCount);
            rowCount++;
            if (countricevute == 0)
            {
              cellRangeAddress = null;

              countCol = 1;
              aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                  .formatCurrency(documento.getImportoLordo()));
              aRow.getCell(countCol).setCellStyle(styleValDefCenter);
              cellRangeAddress = new CellRangeAddress(rowCount - 1,
                  rowCount + totaleRicevute - 1, countCol, countCol);
              sheet.addMergedRegion(cellRangeAddress);
              countCol++;

              aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                  .formatCurrency(documento.getImportoNetto()));
              cellRangeAddress = new CellRangeAddress(rowCount - 1,
                  rowCount + totaleRicevute - 1, countCol, countCol);
              aRow.getCell(countCol).setCellStyle(styleValDefCenter);
              sheet.addMergedRegion(cellRangeAddress);
              countCol++;

              aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                  .formatCurrency(documento.getImportoAssociato()));
              aRow.getCell(countCol).setCellStyle(styleValNumberCenter);
              cellRangeAddress = new CellRangeAddress(rowCount - 1,
                  rowCount + totaleRicevute - 1, countCol, countCol);
              sheet.addMergedRegion(cellRangeAddress);
              countCol++;

              aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                  .formatCurrency(documento.getImportoRendicontato()));
              aRow.getCell(countCol).setCellStyle(styleValNumberCenter);
              cellRangeAddress = new CellRangeAddress(rowCount - 1,
                  rowCount + totaleRicevute - 1, countCol, countCol);
              sheet.addMergedRegion(cellRangeAddress);
              countCol++;

            }
            else
            {
              countCol = 5;
            }
            countricevute++;

            aRow.createCell(countCol)
                .setCellValue(ricevuta.getNumeroPagamento());
            aRow.getCell(countCol).setCellStyle(styleValNumber);
            countCol++;
            aRow.createCell(countCol)
                .setCellValue(ricevuta.getDataPagamentoStr());
            aRow.getCell(countCol).setCellStyle(styleValDef);
            countCol++;
            aRow.createCell(countCol)
                .setCellValue(ricevuta.getModalitaPagamento());
            aRow.getCell(countCol).setCellStyle(styleValDef);
            countCol++;
            aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                .formatCurrency(ricevuta.getImportoPagamento()));
            aRow.getCell(countCol).setCellStyle(styleValNumber);
            countCol++;
            aRow.createCell(countCol).setCellValue(IuffiUtils.FORMAT
                .formatCurrency(ricevuta.getImportoPagamentoAssociato()));
            aRow.getCell(countCol).setCellStyle(styleValNumber);
            countCol++;
          }

          // riga per totali ricevute
          aRow = sheet.createRow(rowCount);
          rowCount++;
          aRow.createCell(5).setCellValue("");
          aRow.getCell(5).setCellStyle(styleValTotaleParziale);
          aRow.createCell(6).setCellValue("");
          aRow.getCell(6).setCellStyle(styleValTotaleParziale);
          aRow.createCell(7).setCellValue("SUBTOTALE: ");
          aRow.getCell(7).setCellStyle(styleValTotaleParziale);
          aRow.createCell(8).setCellValue(
              IuffiUtils.FORMAT.formatCurrency(totaleRicevuteBd));
          aRow.getCell(8).setCellStyle(styleValTotaleParziale);
          aRow.createCell(9).setCellValue(
              IuffiUtils.FORMAT.formatCurrency(totaleRicevuteAssBd));
          aRow.getCell(9).setCellStyle(styleValTotaleParziale);
        }

        // totale per interventi

        if (nInt == nInterv)
        {
          nInt = 0;
          aRow = sheet.createRow(rowCount);
          rowCount++;
          int colTot = 0;
          aRow.createCell(colTot).setCellValue("TOTALE: ");
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
              .formatCurrency(totaleImportoLordoIntervento));
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
              .formatCurrency(totaleImportoNettoIntervento));
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
              .formatCurrency(totaleImportoAssociatoIntervento));
          totaleImportoAssociatoInterventoGLOBALE = totaleImportoAssociatoInterventoGLOBALE
              .add(totaleImportoAssociatoIntervento);
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
              .formatCurrency(totaleImportoRendicontatoIntervento));
          totaleImportoRendicontatoInterventoGLOBALE = totaleImportoRendicontatoInterventoGLOBALE
              .add(totaleImportoRendicontatoIntervento);
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue("");
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue("");
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue("");
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue(
              IuffiUtils.FORMAT.formatCurrency(totaleRicevuteDocumentiBd));
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
          aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
              .formatCurrency(totaleRicevuteDocumentiAssBd));
          totaleRicevuteDocumentiAssBdGLOBALE = totaleRicevuteDocumentiAssBdGLOBALE
              .add(totaleRicevuteDocumentiAssBd);
          aRow.getCell(colTot).setCellStyle(styleValTotale);
          colTot++;
        }

        sheet.addMergedRegion(cellRangeAddressProgressivo);

        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN,
            cellRangeAddressProgressivo, sheet, workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN,
            cellRangeAddressProgressivo, sheet, workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN,
            cellRangeAddressProgressivo, sheet, workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN,
            cellRangeAddressProgressivo, sheet, workbook);
      }

      // CALCOLO TOTALI FINALI DOC DA CONTARE UNA VOLTA SOLA
      Map<Long, BigDecimal> mapImportiLordiDocContatiSoloUnaVolta = new HashMap<>();
      Map<Long, BigDecimal> mapImportiNettiDocContatiSoloUnaVolta = new HashMap<>();
      Map<String, BigDecimal> mapImportiRicevuteContatiSoloUnaVolta = new HashMap<>();

      for (ExcelRicevutePagInterventoDTO intervento : elenco)
        for (ExcelRigaDocumentoSpesaDTO documento : intervento
            .getDettaglioDocumento())
        {
          mapImportiLordiDocContatiSoloUnaVolta.put(documento.getIdDocumento(),
              documento.getImportoLordo());
          mapImportiNettiDocContatiSoloUnaVolta.put(documento.getIdDocumento(),
              documento.getImportoNetto());
          for (ExcelRigaRicevutaPagDTO ricev : documento.getRicevute())
          {
            mapImportiRicevuteContatiSoloUnaVolta.put(
                documento.getIdDocumento() + " " + ricev.getNumeroPagamento(),
                ricev.getImportoPagamento());
          }
        }
      BigDecimal totImportoLordoDocContatiSoloUnaVolta = BigDecimal.ZERO;
      BigDecimal totImportoNettoDocContatiSoloUnaVolta = BigDecimal.ZERO;
      BigDecimal totImportoRicevuteContateSoloUnaVolta = BigDecimal.ZERO;

      Iterator<Entry<Long, BigDecimal>> it = mapImportiLordiDocContatiSoloUnaVolta
          .entrySet().iterator();
      while (it.hasNext())
      {
        Entry<Long, BigDecimal> pair = it.next();
        totImportoLordoDocContatiSoloUnaVolta = totImportoLordoDocContatiSoloUnaVolta
            .add((BigDecimal) pair.getValue());
      }

      it = mapImportiNettiDocContatiSoloUnaVolta.entrySet().iterator();
      while (it.hasNext())
      {
        Entry<Long, BigDecimal> pair = it.next();
        totImportoNettoDocContatiSoloUnaVolta = totImportoNettoDocContatiSoloUnaVolta
            .add((BigDecimal) pair.getValue());
      }

      Iterator<Entry<String, BigDecimal>> it2 = mapImportiRicevuteContatiSoloUnaVolta
          .entrySet().iterator();
      while (it2.hasNext())
      {
        Entry<String, BigDecimal> pair = it2.next();
        totImportoRicevuteContateSoloUnaVolta = totImportoRicevuteContateSoloUnaVolta
            .add((BigDecimal) pair.getValue());
      }

      // totale per TOTALI GLOBALI
      rowCount++;
      aRow = sheet.createRow(rowCount);

      int colTot = 0;
      aRow.createCell(colTot).setCellValue("TOTALE: ");
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
          .formatCurrency(totImportoLordoDocContatiSoloUnaVolta));
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
          .formatCurrency(totImportoNettoDocContatiSoloUnaVolta));
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
          .formatCurrency(totaleImportoAssociatoInterventoGLOBALE));
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
          .formatCurrency(totaleImportoRendicontatoInterventoGLOBALE));
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue("");
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue("");
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue("");
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
          .formatCurrency(totImportoRicevuteContateSoloUnaVolta));
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
      aRow.createCell(colTot).setCellValue(IuffiUtils.FORMAT
          .formatCurrency(totaleRicevuteDocumentiAssBdGLOBALE));
      aRow.getCell(colTot).setCellStyle(styleValTotale);
      colTot++;
    }
    return rowCount + 3;
  }

}
