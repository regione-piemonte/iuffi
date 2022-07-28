package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoDaFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.datibilancio.DatiBilancioDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroDatiBilancio extends Fragment
{
  public static final String TAG_NAME_FRAGMENT              = "QuadroDatiBilancio";

  public static final String TAG_NAME_SEZIONE_TITOLO_QUADRO = "TitoloDatiBilancio";
  public static final String TAG_NAME_SEZIONE_DATI          = "SezDatiBilancio";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    final IQuadroIuffiEJB ejbQuadroIuffi = IuffiUtils.APPLICATION
        .getEjbQuadroIuffi();
    DatiBilancioDTO dati = ejbQuadroIuffi.getDatiBilancio(
        procedimentoOggetto.getIdProcedimentoOggetto());

    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeTag(writer, TAG_NAME_SEZIONE_TITOLO_QUADRO,
        "Quadro - Dati di Bilancio");
    if (dati != null)
    {
      writeVisibility(writer, true);
      writeSezioneDati(writer, procedimentoOggetto, dati);
    }
    else
    {
      writeVisibility(writer, false);
    }
    writer.writeEndElement();
  }

  protected void writeSezioneDati(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, DatiBilancioDTO dati)
      throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_DATI);

    writeTag(writer, "ZonaAltimetrica", dati.getDescZonaAltimetrica());
    writeTag(writer, "DataScadenzaPrestito", dati.getDataScadPrestitoPrecStr());
    writeTag(writer, "FlagRitenAcconto", dati.getFlagRitenAccontoDecode());
    writeTag(writer, "MotivRitenAcconto", dati.getMotivazioneRitAcconto());
    writeTag(writer, "ImpMateriePrime", IuffiUtils.FORMAT.formatGenericNumber(dati.getImportoMateriePrime(), 2, true)+" €");
    writeTag(writer, "ImpServizi", IuffiUtils.FORMAT.formatGenericNumber(dati.getImportoServizi(), 2, true)+" €");
    writeTag(writer, "ImpBeniTerzi", IuffiUtils.FORMAT.formatGenericNumber(dati.getImportoBeniTerzi(), 2, true)+" €");
    writeTag(writer, "ImpSalari", IuffiUtils.FORMAT.formatGenericNumber(dati.getImportoPersSalari(), 2, true)+" €");
    writeTag(writer, "ImpOneriSociali", IuffiUtils.FORMAT.formatGenericNumber(dati.getImportoPersOneri(), 2, true)+" €");
    writeTag(writer, "ImpCapitaleAnticAmmis",  IuffiUtils.FORMAT.formatGenericNumber(dati.getImpCapitaleAnticAmmiss(), 2, true)+" €");
    writeTag(writer, "ImpCreditoClienti",  IuffiUtils.FORMAT.formatGenericNumber(dati.getImpCreditoClienti(), 2, true)+" €");
    writeTag(writer, "ImpFatturaro",  IuffiUtils.FORMAT.formatGenericNumber(dati.getImpFatturato(), 2, true)+" €");
    writeTag(writer, "TempoEsposizBilancio", dati.getTempoEsposizDaBilancio()+" giorni");
    writeTag(writer, "TempoEsposizBenef", dati.getTempoEsposizBenef()+" mesi");
    writeTag(writer, "ImpMaxConcedibile",  IuffiUtils.FORMAT.formatGenericNumber(dati.getImportoTotConcedibile(), 2, true)+" €");
    writeTag(writer, "DataDeliberaIntervento", dati.getDataDeliberaInterventoStr());
    writeTag(writer, "DataUltimoBilancio", dati.getDataUltimoBilancioStr());
    writeTag(writer, "TipoDomanda", dati.getTipoDomandaDecode());

    writer.writeEndElement();
  }

}
