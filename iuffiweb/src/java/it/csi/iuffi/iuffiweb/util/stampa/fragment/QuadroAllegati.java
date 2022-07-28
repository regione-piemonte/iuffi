package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.DettaglioInfoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.GruppoInfoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.ValoriInseritiDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class QuadroAllegati extends Fragment
{
  public static final String TAG_NAME_FRAGMENT_ALLEGATI = "QuadroAllegati";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    QuadroOggettoDTO quadro = procedimentoOggetto
        .findQuadroByCU("CU-IUFFI-108");
    List<GruppoInfoDTO> dichiarazioni = quadroEJB.getDichiarazioniOggetto(
        procedimentoOggetto.getIdProcedimentoOggetto(),
        quadro.getIdQuadroOggetto(),
        procedimentoOggetto.getIdBandoOggetto());

    writer.writeStartElement(TAG_NAME_FRAGMENT_ALLEGATI);
    writeVisibility(writer, true);
    writer.writeStartElement("GruppiAllegati");
    for (GruppoInfoDTO gruppo : dichiarazioni)
    {
      writer.writeStartElement("GruppoAllegati");
      writeTag(writer, "TitoloGruppoAllegati", gruppo.getDescrizione());
      writer.writeStartElement("Allegati");
      for (DettaglioInfoDTO info : gruppo.getDettaglioInfo())
      {
        writer.writeStartElement("Allegato");
        writeTag(writer, "FlagObbligatorio", String.valueOf(
            IuffiConstants.FLAGS.SI.equals(info.getFlagObbligatorio())));
        writeTag(writer, "FlagSelezionato", String.valueOf(info.isChecked()));
        writeTag(writer, "TestoAllegato", getTestoDichiarazione(info));
        writer.writeEndElement(); // Allegato
      }
      writer.writeEndElement(); // Allegati
      writer.writeEndElement(); // GruppoAllegati
    }
    writer.writeEndElement(); // GruppiAllegati
    writer.writeEndElement(); // TAG_NAME_FRAGMENT_ALLEGATI
  }

  protected String getTestoDichiarazione(DettaglioInfoDTO dettaglio)

  {
    String testo = dettaglio.getDescrizione();
    
    int position = 1;
    String repfrom = "\\$\\$STRING";
    String repto = "";
    while (!testo.replaceFirst(repfrom, repto).equals(testo))
    {
      repto = getValore(dettaglio.getValoriInseriti(), position);
      testo = testo.replaceFirst(repfrom, repto);
      position++;
    }

    repfrom = "\\$\\$INTEGER";
    while (!testo.replaceFirst(repfrom, repto).equals(testo))
    {
      repto = getValore(dettaglio.getValoriInseriti(), position);
      testo = testo.replaceFirst(repfrom, repto);
      position++;
    }

    repfrom = "\\$\\$NUMBER";
    while (!testo.replaceFirst(repfrom, repto).equals(testo))
    {
      repto = getValore(dettaglio.getValoriInseriti(), position);
      testo = testo.replaceFirst(repfrom, repto);
      position++;
    }

    repfrom = "\\$\\$DATE";
    while (!testo.replaceFirst(repfrom, repto).equals(testo))
    {
      repto = getValore(dettaglio.getValoriInseriti(), position);
      testo = testo.replaceFirst(repfrom, repto);
      position++;
    }
    return testo;
  }

  protected String getValore(List<ValoriInseritiDTO> valoriInseriti,
      int position)
  {
    if (valoriInseriti != null)
    {
      for (ValoriInseritiDTO val : valoriInseriti)
      {
        if (val.getPosizione() == position)
          return val.getValore();
      }
    }
    return "_______";
  }

}
