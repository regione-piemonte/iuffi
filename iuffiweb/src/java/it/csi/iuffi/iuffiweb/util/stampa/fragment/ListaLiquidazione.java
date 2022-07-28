package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IListeLiquidazioneEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.ImportiRipartitiListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONElencoListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.nuovoprocedimento.BandoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class ListaLiquidazione extends Fragment
{

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {

  }

  @Override
  public void writeFragmentListaLiquidazione(XMLStreamWriter writer,
      RigaJSONElencoListaLiquidazioneDTO listaLiquidazione,
      long idListaLiquidazione,
      IListeLiquidazioneEJB listeLiquidazioneEJB, String cuName)
      throws Exception
  {

    String TAG_NAME_LISTA = "ListaLiquidazione";
    String TAG_NAME_TABELLA = "TabListaLiquidazione";
    String TAG_NAME_RIGA = "RigaListaLiq";
    String TAG_NAME_TOTALI = "Totali";

    BandoDTO bando = listeLiquidazioneEJB
        .getInformazioniBando(listeLiquidazioneEJB
            .getIdBandoByIdListaLiquidazione(idListaLiquidazione));
    if (bando != null && bando.getCodiceTipoBando().compareTo("P") == 0)
    {
      TAG_NAME_LISTA = "ListaLiquidazionePremio";
      TAG_NAME_TABELLA = "TabListaLiquidazionePremio";
      TAG_NAME_RIGA = "RigaListaLiqPremio";
      TAG_NAME_TOTALI = "TotaliPremio";

    }

    BigDecimal totQuotaUE = BigDecimal.ZERO;
    BigDecimal totQuotaReg = BigDecimal.ZERO;
    BigDecimal totQuotaNaz = BigDecimal.ZERO;
    BigDecimal totAnticipoErogato = BigDecimal.ZERO;
    BigDecimal totImportoPremio = BigDecimal.ZERO;

    BigDecimal sommaTot = BigDecimal.ZERO;
    int i = 1;
    writer.writeStartElement(TAG_NAME_LISTA);
    writeVisibility(writer, true);

    List<ImportiRipartitiListaLiquidazioneDTO> l = listeLiquidazioneEJB
        .getImportiRipartitiListaLiquidazione(idListaLiquidazione);

    writer.writeStartElement(TAG_NAME_TABELLA);

    if (l != null)
      for (ImportiRipartitiListaLiquidazioneDTO item : l)
      {
        writer.writeStartElement(TAG_NAME_RIGA);
        writeTag(writer, "Progressivo", String.valueOf(i++));
        writeTag(writer, "Operazione", item.getOperazione());
        writeTag(writer, "Identificativo", item.getIdentificativo());
        writeTag(writer, "CausalePagam", item.getCausalePagam());
        writeTag(writer, "Cuaa", item.getCuaa());
        writeTag(writer, "Denominazione", item.getDenominazione());
        if (bando != null && bando.getCodiceTipoBando().compareTo("P") == 0)
        {
          writeTag(writer, "ImportoPremio",
              IuffiUtils.FORMAT.formatCurrency(item.getImportoPremio()));
          writeTag(writer, "AnticipoErogato",
              IuffiUtils.FORMAT.formatCurrency(item.getAnticipoErogato()));
          if (item.getImportoPremio() != null)
            totImportoPremio = totImportoPremio.add(item.getImportoPremio());
          if (item.getAnticipoErogato() != null)
            totAnticipoErogato = totAnticipoErogato
                .add(item.getAnticipoErogato());
        }

        if (item.getQuotaUe() != null)
        {
          totQuotaUE = totQuotaUE.add(item.getQuotaUe());
          writeTag(writer, "QuotaUE",
              IuffiUtils.FORMAT.formatCurrency(item.getQuotaUe()));
        }
        else
          writeTag(writer, "QuotaUE", "");

        if (item.getQuotaReg() != null)
        {
          totQuotaReg = totQuotaReg.add(item.getQuotaReg());
          writeTag(writer, "QuotaReg",
              IuffiUtils.FORMAT.formatCurrency(item.getQuotaReg()));

        }
        else
          writeTag(writer, "QuotaReg", "");
        if (item.getQuotaNaz() != null)
        {
          totQuotaNaz = totQuotaNaz.add(item.getQuotaNaz());
          writeTag(writer, "QuotaNaz",
              IuffiUtils.FORMAT.formatCurrency(item.getQuotaNaz()));
        }
        else
          writeTag(writer, "QuotaNaz", "");

        if (item.getImportoTotale() != null)
        {
          sommaTot = sommaTot.add(item.getImportoTotale());
          writeTag(writer, "ImportoTotale",
              IuffiUtils.FORMAT.formatCurrency(item.getImportoTotale()));
        }
        else
          writeTag(writer, "ImportoTotale", "");

        writer.writeEndElement();

      }

    writer.writeEndElement();

    writer.writeStartElement(TAG_NAME_TOTALI);

    if (bando != null && bando.getCodiceTipoBando().compareTo("P") == 0)
    {
      writeTag(writer, "TotImpPremio",
          IuffiUtils.FORMAT.formatCurrency(totImportoPremio));
      writeTag(writer, "TotAnticipo",
          IuffiUtils.FORMAT.formatCurrency(totAnticipoErogato));
    }
    writeTag(writer, "TotQuotaUE",
        IuffiUtils.FORMAT.formatCurrency(totQuotaUE));
    writeTag(writer, "TotQuotaReg",
        IuffiUtils.FORMAT.formatCurrency(totQuotaReg));
    writeTag(writer, "TotQuotaNaz",
        IuffiUtils.FORMAT.formatCurrency(totQuotaNaz));
    writeTag(writer, "SommaTotale",
        IuffiUtils.FORMAT.formatCurrency(sommaTot));

    writer.writeEndElement();
    writer.writeEndElement();

  }

}
