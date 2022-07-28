package it.csi.iuffi.iuffiweb.util.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.pdf.IValuableElement.ValuableElementType;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellMetadatoVO;

public class PDFWriteSignatureData
{
  public static byte[] writeSignatureData(List<AgriWellMetadatoVO> metadati,
      byte[] pdf, String testoRifData, String testoRifFirma, String testoData,
      String testoFirma, Double offsetFirma, String integrazioneFirma)
      throws IOException
  {
    if (testoRifData == null && testoRifFirma == null)
    {
      return pdf;
    }
    byte[] newPdf = writeValues(new ByteArrayInputStream(pdf), testoRifData,
        testoRifFirma, testoData, testoFirma, offsetFirma, integrazioneFirma);

    AgriWellMetadatoVO metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta("data_firma");
    metadato.setValoreEtichetta(testoData);
    metadati.add(metadato);

    metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta("utente_firma");
    metadato.setValoreEtichetta(testoFirma);
    metadati.add(metadato);
    return newPdf;
  }

  private static byte[] writeValues(InputStream inputStream,
      String testoRifData, String testoRifFirma, String testoData,
      String testoFirma, Double offsetFirma, String integrazioneFirma)
      throws IOException
  {
    PDDocument doc = PDDocument.load(inputStream);

    for (int pageCounter = 0; pageCounter < doc
        .getNumberOfPages(); ++pageCounter)
    {
      PDPage page = doc.getPage(pageCounter);
      CustomGraphicsStreamEngine engine = new CustomGraphicsStreamEngine(page,
          pageCounter);
      engine.run();
      searchAndAddValues(doc, page, engine.getValuableElements(), testoRifData,
          testoRifFirma, testoData, testoFirma, offsetFirma, integrazioneFirma);
    }

    ByteArrayOutputStream oStream = new ByteArrayOutputStream();
    doc.save(oStream);
    doc.close();

    return oStream.toByteArray();
  }

  private static void searchAndAddValues(PDDocument doc, PDPage page,
      List<IValuableElement> valuableElements,
      String testoRifData, String testoRifFirma, String testoData,
      String testoFirma, Double offsetFirma, String integrazioneFirma)
      throws IOException
  {
    PDFRectangle lastBox = null;
    PDPageContentStream contentStream = null;
    contentStream = new PDPageContentStream(doc, page,true,false);
    PDFont pdfFont = PDType1Font.HELVETICA;
    float fontSize = 8;
    for (IValuableElement element : valuableElements)
    {
      if (element.getType() == ValuableElementType.TEXT)
      {
        String text = ((PDFText) element).getText();
        if (testoRifData != null)
        {
          if (testoRifData.equals(text))
          {
            /* Trovato elemento data ==> prendo l'ultimo rettangolo definito (se il pdf è strutturato correttamente su Modol quello è il box della firma */
            if (lastBox != null)
            {
              
              contentStream.beginText();
              contentStream.setFont(pdfFont, fontSize);
              contentStream.newLineAtOffset(Float.parseFloat(String.valueOf(lastBox.getP0().getX()+10)), Float.parseFloat(String.valueOf(lastBox.getP0().getY()+20))); 
              contentStream.showText(testoData);
              contentStream.endText();
              
            }
            else
            {
              /* C'è qualcosa di fortemente sbagliato... la struttura del pdf è non coerente con quanto previsto... non trovo il quadro Data! ERRORE GRAVE! */
            }
            continue;
          }
        }
        if (testoRifFirma != null && text!=null)
        {
          if (text.startsWith(testoRifFirma))
          {
            /* Trovato elemento data ==> prendo l'ultimo rettangolo definito (se il pdf è strutturato correttamente su Modol quello è il box della firma */
            if (lastBox != null)
            {
              
              contentStream.beginText();
              contentStream.setFont(pdfFont, fontSize);
              double offestFirma = 0;
              if(offsetFirma==null)
              {
                offestFirma = 20;
                contentStream.newLineAtOffset(Float.parseFloat(String.valueOf(lastBox.getP0().getX()+2)), Float.parseFloat(String.valueOf(lastBox.getP0().getY()+offestFirma))); 
              }
              else
              {
                offestFirma = 2+offsetFirma;
                contentStream.newLineAtOffset(Float.parseFloat(String.valueOf(lastBox.getP0().getX()+offestFirma)), Float.parseFloat(String.valueOf(lastBox.getP0().getY()-15))); 
              }
              
              //testoFirma potrebbe contenere &acapo& , in quel caso devo forzare il new line
              if(testoFirma.indexOf(IuffiConstants.FIRMA_GRAFOMETRICA.TESTO.A_CAPO)<0)
              {
                contentStream.showText(testoFirma); 
              }
              else
              {
                String[] testi = testoFirma.split(IuffiConstants.FIRMA_GRAFOMETRICA.TESTO.A_CAPO);
                for(int i=0; i<testi.length; i++)
                {
                  if(i>0)
                  {
                    contentStream.newLine();
                  }
                  contentStream.showText(testi[i]);
                }
              }
              contentStream.endText();
              
              //Gestisco integrazione firma
              if(!GenericValidator.isBlankOrNull(integrazioneFirma))
              {
                contentStream.beginText();
                  contentStream.setFont(pdfFont, fontSize);
                  contentStream.newLineAtOffset(Float.parseFloat(String.valueOf(lastBox.getP0().getX()+2)), Float.parseFloat(String.valueOf(lastBox.getP0().getY()+offestFirma-15)));
                  contentStream.showText(integrazioneFirma);
                  contentStream.endText();
              }
              
            }
            else
            {
              /* C'è qualcosa di fortemente sbagliato... la struttura del pdf è non coerente con quanto previsto... non trovo il quadro firma! ERRORE GRAVE! */
            }
            continue;
          }
        }
      }
      else
      {
      if (element.getType() == ValuableElementType.RECTANGLE)
      {
        lastBox = (PDFRectangle) element;
      }
      }
    }
    contentStream.close();
  }
  
  public static int getPageFromString(byte[] pdf, String testo) throws IOException{
	  PDDocument document = PDDocument.load(new ByteArrayInputStream(pdf));
	  PDPageTree list = document.getDocumentCatalog().getPages();
	  PDFTextStripper textStripper=new PDFTextStripper();
	  String pages;
	  boolean found = false; 
	  for(int i = 0; i < list.getCount()+1; i++) 
	  {
          textStripper.setStartPage(i); 
          textStripper.setEndPage(i); 
          pages = textStripper.getText(document); 
          found = pages.contains(testo);
          if (found) {
             return (i); 
          }
	  }
	  return 1;
  }

}
