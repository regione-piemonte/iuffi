package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IListeLiquidazioneEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.listeliquidazione.RigaJSONElencoListaLiquidazioneDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class FirmaListaLiquidazione extends Fragment
{
  public static final String TAG_NAME_FRAGMENT = "Firma";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {

  }

  @Override
  public void writeFragmentListaLiquidazione(XMLStreamWriter writer,
      RigaJSONElencoListaLiquidazioneDTO listaLiquidazione,
      long idListeLiquidazione, IListeLiquidazioneEJB listeLiquidazioneEJB,
      String cuName) throws Exception
  {
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, true);
    writeTag(writer, "TecnicoLiquidatore",
        IuffiUtils.STRING.concat(" ",
            listaLiquidazione.getNomeTecnicoLiquidatore(),
            listaLiquidazione.getCognomeTecnicoLiquidatore()));
    writer.writeEndElement();
  }

}
