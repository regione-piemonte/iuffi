package it.csi.iuffi.iuffiweb.util.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import it.csi.iuffi.iuffiweb.util.pdf.IValuableElement.ValuableElementType;
import it.csi.smrcomms.siapcommws.dto.smrcomm.SiapCommWsMetadatoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellMetadatoVO;

public class PDFCoordinateExtractor
{
  public void addMetadati(List<AgriWellMetadatoVO> metadati, byte[] pdf,
      String testoData, String testoFirma) throws IOException
  {
    if (testoData == null && testoFirma == null)
    {
      return;
    }
    Map<String, List<PDFRectangle>> map = extractCoordinate(
        new ByteArrayInputStream(pdf), testoData, testoFirma);
    List<PDFRectangle> list = map.get(testoFirma);
    if (list != null)
    {
      for (PDFRectangle box : list)
      {
        addMetadato(metadati, box, "coordinata");
      }
    }
    list = map.get(testoData);
    if (list != null)
    {
      for (PDFRectangle box : list)
      {
        addMetadato(metadati, box, "dataFirma");
      }
    }
  }

  public void addMetadatiSiapWs(List<SiapCommWsMetadatoVO> metadati, byte[] pdf,
      String testoData, String testoFirma) throws IOException
  {
    if (testoData == null && testoFirma == null)
    {
      return;
    }
    Map<String, List<PDFRectangle>> map = extractCoordinate(
        new ByteArrayInputStream(pdf), testoData, testoFirma);
    List<PDFRectangle> list = map.get(testoFirma);
    if (list != null)
    {
      for (PDFRectangle box : list)
      {
        addMetadatoSiapWs(metadati, box, "coordinata");
      }
    }
    list = map.get(testoData);
    if (list != null)
    {
      for (PDFRectangle box : list)
      {
        addMetadatoSiapWs(metadati, box, "dataFirma");
      }
    }
  }

  public void addMetadatoSiapWs(List<SiapCommWsMetadatoVO> metadati,
      PDFRectangle rectangle, String tipo)
  {
    SiapCommWsMetadatoVO metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta(tipo + "Xsinistra");
    metadato.setValoreElemento(String.valueOf(rectangle.getP0().getX()));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta(tipo + "Xdestra");
    metadato.setValoreElemento(String.valueOf(rectangle.getP2().getX()));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta(tipo + "Yalto");
    metadato.setValoreElemento(String.valueOf(rectangle.getP3().getY()));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta(tipo + "Ybasso");
    metadato.setValoreElemento(String.valueOf(rectangle.getP0().getY()));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("paginaFirma");
    metadato.setValoreElemento(String.valueOf(rectangle.getPage() + 1));
    metadati.add(metadato);

  }

  public void addMetadatiProtocollo(List<SiapCommWsMetadatoVO> metadati,
      byte[] pdf, String testoProtocollo) throws IOException
  {
    if (testoProtocollo == null && testoProtocollo == null)
    {
      return;
    }
    Map<String, List<PDFCurvedRectangle>> map = extractCoordinateProtocollo(
        new ByteArrayInputStream(pdf), testoProtocollo);
    List<PDFCurvedRectangle> list = map.get(testoProtocollo);
    if (list != null)
    {
      for (PDFCurvedRectangle box : list)
      {
        addMetadatoProtocollo(metadati, box, "coordinata");
      }
    }
  }

  public void addMetadatoProtocollo(List<SiapCommWsMetadatoVO> metadati,
      PDFCurvedRectangle rectangle, String tipo)
  {
    SiapCommWsMetadatoVO metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta(tipo + "XsinistraProtocollo");
    metadato.setValoreElemento(String.valueOf(rectangle.getX1()));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta(tipo + "XdestraProtocollo");
    metadato.setValoreElemento("18");
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta(tipo + "YaltoProtocollo");
    metadato.setValoreElemento("-35");
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta(tipo + "YbassoProtocollo");
    metadato.setValoreElemento(String.valueOf(rectangle.getY1()));
    metadati.add(metadato);

    metadato = new SiapCommWsMetadatoVO();
    metadato.setNomeEtichetta("numeroPaginaProtocollo");
    metadato.setValoreElemento(String.valueOf(rectangle.getPage() + 1));
    metadati.add(metadato);

  }

  public void addMetadato(List<AgriWellMetadatoVO> metadati,
      PDFRectangle rectangle, String tipo)
  {
    AgriWellMetadatoVO metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta(tipo + "Xsinistra");
    metadato.setValoreEtichetta(String.valueOf(rectangle.getP0().getX()));
    metadati.add(metadato);

    metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta(tipo + "Xdestra");
    metadato.setValoreEtichetta(String.valueOf(rectangle.getP2().getX()));
    metadati.add(metadato);

    metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta(tipo + "Yalto");
    metadato.setValoreEtichetta(String.valueOf(rectangle.getP3().getY()));
    metadati.add(metadato);

    metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta(tipo + "Ybasso");
    metadato.setValoreEtichetta(String.valueOf(rectangle.getP0().getY()));
    metadati.add(metadato);

    metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta("paginaFirma");
    metadato.setValoreEtichetta(String.valueOf(rectangle.getPage() + 1));
    metadati.add(metadato);

  }

