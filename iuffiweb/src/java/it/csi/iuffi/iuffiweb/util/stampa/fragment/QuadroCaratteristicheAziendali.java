package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.DecodificaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.caratteristicheaziendali.CaratteristicheAziendali;

public class QuadroCaratteristicheAziendali extends Fragment {
  
  public static final String TAG_NAME_FRAGMENT           = "QuadroCaratteristicheAziendali";
  public static final String TAG_NAME_REGIME_AIUTO = "SezAziendaBiologica";
 

  @Override
  public void writeFragment(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB, String cuName) throws Exception {
    CaratteristicheAziendali dati = quadroEJB.getCaratteristicheAziendali(procedimentoOggetto.getIdProcedimentoOggetto());    
    if(dati != null) {
      // Se  trovo le caratteritiche aziendali vado a cercare tutte le info da visualizzare (metodi di coltivazione, filiere etc)
      List<DecodificaDTO<Long>> metodiColt = quadroEJB.getListaMetodiAttivi(dati.getId());
      List<DecodificaDTO<Long>> filiere = quadroEJB.getListaFiliereAttivi(dati.getId());
      List<DecodificaDTO<Long>> multi = quadroEJB.getListaMultiAttivi(dati.getId());
      List<String> tipiAttivita = quadroEJB.getListaTipoAttivita(procedimentoOggetto.getIdProcedimento());
      
      // costruisco le descrizioni per attivita, filiere, metodi coltivazione e multifunzionalita
      if(tipiAttivita != null && tipiAttivita.size() > 0) {
        dati.setDesccatattiva(tipiAttivita.get(0));
      }
      
      String metodoCOltivazione = getStringaDescrizione(metodiColt);
      dati.setDescmetodocolt(metodoCOltivazione);
      
      String fil = getStringaDescrizione(filiere);
      dati.setDescfiliera(fil);
      
      String mult = getStringaDescrizione(multi);
      dati.setDescmulti(mult);
    }
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, dati != null);
    writeTag(writer, "TitoloCaratteristicheAziendali",
        "Quadro - Caratteristiche aziendali");
    writeDatiCaratteristicheAziendali(writer, procedimentoOggetto, dati);
    writer.writeEndElement();
  }

  protected void writeDatiCaratteristicheAziendali(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, CaratteristicheAziendali dati) throws XMLStreamException {
    writer.writeStartElement(TAG_NAME_REGIME_AIUTO);
    if(dati!=null) {
      writeTag(writer, "DenominazioneInVetrina", dati.getDenominazione());
      writeTag(writer, "DescrizioneAzienda", dati.getDescrizione());
      writeTag(writer, "OrganismoControllo", dati.getDescodc());
      writeTag(writer, "TipoAttivita", dati.getDesccatattiva());
      writeTag(writer, "AltroTipoAttivita", dati.getAltrotipoattivita());
      writeTag(writer, "MetodoColtivazione", dati.getDescmetodocolt());
      writeTag(writer, "FilieraProduttiva", dati.getDescfiliera());
      writeTag(writer, "AltraFiliera", dati.getAltrafiliera());
      writeTag(writer, "Multifunzionalita", dati.getDescmulti());
      writeTag(writer, "AltraMultifunzionalita", dati.getAltrafunz());
      writeTag(writer, "TrasformazioneProdotti", dati.getDesctrasformazione());
    }
    writer.writeEndElement();
  }
  
  private String getStringaDescrizione(List<DecodificaDTO<Long>> input) {
    String result = "";
    int i = 0;
    for (DecodificaDTO<Long> decodificaDTO : input)
    {
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
