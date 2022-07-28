package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.stampa.placeholder.PlaceHolderManager;

public class FirmaLetteraAmmissionePremio extends Fragment
{
  public static final String TAG_NAME_FIRMA = "RiquadroFirma";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    writer.writeStartElement(TAG_NAME_FIRMA);
    writeTag(writer, "DataFirma",
        IuffiUtils.DATE.formatDate(procedimentoOggetto.getDataFine()));
    writeTag(writer, "FunzionarioIstruttore",
        PlaceHolderManager.PLACEHOLDER_NON_VALORIZZATO);

    String funzionario = quadroEJB
        .getResponsabileProcedimento(procedimentoOggetto.getIdProcedimento());
    if (funzionario != null)
    {
      writeTag(writer, "FunzionarioGradoSuperiore", funzionario);
    }
    else
    {
      writeTag(writer, "FunzionarioGradoSuperiore",
          PlaceHolderManager.PLACEHOLDER_NON_VALORIZZATO);
    }
    // smrcomune_v_amm_competenza.RESPONSABILE
    writeTag(writer, "ResponsabileProcedimento", quadroEJB
        .getResponsabileProcedimento(procedimentoOggetto.getIdProcedimento()));

    writer.writeEndElement();
  }
}