  public Map<String, List<PDFRectangle>> extractCoordinate(
      InputStream inputStream, String testoData, String testoFirma)
      throws IOException
  {

    Map<String, List<PDFRectangle>> mapCoordinate = new HashMap<String, List<PDFRectangle>>();

    PDDocument doc = PDDocument.load(inputStream);

    for (int pageCounter = 0; pageCounter < doc
        .getNumberOfPages(); ++pageCounter)
    {
      PDPage page = doc.getPage(pageCounter);
      CustomGraphicsStreamEngine engine = new CustomGraphicsStreamEngine(page,
          pageCounter);
      engine.run();
      searchAndAddCoordinate(mapCoordinate, engine.getValuableElements(),
          testoData, testoFirma);
    }
    doc.close();
    return mapCoordinate;
  }

  public Map<String, List<PDFCurvedRectangle>> extractCoordinateProtocollo(
      InputStream inputStream, String testoProtocollo) throws IOException
  {

    Map<String, List<PDFCurvedRectangle>> mapCoordinate = new HashMap<String, List<PDFCurvedRectangle>>();

    PDDocument doc = PDDocument.load(inputStream);

    for (int pageCounter = 0; pageCounter < doc
        .getNumberOfPages(); ++pageCounter)
    {
      PDPage page = doc.getPage(pageCounter);
      CustomGraphicsStreamEngine engine = new CustomGraphicsStreamEngine(page,
          pageCounter);
      engine.run();

      if (testoProtocollo != null && testoProtocollo.trim().length() > 0)
      {
        searchAndAddRoundedCoordinate(mapCoordinate,
            engine.getValuableElements(), testoProtocollo);
      }

    }
    doc.close();
    return mapCoordinate;
  }

  protected void searchAndAddRoundedCoordinate(
      Map<String, List<PDFCurvedRectangle>> mapCoordinate,
      List<IValuableElement> valuableElements, String testoProtocollo)
  {
    PDFCurvedRectangle lastBox = null;
    for (IValuableElement element : valuableElements)
    {
      if (element.getType() == ValuableElementType.TEXT)
      {
        String text = ((PDFText) element).getText();
        if (testoProtocollo != null)
        {
          if (testoProtocollo.equals(text))
          {
            /*
             * Trovato elemento data ==> prendo l'ultimo rettangolo definito (se
             * il pdf è strutturato correttamente su Modol quello è il box della
             * firma
             */
            if (lastBox != null)
            {
              addCurvedCoordinate(mapCoordinate, testoProtocollo, lastBox);
            }
            else
            {
              /*
               * C'è qualcosa di fortemente sbagliato... la struttura del pdf è
               * non coerente con quanto previsto... non trovo il quadro Data!
               * ERRORE GRAVE!
               */
            }
            continue;
          }
        }
      }
      else
        if (element.getType() == ValuableElementType.CURVED_RECTANGLE)
        {
          lastBox = (PDFCurvedRectangle) element;
        }
    }
  }

  protected void searchAndAddCoordinate(
      Map<String, List<PDFRectangle>> mapCoordinate,
      List<IValuableElement> valuableElements, String testoData,
      String testoFirma)
  {
    PDFRectangle lastBox = null;
    for (IValuableElement element : valuableElements)
    {
      if (element.getType() == ValuableElementType.TEXT)
      {
        String text = ((PDFText) element).getText();
        if (testoData != null)
        {
          if (testoData.equals(text))
          {
            /*
             * Trovato elemento data ==> prendo l'ultimo rettangolo definito (se
             * il pdf è strutturato correttamente su Modol quello è il box della
             * firma
             */
            if (lastBox != null)
            {
              addCoordinate(mapCoordinate, testoData, lastBox);
            }
            else
            {
              /*
               * C'è qualcosa di fortemente sbagliato... la struttura del pdf è
               * non coerente con quanto previsto... non trovo il quadro Data!
               * ERRORE GRAVE!
               */
            }
            continue;
          }
        }
        if (testoFirma != null && text != null)
        {
          if (text.startsWith(testoFirma))
          {
            /*
             * Trovato elemento data ==> prendo l'ultimo rettangolo definito (se
             * il pdf è strutturato correttamente su Modol quello è il box della
             * firma
             */
            if (lastBox != null)
            {
              addCoordinate(mapCoordinate, testoFirma, lastBox);
            }
            else
            {
              /*
               * C'è qualcosa di fortemente sbagliato... la struttura del pdf è
               * non coerente con quanto previsto... non trovo il quadro firma!
               * ERRORE GRAVE!
               */
            }
            continue;
          }
        }
      }
      else
        if (element.getType() == ValuableElementType.RECTANGLE)
        {
          // Al momento non ci sono altri tipi di IValuableElements, quindi se
          // non è un testo è un rettangolo... Se si aggiungessero nuovi tipi si
          // prega di
          // modificare di conseguenza il codice!
          lastBox = (PDFRectangle) element;
        }
    }
  }

  protected void addCoordinate(Map<String, List<PDFRectangle>> mapCoordinate,
      String testo, PDFRectangle lastBox)
  {
    List<PDFRectangle> list = mapCoordinate.get(testo);
    if (list == null)
    {
      list = new ArrayList<PDFRectangle>();
      mapCoordinate.put(testo, list);
    }
    list.add(lastBox);
  }

  protected void addCurvedCoordinate(
      Map<String, List<PDFCurvedRectangle>> mapCoordinate, String testo,
      PDFCurvedRectangle lastBox)
  {
    List<PDFCurvedRectangle> list = mapCoordinate.get(testo);
    if (list == null)
    {
      list = new ArrayList<PDFCurvedRectangle>();
      mapCoordinate.put(testo, list);
    }
    list.add(lastBox);
  }
}
