package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ConsistenzaAziendale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ProduzioneAnimale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.consistenzaaziendale.ProduzioneVegetale;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.produzionicertificate.ProduzioniCertificate;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.produzionicertificate.ProduzioniTradizionali;

public class QuadroProduzioniCertificate extends Fragment {
  
  public static final String TAG_NAME_FRAGMENT           = "QuadroProduzCertificateTipiche";
 

  @Override
  public void writeFragment(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB, String cuName) throws Exception {
    long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
    long idDatiProcedimento = quadroEJB.getIdDatiProcedimento(idProcedimentoOggetto);
    List<ProduzioniCertificate> listaProduzioniCertificate = quadroEJB.getProduzioniCertificate(idDatiProcedimento);
    List<ProduzioniTradizionali> listaProduzioniTradizionali = quadroEJB.getProduzioniTradizionali(idDatiProcedimento);
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, (listaProduzioniCertificate != null && listaProduzioniCertificate.size() > 0) || (listaProduzioniTradizionali != null && listaProduzioniTradizionali.size() > 0));
    writeTag(writer, "TitoloProduzCertificateTipiche",
        "Quadro - Produzioni certificate e tipiche");
    writeDatiProduzioniCertificate(writer, procedimentoOggetto, listaProduzioniCertificate, listaProduzioniTradizionali);
    writer.writeEndElement();
  }

  protected void writeDatiProduzioniCertificate(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, 
    List<ProduzioniCertificate> listaProduzioniCertificate, List<ProduzioniTradizionali> listaProduzioniTradizionali) throws XMLStreamException {
    if(listaProduzioniCertificate != null) {
      writer.writeStartElement("SezProduzioneCertificata");
      writeVisibility(writer, true);
      writer.writeStartElement("TabProduzioneCertificata");
      for (ProduzioniCertificate element : listaProduzioniCertificate) {
        writer.writeStartElement("RigaProduzioneCertificata");
        writeTag(writer, "TipoProdotto", element.getDescprod());
        writeTag(writer, "ProdottoCertificato", element.getDescprodcert());
        writeTag(writer, "SistemaQualita", element.getDescqualita());
        if(element.getBio() == null || element.getBio().equalsIgnoreCase("N")) {
          writeTag(writer, "ProduzBiologicaCert", "No");
        }
        else {
          writeTag(writer, "ProduzBiologicaCert", "Si");
        }
        
        writer.writeEndElement(); 
      }        
      writer.writeEndElement();
      writer.writeEndElement();
    }
    if(listaProduzioniTradizionali != null) {        
      writer.writeStartElement("SezProduzioneTipica");
      writeVisibility(writer, true);
      writer.writeStartElement("TabProduzioneTipica");
      for (ProduzioniTradizionali element : listaProduzioniTradizionali) {
        writer.writeStartElement("RigaProduzioneTipica");
        writeTag(writer, "TipoProdottoTip", element.getDescprod());
        writeTag(writer, "ProdottoTradizionale", element.getDescprodtrad());
        if(element.getBio() == null || element.getBio().equalsIgnoreCase("N")) {
          writeTag(writer, "ProduzBiologica", "No");
        }
        else {
          writeTag(writer, "ProduzBiologica", "Si");
        }
        writer.writeEndElement(); 
      }  
      writer.writeEndElement();
      writer.writeEndElement();
    }
    
  }
}
