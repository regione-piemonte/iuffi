package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.AnnoCensitoDTO;
import it.csi.iuffi.iuffiweb.dto.DistrettoDTO;
import it.csi.iuffi.iuffiweb.dto.IpotesiPrelievoDTO;
import it.csi.iuffi.iuffiweb.dto.OgurDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroSpecieOGUR extends Fragment
{
  private static final String TAG_NAME_QUADRO_OGUR = "QuadroSpecieOGUR";
  
  @Override
  public void writeFragment(XMLStreamWriter writer,
	  ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
	  {
	    final IQuadroIuffiEJB ejbQuadroIuffi = IuffiUtils.APPLICATION.getEjbQuadroIuffi();
	    final long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
	    List<OgurDTO> listOgur =  ejbQuadroIuffi.getElencoOgur(idProcedimentoOggetto, null);
	
      writer.writeStartElement(TAG_NAME_QUADRO_OGUR);
      writeTag(writer, "VisibilityOGUR", String.valueOf(listOgur!=null && !listOgur.isEmpty()));

      if(listOgur!=null && !listOgur.isEmpty())
	    for(OgurDTO ogur : listOgur)
	    {
	       writer.writeStartElement("SpecieOGUR");
	       
	       
	      ogur = ejbQuadroIuffi.getOgur(ogur.getIdOgur(), true);
	      writer.writeStartElement("HeaderSpecie");
        writeTag(writer, "NomeSpecie", ogur.getDescrizione());
        writeTag(writer, "SuperficieATCCA", IuffiUtils.FORMAT.formatCurrency(ogur.getSuperficieTotaleAtcca()));
	      writer.writeEndElement();//HeaderSpecie
	      
	      writeTag(writer, "VisibilityDistretto", String.valueOf(ogur.getDistretti()!=null && !ogur.getDistretti().isEmpty()));

	      
	      if(ogur.getDistretti()!=null && !ogur.getDistretti().isEmpty())
	      for(DistrettoDTO distretto : ogur.getDistretti())
	      {
	        writer.writeStartElement("Distretto");
	        writeTag(writer, "NomeDistretto", distretto.getNominDistretto());
	        writeTag(writer, "SupeDistretto", IuffiUtils.FORMAT.formatCurrency(distretto.getSuperficieDistretto()));
          writeTag(writer, "SupeVenabile", IuffiUtils.FORMAT.formatCurrency(distretto.getSuperfVenabDistretto()));
          writeTag(writer, "SUS", IuffiUtils.FORMAT.formatCurrency(distretto.getSus()));

          
          for(AnnoCensitoDTO anno : distretto.getAnniCensiti())
          {
            writer.writeStartElement("DatiAnno");
            writeTag(writer, "Anno", ""+anno.getAnno());
            writeTag(writer, "Censito", IuffiUtils.FORMAT.formatCurrency(anno.getTotCensito()));
            writeTag(writer, "SupeCensita", IuffiUtils.FORMAT.formatCurrency(anno.getSuperfCensita()));
            writeTag(writer, "PianoNumerico", IuffiUtils.FORMAT.formatCurrency(anno.getPianoNumerico()));
            writeTag(writer, "Prelevato", IuffiUtils.FORMAT.formatCurrency(anno.getTotPrelevato()));
            writer.writeEndElement();//DatiAnno
          }
          
          writer.writeStartElement("Censimento");
          writeTag(writer, "Anno", ""+distretto.getCensimento().getAnno());
          writeTag(writer, "CapiSupCensita", ""+distretto.getCensimento().getDensitaSupCens());
          writeTag(writer, "CapiSus", ""+distretto.getCensimento().getDensitaCapiSus());
          writer.writeEndElement();//Censimento

          writer.writeStartElement("Previsione");
          writeTag(writer, "DensitaObiettivo", ""+distretto.getCensimento().getDensitaObiettivo());
          writeTag(writer, "Consistenza", ""+distretto.getCensimento().getConsistenzaPotenz());
          writer.writeEndElement();//Previsione
          
          for(IpotesiPrelievoDTO ipotesi : distretto.getIpotesiPrelievo()){
            writer.writeStartElement("IpotesiPrelievo");
            writeTag(writer, "Anno", ""+ipotesi.getAnno());
            writeTag(writer, "Percentuale", IuffiUtils.FORMAT.formatDecimal2(ipotesi.getPercentuale()) + "%");
            writer.writeEndElement();//IpotesiPrelievo
          }
          
          for(AnnoCensitoDTO anno : distretto.getAnniCensiti())
          {
            writer.writeStartElement("DanniCausati");
            writeTag(writer, "Anno", ""+anno.getAnno());
            writeTag(writer, "Valore", "€ " + IuffiUtils.FORMAT.formatCurrency(anno.getDanniCausati()));
            writer.writeEndElement();//DanniCausati
          }
          for(AnnoCensitoDTO anno : distretto.getAnniCensiti())
          {
            writer.writeStartElement("IncidentiStradali");
            writeTag(writer, "Anno", ""+anno.getAnno());
            writeTag(writer, "Valore", "N° " + IuffiUtils.FORMAT.formatGenericNumber(anno.getIncidentiStradali(), 0, false));
            writer.writeEndElement();//IncidentiStradali
          }
          
          writer.writeEndElement();//Distretto
	      }
	     
        writer.writeEndElement();//Specie

	    }
  
      writer.writeEndElement();//TAG_NAME_QUADRO_OGUR

	  }
}
