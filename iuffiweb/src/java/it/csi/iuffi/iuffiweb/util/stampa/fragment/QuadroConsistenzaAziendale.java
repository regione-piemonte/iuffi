package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ConsistenzaAziendale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ProduzioneAnimale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ProduzioneVegetale;

public class QuadroConsistenzaAziendale extends Fragment {
  
  public static final String TAG_NAME_FRAGMENT           = "QuadroConsistenzaAziendale";
 

  @Override
  public void writeFragment(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB, String cuName) throws Exception {
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);
    ConsistenzaAziendale dati = new ConsistenzaAziendale();
    List<ProduzioneAnimale> listaProduzioniAnimali = quadroEJB.getProduzioniAnimali(idDatiProcedimento);
    List<ProduzioneVegetale> listaProduzioniVegetali = quadroEJB.getProduzioniVegetali(idDatiProcedimento);
    if(listaProduzioniVegetali != null && !listaProduzioniVegetali.isEmpty()) {
      dati.setProduzioniVegetali(listaProduzioniVegetali);
    }
    if(listaProduzioniAnimali != null && !listaProduzioniAnimali.isEmpty()) {
      dati.setProduzioniAnimali(listaProduzioniAnimali);
    }
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, (listaProduzioniVegetali != null && !listaProduzioniVegetali.isEmpty()) || (listaProduzioniAnimali != null && !listaProduzioniAnimali.isEmpty()));
    writeTag(writer, "TitoloConsistenzaAziendale",
        "Quadro - Consistenza aziendale");
    writeDatiConsistenzaAziendale(writer, procedimentoOggetto, dati);
    writer.writeEndElement();
  }

  protected void writeDatiConsistenzaAziendale(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, ConsistenzaAziendale dati) throws XMLStreamException {
    if(dati!=null) {
      if(dati.getProduzioniVegetali() != null) {
        writer.writeStartElement("SezProduzioneVegetale");
        writeVisibility(writer, true);
        writer.writeStartElement("TabProduzioneVegetale");
        for (ProduzioneVegetale element : dati.getProduzioniVegetali()) {
          writer.writeStartElement("RigaProduzioneVegetale");
          writeTag(writer, "Comune", element.getComune());
          writeTag(writer, "Sez", element.getSez());
          writeTag(writer, "Fg", element.getFg());
          writeTag(writer, "Part", element.getPart());
          writeTag(writer, "Sub", element.getSub());
          writeTag(writer, "UsoSuolo", element.getUsodelsuolo());
          writeTag(writer, "ProduzioneBiologica", element.getProduzionebiologica());
          if(element.getPubblica() == null || element.getPubblica().equalsIgnoreCase("N")) {
            writeTag(writer, "Pubblica", "No");
          }
          else {
            writeTag(writer, "Pubblica", "Si");
          }
          writer.writeEndElement(); 
        }        
        writer.writeEndElement();
        writer.writeEndElement();
      }
      if(dati.getProduzioniAnimali() != null) {        
        writer.writeStartElement("SezProduzioneAnimale");
        writeVisibility(writer, true);
        writer.writeStartElement("TabProduzioneAnimale");
        for (ProduzioneAnimale element : dati.getProduzioniAnimali()) {
          writer.writeStartElement("RigaProduzioneAnimale");
          writeTag(writer, "CodAzienda", element.getCodiceaziendazootecnica());
          writeTag(writer, "Specie", element.getSpecieanimale());
          writeTag(writer, "Categoria", element.getCategoriaanimale());
          writeTag(writer, "Quantita", element.getQuantita());
          writeTag(writer, "ProduzioeBiologicaAnim", element.getProduzionebiologica());
          if(element.getPubblica() == null || element.getPubblica().equalsIgnoreCase("N")) {
            writeTag(writer, "PubblicaAnim", "No");
          }
          else {
            writeTag(writer, "PubblicaAnim", "Si");
          }
          writer.writeEndElement(); 
        }  
        writer.writeEndElement();
        writer.writeEndElement();
      }
    }
  }
}
