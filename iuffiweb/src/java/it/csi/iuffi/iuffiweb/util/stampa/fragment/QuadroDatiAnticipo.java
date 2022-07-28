package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.anticipo.DatiAnticipo;
import it.csi.iuffi.iuffiweb.integration.RigaAnticipoLivello;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroDatiAnticipo extends Fragment
{
  public static final String TAG_NAME_FRAGMENT                = "QuadroAnticipo";
  public static final String TAG_NAME_SEZIONE_TITOLO_ANTICIPO = "SezioneTitoloAnticipo";
  public static final String TAG_NAME_SEZIONE_ANTICIPO        = "SezioneAnticipo";
  public static final String TAG_NAME_SEZIONE_FIDEIUSSIONE    = "SezioneFideiussione";
  public static final String TAG_NAME_SEZIONE_BANCA           = "SezioneBanca";
  public static final String TAG_NAME_SEZIONE_ALTRO_ISTITUTO  = "SezioneAltroIstituto";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    DatiAnticipo datiAnticipo = quadroEJB
        .getDatiAnticipo(procedimentoOggetto.getIdProcedimentoOggetto());
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeSezioneTitoloAnticipo(writer, datiAnticipo);
    writeDatiAnticipo(writer, datiAnticipo);
    writeDatiFideiussione(writer, datiAnticipo);
    writeDatiBanca(writer, datiAnticipo);
    writeAltroistituto(writer, datiAnticipo);
    writer.writeEndElement();
  }

  protected void writeSezioneTitoloAnticipo(XMLStreamWriter writer,
      DatiAnticipo dati) throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_TITOLO_ANTICIPO);
    writeVisibility(writer, true);
    writeTag(writer, "TitoloQuadroAnticipo", "QUADRO ANTICIPO");
    writer.writeEndElement();
  }

  protected void writeDatiAnticipo(XMLStreamWriter writer, DatiAnticipo dati)
      throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_ANTICIPO);
    writeTag(writer, "TitolosezAnticipo", "DATI ANTICIPO");
    writer.writeStartElement("TabellaSezioneAnticipo");
    final List<RigaAnticipoLivello> ripartizioneAnticipo = dati
        .getRipartizioneAnticipo();
    if (ripartizioneAnticipo != null)
    {
      for (RigaAnticipoLivello riga : ripartizioneAnticipo)
      {
        writer.writeStartElement("RigaSezioneAnticipo");
        writeTag(writer, "Operazione", riga.getCodiceLivello());
        writeTag(writer, "ImportoInvestimento",
            IuffiUtils.FORMAT
                .formatGenericNumber(riga.getImportoInvestimento(), 2, true));
        writeTag(writer, "ImportoAmmesso", IuffiUtils.FORMAT
            .formatGenericNumber(riga.getImportoAmmesso(), 2, true));
        writeTag(writer, "ImportoContributo",
            IuffiUtils.FORMAT
                .formatGenericNumber(riga.getImportoContributo(), 2, true));
        if (IuffiConstants.FLAGS.SI.equals(riga.getFlagAnticipo()))
        {
          writeTag(writer, "PercentualeAnticipo",
              IuffiUtils.FORMAT
                  .formatGenericNumber(dati.getPercentualeAnticipo(), 2, true));
        }
        else
        {
          writeTag(writer, "PercentualeAnticipo", "0,00");
        }
        writeTag(writer, "ImportoAnticipo", IuffiUtils.FORMAT
            .formatGenericNumber(riga.getImportoAnticipo(), 2, true));
        writer.writeEndElement(); // RigaSezioneAnticipo
      }
    }
    writer.writeEndElement(); // TabellaSezioneAnticipo
    writer.writeEndElement(); // TAG_NAME_SEZIONE_ANTICIPO
  }

  protected void writeDatiFideiussione(XMLStreamWriter writer,
      DatiAnticipo dati) throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_FIDEIUSSIONE);
    writeTag(writer, "TitoloSezFideiussione", "DATI FIDEJIUSSIONE");
    writeTag(writer, "NumeroFideiussione", dati.getNumeroFideiussione());
    writeTag(writer, "ImportoFideiussione",
        IuffiUtils.FORMAT.formatGenericNumber(dati.getImportoFideiussione(),
            2, true));
    writeTag(writer, "DataStipula",
        IuffiUtils.DATE.formatDate(dati.getDataStipula()));
    writeTag(writer, "DataScadenza",
        IuffiUtils.DATE.formatDate(dati.getDataScadenza()));
    writer.writeEndElement();
  }

  protected void writeDatiBanca(XMLStreamWriter writer, DatiAnticipo dati)
      throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_BANCA);
    writeTag(writer, "VisibilitaBanca",
        String.valueOf(dati.getExtIdSportello() != null));
    writeTag(writer, "TitoloSezBanca", "ISTITUTO BANCARIO");
    writeTag(writer, "DenominazioneBanca", dati.getDenominazioneBanca());
    writeTag(writer, "SportelloIndCapComuneProv",
        dati.getDescCompletaComuneSportello());
    writer.writeEndElement();
  }

  protected void writeAltroistituto(XMLStreamWriter writer, DatiAnticipo dati)
      throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_ALTRO_ISTITUTO);
    writeTag(writer, "VisibilitaIstituto",
        String.valueOf(dati.getExtIdSportello() == null));
    writeTag(writer, "TitoloSezAltroIstituto", "ALTRO ISTITUTO");
    writeTag(writer, "AltroIstituto", dati.getAltroIstituto());
    writeTag(writer, "IndirizzoAltroIstituto",
        dati.getIndirizzoAltroIstituto());
    writeTag(writer, "AltroIstitutoCapComuneProv",
        dati.getDescCompletaComuneAltroIstituto());
    writer.writeEndElement();
  }
}
