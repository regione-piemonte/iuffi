package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.IntegrazioneAlPremioDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroIntegrazione extends Fragment
{
  public static final String TAG_NAME_FRAGMENT_INTEGRAZIONE = "QuadroIntegrazione";
  public static final String TAG_ELENCO_INTEGRAZIONE        = "ElencoIntegrazione";
  public static final String TAG_RIGA_INTEGRAZIONE          = "RigaIntegrazione";
  public static final String TAG_TOTALE_INTEGRAZIONE        = "TotaleIntegrazione";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    List<IntegrazioneAlPremioDTO> elenco = quadroEJB.getIntegrazioneAlPremio(
        procedimentoOggetto.getIdProcedimento(),
        procedimentoOggetto.getIdProcedimentoOggetto(), null);
    writer.writeStartElement(TAG_NAME_FRAGMENT_INTEGRAZIONE);
    if (elenco != null && !elenco.isEmpty())
    {
      writeTag(writer, "VisibilityIntegrazione", "true");
      writer.writeStartElement(TAG_ELENCO_INTEGRAZIONE);
      BigDecimal totaleContributoConcesso = BigDecimal.ZERO;
      BigDecimal totaleLiquidato = BigDecimal.ZERO;
      BigDecimal totaleEconomie = BigDecimal.ZERO;
      BigDecimal totaleContributoIntegrazione = BigDecimal.ZERO;
      BigDecimal totaleSanzioni = BigDecimal.ZERO;
      for (IntegrazioneAlPremioDTO integrazione : elenco)
      {
        writer.writeStartElement(TAG_RIGA_INTEGRAZIONE);
        writeTag(writer, "Livello", integrazione.getOperazione());
        final BigDecimal contributoConcesso = integrazione
            .getContributoConcesso();
        totaleContributoConcesso = IuffiUtils.NUMBERS
            .add(totaleContributoConcesso, contributoConcesso);
        writeTag(writer, "ContributoConcesso",
            IuffiUtils.FORMAT.formatCurrency(contributoConcesso));
        final BigDecimal liquidato = integrazione.getTotaleLiquidato();
        totaleLiquidato = IuffiUtils.NUMBERS.add(totaleLiquidato, liquidato);
        writeTag(writer, "Liquidato",
            IuffiUtils.FORMAT.formatCurrency(liquidato));
        final BigDecimal economie = integrazione.getEconomie();
        totaleEconomie = IuffiUtils.NUMBERS.add(totaleEconomie, economie);
        writeTag(writer, "Economie",
            IuffiUtils.FORMAT.formatCurrency(economie));
        final BigDecimal contributoIntegrazione = integrazione
            .getContributoIntegrazione();
        totaleContributoIntegrazione = IuffiUtils.NUMBERS
            .add(totaleContributoIntegrazione, contributoIntegrazione);
        writeTag(writer, "ContributoIntegrazione",
            IuffiUtils.FORMAT.formatCurrency(contributoIntegrazione));
        final BigDecimal sanzioni = integrazione
            .getContributoRiduzioniSanzioni();
        totaleSanzioni = IuffiUtils.NUMBERS.add(totaleSanzioni, sanzioni);
        writeTag(writer, "Sanzioni",
            IuffiUtils.FORMAT.formatCurrency(sanzioni));
        writer.writeEndElement(); // TAG_RIGA_INTEGRAZIONE
      }
      writer.writeEndElement(); // TAG_ELENCO_INTEGRAZIONE

      writer.writeStartElement(TAG_TOTALE_INTEGRAZIONE);
      writeTag(writer, "TotaleConcesso",
          IuffiUtils.FORMAT.formatCurrency(totaleContributoConcesso));
      writeTag(writer, "TotaleLiquidato",
          IuffiUtils.FORMAT.formatCurrency(totaleLiquidato));
      writeTag(writer, "TotaleEconomie",
          IuffiUtils.FORMAT.formatCurrency(totaleEconomie));
      writeTag(writer, "TotaleContrInteg",
          IuffiUtils.FORMAT.formatCurrency(totaleContributoIntegrazione));
      writeTag(writer, "TotaleSanzioni",
          IuffiUtils.FORMAT.formatCurrency(totaleSanzioni));
      writer.writeEndElement(); // TAG_TOTALE_INTEGRAZIONE
    }
    else
    {
      writeTag(writer, "VisibilityIntegrazione", "false");
    }
    writer.writeEndElement(); // TAG_NAME_FRAGMENT_INTEGRAZIONE
  }

}
