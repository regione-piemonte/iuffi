package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaElencoInterventi;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroInterventiInfrastrutture extends Fragment
{
  private static final String TAG_NAME_FRAGMENT_QUADRO_INTERVENTI 					= "QuadroInterInfrastrutture";
  private static final String SEZIONE_VUOTA 										= "SezioneVuota";
  private static final String TAG_NAME_TITOLO_SEZIONE_INTERVENTI 					= "TitoloSezioneInterInfrastrutture";
  private static final String TAG_NAME_INTERVENTI 									= "InterInfrastrutture";
  private static final String TAG_NAME_TAB_INTERVENTI 								= "TabInterInfrastrutture";
  private static final String TAG_NAME_RIGA_INTERVENTI								= "RigaInterInfrastrutture";
  
  private static final String TAG_NAME_PROGRESSIVO_INTERVENTO 							= "ProgressivoIntervento";
  private static final String TAG_NAME_INTERVENTO 										= "Intervento";
  private static final String TAG_NAME_DESC_INTERVENTO 									= "DescIntervento";
  private static final String TAG_NAME_IMPORTO_INTERVENTO 								= "ImportoIntervento";
  
  private static final String TAG_NAME_RIGA_TOTALE										= "RigaTotale";
  private static final String TAG_NAME_TOTALE_IMPORTO									= "TotImporto";
  
  private static final String TAG_NAME_LOCALIZZAZIONE									= "Localizzazione";
  private static final String TAG_NAME_VISIBILITY_LOC									= "VisibilityLoc";
  private static final String TAG_NAME_TAB_LOC											= "TabLoc";
  private static final String TAG_NAME_RIGA_LOC											= "RigaLoc";
  
  private static final String TAG_NAME_COMUNE											= "Comune";

	@Override
	public void writeFragment(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
			String cuName) throws Exception
	{
		
	    final IInterventiEJB ejbInterventi = IuffiUtils.APPLICATION.getEjbInterventi();
	    final long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
	    List<RigaElencoInterventi> elenco = ejbInterventi
	            .getElencoInterventiProcedimentoOggetto(idProcedimentoOggetto,
	                IuffiConstants.FLAGS.NO, procedimentoOggetto.getDataFine());
	    

		writer.writeStartElement(TAG_NAME_FRAGMENT_QUADRO_INTERVENTI);
		writeTag(writer, SEZIONE_VUOTA, "false");
		writeVisibility(writer, true);
		writeTag(writer, TAG_NAME_TITOLO_SEZIONE_INTERVENTI, "Quadro - Interventi Richiesti");
		writer.writeStartElement(TAG_NAME_INTERVENTI);
		writer.writeStartElement(TAG_NAME_TAB_INTERVENTI);
		BigDecimal somma = new BigDecimal("0.0");
		int countComuni = 0;
		if (elenco != null && elenco.size() > 0)
		{
			for (RigaElencoInterventi item : elenco)
			{
				writer.writeStartElement(TAG_NAME_RIGA_INTERVENTI);		// TAG_NAME_RIGA_INTERVENTI
				writeTag(writer, TAG_NAME_PROGRESSIVO_INTERVENTO, IuffiUtils.STRING.nvl(item.getProgressivo()));
				writeTag(writer, TAG_NAME_INTERVENTO, item.getDescIntervento());
				writeTag(writer, TAG_NAME_DESC_INTERVENTO, item.getUlterioriInformazioni());
				writeTag(writer, TAG_NAME_IMPORTO_INTERVENTO, IuffiUtils.FORMAT.formatDecimal2(item.getImportoInvestimento()) + " €");
				writer.writeEndElement(); 								// TAG_NAME_RIGA_INTERVENTI
				somma = somma.add(item.getImportoInvestimento());
				if(item.getDescComuni() != null && !item.getDescComuni().equals(""))
				{
					countComuni++;
				}
			}
		}
		else
		{
			writer.writeStartElement(TAG_NAME_RIGA_INTERVENTI);			// TAG_NAME_RIGA_INTERVENTI
			writeTag(writer, TAG_NAME_PROGRESSIVO_INTERVENTO, "");
			writeTag(writer, TAG_NAME_INTERVENTO, "");
			writeTag(writer, TAG_NAME_DESC_INTERVENTO, "");
			writeTag(writer, TAG_NAME_IMPORTO_INTERVENTO, "");
			writer.writeEndElement(); 									// TAG_NAME_RIGA_INTERVENTI
		}
		String sommaFormatted = IuffiUtils.FORMAT.formatDecimal2(somma) + " €";
		writer.writeStartElement(TAG_NAME_RIGA_TOTALE);					//TAG_NAME_RIGA_TOTALE
		writeTag(writer,TAG_NAME_TOTALE_IMPORTO,sommaFormatted);
		writer.writeEndElement(); 										// TAG_NAME_RIGA_TOTALE
		writer.writeEndElement(); 										// TAG_NAME_TAB_INTERVENTI
		writer.writeEndElement(); 										// TAG_NAME_INTERVENTI
		
		
		
		writer.writeStartElement(TAG_NAME_LOCALIZZAZIONE); 		//TAG_NAME_LOCALIZZAZIONE
		if (countComuni > 0)
		{
			writeTag(writer,TAG_NAME_VISIBILITY_LOC,"true");
			writer.writeStartElement(TAG_NAME_TAB_LOC); 
			for(RigaElencoInterventi item : elenco)
			{
				if(item.getDescComuni() != null && !item.getDescComuni().equals(""))
				{
					String[] comuni = item.getDescComuni().split("<br />");
					for(String comune : comuni)
					{
						writer.writeStartElement(TAG_NAME_RIGA_LOC);		//TAG_NAME_RIGA_LOC
						writeTag(writer, TAG_NAME_PROGRESSIVO_INTERVENTO, IuffiUtils.STRING.nvl(item.getProgressivo()));
						writeTag(writer, TAG_NAME_COMUNE, comune);
						writer.writeEndElement();							//TAG_NAME_RIGA_LOC
					}
				}
			}
			writer.writeEndElement();
		}
		else
		{
			writeTag(writer,TAG_NAME_VISIBILITY_LOC,"false");
			writer.writeStartElement(TAG_NAME_TAB_LOC);			//TAG_NAME_TAB_LOC
			writer.writeStartElement(TAG_NAME_RIGA_LOC);		//TAG_NAME_RIGA_LOC
			writeTag(writer, TAG_NAME_PROGRESSIVO_INTERVENTO, "");
			writeTag(writer, TAG_NAME_COMUNE, "");
			writer.writeEndElement();							//TAG_NAME_RIGA_LOC
			writer.writeEndElement();							//TAG_NAME_TAB_LOC
		}
		writer.writeEndElement();								// TAG_NAME_LOCALIZZAZIONE 
		writer.writeEndElement(); 								// TAG_NAME_FRAGMENT_QUADRO_INTERVENTI
	}
}
