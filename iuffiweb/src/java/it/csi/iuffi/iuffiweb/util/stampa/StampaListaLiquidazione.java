package it.csi.iuffi.iuffiweb.util.stampa;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IListeLiquidazioneEJB;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONElencoListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class StampaListaLiquidazione extends Stampa
{
  public static final String ROOT_TAG = "Domanda";

  @Override
  public byte[] genera(long idListaLiquidazione, String cuName) throws Exception
  {
    ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
    XMLStreamWriter writer = getXMLStreamWriter(xmlOutputStream);
    IListeLiquidazioneEJB listaLiquidazioneEJB = IuffiUtils.APPLICATION
        .getEjbListeLiquidazione();
    generaXML(writer, idListaLiquidazione, listaLiquidazioneEJB, cuName);
    return callModol(xmlOutputStream.toByteArray());
  }

  protected void generaXML(XMLStreamWriter writer, long idListaLiquidazione,
      IListeLiquidazioneEJB listaLiquidazioneEJB, String cuName)
      throws Exception
  {

    writer.writeStartDocument(DEFAULT_ENCODING, "1.0");
    writer.writeStartElement(ROOT_TAG);
    writer.writeStartElement("Globale");
    // Scrivo i blocchi di default (simulando come se fossero dei fragment)
    RigaJSONElencoListaLiquidazioneDTO listaLiquidazione = listaLiquidazioneEJB
        .getListaLiquidazioneById(idListaLiquidazione);

    writeFragment("TITOLI_LISTA_LIQUIDAZIONE", writer, listaLiquidazione,
        listaLiquidazioneEJB, idListaLiquidazione, cuName);
    writeFragment("LISTA_LIQUIDAZIONE", writer, listaLiquidazione,
        listaLiquidazioneEJB, idListaLiquidazione, cuName);
    writeFragment("FIRMA_LISTA_LIQUIDAZIONE", writer, listaLiquidazione,
        listaLiquidazioneEJB, idListaLiquidazione, cuName);

    BandoDTO bando = listaLiquidazioneEJB
        .getInformazioniBando(listaLiquidazioneEJB
            .getIdBandoByIdListaLiquidazione(idListaLiquidazione));
    if (bando != null && bando.getCodiceTipoBando().compareTo("P") == 0)
      writeTag(writer, "ListaPremio", "true", true);
    else
      writeTag(writer, "ListaPremio", "false", true);

    writer.writeEndElement();
    writer.writeEndElement();
    writer.writeEndDocument();
  }

  protected String getCodiceModulo()
  {
    return "IUF_ListaLiquidazione";
  }

  protected String getCodiceModello()
  {
    return "IUF_ListaLiquidazione";
  }

  protected String getRifAdobe()
  {
    return BASE_RIF_ADOBE + "ListaLiquidazione.xdp";
  }

  @Override
  public String getDefaultFileName(long idProcedimentoOggetto)
  {
    return "Lista liquidazione.pdf";
  }

  @Override
  public Stampa findStampaFinale(long idProcedimentoOggetto, String cuName)
      throws Exception
  {
    return null;
  }

  protected void writeTag(XMLStreamWriter writer, String name, String value,
      boolean blankAsNull) throws XMLStreamException
  {
    if (value == null)
    {
      if (blankAsNull)
      {
        value = "";
      }
      else
      {
        return;
      }
    }
    writer.writeStartElement(name);
    try
    {
      writer.writeCharacters(value);
    }
    catch (Exception e)
    {
      throw new XMLStreamException(e);
    }
    writer.writeEndElement();
  }
}