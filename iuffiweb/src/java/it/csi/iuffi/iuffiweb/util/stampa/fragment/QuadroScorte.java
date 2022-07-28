package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.scorte.ScorteDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroScorte extends Fragment
{
  private static final String TAG_NAME_DESCRIZIONE_SCORTE = "DescrizioneScorte";
private static final String TAG_NAME_QUANTITA_SCORTE = "QuantitaScorte";
private static final String TAG_NAME_TIPO_SCORTE = "TipoScorte";
private static final String TAG_NAME_FRAGMENT_SCORTE 		= "QuadroScorte";
  private static final String SEZIONE_VUOTA 				= "SezioneVuota";
  private static final String TAG_NAME_TITOLO_SEZIONE 		= "TitoloSezioneScorte";
  private static final String TAG_NAME_SCORTE 				= "Scorte";
  private static final String TAG_NAME_TAB_SCORTE 			= "TabScorte";
  private static final String TAG_NAME_RIGA_SCORTE			= "RigaScorte";
  private static final String TAG_NAME_VISIBILITY_SCORTE	= "VisibilityScorte";
  private static final String TAG_NAME_SEZIONE_NULLA		= "SezioneNulla";
  private static final String TAG_NAME_VISIBILITY_TESTO_NULLO	= "VisibilityTestoNullo";
  
  @Override
  public void writeFragment(XMLStreamWriter writer,
	  ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
	  {
	  	String visibilityTestoNullo = "false";
	    final IQuadroIuffiEJB ejbQuadroIuffi = IuffiUtils.APPLICATION.getEjbQuadroIuffi();
	    final long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
	    List<ScorteDTO> listScorte =  ejbQuadroIuffi.getListaScorteByProcedimentoOggetto(idProcedimentoOggetto);
	
	    writer.writeStartElement(TAG_NAME_FRAGMENT_SCORTE);
	    writeTag(writer,SEZIONE_VUOTA,"false");
	    writeVisibility(writer, true);
	    writeTag(writer, TAG_NAME_TITOLO_SEZIONE, "Quadro - Scorte");
	    writer.writeStartElement(TAG_NAME_SCORTE);
		
	    if(listScorte != null && listScorte.size()>0)
		{
	    	writeTag(writer,TAG_NAME_VISIBILITY_SCORTE,"true");
	    	writer.writeStartElement(TAG_NAME_TAB_SCORTE);
	    	
			for(ScorteDTO item : listScorte)
	    	{
	    		writer.writeStartElement(TAG_NAME_RIGA_SCORTE);
		    		writeTag(writer, TAG_NAME_TIPO_SCORTE, item.getDescrizioneScorta());
		    		writeTag(writer, TAG_NAME_QUANTITA_SCORTE, item.getQuantitaUnitaMisuraFormatter());
		    		writeTag(writer, TAG_NAME_DESCRIZIONE_SCORTE, item.getDescrizionePerStampa());
	    		writer.writeEndElement(); 		//TAG_NAME_RIGA_PLV
	    	}
	    	writer.writeEndElement(); 			//TAG_NAME_TAB_SCORTE
	    	visibilityTestoNullo = "false";
		}
	    else
	    {
	    	writeTag(writer,TAG_NAME_VISIBILITY_SCORTE,"false");
	    	writer.writeStartElement(TAG_NAME_TAB_SCORTE);		//TAG_NAME_TAB_SCORTE
	    	writer.writeStartElement(TAG_NAME_RIGA_SCORTE);		//TAG_NAME_RIGA_SCORTE
	    		writeTag(writer, TAG_NAME_TIPO_SCORTE, "");
	    		writeTag(writer, TAG_NAME_QUANTITA_SCORTE, "");
	    		writeTag(writer, TAG_NAME_DESCRIZIONE_SCORTE, "");
	    	writer.writeEndElement(); 							//TAG_NAME_RIGA_SCORTE
	    	writer.writeEndElement(); 							//TAG_NAME_TAB_SCORTE
	    	visibilityTestoNullo = "true";
	    }
	    writer.writeEndElement(); 								//TAG_NAME_SCORTE
	    writer.writeStartElement(TAG_NAME_SEZIONE_NULLA); 		//TAG_NAME_SEZIONE_NULLA
	    writeTag(writer, TAG_NAME_VISIBILITY_TESTO_NULLO, visibilityTestoNullo);
	    writer.writeEndElement(); 								//TAG_NAME_SEZIONE_NULLA
		writer.writeEndElement(); 								//TAG_NAME_FRAGMENT
	  }
}
