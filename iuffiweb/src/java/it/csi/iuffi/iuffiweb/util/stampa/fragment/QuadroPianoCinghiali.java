package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.PianoSelettivoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroPianoCinghiali extends Fragment
{
  private static final String TAG_NAME_QUADRO_PIANO_CINGHIALI = "QuadroPianoCinghiali";

  @Override
  public void writeFragment(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB, String cuName) throws Exception
  {

    final IQuadroIuffiEJB ejbQuadroIuffi = IuffiUtils.APPLICATION.getEjbQuadroIuffi();
    final long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    PianoSelettivoDTO piano = ejbQuadroIuffi.getPianoSelettivo(idProcedimentoOggetto);

    writer.writeStartElement(TAG_NAME_QUADRO_PIANO_CINGHIALI);

    writer.writeStartElement("TitoloQuadroPianoCinghiali");
    writeTag(writer, "Visibility", piano == null ? "false" : "true");
    writer.writeEndElement();// TitoloQuadroPianoCinghiali

    writer.writeStartElement("SezCapi");
    writeTag(writer, "MaschiAdultiC", "" + piano.getCensitiAdultiM());
    writeTag(writer, "FemmineAdulteC", "" + piano.getCensitiAdultiF());
    writeTag(writer, "PiccoliStriatiC", "" + piano.getCensitiPiccoliStriati());
    writeTag(writer, "PiccoliRossiC", "" + piano.getCensitiPiccoliRossi());
    writeTag(writer, "TotaleCensiti", "" + piano.getTotaleCensiti());

    writeTag(writer, "MaschiAdultiP", "" + piano.getPrelievoAdultiM());
    writeTag(writer, "FemmineAdulteP", "" + piano.getPrelievoAdultiF());
    writeTag(writer, "PiccoliStriatiP", "" + piano.getPrelievoPiccoli());
    writeTag(writer, "TotalePrelievo", "" + piano.getTotalePrelievo());

    writeTag(writer, "Metodologia", piano.getDescMetodologia());
    writeTag(writer, "MetodologiaUnMisura", piano.getDescUnitaMisura());
    writeTag(writer, "MetodologiaMisura", "" + piano.getValoreMetodoCensimento());
    
    writer.writeStartElement("SezMetodAltra");

    writeTag(writer, "VisibilityMetodAltra", piano.getDescrAltraMetod()==null?"false" : "true");
    writeTag(writer, "MetodologiaAltra",  piano.getDescrAltraMetod());

    writer.writeEndElement();// SezMetodAltra

    writer.writeEndElement();// SezCapi

    writer.writeStartElement("SezCensimento");
    writer.writeStartElement("TabCensimento");

    if (piano.getElencoDate() != null && !piano.getElencoDate().isEmpty())
      for (String data : piano.getElencoDateStr())
      {
        writer.writeStartElement("RigaCensimento");
        writeTag(writer, "DataCensimento", data);
        writer.writeEndElement();// RigaCensimento
      }
    writer.writeEndElement();// TabCensimento
    writer.writeEndElement();// SezCensimento

    writer.writeEndElement();// TAG_NAME_QUADRO_PIANO_CINGHIALI

  }
}
