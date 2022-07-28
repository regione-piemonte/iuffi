package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoDaFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroDatiIdentificativiDanno extends Fragment
{
  public static final String TAG_NAME_FRAGMENT              = "QuadroDatiIdentificativiDanno";

  public static final String TAG_NAME_SEZIONE_TITOLO_QUADRO = "TitoloQuadroDatiDanno";
  public static final String TAG_NAME_SEZIONE_DATI          = "DatiIdentificativiDanno";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    final IQuadroIuffiEJB ejbQuadroIuffi = IuffiUtils.APPLICATION
        .getEjbQuadroIuffi();
    DannoDaFaunaDTO dati = ejbQuadroIuffi.getDatiIdentificativiDanniDaFauna(
        procedimentoOggetto.getIdProcedimentoOggetto());

    writer.writeStartElement(TAG_NAME_FRAGMENT);

    writer.writeStartElement(TAG_NAME_SEZIONE_TITOLO_QUADRO);
    if (dati != null)
    {

      writeVisibility(writer, true);
      writeTag(writer, "TitoloDatiDanno",
          "Quadro - Dati Identificativi del danno");
      writer.writeEndElement();
      writeSezioneDati(writer, procedimentoOggetto, dati);
    }
    else
    {
      writeVisibility(writer, false);
      writeTag(writer, "TitoloDatiDanno",
          "Quadro - Dati Identificativi del danno");
      writer.writeEndElement();
    }

    writer.writeEndElement();
  }

  protected void writeSezioneDati(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, DannoDaFaunaDTO dati)
      throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_DATI);

    writeTag(writer, "DataDanno", dati.getDataDannoFormatted());
    writeTag(writer, "ProvDanno", dati.getProvincia());
    writeTag(writer, "ComuneDanno", dati.getComune());
    writeTag(writer, "IstitutoDanniFauna", dati.getIstituto());
    writeTag(writer, "IstitutoNominativo", dati.getNominativo());
    if (dati.getUrgenzaPerizia() != null)
      writeTag(writer, "UrgenzaPerizia",
          dati.getUrgenzaPerizia() ? "Sì" : "No");
    writeTag(writer, "MotivoUrgenza", dati.getMotivazione());
    writeTag(writer, "NotaUrgenza", dati.getNoteUrgenza());
    writeTag(writer, "NoteDatiDanno", dati.getNote());

    writer.writeEndElement();
  }

}
