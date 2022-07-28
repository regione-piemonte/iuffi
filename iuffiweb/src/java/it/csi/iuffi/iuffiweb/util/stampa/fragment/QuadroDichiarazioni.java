package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.DettaglioInfoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.GruppoInfoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.dichiarazioni.ValoriInseritiDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class QuadroDichiarazioni extends Fragment
{
  public static final String TAG_NAME_FRAGMENT_DICHIARAZIONI = "QuadroDichiarazioni";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    QuadroOggettoDTO quadro = procedimentoOggetto
        .findQuadroByCU("CU-IUFFI-106-D");
    List<GruppoInfoDTO> dichiarazioni = quadroEJB.getDichiarazioniOggetto(
        procedimentoOggetto.getIdProcedimentoOggetto(),
        quadro.getIdQuadroOggetto(),
        procedimentoOggetto.getIdBandoOggetto());

    writer.writeStartElement(TAG_NAME_FRAGMENT_DICHIARAZIONI);
    writeVisibility(writer, true);

    if (dichiarazioni != null && dichiarazioni.size() > 0)
    {
      writer.writeStartElement("GruppiDichiarazioni");
      for (GruppoInfoDTO gruppo : dichiarazioni)
      {
        writer.writeStartElement("GruppoDichiarazioni");
        writeTag(writer, "TitoloGruppoDichiarazioni", gruppo.getDescrizione());
        writer.writeStartElement("Dichiarazioni");
        for (DettaglioInfoDTO info : gruppo.getDettaglioInfo())
        {
          writer.writeStartElement("Dichiarazione");
          writeTag(writer, "FlagObbligatorio", String.valueOf(
              IuffiConstants.FLAGS.SI.equals(info.getFlagObbligatorio())));
          writeTag(writer, "FlagSelezionato", String.valueOf(info.isChecked()));
          writeCDataTag(writer, "TestoDichiarazione",
              getTestoDichiarazione(info));
          writer.writeEndElement(); // Dichiarazione
        }
        writer.writeEndElement(); // Dichiarazioni
        writer.writeEndElement(); // GruppoDichiarazioni
      }
      writer.writeEndElement(); // GruppiDichiarazioni
    }
    writer.writeEndElement(); // TAG_NAME_FRAGMENT_DICHIARAZIONI
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
