package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.Date;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiAziendaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiIdentificativi;
import it.csi.iuffi.iuffiweb.exception.ApplicationException;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class HeaderLetteraAmmissioneFinanziamento extends Fragment
{
  public static final String TAG_NAME_HEADER = "Header";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {

    TestataProcedimento testata = quadroEJB
        .getTestataProcedimento(procedimentoOggetto.getIdProcedimento());
    final long idProcedimentoOggetto = procedimentoOggetto
        .getIdProcedimentoOggetto();
    Procedimento procedimento = quadroEJB
        .getProcedimento(procedimentoOggetto.getIdProcedimento());
    final Date dataFine = procedimentoOggetto.getDataFine();

    DatiIdentificativi datiAzienda = quadroEJB
        .getDatiIdentificativiProcedimentoOggetto(idProcedimentoOggetto, 0,
            dataFine, 0);
    if (datiAzienda == null)
    {
      throw new ApplicationException(
          "Impossibile trovare i dati dell'azienda alla data "
              + (dataFine == null ? "corrente"
                  : IuffiUtils.DATE.formatDate(dataFine)));
    }
    writer.writeStartElement(TAG_NAME_HEADER);

    writeTag(writer, "Titolo1", "REGIONE PIEMONTE");
    writeTag(writer, "Titolo2", testata.getDescAmmCompetenza());
    writeTag(writer, "Titolo3", "");
    writer.writeStartElement("DatiAzienda");
    final DatiAziendaDTO azienda = datiAzienda.getAzienda();
    writeTag(writer, "Denominazione", azienda.getDenominazione());
    writeTag(writer, "Indirizzo", azienda.getIndirizzoSedeLegale());
    writeTag(writer, "CUAA", azienda.getCuaa());
    writeTag(writer, "PEC", azienda.getPec());
    writer.writeEndElement();
    writer.writeStartElement("RiferimentiDomanda");
    writeTag(writer, "NumeroDomanda", procedimento.getIdentificativo());

    writer.writeEndElement();
    writer.writeStartElement("Oggetto");
    writeTag(writer, "Bando",
        testata.getAnnoCampagna() + " - " + testata.getDenominazioneBando());

    writer.writeEndElement();
    writer.writeEndElement();
  }
}
