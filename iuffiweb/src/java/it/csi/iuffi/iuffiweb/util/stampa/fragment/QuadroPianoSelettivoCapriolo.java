package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.DataCensimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.Distretto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.pianiselettivi.RicercaDistretto;

public class QuadroPianoSelettivoCapriolo extends Fragment {
  
  public static final String TAG_NAME_FRAGMENT           = "QuadroPianoSelettivo";
  public static final long idSpecie= 2;

  @Override
  public void writeFragment(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB, String cuName) throws Exception {
    List<RicercaDistretto> idDistretto = quadroEJB.getIdDistrettoOgur(procedimentoOggetto.getIdProcedimentoOggetto());    
    List<Distretto> distretti = new ArrayList<Distretto>();
    List<Distretto> distrettiRegione = new ArrayList<Distretto>();
    String idDistrettoS = "";
    String idDistrettoRegioneS = "";
    if(idDistretto != null) {
      for (RicercaDistretto ricercaDistretto : idDistretto) {
        if(ricercaDistretto.getIddistretto() != null) {
          if(idDistrettoS.equalsIgnoreCase("")) {
            idDistrettoS = idDistrettoS + ricercaDistretto.getIddistretto();
          }
          else {
            idDistrettoS = idDistrettoS + ", " + ricercaDistretto.getIddistretto();
          }
        }
        else if(ricercaDistretto.getIddistrettoregione() != null) {
          if(idDistrettoRegioneS.equalsIgnoreCase("")) {
            idDistrettoRegioneS = idDistrettoRegioneS + ricercaDistretto.getIddistrettoregione();
          }
          else {
            idDistrettoRegioneS = idDistrettoRegioneS + ", " + ricercaDistretto.getIddistrettoregione();
          }
        }        
      }
    }
    
    if(!idDistrettoS.equalsIgnoreCase("")) {
      distretti = quadroEJB.getElencoDistrettiOgur(false, "("+ idDistrettoS + ")", idSpecie, null, procedimentoOggetto.getIdProcedimentoOggetto());
    }
    else if(!idDistrettoRegioneS.equalsIgnoreCase("")) {
      distrettiRegione = quadroEJB.getElencoDistrettiOgur(true, "("+ idDistrettoRegioneS + ")", idSpecie, null, procedimentoOggetto.getIdProcedimentoOggetto());
    }
    for (Distretto distretto : distretti){
      distretto.setTipo("distretto");
    }
    for (Distretto distretto : distrettiRegione){
      distretto.setTipo("regione");
    }    
    writer.writeStartElement(TAG_NAME_FRAGMENT);
    writeVisibility(writer, (distretti != null && distretti.size() > 0) || (distrettiRegione != null && distrettiRegione.size() > 0));
    writeTag(writer, "TitoloPianoSelettivo",
        "Quadro - Piano selettivo caprioli");
    writeDatiPianoSelettivo(writer, procedimentoOggetto, distretti, distrettiRegione, quadroEJB);
    writer.writeEndElement();
  }

