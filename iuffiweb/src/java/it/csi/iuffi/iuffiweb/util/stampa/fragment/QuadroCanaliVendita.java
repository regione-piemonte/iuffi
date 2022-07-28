package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.canalidivendita.CanaliDiVendita;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ConsistenzaAziendale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ProduzioneAnimale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ProduzioneVegetale;

public class QuadroCanaliVendita extends Fragment {
  
  public static final String TAG_NAME_FRAGMENT           = "QuadroCanaliVendita";
 

  @Override
  public void writeFragment(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB, String cuName) throws Exception {
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);
    CanaliDiVendita dati = quadroEJB.getCanaliDiVendita(idDatiProcedimento);
    if(dati != null) {
      // Se  trovo le caratteritiche aziendali vado a cercare tutte le info da visualizzare (metodi di coltivazione, filiere etc)
      List<DecodificaDTO<Long>> listaCanali = quadroEJB.getListaCanaliDiVenditaAttivi(dati.getId());      
      // costruisco le descrizioni per attivita, filiere, metodi coltivazione e multifunzionalita
      String canali = getStringaDescrizione(listaCanali);
      dati.setCanale(canali);
    }
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, dati != null);
    writeTag(writer, "TitoloCanaliVendita",
        "Quadro - Canali di vendita");
    writeDatiCanaliVendita(writer, procedimentoOggetto, dati);
    writer.writeEndElement();
  }

  protected void writeDatiCanaliVendita(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, CanaliDiVendita dati) throws XMLStreamException {
    writer.writeStartElement("SezCanaliVendita");
    if(dati!=null) {
      writeTag(writer, "CanaleVendita", dati.getCanale());
      writeTag(writer, "AltroCanaleVendita", dati.getAltrocanale());
      writeTag(writer, "SitoWeb", dati.getSitoweb());
      writeTag(writer, "Amazon", dati.getAmazon());
      writeTag(writer, "Orari", dati.getOrari());
      writeTag(writer, "IndirizzoVendita", dati.getIndirizzo());
      writeTag(writer, "Telefono", dati.getTelefono());
      writeTag(writer, "Email", dati.getEmail());
      writeTag(writer, "LuogGiorno", dati.getDettaglimercati());
      writeTag(writer, "Come", dati.getComearrivate());
      writeTag(writer, "Facebook", dati.getFacebook());
      writeTag(writer, "Instagram", dati.getInstagram());
      writeTag(writer, "Note", dati.getNote());
      writeTag(writer, "Immagine", dati.getDescimmagine());
    }
    writer.writeEndElement();
  }
  
  private String getStringaDescrizione(List<DecodificaDTO<Long>> input) {
    String result = "";
    int i = 0;
    for (DecodificaDTO<Long> decodificaDTO : input) {
      if(i == 0) {
        result = decodificaDTO.getDescrizione();
      }
      else {
        result = result + ", " + decodificaDTO.getDescrizione();
      }
      i++;
    }
    return result;
    
  }
}
