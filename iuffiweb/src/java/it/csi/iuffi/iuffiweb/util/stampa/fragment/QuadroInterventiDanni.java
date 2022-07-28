package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IInterventiEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.DatiLocalizzazioneParticellarePerStampa;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.interventi.RigaElencoInterventi;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroInterventiDanni extends Fragment
{
  private static final String TAG_NAME_FRAGMENT_QUADRO_INTERVENTI 					= "QuadroInterventi";
  private static final String SEZIONE_VUOTA 										= "SezioneVuota";
  private static final String TAG_NAME_TITOLO_SEZIONE_INTERVENTI 					= "TitoloSezioneInterventi";
  private static final String TAG_NAME_INTERVENTI 									= "Interventi";
  private static final String TAG_NAME_TAB_INTERVENTI 								= "TabInterventi";
  private static final String TAG_NAME_RIGA_INTERVENTI								= "RigaInterventi";
  
  private static final String TAG_NAME_PROGRESSIVO_DANNO	 								= "ProgressivoDanno";
  private static final String TAG_NAME_TIPO_DANNO 										= "TipoDanno";
  private static final String TAG_NAME_DANNO 											= "Danno";
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
  
  private static final String TAG_NAME_PROGRESSIVO										= "Progressivo";
  private static final String TAG_NAME_COMUNE											= "Comune";
  private static final String TAG_NAME_SEZIONE											= "Sezione";
  private static final String TAG_NAME_FOGLIO											= "Foglio";
  private static final String TAG_NAME_PARTICELLA										= "Particella";
  private static final String TAG_NAME_SUBALTERNO										= "Subalterno";
  private static final String TAG_NAME_SUPCATASTALE										= "SupCatastale";
  private static final String TAG_NAME_OCCUPAZIONE										= "Occupazione";
  private static final String TAG_NAME_DESTINAZIONE										= "Destinazione";
  private static final String TAG_NAME_SUPUTILIZZATA									= "SupUtilizzata";
  

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
		writeTag(writer, TAG_NAME_TITOLO_SEZIONE_INTERVENTI, "Quadro - Interventi");
		writer.writeStartElement(TAG_NAME_INTERVENTI);
		writer.writeStartElement(TAG_NAME_TAB_INTERVENTI);
		BigDecimal somma = new BigDecimal("0.0");
		if (elenco != null && elenco.size() > 0)
		{
			for (RigaElencoInterventi item : elenco)
			{
				writer.writeStartElement(TAG_NAME_RIGA_INTERVENTI);
				writeTag(writer, TAG_NAME_PROGRESSIVO_DANNO, IuffiUtils.STRING.nvl(item.getProgressivoDanno()));
				writeTag(writer, TAG_NAME_TIPO_DANNO, item.getDescTipoDanno());
				writeTag(writer, TAG_NAME_DANNO, item.getDescDanno());
				writeTag(writer, TAG_NAME_PROGRESSIVO_INTERVENTO, IuffiUtils.STRING.nvl(item.getProgressivo()));
				writeTag(writer, TAG_NAME_INTERVENTO, item.getDescIntervento());
				writeTag(writer, TAG_NAME_DESC_INTERVENTO, item.getUlterioriInformazioni());
				writeTag(writer, TAG_NAME_IMPORTO_INTERVENTO, IuffiUtils.FORMAT.formatDecimal2(item.getImportoInvestimento()) + " €");
				writer.writeEndElement(); // TAG_NAME_RIGA_PLV
				somma = somma.add(item.getImportoInvestimento());
			}
		}
		else
		{
			writer.writeStartElement(TAG_NAME_RIGA_INTERVENTI);
			writeTag(writer, TAG_NAME_PROGRESSIVO_DANNO,"");
			writeTag(writer, TAG_NAME_TIPO_DANNO, "");
			writeTag(writer, TAG_NAME_DANNO, "");
			writeTag(writer, TAG_NAME_PROGRESSIVO_INTERVENTO, "");
			writeTag(writer, TAG_NAME_INTERVENTO, "");
			writeTag(writer, TAG_NAME_DESC_INTERVENTO, "");
			writeTag(writer, TAG_NAME_IMPORTO_INTERVENTO, "");
			writer.writeEndElement(); // TAG_NAME_RIGA_PLV
		}
		String sommaFormatted = IuffiUtils.FORMAT.formatDecimal2(somma) + " €";
		writer.writeStartElement(TAG_NAME_RIGA_TOTALE);		//TAG_NAME_RIGA_TOTALE
		writeTag(writer,TAG_NAME_TOTALE_IMPORTO,sommaFormatted);
		writer.writeEndElement(); 							// TAG_NAME_RIGA_TOTALE
		writer.writeEndElement(); 							// TAG_NAME_TAB_INTERVENTI
		writer.writeEndElement(); 							// TAG_NAME_INTERVENTI
		
	    List<DatiLocalizzazioneParticellarePerStampa> listParticellare = ejbInterventi
		        .getLocalizzazioneParticellePerStampa(idProcedimentoOggetto,
		            IuffiConstants.FLAGS.NO);
		
		writer.writeStartElement(TAG_NAME_LOCALIZZAZIONE); 	//TAG_NAME_LOCALIZZAZIONE
		if(listParticellare != null && listParticellare.size()>0)
		{
			writeTag(writer, TAG_NAME_VISIBILITY_LOC, "true");
			writer.writeStartElement(TAG_NAME_TAB_LOC);			//TAG_NAME_TAB_LOC
			for(DatiLocalizzazioneParticellarePerStampa particella: listParticellare)
			{
				writer.writeStartElement(TAG_NAME_RIGA_LOC);	//TAG_NAME_RIGA_LOC
				writeTag(writer,TAG_NAME_PROGRESSIVO,IuffiUtils.STRING.nvl(particella.getProgressivo()));
				writeTag(writer,TAG_NAME_COMUNE,particella.getDescComune());
				writeTag(writer,TAG_NAME_SEZIONE,particella.getSezione());
				writeTag(writer,TAG_NAME_FOGLIO,IuffiUtils.STRING.nvl(particella.getFoglio()));
				writeTag(writer,TAG_NAME_PARTICELLA,IuffiUtils.STRING.nvl(particella.getParticella()));
				writeTag(writer,TAG_NAME_SUBALTERNO,IuffiUtils.STRING.nvl(particella.getSubalterno()));
				writeTag(writer,TAG_NAME_SUPCATASTALE,particella.getSupCatastale());
				writeTag(writer,TAG_NAME_OCCUPAZIONE,particella.getDescTipoUtilizzo());
				writeTag(writer,TAG_NAME_DESTINAZIONE,particella.getDescTipoUtilizzo());
				writeTag(writer,TAG_NAME_SUPUTILIZZATA,particella.getSuperficieUtilizzata());
				writer.writeEndElement(); 						//TAG_NAME_RIGA_LOC
			}
			writer.writeEndElement(); 							//TAG_NAME_TAB_LOC
		}
		else
		{
			writeTag(writer, TAG_NAME_VISIBILITY_LOC, "false");
			writer.writeStartElement(TAG_NAME_TAB_LOC);		//TAG_NAME_TAB_LOC
			writer.writeStartElement(TAG_NAME_RIGA_LOC);	//TAG_NAME_RIGA_LOC
			writeTag(writer,TAG_NAME_PROGRESSIVO,"");
			writeTag(writer,TAG_NAME_COMUNE,"");
			writeTag(writer,TAG_NAME_SEZIONE,"");
			writeTag(writer,TAG_NAME_FOGLIO,"");
			writeTag(writer,TAG_NAME_PARTICELLA,"");
			writeTag(writer,TAG_NAME_SUBALTERNO,"");
			writeTag(writer,TAG_NAME_SUPCATASTALE,"");
			writeTag(writer,TAG_NAME_OCCUPAZIONE,"");
			writeTag(writer,TAG_NAME_DESTINAZIONE,"");
			writeTag(writer,TAG_NAME_SUPUTILIZZATA,"");
			writer.writeEndElement(); 						//TAG_NAME_RIGA_LOC
			writer.writeEndElement(); 						// TAG_NAME_TAB_LOC 
		}
		writer.writeEndElement();							// TAG_NAME_PART_INTERVENTI 
		writer.writeEndElement(); 							// TAG_NAME_FRAGMENT_QUADRO_INTERVENTI
	}
	

}