  protected void writeDatiPianoSelettivo(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, List<Distretto> distretti, List<Distretto> distrettiRegione, IQuadroEJB quadroEJB) throws XMLStreamException, Exception {
    if((distretti!=null && distretti.size() > 0) || (distrettiRegione!=null && distrettiRegione.size() > 0)) {
      writer.writeStartElement("TabDistretti");
      List<Distretto> distrettiList = new ArrayList<Distretto>();
      if(distretti != null) {
        for (Distretto distretto : distretti) {
          List<Distretto> distrettoCompleto = quadroEJB.getElencoDistrettiOgur(false, "("+ String.valueOf(distretto.getIdDistretto()) + ")", idSpecie, distretto.getIdPianoDistrettoOgur(), procedimentoOggetto.getIdProcedimentoOggetto());
          distrettiList.add(distrettoCompleto.get(0));
        }
        for (Distretto distretto : distrettiRegione) {
          List<Distretto> distrettoCompleto = quadroEJB.getElencoDistrettiOgur(true, "("+ String.valueOf(distretto.getIdDistretto()) + ")", idSpecie, distretto.getIdPianoDistrettoOgur(), procedimentoOggetto.getIdProcedimentoOggetto());
          distrettiList.add(distrettoCompleto.get(0));
        }
      }
      if(distrettiList != null) {      
        for (Distretto distrettoCompleto : distrettiList) {
          List<DataCensimento> date = quadroEJB.getDateCensimento(distrettoCompleto.getIdPianoDistrettoOgur(), idSpecie);
          
          writer.writeStartElement("RigaDistretti");
          writeTag(writer, "NomeDistretto", distrettoCompleto.getNominDistretto());
          writeTag(writer, "SupTotale", distrettoCompleto.getSuperficieDistretto().toString());
          writeTag(writer, "SupUtile", distrettoCompleto.getSus().toString());
          writer.writeStartElement("TabCensimenti");
          
          writer.writeStartElement("RigaCensimenti");
          writeTag(writer, "Etichetta", "Totale");
          writeTag(writer, "Contenuto", distrettoCompleto.getTotaleCensito());
          writer.writeEndElement(); //RigaCensimenti
          
          writer.writeStartElement("RigaCensimenti");
          writeTag(writer, "Etichetta", distrettoCompleto.getEt1());
          writeTag(writer, "Contenuto", distrettoCompleto.getCensito1());
          writer.writeEndElement(); //RigaCensimenti
          
          writer.writeStartElement("RigaCensimenti");
          writeTag(writer, "Etichetta", distrettoCompleto.getEt2());
          writeTag(writer, "Contenuto", distrettoCompleto.getCensito2());
          writer.writeEndElement(); //RigaCensimenti
          
          writer.writeStartElement("RigaCensimenti");
          writeTag(writer, "Etichetta", distrettoCompleto.getEt3());
          writeTag(writer, "Contenuto", distrettoCompleto.getCensito3());
          writer.writeEndElement(); //RigaCensimenti
          
          writer.writeStartElement("RigaCensimenti");
          writeTag(writer, "Etichetta", "Indeterminati");
          writeTag(writer, "Contenuto", distrettoCompleto.getIndeterminatiCensito());
          writer.writeEndElement(); //RigaCensimenti
          
          writer.writeEndElement(); //TabCensimenti
          
          writer.writeStartElement("SezPianiPrelievo");
          writeTag(writer, "PercPrelievo", distrettoCompleto.getPercentuale() != null ? distrettoCompleto.getPercentuale().toString()+"%" : "0.00%");
          writeTag(writer, "MaxPrelievo", distrettoCompleto.getMaxCapiPrelievo());
          
          writer.writeStartElement("TabControlliPiano");
          
          writer.writeStartElement("RigaControlliPiano");
          writeTag(writer, "Etich1", "Totale");
          writeTag(writer, "Conten1", distrettoCompleto.getTotalePrelievo());
          writeTag(writer, "Etich2", "Percentuale");
          if(distrettoCompleto.getMaxCapiPrelievo() == null || distrettoCompleto.getMaxCapiPrelievo().equalsIgnoreCase("0")) {
            distrettoCompleto.setPercentualeTotale(new BigDecimal(0));
          }
          else {
            distrettoCompleto.setPercentualeTotale(new BigDecimal(distrettoCompleto.getTotalePrelievo()).divide(new BigDecimal(distrettoCompleto.getMaxCapiPrelievo()), 2, RoundingMode.HALF_UP));
          }      
          if(distrettoCompleto.getPercentualeTotale() != null) {
            writeTag(writer, "Conten2", distrettoCompleto.getPercentualeTotale().toString());
          }
          else {
            writeTag(writer, "Conten2", "0.00");
          }
          writeTag(writer, "Etich3", "Controllo");
          if(distrettoCompleto.getEsitoTotalePrelievo() == null || distrettoCompleto.getEsitoTotalePrelievo().equalsIgnoreCase("")) {
            writeTag(writer, "Conten3", "");
          }
          else {
            writeTag(writer, "Conten3", distrettoCompleto.getEsitoTotalePrelievo().equalsIgnoreCase("S") ? "ok" : "ko");
          }          
          writer.writeEndElement(); //RigaControlliPiano
          
          writer.writeStartElement("RigaControlliPiano");
          writeTag(writer, "Etich1", distrettoCompleto.getEt1());
          writeTag(writer, "Conten1", distrettoCompleto.getPrelievo1());
          writeTag(writer, "Etich2", "Percentuale");
          writeTag(writer, "Conten2", distrettoCompleto.getPerc1() != null ? distrettoCompleto.getPerc1().toString() : "0.00");
          writeTag(writer, "Etich3", "Controllo");
          if(distrettoCompleto.getEsito1() == null || distrettoCompleto.getEsito1().equalsIgnoreCase("")) {
            writeTag(writer, "Conten3", "");
          }
          else {
            writeTag(writer, "Conten3", distrettoCompleto.getEsito1().equalsIgnoreCase("S") ? "ok" : "ko");
          }      
          writer.writeEndElement(); //RigaControlliPiano
          
          writer.writeStartElement("RigaControlliPiano");
          writeTag(writer, "Etich1", distrettoCompleto.getEt2());
          writeTag(writer, "Conten1", distrettoCompleto.getPrelievo2());
          writeTag(writer, "Etich2", "Percentuale");
          writeTag(writer, "Conten2", distrettoCompleto.getPerc2() != null ? distrettoCompleto.getPerc2().toString() : "0.00");
          writeTag(writer, "Etich3", "Controllo");
          if(distrettoCompleto.getEsito2() == null || distrettoCompleto.getEsito2().equalsIgnoreCase("")) {
            writeTag(writer, "Conten3", "");
          }
          else {
            writeTag(writer, "Conten3", distrettoCompleto.getEsito2().equalsIgnoreCase("S") ? "ok" : "ko");
          }      
          writer.writeEndElement(); //RigaControlliPiano
          
          writer.writeStartElement("RigaControlliPiano");
          writeTag(writer, "Etich1", distrettoCompleto.getEt3());
          writeTag(writer, "Conten1", distrettoCompleto.getPrelievo3());
          writeTag(writer, "Etich2", "Percentuale");
          writeTag(writer, "Conten2", distrettoCompleto.getPerc3() != null ? distrettoCompleto.getPerc3().toString() : "0.00");
          writeTag(writer, "Etich3", "Controllo");
          if(distrettoCompleto.getEsito3() == null || distrettoCompleto.getEsito3().equalsIgnoreCase("")) {
            writeTag(writer, "Conten3", "");
          }
          else {
            writeTag(writer, "Conten3", distrettoCompleto.getEsito3().equalsIgnoreCase("S") ? "ok" : "ko");
          }      
          writer.writeEndElement(); //RigaControlliPiano
          
          writer.writeEndElement(); //TabControlliPiano
          writeTag(writer, "Indeterminati", distrettoCompleto.getIndeterminatiPrelievo());
          writer.writeEndElement(); //SezPianiPrelievo
          writer.writeStartElement("TabDateCensimento");
          if(date != null) {
            for (DataCensimento data : date) {
              String dataYYYYMMGG = data.getDataCensimento().toString().substring(0, 10);
              String [] dataArray = dataYYYYMMGG.split("-");
              String dataGGMMYYYY = dataArray[2] + "-" + dataArray[1] + "-" + dataArray[0];
              writer.writeStartElement("RigaDateCensimento");
              writeTag(writer, "Data", dataGGMMYYYY);
              writeTag(writer, "Metodo", data.getDescMetodologia());
              writeTag(writer, "UniMisura", data.getUnita() + " - " + data.getDescrizione());
              writer.writeEndElement(); //RigaDateCensimento
            }
          }         
          writer.writeEndElement(); //TabDateCensimento
          writer.writeEndElement(); //RigaDistretti
        }
      }
      writer.writeEndElement(); //TabDistretti
    }
  }
}
