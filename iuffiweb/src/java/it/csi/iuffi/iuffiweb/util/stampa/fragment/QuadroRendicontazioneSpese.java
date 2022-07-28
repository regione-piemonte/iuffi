package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DatiLocalizzazioneParticellarePerStampa;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaRendicontazioneSpese;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroRendicontazioneSpese extends Fragment
{
  public static final String TAG_NAME_FRAGMENT       = "QuadroSpese";
  public static final String TAG_NAME_TABELLA_SPESE  = "TabellaSpese";
  public static final String TAG_NAME_RIGA_SPESE     = "RigaSpese";
  public static final String TAG_NAME_RIGA_TOTALI    = "TotaliSpese";
  public static final String TAG_NAME_LOCALIZZAZIONE = "Localizzazione";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    List<RigaRendicontazioneSpese> elenco = quadroEJB
        .getElencoRendicontazioneSpese(
            procedimentoOggetto.getIdProcedimentoOggetto(), null);
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, true);
    writeTag(writer, "TitoloSezioneSpese", "Quadro - Rendicontazione Spese");
    writer.writeStartElement(TAG_NAME_TABELLA_SPESE);
    BigDecimal totImportoAmmesso = new BigDecimal(0);
    BigDecimal totImportoContributo = new BigDecimal(0);
    BigDecimal totImportoSpesa = new BigDecimal(0);
    BigDecimal totContributoRichiesto = new BigDecimal(0);
    if (elenco != null)
    {
      for (RigaRendicontazioneSpese riga : elenco)
      {
        writeRiga(writer, riga);
        totImportoAmmesso = totImportoAmmesso.add(riga.getSpesaAmmessa());
        totImportoContributo = totImportoContributo
            .add(riga.getImportoContributo());
        totImportoSpesa = totImportoSpesa.add(riga.getImportoSpesa());
        totContributoRichiesto = totContributoRichiesto
            .add(riga.getContributoRichiesto());
      }
    }
    writer.writeEndElement();
    // RIGA TOTALI
    writer.writeStartElement(TAG_NAME_RIGA_TOTALI);
    writeTag(writer, "TotImportoAmmesso",
        IuffiUtils.FORMAT.formatGenericNumber(totImportoAmmesso, 2, false));
    writeTag(writer, "TotImportoContributo", IuffiUtils.FORMAT
        .formatGenericNumber(totImportoContributo, 2, false));
    writeTag(writer, "TotImportoSpesa",
        IuffiUtils.FORMAT.formatGenericNumber(totImportoSpesa, 2, false));
    writeTag(writer, "TotContributoRichiesto", IuffiUtils.FORMAT
        .formatGenericNumber(totContributoRichiesto, 2, false));
    writer.writeEndElement();

    // se codice_livello = 8.1.1 e idTipoLivello = 1 allora nelle spese true e
    // interventi false
    final IInterventiEJB ejbInterventi = IuffiUtils.APPLICATION
        .getEjbInterventi();
    boolean visibilityLocalizz = (IuffiConstants.OGGETTO.CODICE.DOMANDA_SALDO
        .equals(procedimentoOggetto.getCodOggetto())
        && quadroEJB.isCodiceLivelloInvestimentoEsistente(
            procedimentoOggetto.getIdProcedimento(),
            IuffiConstants.LIVELLO.CODICE_LIVELLO_8_1_1));
    writeLocalizzazione(procedimentoOggetto.getIdProcedimentoOggetto(), "SUP",
        writer, ejbInterventi, visibilityLocalizz);

    writer.writeEndElement();
  }

  protected void writeRiga(XMLStreamWriter writer,
      RigaRendicontazioneSpese dati) throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_RIGA_SPESE);
    writeTag(writer, "Progressivo", String.valueOf(dati.getProgressivo()));
    writeTag(writer, "DescrizioneIntervento", dati.getDescIntervento());
    writeTag(writer, "ImportoAmmesso", IuffiUtils.FORMAT
        .formatGenericNumber(dati.getSpesaAmmessa(), 2, false));
    writeTag(writer, "ImportoContributo", IuffiUtils.FORMAT
        .formatGenericNumber(dati.getImportoContributo(), 2, false));
    writeTag(writer, "ImportoSpesa", IuffiUtils.FORMAT
        .formatGenericNumber(dati.getImportoSpesa(), 2, false));
    writeTag(writer, "ContributoRichiesto", IuffiUtils.FORMAT
        .formatGenericNumber(dati.getContributoRichiesto(), 2, false));
    writer.writeEndElement();
  }

  private void writeLocalizzazione(long idProcedimentoOggetto,
      String tipoLocalizzazione, XMLStreamWriter writer,
      IInterventiEJB ejbInterventi, boolean visibilityLocalizzazione)
      throws XMLStreamException, InternalUnexpectedException
  {
    final String TAG_NAME_LOCALIZZAZIONE = "LocalizzazioneSP";
    final String TAG_NAME_ELENCO_PARTICELLE = "ElencoParticelleIntSP";
    final String TAG_NAME_DATI_PARTICELLA = "DatiParticellaIntSP";

    List<DatiLocalizzazioneParticellarePerStampa> listParticellare = ejbInterventi
        .getLocalizzazioneParticellePerStampa(idProcedimentoOggetto,
            IuffiConstants.FLAGS.NO);
    writer.writeStartElement(TAG_NAME_LOCALIZZAZIONE);
    if (listParticellare != null && !listParticellare.isEmpty())
    {

      BigDecimal totSupUtilizzata = BigDecimal.ZERO;
      BigDecimal totSupImpegno = BigDecimal.ZERO;
      BigDecimal totSupEffettiva = BigDecimal.ZERO;

      writeVisibility(writer, visibilityLocalizzazione);
      writeTag(writer, "TipoLocalizzazione", tipoLocalizzazione);
      if (visibilityLocalizzazione)
      {
        writer.writeStartElement(TAG_NAME_ELENCO_PARTICELLE);
        for (DatiLocalizzazioneParticellarePerStampa particella : listParticellare)
        {

          totSupUtilizzata = totSupUtilizzata.add(IuffiUtils.NUMBERS
              .getBigDecimalNvl(particella.getSuperficieUtilizzata()));
          totSupImpegno = totSupImpegno.add(IuffiUtils.NUMBERS
              .getBigDecimalNvl(particella.getSuperficieImpegno()));
          totSupEffettiva = totSupEffettiva.add(
              IuffiUtils.NUMBERS.nvl(particella.getSuperficieEffettiva()));

          writer.writeStartElement(TAG_NAME_DATI_PARTICELLA);
          writeTag(writer, "Numero",
              IuffiUtils.STRING.nvl(particella.getProgressivo()));
          writeTag(writer, "DescrizioneIntervento",
              particella.getDescIntervento());
          writeTag(writer, "ComuneInt", particella.getDescComune());
          writeTag(writer, "SezioneInt", particella.getSezione());
          writeTag(writer, "FoglioInt",
              IuffiUtils.STRING.nvl(particella.getFoglio()));
          writeTag(writer, "ParticellaInt",
              IuffiUtils.STRING.nvl(particella.getParticella()));
          writeTag(writer, "SubalternoInt",
              IuffiUtils.STRING.nvl(particella.getSubalterno()));
          writeTag(writer, "SupCatastaleInt", particella.getSupCatastale());
          writeTag(writer, "DescDestinazioneProduttivaInt",
              particella.getDescTipoUtilizzo());
          writeTag(writer, "SupUtilizzataInt",
              IuffiUtils.FORMAT.formatDecimal4(IuffiUtils.NUMBERS
                  .getBigDecimal(particella.getSuperficieUtilizzata())));
          writeTag(writer, "SupImpegnoInt",
              IuffiUtils.FORMAT.formatDecimal4(IuffiUtils.NUMBERS
                  .getBigDecimal(particella.getSuperficieImpegno())));
          writeTag(writer, "SupEffettivaInt", IuffiUtils.FORMAT
              .formatDecimal4(particella.getSuperficieEffettiva()));
          writer.writeEndElement(); // TAG_NAME_DATI_PARTICELLA
        }
        writer.writeEndElement(); // TAG_NAME_ELENCO_PARTICELLE

        writer.writeStartElement("RigaTotaliSP");
        writeTag(writer, "TotaleSupUtilizzata",
            IuffiUtils.FORMAT.formatDecimal4(totSupUtilizzata));
        writeTag(writer, "TotaleSupImpegno",
            IuffiUtils.FORMAT.formatDecimal4(totSupImpegno));
        writeTag(writer, "TotaleSupEffettiva",
            IuffiUtils.FORMAT.formatDecimal4(totSupEffettiva));
        writer.writeEndElement(); // RigaTotaliSP
      }
    }
    else
    {
      writeVisibility(writer, false);
    }
    writer.writeEndElement(); // TAG_NAME_LOCALIZZAZIONE
  }

}
