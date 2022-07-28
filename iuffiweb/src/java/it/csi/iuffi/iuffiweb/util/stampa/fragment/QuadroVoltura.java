package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.VolturaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;

public class QuadroVoltura extends Fragment
{
  public static final String TAG_NAME_FRAGMENT = "QuadroVoltura";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {

    writer.writeStartElement(TAG_NAME_FRAGMENT);

    VolturaDTO voltura = quadroEJB
        .getVoltura(procedimentoOggetto.getIdProcedimentoOggetto());
    if (voltura != null)
    {
      writeVisibility(writer, Boolean.TRUE);
      writeTag(writer, "DenAzienda", voltura.getDenominazioneAzienda());
      writeTag(writer, "CUAA", voltura.getCuaa());
      writeTag(writer, "SedeLegale", voltura.getSedeLegale());
      writeTag(writer, "LegaleRappr", voltura.getRappresentanteLegale());
      writeTag(writer, "Motivazione", voltura.getNote());
    }
    else
      writeVisibility(writer, Boolean.FALSE);

    writer.writeEndElement();

  }

}
