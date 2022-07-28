package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.trasformazioneprodotti.ProdottoTrasformato;

public class QuadroTrasformazioneProdotti extends Fragment {
  
  public static final String TAG_NAME_FRAGMENT           = "QuadroTrasformazioneProdottiBio";
 

  @Override
  public void writeFragment(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB, String cuName) throws Exception {
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);
    List<ProdottoTrasformato> listaProdottoTrasformato = quadroEJB.getProdottoTrasformato(idDatiProcedimento);
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, listaProdottoTrasformato != null && listaProdottoTrasformato.size() > 0);
    writeTag(writer, "TitoloTrasformazioneProdottiBio",
        "Quadro - Trasformazione prodotti biologici");
    writeDatiTrasformazioneProdotti(writer, procedimentoOggetto, listaProdottoTrasformato);
    writer.writeEndElement();
  }

  protected void writeDatiTrasformazioneProdotti(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, List<ProdottoTrasformato> listaProdottoTrasformato) throws XMLStreamException {
    if(listaProdottoTrasformato != null) {
      writer.writeStartElement("SezTrasformazioneProdottiBio");
      writeVisibility(writer, true);
      writer.writeStartElement("TabProdottiBio");
      for (ProdottoTrasformato element : listaProdottoTrasformato) {
        writer.writeStartElement("RigaProdottiBio");
        writeTag(writer, "ProdottoTrasformato", element.getDescrizione());
        writeTag(writer, "DettProduzioneBio", element.getNote());
        writer.writeEndElement(); 
      }        
      writer.writeEndElement();
      writer.writeEndElement();
    }
  }
}
