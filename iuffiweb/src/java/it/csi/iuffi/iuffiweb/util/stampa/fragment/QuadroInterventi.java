package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.validator.GenericValidator;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DatiLocalizzazioneParticellarePerStampa;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InfoMisurazioneIntervento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.InfoRiduzione;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaElencoInterventi;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroInterventi extends Fragment
{
  public static final String TAG_NAME_FRAGMENT_INTERVENTI = "QuadroInterventi";
  public static final String TAG_NAME_RIBASSO             = "Ribasso";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    String dato = "";
    String valore = "";
    String um = "";

    final IInterventiEJB ejbInterventi = IuffiUtils.APPLICATION
        .getEjbInterventi();
    final long idProcedimentoOggetto = procedimentoOggetto
        .getIdProcedimentoOggetto();
    List<RigaElencoInterventi> elenco = ejbInterventi
        .getElencoInterventiProcedimentoOggetto(idProcedimentoOggetto,
            IuffiConstants.FLAGS.NO, procedimentoOggetto.getDataFine());
    final boolean bandoConPercentualeRiduzione = ejbInterventi
        .isBandoConPercentualeRiduzione(idProcedimentoOggetto);
    if (elenco != null)
    {

      writer.writeStartElement(TAG_NAME_FRAGMENT_INTERVENTI);
      writeVisibility(writer, true);

      String tipoLocalizzazione = "NOSUP";
      long idLocalizzRif = 8;
      boolean attivacolonnacuaa = false;
      for (RigaElencoInterventi item : elenco)
      {
        if (item.getIdTipoLocalizzazione() == idLocalizzRif)
        {
          tipoLocalizzazione = "SUP";
        }
        if (IuffiConstants.FLAGS.SI.equals(item.getFlagBeneficiario()))
        {
          attivacolonnacuaa = true;
        }
      }
      writeTag(writer, "FlagBeneficiario", String.valueOf(attivacolonnacuaa));

      writer.writeStartElement("Interventi");
      BigDecimal totaleInvestimento = BigDecimal.ZERO;

      for (RigaElencoInterventi item : elenco)
      {
        if (IuffiConstants.INTERVENTI.TIPO_OPERAZIONE_ELIMINAZIONE
            .equals(item.getFlagTipoOperazione()))
        {
          // Ignoro gli interventi eliminati
          continue;
        }
        writer.writeStartElement("Intervento");
        writeTag(writer, "Numero", String.valueOf(
            (item.getProgressivo() != null) ? item.getProgressivo() : " "));

        writeTag(writer, "TipoIntervento", item.getDescTipoClassificazione());

        if (!GenericValidator.isBlankOrNull(item.getUlterioriInformazioni()))
        {
          writeTag(writer, "DescrizioneIntervento", item.getDescIntervento()
              + " - " + item.getUlterioriInformazioni());
        }
        else
        {
          writeTag(writer, "DescrizioneIntervento", item.getDescIntervento());
        }

        writeTag(writer, "CUAA_Beneficiario",
            item.getCuaaPartecipanteCompletoStampa());

        writeCDataTag(writer, "Comuni", (item.getDescComuni() != null)
            ? item.getDescComuni().replace("<br />", "\n") : " ");

        dato = "";
        valore = "";
        um = "";
        int count = 0;
        String sep = "";
        String datoValoreUM = "";

        for (InfoMisurazioneIntervento info : item.getMisurazioni())
        {
          if (count > 0)
          {
            sep = "\n";
          }

          dato = info.getDescMisurazione();
          if (info.isMisuraVisibile())
          {
            valore = IuffiUtils.FORMAT.formatGenericNumber(info.getValore(),
                4, false);
            um = ((info.getCodiceUnitaMisura() != null)
                ? info.getCodiceUnitaMisura() : " ");
            datoValoreUM += sep + dato + " " + valore + " " + um;
          }
          else
          {
            datoValoreUM += sep + dato;
          }
        }

        writeCDataTag(writer, "DatoValoreUM", datoValoreUM);
        final BigDecimal importoInvestimento = item.getImportoInvestimento()
            .setScale(2, RoundingMode.HALF_UP);
        totaleInvestimento = IuffiUtils.NUMBERS.add(totaleInvestimento,
            importoInvestimento);
        writeTag(writer, "Importo",
            IuffiUtils.FORMAT.formatDecimal2(importoInvestimento));

        writer.writeEndElement(); // Intervento
      }
      writer.writeEndElement(); // Interventi
      writeTag(writer, "TotaleInvestimento",
          IuffiUtils.FORMAT.formatDecimal2(totaleInvestimento));

      if (bandoConPercentualeRiduzione)
      {
        InfoRiduzione infoRiduzione = ejbInterventi
            .getInfoRiduzione(idProcedimentoOggetto);
        writer.writeStartElement(TAG_NAME_RIBASSO);
        writeVisibility(writer, true);
        writeTag(writer, "PercentualeRiduzione", IuffiUtils.FORMAT
            .formatDecimal2(infoRiduzione.getPercentuale()));
        writeTag(writer, "TotaleImportoRichiesto", IuffiUtils.FORMAT
            .formatDecimal2(infoRiduzione.getTotaleRichiesto()));
        writer.writeEndElement(); // TAG_NAME_RIBASSO
      }
      // se codice_livello = 8.1.1 e idTipoLivello = 1 allora nelle spese true e
      // interventi false
      boolean visibilityLocalizz = (IuffiConstants.OGGETTO.CODICE.DOMANDA_SALDO
          .equals(procedimentoOggetto.getCodOggetto())
          && quadroEJB.isCodiceLivelloInvestimentoEsistente(
              procedimentoOggetto.getIdProcedimento(),
              IuffiConstants.LIVELLO.CODICE_LIVELLO_8_1_1));
      visibilityLocalizz = !visibilityLocalizz;
      writeLocalizzazione(procedimentoOggetto.getIdProcedimentoOggetto(),
          tipoLocalizzazione, writer, ejbInterventi, visibilityLocalizz);

      writer.writeEndElement(); // TAG_NAME_FRAGMENT_INTERVENTI
    }
  }

  private void writeLocalizzazione(long idProcedimentoOggetto,
      String tipoLocalizzazione, XMLStreamWriter writer,
      IInterventiEJB ejbInterventi, boolean visibilityLocalizz)
      throws XMLStreamException, InternalUnexpectedException
  {
    final String TAG_NAME_LOCALIZZAZIONE = "Localizzazione";
    final String TAG_NAME_ELENCO_PARTICELLE = "ElencoParticelleInt";
    final String TAG_NAME_DATI_PARTICELLA = "DatiParticellaInt";

    List<DatiLocalizzazioneParticellarePerStampa> listParticellare = ejbInterventi
        .getLocalizzazioneParticellePerStampa(idProcedimentoOggetto,
            IuffiConstants.FLAGS.NO);
    writer.writeStartElement(TAG_NAME_LOCALIZZAZIONE);
    if (listParticellare != null && !listParticellare.isEmpty())
    {
      writeVisibility(writer, visibilityLocalizz);
      writeTag(writer, "TipoLocalizzazione", tipoLocalizzazione);
      writer.writeStartElement(TAG_NAME_ELENCO_PARTICELLE);
      for (DatiLocalizzazioneParticellarePerStampa particella : listParticellare)
      {
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
            particella.getSuperficieUtilizzata());
        writeTag(writer, "SupImpegnoInt",
            IuffiUtils.FORMAT.formatDecimal4(IuffiUtils.NUMBERS
                .getBigDecimal(particella.getSuperficieImpegno())));
        writeTag(writer, "SupEffettivaInt", IuffiUtils.FORMAT
            .formatDecimal4(particella.getSuperficieEffettiva()));
        writer.writeEndElement(); // TAG_NAME_DATI_PARTICELLA
      }
      writer.writeEndElement(); // TAG_NAME_ELENCO_PARTICELLE
    }
    else
    {
      writeVisibility(writer, false);
    }
    writer.writeEndElement(); // TAG_NAME_LOCALIZZAZIONE
  }

}
