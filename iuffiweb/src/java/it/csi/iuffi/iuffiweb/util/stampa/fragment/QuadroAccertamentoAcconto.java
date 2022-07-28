package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IRendicontazioneEAccertamentoSpeseEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DatiLocalizzazioneParticellarePerStampa;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaAccertamentoSpese;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaProspetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.TotaleContributoAccertamentoElencoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroAccertamentoAcconto extends Fragment
{
  public static final String TAG_NAME_FRAGMENT = "QuadroInterventiPag";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    IRendicontazioneEAccertamentoSpeseEJB rendicontazioneEAccertamentoSpeseEJB = IuffiUtils.APPLICATION
        .getEjbAccertamenti();
    List<RigaAccertamentoSpese> elenco = rendicontazioneEAccertamentoSpeseEJB
        .getElencoAccertamentoSpese(
            procedimentoOggetto.getIdProcedimentoOggetto(), null);
    List<TotaleContributoAccertamentoElencoDTO> contributi = rendicontazioneEAccertamentoSpeseEJB
        .getTotaleContributoErogabileNonErogabileESanzioniAcconto(
            procedimentoOggetto.getIdProcedimentoOggetto());
    List<RigaProspetto> prospetto = rendicontazioneEAccertamentoSpeseEJB
        .getElencoProspetto(procedimentoOggetto.getIdProcedimento(),
            procedimentoOggetto.getIdProcedimentoOggetto());

    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, true);

    if (elenco != null)
    {
      /*
       * 245-P (Lettera acconto) -> writeElencoContributi 245-PN (Lettera
       * acconto) -> writeElencoContributi , writeElencoInterventi
       * 
       * 246 (Lettera anticipo) -> writeElencoContributi 247-P (Lettera saldo)->
       * writeElencoContributi 247-PN (Lettera saldo)-> writeElencoContributi,
       * writeElencoInterventi
       * 
       * per i verbali stampo tutti e tre
       * 
       */

      boolean isComunicazione = quadroEJB
          .isComunicazionePagamento(procedimentoOggetto.getIdOggetto(), cuName);
      boolean isEsitoNegativo = cuName.endsWith("-N");
      boolean isINTPR = procedimentoOggetto.getCodOggetto().equals("INTPR");

      if (isComunicazione)
      {
        if (!isEsitoNegativo)
        {
          writeElencoContributi(contributi, writer, isINTPR);
          writeElencoInterventi(elenco, writer, isINTPR);
          writeProspetto(prospetto, writer);
        }
      }
      else
      {
        writeElencoInterventi(elenco, writer, isINTPR);
        writeElencoContributi(contributi, writer, isINTPR);
        writeProspetto(prospetto, writer);
      }

      // se codice_livello = 8.1.1 e idTipoLivello = 1 allora nelle spese true e
      // interventi false
      final IInterventiEJB ejbInterventi = IuffiUtils.APPLICATION
          .getEjbInterventi();
      boolean visibilityLocalizz = (IuffiConstants.OGGETTO.CODICE.ISTRUTTORIA_SALDO
          .equals(procedimentoOggetto.getCodOggetto())
          && !isINTPR
          && quadroEJB.isCodiceLivelloInvestimentoEsistente(
              procedimentoOggetto.getIdProcedimento(),
              IuffiConstants.LIVELLO.CODICE_LIVELLO_8_1_1));
      writeLocalizzazione(procedimentoOggetto.getIdProcedimentoOggetto(), "SUP",
          writer, ejbInterventi, visibilityLocalizz);

    }

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

          totSupUtilizzata = totSupUtilizzata.add(
              IuffiUtils.NUMBERS.nvl(particella.getSuperficieIstruttoria()));
          totSupImpegno = totSupImpegno.add(IuffiUtils.NUMBERS
              .nvl(particella.getSuperficieAccertataGis()));
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
          writeTag(writer, "SupIstruttoriaInt", IuffiUtils.FORMAT
              .formatDecimal4(particella.getSuperficieIstruttoria()));
          writeTag(writer, "SupGIS", IuffiUtils.FORMAT
              .formatDecimal4(particella.getSuperficieAccertataGis()));
          writer.writeEndElement(); // TAG_NAME_DATI_PARTICELLA
        }
        writer.writeEndElement(); // TAG_NAME_ELENCO_PARTICELLE
      }

      writer.writeStartElement("RigaTotaliSP");
      writeTag(writer, "TotaleSupIstruttoria",
          IuffiUtils.FORMAT.formatDecimal4(totSupUtilizzata));
      writeTag(writer, "TotaleSupGIS",
          IuffiUtils.FORMAT.formatDecimal4(totSupImpegno));
      writeTag(writer, "TotaleSupEffettiva",
          IuffiUtils.FORMAT.formatDecimal4(totSupEffettiva));
      writer.writeEndElement(); // RigaTotaliSP
    }
    else
    {
      writeVisibility(writer, false);
    }
    writer.writeEndElement(); // TAG_NAME_LOCALIZZAZIONE
  }

  public void writeProspetto(List<RigaProspetto> prospetto,
      XMLStreamWriter writer) throws XMLStreamException
  {
    writer.writeStartElement("ElencoProspetto");
    writeVisibility(writer, prospetto != null && prospetto.size() > 0);
    writer.writeStartElement("Prospetti");
    BigDecimal contrRichiestoTot = BigDecimal.ZERO;
    BigDecimal importoSanzioniTot = BigDecimal.ZERO;
    BigDecimal liquidazioneTot = BigDecimal.ZERO;

    if (prospetto != null)
    {
      for (RigaProspetto riga : prospetto)
      {
        contrRichiestoTot = contrRichiestoTot.add(riga.getContribRichiesto());
        importoSanzioniTot = importoSanzioniTot.add(riga.getImportoSanzioni());
        liquidazioneTot = liquidazioneTot.add(riga.getInLiquidazione());

        writer.writeStartElement("Prospetto");
        writeTag(writer, "TipoIstanza", riga.getDescrizione());
        writeTag(writer, "DataPresentazione",
            IuffiUtils.DATE.formatDate(riga.getDataPresentazione()));
        writeTag(writer, "ContributoRichiesto",
            IuffiUtils.FORMAT.formatDecimal2(riga.getContribRichiesto()));
        writeTag(writer, "ImportoSanzioni",
            IuffiUtils.FORMAT.formatDecimal2(riga.getImportoSanzioni()));
        writeTag(writer, "Liquidazione",
            IuffiUtils.FORMAT.formatDecimal2(riga.getInLiquidazione()));
        writer.writeEndElement(); // Prospetto
      }
    }
    writer.writeEndElement(); // Prospetti

    writer.writeStartElement("TotaliProspetti");
    writer.writeStartElement("RigaTotali");
    writeTag(writer, "ContributoRichiestoTot",
        IuffiUtils.FORMAT.formatDecimal2(contrRichiestoTot));
    writeTag(writer, "ImportoSanzioniTot",
        IuffiUtils.FORMAT.formatDecimal2(importoSanzioniTot));
    writeTag(writer, "LiquidazioneTot",
        IuffiUtils.FORMAT.formatDecimal2(liquidazioneTot));
    writer.writeEndElement(); // RigaTotali
    writer.writeEndElement(); // TotaliProspetti

    writer.writeEndElement(); // ElencoProspetto
  }

  public void writeElencoContributi(
      List<TotaleContributoAccertamentoElencoDTO> contributi,
      XMLStreamWriter writer, boolean isINTPR) throws XMLStreamException
  {
    writer.writeStartElement("ElencoContributi");

    writeVisibility(writer,
        contributi != null && contributi.size() > 0 && !isINTPR);
    writer.writeStartElement("Contributi");
    if (contributi != null)
    {
      for (TotaleContributoAccertamentoElencoDTO riga : contributi)
      {
        writer.writeStartElement("Contributo");
        writeTag(writer, "Operazione", riga.getCodiceOperazione());
        writeTag(writer, "ContributoErogabile",
            IuffiUtils.FORMAT.formatDecimal2(riga.getContributoErogabile()));
        writeTag(writer, "ContributoNonErogabile", IuffiUtils.FORMAT
            .formatDecimal2(riga.getContributoNonErogabile()));
        writeTag(writer, "SanzioniRiduzioni",
            IuffiUtils.FORMAT.formatDecimal2(riga.getImportoSanzioni()));
        writer.writeEndElement(); // Contributo
      }
    }
    writer.writeEndElement(); // Contributi
    writer.writeEndElement(); // ElencoContributi
  }

  public void writeElencoInterventi(List<RigaAccertamentoSpese> elenco,
      XMLStreamWriter writer, boolean isINTPR) throws XMLStreamException
  {

    writer.writeStartElement("ElencoInterventi");
    writeVisibility(writer, elenco != null && elenco.size() > 0 && !isINTPR);

    writer.writeStartElement("Interventi");

    BigDecimal spesaAmmessaTot = BigDecimal.ZERO;
    BigDecimal spesaRendicontataTot = BigDecimal.ZERO;
    BigDecimal spesaAccertataTot = BigDecimal.ZERO;
    BigDecimal spesaRiconosciutaTot = BigDecimal.ZERO;

    if (elenco != null)
    {
      for (RigaAccertamentoSpese riga : elenco)
      {
        spesaAmmessaTot = spesaAmmessaTot.add(riga.getSpesaAmmessa());
        spesaRendicontataTot = spesaRendicontataTot
            .add(riga.getSpesaSostenutaAttuale());
        spesaAccertataTot = spesaAccertataTot
            .add(riga.getSpeseAccertateAttuali());
        spesaRiconosciutaTot = spesaRiconosciutaTot
            .add(riga.getSpesaRiconosciutaPerCalcolo());

        writer.writeStartElement("Intervento");
        writeTag(writer, "Numero", String.valueOf(riga.getProgressivo()));
        writeTag(writer, "DescrizioneIntervento", riga.getDescIntervento());
        writeTag(writer, "SpesaAmmessa",
            IuffiUtils.FORMAT.formatDecimal2(riga.getSpesaAmmessa()));
        writeTag(writer, "SpesaRendicontata", IuffiUtils.FORMAT
            .formatDecimal2(riga.getSpesaSostenutaAttuale()));
        writeTag(writer, "SpesaAccertata", IuffiUtils.FORMAT
            .formatDecimal2(riga.getSpeseAccertateAttuali()));
        writeTag(writer, "SpesaRiconosciuta", IuffiUtils.FORMAT
            .formatDecimal2(riga.getSpesaRiconosciutaPerCalcolo()));
        writer.writeEndElement();
      }
    }
    writer.writeEndElement();

    writer.writeStartElement("TotaliInterventi");
    writer.writeStartElement("TotaleIntervento");
    writeTag(writer, "SpesaAmmessaTot",
        IuffiUtils.FORMAT.formatDecimal2(spesaAmmessaTot));
    writeTag(writer, "SpesaRendicontataTot",
        IuffiUtils.FORMAT.formatDecimal2(spesaRendicontataTot));
    writeTag(writer, "SpesaAccertataTot",
        IuffiUtils.FORMAT.formatDecimal2(spesaAccertataTot));
    writeTag(writer, "SpesaRiconosciutaTot",
        IuffiUtils.FORMAT.formatDecimal2(spesaRiconosciutaTot));
    writer.writeEndElement();// TotaleIntervento
    writer.writeEndElement();// TotaliInterventi
    writer.writeEndElement(); // ElencoInterventi
  }

}
