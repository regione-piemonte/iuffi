package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiAziendaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiIdentificativi;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiRappresentanteLegaleDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiSoggettoFirmatarioDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.regimeaiuto.RegimeAiuto;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroRegimeAiuto extends Fragment {
  
  public static final String TAG_NAME_FRAGMENT           = "QuadroRegimeAiuto";
  public static final String TAG_NAME_REGIME_AIUTO = "DatiRegimeAiuto";
 

  @Override
  public void writeFragment(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB, String cuName) throws Exception {
    RegimeAiuto dati = quadroEJB.getRegimeAiutoProcedimentoOggetto(procedimentoOggetto.getIdProcedimentoOggetto());
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, true);
    writeTag(writer, "TitoloRegimeAiuto",
        "Quadro - Dati Regime Aiuto");
    writeDatiRegimeAiuto(writer, procedimentoOggetto, dati);
    writer.writeEndElement();
  }

  protected void writeDatiRegimeAiuto(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, RegimeAiuto dati) throws XMLStreamException {
    writer.writeStartElement(TAG_NAME_REGIME_AIUTO);
    if(dati!=null)
    {
    writeTag(writer, "DescBreve", dati.getDescbreve());
    writeTag(writer, "DescEstesa", dati.getDescestesa());
    writeTag(writer, "ABI", dati.getAbi());
    writeTag(writer, "DenominazBanca", dati.getDenominazione());
    writeTag(writer, "CAB", dati.getCab());
    writeTag(writer, "IndirizzoBanca", dati.getIndirizzo());
    writeTag(writer, "ProvinciaBanca", dati.getProvincia());
    writeTag(writer, "ComuneBanca", dati.getDesccomune());
    writeTag(writer, "CapBanca", dati.getCap());
    writeTag(writer, "AltroIstituto", dati.getAltroistituto());
    writeTag(writer, "PecBanca", dati.getPec());
    writeTag(writer, "MailBanca", dati.getEmail());
    }
    writer.writeEndElement();
  }
}
