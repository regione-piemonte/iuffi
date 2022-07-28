package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.DocumentiRichiestiDaVisualizzareDTO;
import it.csi.iuffi.iuffiweb.dto.SezioneDocumentiRichiestiDTO;
import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroDocumentiRichiesti extends Fragment
{
	
	
	private static final String CONTENUTO_TITOLO_1 					= "Direzione Agricoltura - Settore A1711B - Attuazione programmi relativi alle strutture delle aziende agricole e alle avversità atmosferiche";
	private static final String CONTENUTO_TITOLO_2 					= "D.Lgs. 102/2004 e s.m.i. art.5 c.3";
	private static final String CONTENUTO_TITOLO_3 					= "ELENCO DOCUMENTI RIPRISTINO STRUTTURE";
	
	private static final String ID_SEZIONE_A						= "A";
	private static final String ID_SEZIONE_B						= "B";
	private static final String ID_SEZIONE_C						= "C";
	private static final String ID_SEZIONE_D						= "D";
	private static final String ID_SEZIONE_E						= "E";
	private static final String ID_SEZIONE_F						= "F";
	private static final String ID_SEZIONE_G						= "G";
	private static final String ID_SEZIONE_H						= "H";
	
	private static final String TRUE								= "true";
	private static final String FALSE								= "false";
	
	private static final String TAG_F_LETTERA_DOCUMENTI_RICHIESTI	= "FLetteraDocumentiRichiesti";
	
	private static final String TAG_NAME_HEADER_SEZIONI				= "HeaderSezioni";
		
	private static final String TAG_NAME_TITOLO_1 					= "Titolo1";
	private static final String TAG_NAME_TITOLO_2 					= "Titolo2";
	private static final String TAG_NAME_TITOLO_3 					= "Titolo3";
	
	private static final String TAG_NAME_SEZ_A 						= "SezA";
	private static final String TAG_NAME_VISIBILITY_A 				= "VisibilityA";
	private static final String TAG_NAME_HEADER_ROW_A 				= "HeaderRowA";
	private static final String TAG_NAME_CODICE_SEZ_A 				= "CodiceSezA";
	private static final String TAG_NAME_DESC_SEZ_A 				= "DescSezA";
	private static final String TAG_NAME_TAB_SEZ_A 					= "TabSezA";
	private static final String TAG_NAME_RIGA_SEZ_A 				= "RigaSezA";
	private static final String TAG_NAME_DOC_A 						= "DocA";
	
	private static final String TAG_NAME_SEZ_B 						= "SezB";
	private static final String TAG_NAME_VISIBILITY_B 				= "VisibilityB";
	private static final String TAG_NAME_HEADER_ROW_B 				= "HeaderRowB";
	private static final String TAG_NAME_CODICE_SEZ_B 				= "CodiceSezB";
	private static final String TAG_NAME_DESC_SEZ_B					= "DescSezB";
	private static final String TAG_NAME_TAB_SEZ_B 					= "TabSezB";
	private static final String TAG_NAME_RIGA_SEZ_B 				= "RigaSezB";
	private static final String TAG_NAME_DOC_B 						= "DocB";
	
	private static final String TAG_NAME_SEZ_C 						= "SezC";
	private static final String TAG_NAME_VISIBILITY_C 				= "VisibilityC";
	private static final String TAG_NAME_HEADER_ROW_C 				= "HeaderRowC";
	private static final String TAG_NAME_CODICE_SEZ_C 				= "CodiceSezC";
	private static final String TAG_NAME_DESC_SEZ_C 				= "DescSezC";
	private static final String TAG_NAME_TAB_SEZ_C 					= "TabSezC";
	private static final String TAG_NAME_RIGA_SEZ_C 				= "RigaSezC";
	private static final String TAG_NAME_DOC_C 						= "DocC";
	
	private static final String TAG_NAME_SEZ_D 						= "SezD";
	private static final String TAG_NAME_VISIBILITY_D 				= "VisibilityD";
	private static final String TAG_NAME_HEADER_ROW_D 				= "HeaderRowD";
	private static final String TAG_NAME_CODICE_SEZ_D 				= "CodiceSezD";
	private static final String TAG_NAME_DESC_SEZ_D 				= "DescSezD";
	private static final String TAG_NAME_TAB_SEZ_D 					= "TabSezD";
	private static final String TAG_NAME_RIGA_SEZ_D 				= "RigaSezD";
	private static final String TAG_NAME_DOC_D 						= "DocD";
	
	private static final String TAG_NAME_SEZ_E 						= "SezE";
	private static final String TAG_NAME_VISIBILITY_E 				= "VisibilityE";
	private static final String TAG_NAME_HEADER_ROW_E 				= "HeaderRowE";
	private static final String TAG_NAME_CODICE_SEZ_E 				= "CodiceSezE";
	private static final String TAG_NAME_DESC_SEZ_E 				= "DescSezE";
	private static final String TAG_NAME_TAB_SEZ_E 					= "TabSezE";
	private static final String TAG_NAME_RIGA_SEZ_E 				= "RigaSezE";
	private static final String TAG_NAME_DOC_E 						= "DocE";
	
	private static final String TAG_NAME_SEZ_F 						= "SezF";
	private static final String TAG_NAME_VISIBILITY_F 				= "VisibilityF";
	private static final String TAG_NAME_HEADER_ROW_F				= "HeaderRowF";
	private static final String TAG_NAME_CODICE_SEZ_F 				= "CodiceSezF";
	private static final String TAG_NAME_DESC_SEZ_F 				= "DescSezF";
	private static final String TAG_NAME_TAB_SEZ_F 					= "TabSezF";
	private static final String TAG_NAME_RIGA_SEZ_F 				= "RigaSezF";
	private static final String TAG_NAME_DOC_F 						= "DocF";
	
	private static final String TAG_NAME_SEZ_G 						= "SezG";
	private static final String TAG_NAME_VISIBILITY_G 				= "VisibilityG";
	private static final String TAG_NAME_HEADER_ROW_G 				= "HeaderRowG";
	private static final String TAG_NAME_CODICE_SEZ_G 				= "CodiceSezG";
	private static final String TAG_NAME_DESC_SEZ_G 				= "DescSezG";
	private static final String TAG_NAME_TAB_SEZ_G 					= "TabSezG";
	private static final String TAG_NAME_RIGA_SEZ_G 				= "RigaSezG";
	private static final String TAG_NAME_DOC_G 						= "DocG";
	
	private static final String TAG_NAME_SEZ_H 						= "SezH";
	private static final String TAG_NAME_VISIBILITY_H 				= "VisibilityH";
	private static final String TAG_NAME_HEADER_ROW_H 				= "HeaderRowH";
	private static final String TAG_NAME_CODICE_SEZ_H 				= "CodiceSezH";
	private static final String TAG_NAME_DESC_SEZ_H 				= "DescSezH";
	private static final String TAG_NAME_TAB_SEZ_H 					= "TabSezH";
	private static final String TAG_NAME_RIGA_SEZ_H 				= "RigaSezH";
	private static final String TAG_NAME_DOC_H 						= "DocH";
	
	private static final String VISIBILITY		 					= "Visibility";
	private static final String VISIBILITY_VALUE		 			= "true";
  
	@Override
	public void writeFragment(XMLStreamWriter writer,
			ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
			String cuName) throws Exception
	  	{
		    final IQuadroIuffiEJB quadroIuffiEJB = IuffiUtils.APPLICATION.getEjbQuadroIuffi();
		    final long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
			
		    List<SezioneDocumentiRichiestiDTO> sezioniDaVisualizzare = quadroIuffiEJB.getListDocumentiRichiestiDaVisualizzare(idProcedimentoOggetto, true);
 
		    writer.writeStartElement(TAG_F_LETTERA_DOCUMENTI_RICHIESTI);					//FLetteraDocumentiRichiesti
		    writer.writeStartElement(TAG_NAME_HEADER_SEZIONI);								//HeaderSezioni
		    
		    
		    TestataProcedimento testata = quadroEJB
		            .getTestataProcedimento(procedimentoOggetto.getIdProcedimento());
		    
		    writeTag(writer, TAG_NAME_TITOLO_1, "REGIONE PIEMONTE");
		    writeTag(writer, TAG_NAME_TITOLO_2, testata.getDescAmmCompetenza());
		    writeTag(writer, TAG_NAME_TITOLO_3, "");
    

		    // SEZIONE A
		    writer.writeStartElement(TAG_NAME_SEZ_A); 										//TAG SEZA
		    if(listSezioniContainsSezione(sezioniDaVisualizzare, ID_SEZIONE_A)){
		    	writeTag(writer,TAG_NAME_VISIBILITY_A, TRUE);								//TAG VISIBILITYA
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_A); 								//TAG TABSEZA
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_A);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_A, ID_SEZIONE_A);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_A, getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_A).getDescrizione());
			    	writer.writeEndElement();
			    	for(DocumentiRichiestiDaVisualizzareDTO doc : getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_A).getList()){
			    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_A); 							//TAG RIGASEZA
			    			writeTag(writer, TAG_NAME_DOC_A, doc.getDescrizione()); 				//TAG docA
			    		writer.writeEndElement();
			    	}		    	
		    	writer.writeEndElement(); 													//TAG SEZA
		    }else{
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_A); 								//TAG TABSEZA
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_A);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_A,ID_SEZIONE_A);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_A,"");
			    	writer.writeEndElement();
			    	writer.writeStartElement(TAG_NAME_RIGA_SEZ_A); 								//TAG RIGASEZA
		    			writeTag(writer, TAG_NAME_DOC_A, ""); 										//TAG docA	
		    		writer.writeEndElement();
		    	writer.writeEndElement(); 													//TAG SEZA
		    }
		    writer.writeEndElement();	
		    // FINE SEZIONE A
		    	
		    // SEZIONE B
		    writer.writeStartElement(TAG_NAME_SEZ_B); 										//TAG SEZB
		    if(listSezioniContainsSezione(sezioniDaVisualizzare, ID_SEZIONE_B)){
		    	writeTag(writer,TAG_NAME_VISIBILITY_B, TRUE);								//TAG VISIBILITYB
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_B); 								//TAG TABSEZB		    	
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_B);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_B,ID_SEZIONE_B);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_B,getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_B).getDescrizione());
			    	writer.writeEndElement();
			    	for(DocumentiRichiestiDaVisualizzareDTO doc : getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_B).getList()){
			    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_B); 							//TAG RIGASEZB
			    			writeTag(writer, TAG_NAME_DOC_B, doc.getDescrizione()); 				//TAG docB
			    		writer.writeEndElement();
			    	}		    	
		    	writer.writeEndElement(); 													//TAG SEZB
		    }else{
		    	 writeTag(writer,TAG_NAME_VISIBILITY_B, FALSE);								//TAG VISIBILITYB
		    	 writer.writeStartElement(TAG_NAME_TAB_SEZ_B); 								//TAG TABSEZB		    	
			    	 writer.writeStartElement(TAG_NAME_HEADER_ROW_B);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_B,ID_SEZIONE_B);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_B,"");
			    	 writer.writeEndElement();
			    	 writer.writeStartElement(TAG_NAME_RIGA_SEZ_B); 							//TAG RIGASEZB
			    	 	writeTag(writer, TAG_NAME_DOC_B, ""); 				//TAG docB
			         writer.writeEndElement();		    			    	
		    	writer.writeEndElement(); 	
		    }
		    writer.writeEndElement();	
		    // FINE SEZIONE B
		    

		    // SEZIONE C
		    writer.writeStartElement(TAG_NAME_SEZ_C); 										//TAG SEZC
		    if(listSezioniContainsSezione(sezioniDaVisualizzare, ID_SEZIONE_C)){
		    	writeTag(writer,TAG_NAME_VISIBILITY_C, TRUE);								//TAG VISIBILITYC
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_C); 								//TAG TABSEZC		  
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_C);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_C,ID_SEZIONE_C);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_C,getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_C).getDescrizione());
		    		writer.writeEndElement();
			    	for(DocumentiRichiestiDaVisualizzareDTO doc : getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_C).getList()){
			    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_C); 							//TAG RIGASEZC
			    			writeTag(writer, TAG_NAME_DOC_C, doc.getDescrizione()); 				//TAG docC
			    		writer.writeEndElement();
			    	}		    	
		    	writer.writeEndElement(); 													//TAG SEZC
		    }else{
		    	writeTag(writer,TAG_NAME_VISIBILITY_C, FALSE);								//TAG VISIBILITYC
		    	 writer.writeStartElement(TAG_NAME_TAB_SEZ_C); 								//TAG TABSEZC		    	
			    	 writer.writeStartElement(TAG_NAME_HEADER_ROW_C);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_C,ID_SEZIONE_C);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_C, "");
			    	 writer.writeEndElement();
			    	 writer.writeStartElement(TAG_NAME_RIGA_SEZ_C); 							//TAG RIGASEZC
			    	 	writeTag(writer, TAG_NAME_DOC_C, ""); 				//TAG docC
			         writer.writeEndElement();		    			    	
		    	writer.writeEndElement(); 									//TAG DOCC
		    }
		    writer.writeEndElement();	
		    // FINE SEZIONE C
		    
		    // SEZIONE D
		    writer.writeStartElement(TAG_NAME_SEZ_D); 										//TAG SEZD
		    if(listSezioniContainsSezione(sezioniDaVisualizzare, ID_SEZIONE_D)){
		    	writeTag(writer,TAG_NAME_VISIBILITY_D, TRUE);								//TAG VISIBILITYD
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_D); 								//TAG TABSEZD		  
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_D);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_D,ID_SEZIONE_D);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_D,getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_D).getDescrizione());
		    		writer.writeEndElement();
			    	for(DocumentiRichiestiDaVisualizzareDTO doc : getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_D).getList()){
			    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_D); 							//TAG RIGASEZD
			    			writeTag(writer, TAG_NAME_DOC_D, doc.getDescrizione()); 				//TAG docD
			    		writer.writeEndElement();
			    	}		    	
		    	writer.writeEndElement(); 													//TAG SEZD
		    }else{
		    	writeTag(writer,TAG_NAME_VISIBILITY_D, FALSE);								//TAG VISIBILITYD
		    	 writer.writeStartElement(TAG_NAME_TAB_SEZ_D); 								//TAG TABSEZD		    	
			    	 writer.writeStartElement(TAG_NAME_HEADER_ROW_D);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_D,ID_SEZIONE_D);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_D, "" );
			    	 writer.writeEndElement();
			    	 writer.writeStartElement(TAG_NAME_RIGA_SEZ_D); 							//TAG RIGASEZD
			    	 	writeTag(writer, TAG_NAME_DOC_D, ""); 				//TAG docD
			         writer.writeEndElement();		    			    	
		    	writer.writeEndElement(); 									//TAG DOCD
		    }
		    writer.writeEndElement();	
		    // FINE SEZIONE D

		    // SEZIONE E
		    writer.writeStartElement(TAG_NAME_SEZ_E); 										//TAG SEZE
		    if(listSezioniContainsSezione(sezioniDaVisualizzare, ID_SEZIONE_E)){
		    	writeTag(writer,TAG_NAME_VISIBILITY_E, TRUE);								//TAG VISIBILITYE
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_E); 								//TAG TABSEZE		  
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_E);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_E,ID_SEZIONE_E);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_E, getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_E).getDescrizione());
		    		writer.writeEndElement();
			    	for(DocumentiRichiestiDaVisualizzareDTO doc : getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_E).getList()){
			    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_E); 							//TAG RIGASEZE
			    			writeTag(writer, TAG_NAME_DOC_E, doc.getDescrizione()); 				//TAG docE
			    		writer.writeEndElement();
			    	}		    	
		    	writer.writeEndElement(); 													//TAG SEZE
		    }else{
		    	writeTag(writer,TAG_NAME_VISIBILITY_E, FALSE);								//TAG VISIBILITYE
		    	 writer.writeStartElement(TAG_NAME_TAB_SEZ_E); 								//TAG TABSEZE		    	
			    	 writer.writeStartElement(TAG_NAME_HEADER_ROW_E);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_E,ID_SEZIONE_E);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_E,"");
			    	 writer.writeEndElement();
			    	 writer.writeStartElement(TAG_NAME_RIGA_SEZ_E); 							//TAG RIGASEZE
			    	 	writeTag(writer, TAG_NAME_DOC_E, ""); 				//TAG docE
			         writer.writeEndElement();		    			    	
		    	writer.writeEndElement(); 									//TAG DOCE
		    }
		    writer.writeEndElement();	
		    // FINE SEZIONE E
		    
		    // SEZIONE F
		    writer.writeStartElement(TAG_NAME_SEZ_F); 										//TAG SEZF
		    if(listSezioniContainsSezione(sezioniDaVisualizzare, ID_SEZIONE_F)){
		    	writeTag(writer,TAG_NAME_VISIBILITY_F, TRUE);								//TAG VISIBILITYF
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_F); 								//TAG TABSEZF		  
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_F);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_F,ID_SEZIONE_F);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_F,getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_F).getDescrizione());
		    		writer.writeEndElement();
			    	for(DocumentiRichiestiDaVisualizzareDTO doc : getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_F).getList()){
			    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_F); 							//TAG RIGASEZF
			    			writeTag(writer, TAG_NAME_DOC_F, doc.getDescrizione()); 				//TAG docF
			    		writer.writeEndElement();
			    	}		    	
		    	writer.writeEndElement(); 													//TAG SEZF
		    }else{
		    	writeTag(writer,TAG_NAME_VISIBILITY_F, FALSE);								//TAG VISIBILITYF
		    	 writer.writeStartElement(TAG_NAME_TAB_SEZ_F); 								//TAG TABSEZF		    	
			    	 writer.writeStartElement(TAG_NAME_HEADER_ROW_F);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_F,ID_SEZIONE_F);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_F, "");
			    	 writer.writeEndElement();
			    	 writer.writeStartElement(TAG_NAME_RIGA_SEZ_F); 							//TAG RIGASEZF
			    	 	writeTag(writer, TAG_NAME_DOC_F, ""); 				//TAG docF
			         writer.writeEndElement();		    			    	
		    	writer.writeEndElement(); 									//TAG DOCF
		    }
		    writer.writeEndElement();	
		    // FINE SEZIONE F

		    // SEZIONE G
		    writer.writeStartElement(TAG_NAME_SEZ_G); 										//TAG SEZG
		    if(listSezioniContainsSezione(sezioniDaVisualizzare, ID_SEZIONE_G)){
		    	writeTag(writer,TAG_NAME_VISIBILITY_G, TRUE);								//TAG VISIBILITYG
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_G); 								//TAG TABSEG		  
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_G);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_G,ID_SEZIONE_G);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_G,getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_G).getDescrizione());
		    		writer.writeEndElement();
			    	for(DocumentiRichiestiDaVisualizzareDTO doc : getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_G).getList()){
			    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_G); 							//TAG RIGASEZG
			    			writeTag(writer, TAG_NAME_DOC_G, doc.getDescrizione()); 				//TAG docG
			    		writer.writeEndElement();
			    	}		    	
		    	writer.writeEndElement(); 													//TAG SEZG
		    }else{
		    	writeTag(writer,TAG_NAME_VISIBILITY_G, FALSE);								//TAG VISIBILITYG
		    	 writer.writeStartElement(TAG_NAME_TAB_SEZ_G); 								//TAG TABSEZG		    	
			    	 writer.writeStartElement(TAG_NAME_HEADER_ROW_G);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_G,ID_SEZIONE_G);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_G, "");
			    	 writer.writeEndElement();
			    	 writer.writeStartElement(TAG_NAME_RIGA_SEZ_G); 							//TAG RIGASEZG
			    	 	writeTag(writer, TAG_NAME_DOC_G, ""); 				//TAG docG
			         writer.writeEndElement();		    			    	
		    	writer.writeEndElement(); 									//TAG DOCG
		    }
		    writer.writeEndElement();	
		    // FINE SEZIONE G
		    
		    // SEZIONE H
		    writer.writeStartElement(TAG_NAME_SEZ_H); 										//TAG SEZH
		    String altroDoc = getAltroDoc(sezioniDaVisualizzare);
		    if(altroDoc!=null){
		    	writeTag(writer,TAG_NAME_VISIBILITY_H, TRUE);								//TAG VISIBILITYH
		    	writer.writeStartElement(TAG_NAME_TAB_SEZ_H); 								//TAG TABSEZH		  
			    	writer.writeStartElement(TAG_NAME_HEADER_ROW_H);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_H,ID_SEZIONE_H);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_H,getSezioneByIdSezione(sezioniDaVisualizzare, ID_SEZIONE_H).getDescrizione());
		    		writer.writeEndElement();
		    		writer.writeStartElement(TAG_NAME_RIGA_SEZ_H); 							//TAG RIGASEZH
		    			writeTag(writer, TAG_NAME_DOC_H, altroDoc); 				//TAG docH
		    		writer.writeEndElement();
			    			    	
		    	writer.writeEndElement(); 													//TAG SEZH
		    }else{
		    	writeTag(writer,TAG_NAME_VISIBILITY_H, FALSE);								//TAG VISIBILITYH
		    	 writer.writeStartElement(TAG_NAME_TAB_SEZ_H); 								//TAG TABSEZH		    	
			    	 writer.writeStartElement(TAG_NAME_HEADER_ROW_H);
			    		writeTag(writer,TAG_NAME_CODICE_SEZ_H,ID_SEZIONE_H);
			    		writeTag(writer,TAG_NAME_DESC_SEZ_H,"");
			    	 writer.writeEndElement();
			    	 writer.writeStartElement(TAG_NAME_RIGA_SEZ_H); 							//TAG RIGASEZH
			    	 	writeTag(writer, TAG_NAME_DOC_H, ""); 				//TAG docH
			         writer.writeEndElement();		    			    	
		    	writer.writeEndElement(); 									//TAG DOCH
		    }
		    writer.writeEndElement();	
		    // FINE SEZIONE H
		    
		    writeTag(writer,VISIBILITY,VISIBILITY_VALUE);							//Visibility
		    
		    writer.writeEndElement();	//header sezioni
		    writer.writeEndElement();	//f_lettera

	  }
	
	public String getAltroDoc(List<SezioneDocumentiRichiestiDTO> sezioniDaVisualizzare){
		if(sezioniDaVisualizzare!=null){
			for(SezioneDocumentiRichiestiDTO s : sezioniDaVisualizzare){
		
				if(s.getIdSezione().equals(ID_SEZIONE_H))
					return s.getList().get(0).getDescrizione();
			}
		}
		return null;
	}


	public boolean listSezioniContainsSezione(List<SezioneDocumentiRichiestiDTO> list, String idSezione){
		if(list==null){
			return false;
		}else{
			for(SezioneDocumentiRichiestiDTO sez:list){
				if(sez.getIdSezione().equals(idSezione)){
					return true;
				}
			}
		}
	    return false;
	}
	
	public SezioneDocumentiRichiestiDTO getSezioneByIdSezione(List<SezioneDocumentiRichiestiDTO> list, String idSezione){
		if(list==null){
	    	return null;
	    }else{
			for(SezioneDocumentiRichiestiDTO sez:list){
				if(sez.getIdSezione().equals(idSezione)){
					return sez;
				}
			}
		}
	    return null;
	}
	
	
}
