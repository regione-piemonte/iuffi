package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.conticorrenti.ContoCorrenteEstesoDTO;

public class QuadroContiCorrenti extends Fragment
{
  public static final String TAG_NAME_FRAGMENT_CONTI_CORRENTI = "QuadroContoCorrente";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    ContoCorrenteEstesoDTO contoCorrente = quadroEJB.getContoCorrente(
        procedimentoOggetto.getIdProcedimentoOggetto(),
        procedimentoOggetto.getDataFine());
    writer.writeStartElement(TAG_NAME_FRAGMENT_CONTI_CORRENTI);
    writeVisibility(writer, true);
    if (contoCorrente != null)
    {
      writer.writeStartElement("ContoCorrente");
      writeTag(writer, "IBAN", contoCorrente.getIban());
      writeTag(writer, "CifraControllo", contoCorrente.getCifraControllo());
      writeTag(writer, "CIN", contoCorrente.getCin());
      writeTag(writer, "ABI", contoCorrente.getAbi());
      writeTag(writer, "CAB", contoCorrente.getCab());
      writeTag(writer, "NumeroContoCorrente",
          contoCorrente.getNumeroContoCorrente());
      writeTag(writer, "Istituto", contoCorrente.getDenominazioneBanca());
      writeTag(writer, "Agenzia", contoCorrente.getDenominazioneSportello());
      writeTag(writer, "Intestazione", contoCorrente.getIntestazione());
      writer.writeEndElement(); // ContoCorrente
    }
    writer.writeEndElement(); // TAG_NAME_FRAGMENT_CONTI_CORRENTI
  }
}
