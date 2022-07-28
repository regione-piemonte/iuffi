package it.csi.iuffi.iuffiweb.presentation.estrazionicampione;

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

import it.csi.iuffi.iuffiweb.dto.estrazionecampione.DettaglioEstrazioneDTO;
import it.csi.iuffi.iuffiweb.dto.estrazionecampione.DettaglioImportoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.estrazionecampione.RigaSimulazioneEstrazioneDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DettaglioEstrazioneExcelBuilder extends AbstractExcelView
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

    DettaglioEstrazioneDTO dettaglio = (DettaglioEstrazioneDTO) model
        .get("estrazioneDTO");

    // create a new Excel sheet
    HSSFSheet sheet = workbook.createSheet("Totali");
    sheet.setDefaultColumnWidth(30);

    HSSFRow aRow = null;
    int rowCount = 0;
    int countCol = -1;
    if (dettaglio.getTotali() != null
        && dettaglio.getTotali().getElencoDettagliImporti() != null)
    {
      // Stampo i totali
      for (DettaglioImportoDTO item : dettaglio.getTotali()
          .getElencoDettagliImporti())
      {
        countCol = -1;
        aRow = sheet.createRow(rowCount++);

        countCol++;
        aRow.createCell(countCol).setCellValue(item.getCodiceLivello());
        aRow.getCell(countCol).setCellStyle(styleValDef);

        countCol++;
        aRow.createCell(countCol).setCellValue(item.getDecodificaImporto());
        aRow.getCell(countCol).setCellStyle(styleValDef);

        countCol++;
        aRow.createCell(countCol).setCellValue(item.getImportoStr());
        aRow.getCell(countCol).setCellStyle(styleCurrency);

      }

      countCol = 1;
      aRow = sheet.createRow(rowCount++);
      aRow.createCell(countCol)
          .setCellValue("Importo totale richiesto complessivo");
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol)
          .setCellValue(dettaglio.getTotali().getImpTotaleComplessivoStr());
      aRow.getCell(countCol).setCellStyle(styleCurrency);

      countCol = 1;
      aRow = sheet.createRow(rowCount++);
      aRow.createCell(countCol)
          .setCellValue("Importo totale richiesto estraz. attuale");
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol)
          .setCellValue(dettaglio.getTotali().getImpTotaleAttualeStr());
      aRow.getCell(countCol).setCellStyle(styleCurrency);
      countCol++;
      aRow.createCell(countCol).setCellValue("Data estraz. attuale");
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol)
          .setCellValue(dettaglio.getTotali().getDataEstrazioneAttualeStr());
      aRow.getCell(countCol).setCellStyle(styleValDef);
    }

    rowCount++;
    rowCount++;

    // Importi prec/Attuali
    if (dettaglio.getImportiPA() != null && dettaglio.getElenco() != null
        && dettaglio.getImportiPA().getElencoImporti() != null)
    {
      countCol = -1;
      for (DettaglioImportoDTO item : dettaglio.getImportiPA()
          .getElencoImporti())
      {
        countCol = -1;
        aRow = sheet.createRow(rowCount++);

        countCol++;
        aRow.createCell(countCol).setCellValue(item.getCodiceLivello());
        aRow.getCell(countCol).setCellStyle(styleValDef);

        if ("A".equals(item.getTipoImporto()))
        {
          countCol++;
          aRow.createCell(countCol).setCellValue("Imp. estrazione attuale");
          aRow.getCell(countCol).setCellStyle(styleValDef);
          countCol++;
          aRow.createCell(countCol).setCellValue(item.getImportoRichiestoStr());
          aRow.getCell(countCol).setCellStyle(styleCurrency);
          countCol++;
          aRow.createCell(countCol)
              .setCellValue("Imp. estrazione attuale - casuale");
          aRow.getCell(countCol).setCellStyle(styleValDef);
          countCol++;
          aRow.createCell(countCol)
              .setCellValue(item.getImportoRichiestoParteCasualStr());
          aRow.getCell(countCol).setCellStyle(styleCurrency);
          countCol++;
          aRow.createCell(countCol)
              .setCellValue("Imp. estrazione attuale -  analisi del rischio");
          aRow.getCell(countCol).setCellStyle(styleValDef);
          countCol++;
          aRow.createCell(countCol).setCellValue(item.getAnalisiRischioStr());
          aRow.getCell(countCol).setCellStyle(styleCurrency);
        }
        else
          if ("P".equals(item.getTipoImporto()))
          {
            countCol++;
            aRow.createCell(countCol)
                .setCellValue("Imp. estrazioni precedenti");
            aRow.getCell(countCol).setCellStyle(styleValDef);
            countCol++;
            aRow.createCell(countCol)
                .setCellValue(item.getImportoRichiestoStr());
            aRow.getCell(countCol).setCellStyle(styleCurrency);
            countCol++;
            aRow.createCell(countCol)
                .setCellValue("Imp. estrazioni precedenti - casuale");
            aRow.getCell(countCol).setCellStyle(styleValDef);
            countCol++;
            aRow.createCell(countCol)
                .setCellValue(item.getImportoRichiestoParteCasualStr());
            aRow.getCell(countCol).setCellStyle(styleCurrency);
            countCol++;
            aRow.createCell(countCol).setCellValue(
                "Imp. estrazioni precedenti - analisi del rischio");
            aRow.getCell(countCol).setCellStyle(styleValDef);
            countCol++;
            aRow.createCell(countCol).setCellValue(item.getAnalisiRischioStr());
            aRow.getCell(countCol).setCellStyle(styleCurrency);
          }
      }

      countCol = -1;
      aRow = sheet.createRow(rowCount++);

      countCol++;
      aRow.createCell(countCol).setCellValue("TOTALI");
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol).setCellValue("Importo totale estratto");
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol)
          .setCellValue(dettaglio.getImportiPA().getTotaleImportiEstrattiStr());
      aRow.getCell(countCol).setCellStyle(styleCurrency);
      countCol++;
      aRow.createCell(countCol)
          .setCellValue("Importo totale estratto - casuale");
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol).setCellValue(
          dettaglio.getImportiPA().getTotaleImportiEstrattiCasualeStr());
      aRow.getCell(countCol).setCellStyle(styleCurrency);
      countCol++;
      aRow.createCell(countCol)
          .setCellValue("Importo totale estratto - analisi del rischio");
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol).setCellValue(
          dettaglio.getImportiPA().getTotaleAnalisiDelRischioStr());
      aRow.getCell(countCol).setCellStyle(styleCurrency);

      countCol = -1;
      aRow = sheet.createRow(rowCount++);

      countCol++;
      aRow.createCell(countCol).setCellValue("% Estrazione");
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol).setCellValue("% Totale");
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol).setCellValue(
          dettaglio.getImportiPA().getPercTotaleImportiEstrattiStr());
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol).setCellValue("% Casuale");
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol).setCellValue(
          dettaglio.getImportiPA().getPercTotaleImportiEstrattiCasualeStr());
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol).setCellValue("% Analisi del rischio");
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol).setCellValue(
          dettaglio.getImportiPA().getPercTotaleAnalisiDelRischioStr());
      aRow.getCell(countCol).setCellStyle(styleValDef);

    }

    sheet = workbook.createSheet("Elenco");
    sheet.setDefaultColumnWidth(30);

    rowCount = 0;
    countCol = -1;

    if (dettaglio.getImportiPA() != null && dettaglio.getElenco() != null
        && dettaglio.getImportiPA().getElencoImporti() != null)
    {
      // Descrizione
      countCol = -1;
      aRow = sheet.createRow(rowCount++);

      countCol++;
      aRow.createCell(countCol).setCellValue("Tipo Estrazione");
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol).setCellValue(dettaglio.getImportiPA()
          .getElencoImporti().get(0).getDescrTipoEstrazione());
      aRow.getCell(countCol).setCellStyle(styleValDef);

      countCol++;
      aRow.createCell(countCol).setCellValue("Stato Estrazione");
      aRow.getCell(countCol).setCellStyle(styleValDef);
      countCol++;
      aRow.createCell(countCol).setCellValue(dettaglio.getImportiPA()
          .getElencoImporti().get(0).getDescrStatoEstrazione());
      aRow.getCell(countCol).setCellStyle(styleValDef);
    }
    rowCount++;

    // Elencorisultati
    countCol = -1;
    HSSFRow header = sheet.createRow(rowCount++);

    countCol++;
    header.createCell(countCol).setCellValue("Ente Delegato");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Misura");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Num. domanda");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Tipo domanda");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Stato");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("CUAA");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Denominazione");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Imp. richiesto");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Punti");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Classe");
    header.getCell(countCol).setCellStyle(styleHeaderTab);
    countCol++;
    header.createCell(countCol).setCellValue("Estr.");
    header.getCell(countCol).setCellStyle(styleHeaderTab);

    if (dettaglio.getElenco() != null && dettaglio.getElenco().size() > 0)
    {
      for (RigaSimulazioneEstrazioneDTO item : dettaglio.getElenco())
      {
        countCol = -1;
        aRow = sheet.createRow(rowCount++);

        countCol++;
        aRow.createCell(countCol).setCellValue(item.getDescrEnteDelegato());
        aRow.getCell(countCol).setCellStyle(styleValDef);

        countCol++;
        aRow.createCell(countCol).setCellValue(item.getListLivelliText());
        aRow.getCell(countCol).setCellStyle(styleValDef);

        countCol++;
        aRow.createCell(countCol).setCellValue(item.getIdentificativo());
        aRow.getCell(countCol).setCellStyle(styleValDef);

        countCol++;
        aRow.createCell(countCol).setCellValue(item.getDescrTipoDomanda());
        aRow.getCell(countCol).setCellStyle(styleValDef);

        countCol++;
        aRow.createCell(countCol).setCellValue(item.getDescrStato());
        aRow.getCell(countCol).setCellStyle(styleValDef);

        countCol++;
        aRow.createCell(countCol).setCellValue(item.getCuaAzie());
        aRow.getCell(countCol).setCellStyle(styleValDef);

        countCol++;
        aRow.createCell(countCol).setCellValue(item.getDenominazioneAzie());
        aRow.getCell(countCol).setCellStyle(styleValDef);

        countCol++;
        aRow.createCell(countCol).setCellValue(
            IuffiUtils.FORMAT.formatCurrency(item.getImportoRichiesto()));
        aRow.getCell(countCol).setCellStyle(styleCurrency);

        countCol++;
        aRow.createCell(countCol).setCellValue(
            IuffiUtils.FORMAT.formatCurrency(item.getPunteggio()));
        aRow.getCell(countCol).setCellStyle(styleValDef);

        countCol++;
        aRow.createCell(countCol).setCellValue(item.getClasse());
        aRow.getCell(countCol).setCellStyle(styleValDef);

        countCol++;
        aRow.createCell(countCol).setCellValue(item.getFlagEstratta());
        aRow.getCell(countCol).setCellStyle(styleValDef);
      }
    }
  }

}
