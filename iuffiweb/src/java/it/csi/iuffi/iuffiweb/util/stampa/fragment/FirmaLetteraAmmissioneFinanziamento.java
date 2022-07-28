package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.stampa.placeholder.PlaceHolderManager;

public class FirmaLetteraAmmissioneFinanziamento extends Fragment
{
  public static final String TAG_NAME_FIRMA = "RiquadroFirma";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    QuadroOggettoDTO quadro = procedimentoOggetto
        .findQuadroByCU("CU-IUFFI-166-V");
    EsitoFinaleDTO esitoFinaleDTO = null;
    if (quadro != null)
      esitoFinaleDTO = quadroEJB.getEsitoFinale(
          procedimentoOggetto.getIdProcedimentoOggetto(),
          quadro.getIdQuadroOggetto());

    writer.writeStartElement(TAG_NAME_FIRMA);
    writeTag(writer, "DataFirma",
        IuffiUtils.DATE.formatDate(procedimentoOggetto.getDataFine()));
    if (esitoFinaleDTO != null)
    {
      writeTag(writer, "FunzionarioIstruttore",
          esitoFinaleDTO.getDescrTecnico());
      writeTag(writer, "FunzionarioGradoSuperiore",
          esitoFinaleDTO.getDescrGradoSup());
    }
    else
    {
      writeTag(writer, "FunzionarioIstruttore",
          PlaceHolderManager.PLACEHOLDER_NON_VALORIZZATO);
      writeTag(writer, "FunzionarioGradoSuperiore",
          PlaceHolderManager.PLACEHOLDER_NON_VALORIZZATO);
    }
    // smrcomune_v_amm_competenza.RESPONSABILE
    writeTag(writer, "ResponsabileProcedimento", quadroEJB
        .getResponsabileProcedimento(procedimentoOggetto.getIdProcedimento()));

    writer.writeEndElement();
  }
}
