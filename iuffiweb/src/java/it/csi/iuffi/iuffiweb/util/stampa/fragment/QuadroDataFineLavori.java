package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datafinelavori.DataFineLavoriDTO;

public class QuadroDataFineLavori extends Fragment
{
  public static final String TAG_NAME_FRAGMENT = "QuadroDateFineLavori";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    DataFineLavoriDTO fineLavori = quadroEJB
        .getLastDataFineLavori(procedimentoOggetto.getIdProcedimento());
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, true);
    if (fineLavori != null)
    {
      writer.writeStartElement("SezDateFineLavori");
      writeTag(writer, "DataProroga", fineLavori.getDataProrogaStr());
      writeTag(writer, "NoteDateFineLavori", fineLavori.getNote());
      writer.writeEndElement(); // SezDateFineLavori
    }
    writer.writeEndElement(); // TAG_NAME_FRAGMENT
  }
}
