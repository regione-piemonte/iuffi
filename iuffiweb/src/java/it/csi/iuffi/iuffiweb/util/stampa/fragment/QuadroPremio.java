package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.premioprestitoconduzione.PremiAllevamento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.premioprestitoconduzione.PremiColture;

public class QuadroPremio extends Fragment
{
 
  
  @Override
  public void writeFragment(XMLStreamWriter writer,
	  ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
	  {
	  	
    
	    final long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
	    procedimentoOggetto = quadroEJB.getProcedimentoEProcedimentoOggettoByIdProcedimentoOggetto(idProcedimentoOggetto, false).getProcedimentoOggetto();
	    BigDecimal totaleColture = new BigDecimal(0);
	    BigDecimal totaleAllevamento = new BigDecimal(0);
	    BigDecimal totale = new BigDecimal(0);
	    List<PremiColture> listaPremiColture = quadroEJB.getPremiColture(idProcedimentoOggetto, procedimentoOggetto.getExtIdDichiarazioneConsistenza());
	    List<PremiAllevamento> listaPremiAllevamento = quadroEJB.getPremiAllevamento(idProcedimentoOggetto, procedimentoOggetto.getExtIdDichiarazioneConsistenza());
	    if(listaPremiColture != null) {
	      for (PremiColture premiColture : listaPremiColture){
	        BigDecimal tmp = premiColture.getImportopremio();
	        totaleColture = totaleColture.add(tmp.setScale(2,BigDecimal.ROUND_HALF_UP));
	        String tmp2 = premiColture.getSuperficie().substring(0, 1);
	        if(tmp2.equalsIgnoreCase(".")) {
	          premiColture.setSuperficie("0" + premiColture.getSuperficie());
	        }
	        premiColture.setSuperficie(premiColture.getSuperficie().replace(".", ","));
	        if(premiColture.getSuperficie().equalsIgnoreCase("0")) {
	          premiColture.setSuperficie("0,00");
	        }
	        premiColture.setImportopremio(premiColture.getImportopremio().setScale(2,BigDecimal.ROUND_HALF_UP));
	        premiColture.setImportounitario(premiColture.getImportounitario().setScale(2,BigDecimal.ROUND_HALF_UP));
	        premiColture.setImportopremioS(premiColture.getImportopremio().toString().replace(".", ","));
	        premiColture.setImportounitarioS(premiColture.getImportounitario().toString().replace(".", ","));
	      }
	    }
	    if(listaPremiAllevamento != null) {
	      for (PremiAllevamento premiAllevamento : listaPremiAllevamento){
	        premiAllevamento.setUbaS(premiAllevamento.getUba().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
	        premiAllevamento.setCoefficienteS(premiAllevamento.getCoefficiente().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
	        BigDecimal tmp = premiAllevamento.getImportopremio();
	        totaleAllevamento = totaleAllevamento.add(tmp.setScale(2,BigDecimal.ROUND_HALF_UP));
	        String tmp2 = premiAllevamento.getUbaS().substring(0, 1);
	        if(tmp2.equalsIgnoreCase(".")) {
	          premiAllevamento.setUbaS("0" + premiAllevamento.getUba());
	        }
	        
	        String tmp3 = premiAllevamento.getCoefficienteS().substring(0, 1);
	        if(tmp3.equalsIgnoreCase(".")) {
	          premiAllevamento.setCoefficienteS("0" + premiAllevamento.getCoefficiente());
	        }
	        premiAllevamento.setUbaS(premiAllevamento.getUbaS().replace(".", ","));
	        premiAllevamento.setCoefficienteS(premiAllevamento.getCoefficienteS().replace(".", ","));
	        if(premiAllevamento.getUbaS().equalsIgnoreCase("0")) {
	          premiAllevamento.setUbaS("0,00");
	        }
	        if(premiAllevamento.getCoefficienteS().equalsIgnoreCase("0")) {
	          premiAllevamento.setCoefficienteS("0,00");
	        }
	        premiAllevamento.setImportopremio(premiAllevamento.getImportopremio().setScale(2,BigDecimal.ROUND_HALF_UP));
	        premiAllevamento.setImportounitario(premiAllevamento.getImportounitario().setScale(2,BigDecimal.ROUND_HALF_UP));
	        premiAllevamento.setImportopremioS(premiAllevamento.getImportopremio().toString().replace(".", ","));
	        premiAllevamento.setImportounitarioS(premiAllevamento.getImportounitario().toString().replace(".", ","));
	      }
	    }
	    
	    totale = totaleAllevamento.add(totaleColture).setScale(2,BigDecimal.ROUND_HALF_UP);
	    
	    
	    
	    
	    
	    
	    writer.writeStartElement("QuadroPremio");
	    writeVisibility(writer, true);
	    writeTag(writer, "TitoloQuadroPremio", "Quadro - Dettaglio premio prestiti di conduzione");
	    
	    
	    if(listaPremiColture!=null)
	    {
	      writer.writeStartElement("SezPremioSuperfici");
	      writeVisibility(writer, true);
	      writer.writeStartElement("TabPremioSuperfici");
	      
	      for(PremiColture item:listaPremiColture) {
	        writer.writeStartElement("RigaPremioSuperfici");
	        writeTag(writer, "Utilizzo", item.getTipo());
	        writeTag(writer, "SupUtilizzata", item.getSuperficie());
	        writeTag(writer, "PremioUnitario", item.getImportounitarioS());
	        writeTag(writer, "ImportoPremio", item.getImportopremioS());
	        writer.writeEndElement(); 
	      }
	      writer.writeStartElement("SezTotSuperfici");
	      writeTag(writer, "TotaleSuperfici", totaleColture.toString().replace(".", ","));
	      writer.writeEndElement(); 
	      
	      writer.writeEndElement(); 
	      writer.writeEndElement(); 
	    }
	    
	    if(listaPremiAllevamento!=null)
      {
        writer.writeStartElement("SezPremioAllevamenti");
        writeVisibility(writer, true);
        writer.writeStartElement("TabPremioAllevamenti");
        
        for(PremiAllevamento item:listaPremiAllevamento) {
          writer.writeStartElement("RigaPremioAllevamenti");
          writeTag(writer, "Specie", item.getSpecie());
          writeTag(writer, "Categoria", item.getCategoria());
          writeTag(writer, "Uba", item.getUbaS());
          writeTag(writer, "CoeffUba", item.getCoefficienteS());
          writeTag(writer, "PremioUnitario", item.getImportounitarioS());
          writeTag(writer, "ImpPremio", item.getImportopremioS());
          writer.writeEndElement(); 
        }
        writer.writeStartElement("SezTotAllevamenti");
        writeTag(writer, "TotaleAllevamenti", totaleAllevamento.toString().replace(".", ","));
        writer.writeEndElement();
        
        writer.writeEndElement(); 
        writer.writeEndElement(); 
      }
	    
	    writer.writeStartElement("SezRiepilogoPremio");
	    writeTag(writer, "RiepilogoPremio", totale.toString().replace(".", ","));
	    writer.writeEndElement(); 
	    writer.writeEndElement(); 								//QuadroPremio
	  }
}
