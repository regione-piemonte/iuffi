package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiAziendaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiIdentificativi;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiRappresentanteLegaleDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiSoggettoFirmatarioDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroDatiIdentificativi extends Fragment
{
  public static final String TAG_NAME_FRAGMENT           = "QuadroDatiIdentificativi";
  public static final String TAG_NAME_SEZIONE_ANAGRAFICA = "SezioneAnagrafica";
  public static final String TAG_NAME_SEZIONE_TITOLARE   = "SezioneTitolare";
  public static final String TAG_NAME_SEZIONE_FIRMATARIO = "SezioneFirmatario";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    DatiIdentificativi dati = quadroEJB
        .getDatiIdentificativiProcedimentoOggetto(
            procedimentoOggetto.getIdProcedimentoOggetto(), procedimentoOggetto
                .findQuadroByCU(
                    IuffiConstants.USECASE.DATI_IDENTIFICATIVI.DETTAGLIO)
                .getIdQuadroOggetto(),
            procedimentoOggetto.getDataFine(), 0);
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeSezioneAnagrafica(writer, procedimentoOggetto, dati);
    writeSezioneTitolare(writer, procedimentoOggetto, dati);
    writeSezioneFirmatario(writer, procedimentoOggetto, dati);
    writer.writeEndElement();
  }

  protected void writeSezioneAnagrafica(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, DatiIdentificativi dati)
      throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_ANAGRAFICA);
    writeVisibility(writer, true);
    DatiAziendaDTO azienda = dati.getAzienda();
    writeTag(writer, "Cuaa", azienda.getCuaa());
    writeTag(writer, "PartitaIva", azienda.getPartitaIva());
    writeTag(writer, "IntestazionePartitaIva",
        azienda.getItestazionePartitaIva());
    writeTag(writer, "Denominazione", azienda.getDenominazione());
    writeTag(writer, "FormaGiuridica", azienda.getFormaGiuridica());
    writeTag(writer, "IndirizzoSedeLeg", azienda.getIndirizzoSedeLegale());
    writeTag(writer, "Pec", azienda.getPec());
    writeTag(writer, "email", azienda.getEmail());
    writeTag(writer, "Tel", azienda.getTelefono());
    writeTag(writer, "Fax", azienda.getFax());
    writeTag(writer, "Ateco", azienda.getAttivitaAteco());
    writeTag(writer, "Ote", azienda.getAttivitaOte());
    writeTag(writer, "Registro", azienda.getCciaaNumeroRegistroImprese());
    writeTag(writer, "Anno", azienda.getCciaaAnnoIscrizione());
    writer.writeEndElement();
  }

  protected void writeSezioneTitolare(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, DatiIdentificativi dati)
      throws XMLStreamException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_TITOLARE);
    DatiRappresentanteLegaleDTO rappLegale = dati.getRappLegale();
    writeVisibility(writer, true);
    writeTag(writer, "CognomeTitolare", rappLegale.getCognome());
    writeTag(writer, "NomeTitolare", rappLegale.getNome());
    writeTag(writer, "CodiceFiscaleTitolare", rappLegale.getCodiceFiscale());
    writeTag(writer, "SessoTitolare", rappLegale.getSesso());
    writeTag(writer, "MailTitolare", rappLegale.getMail());
    writeTag(writer, "TelefonoTitolare", rappLegale.getTelefono());
    writeTag(writer, "IndirizzoResidenzaTitolare",
        rappLegale.getIndirizzoResidenza());
    writeTag(writer, "DataNascitaTitolare",
        IuffiUtils.DATE.formatDate(rappLegale.getDataNascita()));
    writeTag(writer, "LuogoNascitaTitolare", rappLegale.getLuogoNascita());
    writer.writeEndElement();
  }

  protected void writeSezioneFirmatario(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, DatiIdentificativi dati)
      throws XMLStreamException
  {
    DatiSoggettoFirmatarioDTO item = dati.getSoggFirmatario();
    if (item != null)
    {
      writer.writeStartElement(TAG_NAME_SEZIONE_FIRMATARIO);
      writeVisibility(writer, true);
      writeTag(writer, "RuoloFirmatario", item.getDescrizioneRuolo());
      writeTag(writer, "CodiceFiscaleFirmatario", item.getCodiceFiscale());
      writeTag(writer, "CognomeFirmatario", item.getCognome());
      writeTag(writer, "NomeFirmatario", item.getNome());
      writeTag(writer, "SessoFirmatario", item.getSesso());
      writeTag(writer, "MailFirmatario", item.getMail());
      writeTag(writer, "TelefonoFirmatario", item.getTelefono());
      writeTag(writer, "IndirizzoResidenzaFirmatario",
          item.getIndirizzoResidenza());
      writeTag(writer, "DataNascitaFirmatario",
          IuffiUtils.DATE.formatDate(item.getNascitaData()));
      writeTag(writer, "LuogoNascitaFirmatario", item.getComuneCitta());
      writer.writeEndElement();
    }
  }
}
