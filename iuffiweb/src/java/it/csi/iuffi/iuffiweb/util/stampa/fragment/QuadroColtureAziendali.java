package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.coltureaziendali.ColtureAziendaliDettaglioDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroColtureAziendali extends Fragment {
    private static final String TAG_NAME_SEZIONE_VUOTA = "SezioneVuota";
    public static final String TAG_NAME_DOMANDA = "Domanda";
    public static final String TAG_NAME_FRAGMENT_QUADRO_COLTURE_AZIENDALI = "QuadroColtureAziendali";
    public static final String TAG_NAME_TITOLO_SEZIONE_COLTURE_AZIENDALI = "TitoloSezioneColtureAziendali";
    public static final String TAG_NAME_SEZ_DATI_COLTURE = "SezDatiColture";
    public static final String TAG_NAME_VISIBILITY_DATI = "VisibilityDati";

    public static final String TAG_NAME_SEZ_RIEPILOGO = "SezRiepilogo";
    public static final String TAG_NAME_TOT_SUP = "TotSup";
    public static final String TAG_NAME_PLV_ORDINARIA = "PLVOrdinaria";
    public static final String TAG_NAME_PLV_EFFETTIVA = "PLVEffettiva";
    public static final String TAG_NAME_PERC_DANNO = "PercDanno";
    public static final String TAG_NAME_INDRICH = "IndRich";

    public static final String TAG_NAME_SEZ_ELENCO_COLTURE = "SezElencoColture";
    public static final String TAG_NAME_TAB_COLTURE_AZIENDALI = "TabColtureAziendali";

    public static final String TAG_NAME_RIGA_COLTURE_AZIENDALI = "RigaColtureAziendali";
    public static final String TAG_NAME_UBICAZIONETERRENO = "UbicazioneTerreno";
    public static final String TAG_NAME_UTILIZZO = "Utilizzo";
    public static final String TAG_NAME_DESTINAZIONE = "Destinazione";
    public static final String TAG_NAME_DETTAGLIOUSO = "DettaglioUso";
    public static final String TAG_NAME_QUALITAUSO = "QualitaUso";
    public static final String TAG_NAME_VARIETA = "Varieta";
    public static final String TAG_NAME_SUPERFICIE = "Superficie";
    public static final String TAG_NAME_DANNEGGIATO = "Danneggiato";
    public static final String TAG_NAME_QUINTALIHA = "QuintaliHa";
    public static final String TAG_NAME_TOTQLIORD = "TotQliOrd";
    public static final String TAG_NAME_PREZZOQLEORD = "PrezzoQleOrd";
    public static final String TAG_NAME_TOTEUROORD = "TotEuroOrd";
    public static final String TAG_NAME_GIORLAVHA = "GiornateLavorateHa";
    public static final String TAG_NAME_GIORLAV = "GiornateLavorate";
    public static final String TAG_NAME_NOTE = "Note";
    public static final String TAG_NAME_QUINTALIHAEFF = "QuintaliHaEff";
    public static final String TAG_NAME_TOTQLIEFF = "TotQliEff";
    public static final String TAG_NAME_PREZZOQLEEFF = "PrezzoQleEff";
    public static final String TAG_NAME_TOTEUROEFF = "TotEuroEff";
    public static final String TAG_NAME_RIMBASSIC = "RimbAssic";
    public static final String TAG_NAME_TOTCONNRIMB = "TotConRimb";
    public static final String TAG_NAME_EURODANNO = "EuroDanno";
    public static final String TAG_NAME_PERCENTUALEDANNO = "PercDanno";

    public static final String TAG_NAME_SEZ_NULLA = "SezNulla";
    public static final String TAG_NAME_VISIBILITY_SEZ_NULLA = "VisibilitySezNulla";

    
    @Override
    public void writeFragment(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB, String cuName) throws Exception {
	final IQuadroIuffiEJB ejbQuadroIuffi = IuffiUtils.APPLICATION.getEjbQuadroIuffi();
	final long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();

	List<ColtureAziendaliDettaglioDTO> listColtureAziendaliDettaglio = ejbQuadroIuffi.getListColtureAziendali(idProcedimentoOggetto);

	writer.writeStartElement(TAG_NAME_FRAGMENT_QUADRO_COLTURE_AZIENDALI); // TAG_NAME_FRAGMENT_QUADRO_COLTURE_AZIENDALI
	writeTag(writer, TAG_NAME_SEZIONE_VUOTA, "false");
	writeVisibility(writer, true);
	writeTag(writer, TAG_NAME_TITOLO_SEZIONE_COLTURE_AZIENDALI, "Quadro - Colture Aziendali");

	writer.writeStartElement(TAG_NAME_SEZ_DATI_COLTURE); // TAG_NAME_SEZ_DATI_COLTURE

	if (listColtureAziendaliDettaglio != null && listColtureAziendaliDettaglio.size() > 0) {
	    writeTag(writer, TAG_NAME_VISIBILITY_DATI, "true");
	    
	    writer.writeStartElement(TAG_NAME_SEZ_ELENCO_COLTURE); // TAG_NAME_SEZ_ELENCO_COLTURE
	    writer.writeStartElement(TAG_NAME_TAB_COLTURE_AZIENDALI); // TAG_NAME_SEZ_ELENCO_COLTURE
	    
	    BigDecimal totSuperficie = BigDecimal.ZERO;
      BigDecimal totTotEuroOrd = BigDecimal.ZERO;
      BigDecimal totTotEuroEff = BigDecimal.ZERO;
      BigDecimal totRimborsoEff = BigDecimal.ZERO;
      BigDecimal totTotConRimbEff = null;
      BigDecimal totEuroDanno = BigDecimal.ZERO;
      
	    for (ColtureAziendaliDettaglioDTO item : listColtureAziendaliDettaglio) {
	        totSuperficie = totSuperficie.add(IuffiUtils.NUMBERS.nvl(item.getSuperficieUtilizzata()));
	        totTotEuroOrd = totTotEuroOrd.add(IuffiUtils.NUMBERS.nvl(item.getTotaleEuroPlvOrd()));
	        totTotEuroEff = totTotEuroEff.add(IuffiUtils.NUMBERS.nvl(item.getTotaleEuroPlvEff()));
	        totRimborsoEff = totRimborsoEff.add(IuffiUtils.NUMBERS.nvl(item.getImportoRimborso()));
	        totEuroDanno = totEuroDanno.add(IuffiUtils.NUMBERS.nvl(item.getEuroDanno()));
	       
      		writer.writeStartElement(TAG_NAME_RIGA_COLTURE_AZIENDALI); // TAG_NAME_RIGA_COLTURE_AZIENDALI
      		writeTag(writer, TAG_NAME_UBICAZIONETERRENO, item.getUbicazioneTerreno());
      		writeTag(writer, TAG_NAME_UTILIZZO, item.getTipoUtilizzoDescrizione());
      		writeTag(writer, TAG_NAME_SUPERFICIE, item.getSuperficieUtilizzataFormatted());
      		writeTag(writer, TAG_NAME_DANNEGGIATO, item.getFlagDanneggiato());
          writeTag(writer, TAG_NAME_TOTEUROORD, item.getTotaleEuroPlvOrdFormatted());
          writeTag(writer, TAG_NAME_TOTEUROEFF, item.getTotaleEuroPlvEffFormatted());
          writeTag(writer, "RimborsoEff", item.getImportoRimborsoFormatted());
          
          BigDecimal totConRimbEff = IuffiUtils.NUMBERS.nvl(item.getImportoRimborso()).add(IuffiUtils.NUMBERS.nvl(item.getTotaleEuroPlvEff()));
          totTotConRimbEff = IuffiUtils.NUMBERS.nvl(totTotConRimbEff).add(IuffiUtils.NUMBERS.nvl(totConRimbEff));
          writeTag(writer, "TotConRimbEff", (totConRimbEff.compareTo(BigDecimal.ZERO) == 0) ? "" :IuffiUtils.FORMAT.formatDecimal2(totConRimbEff));
 
          writeTag(writer, TAG_NAME_EURODANNO, item.getEuroDannoFormatted());
          writeTag(writer, "Danno", item.getPercentualeDannoFormatted());
      		writer.writeEndElement(); // TAG_NAME_RIGA_COLTURE_AZIENDALI
	    }
	    
	    
	    writer.writeStartElement("TotaliColtureAziendali"); 
	    writeTag(writer, "TotSuperficie",  IuffiUtils.FORMAT.formatDecimal4(totSuperficie) );
	    writeTag(writer, "TotTotEuroOrd",  IuffiUtils.FORMAT.formatDecimal2(totTotEuroOrd) );
	    writeTag(writer, "TotTotEuroEff",  IuffiUtils.FORMAT.formatDecimal2(totTotEuroEff) );
	    writeTag(writer, "TotRimborsoEff",  IuffiUtils.FORMAT.formatDecimal2(totRimborsoEff) );
	    writeTag(writer, "TotTotConRimbEff",  IuffiUtils.FORMAT.formatDecimal2(totTotConRimbEff) );
	    writeTag(writer, "TotEuroDanno",  IuffiUtils.FORMAT.formatDecimal2(totEuroDanno) );
	    //(1 - ("PLV effettiva - Tot con rimborsi"/ "PLV ordinaria - Tot"))*100
	    BigDecimal totDannotmp = new BigDecimal("1").subtract( totTotConRimbEff.divide(totTotEuroOrd, MathContext.DECIMAL128) );
	    BigDecimal totDanno = totDannotmp.multiply(new BigDecimal("100"));
	    writeTag(writer, "TotDanno",  IuffiUtils.FORMAT.formatDecimal2(totDanno) );
	    writer.writeEndElement(); 
	      
	    writer.writeEndElement(); // TAG_NAME_TAB_COLTURE_AZIENDALI
	    writer.writeEndElement(); // TAG_NAME_SEZ_ELENCO_COLTURE

	    writer.writeStartElement(TAG_NAME_SEZ_NULLA); // TAG_NAME_SEZ_NULLA
	    writeTag(writer, TAG_NAME_VISIBILITY_SEZ_NULLA, "false");
	    writer.writeEndElement(); // TAG_NAME_SEZ_NULLA

	} else {
	    writeTag(writer, TAG_NAME_VISIBILITY_DATI, "true");


	    writer.writeStartElement(TAG_NAME_SEZ_ELENCO_COLTURE); // TAG_NAME_SEZ_ELENCO_COLTURE
	    writer.writeStartElement(TAG_NAME_TAB_COLTURE_AZIENDALI); // TAG_NAME_SEZ_ELENCO_COLTURE
	    writer.writeStartElement(TAG_NAME_RIGA_COLTURE_AZIENDALI); // TAG_NAME_RIGA_COLTURE_AZIENDALI
	   
	    writeTag(writer, TAG_NAME_UBICAZIONETERRENO, "");
      writeTag(writer, TAG_NAME_UTILIZZO, "");
      writeTag(writer, TAG_NAME_SUPERFICIE, "");
      writeTag(writer, TAG_NAME_DANNEGGIATO, "");
      writeTag(writer, TAG_NAME_TOTEUROORD, "");
      writeTag(writer, TAG_NAME_TOTEUROEFF, "");
      writeTag(writer, "RimborsoEff", "");
      writeTag(writer, "TotConRimbEff", "");
      writeTag(writer, TAG_NAME_EURODANNO, "");
      writeTag(writer, "Danno", "");
	    
      writer.writeStartElement("TotaliColtureAziendali"); 
      writeTag(writer, "TotSuperficie",  "");
      writeTag(writer, "TotTotEuroOrd", "");
      writeTag(writer, "TotTotEuroEff",  "" );
      writeTag(writer, "TotRimborsoEff",  "" );
      writeTag(writer, "TotTotConRimbEff",  "" );
      writeTag(writer, "TotEuroDanno",  "");
      writeTag(writer, "TotDanno",  "" );
      writer.writeEndElement(); 
      
	    
	    writer.writeEndElement(); // TAG_NAME_RIGA_COLTURE_AZIENDALI
	    writer.writeEndElement(); // TAG_NAME_TAB_COLTURE_AZIENDALI
	    writer.writeEndElement(); // TAG_NAME_SEZ_ELENCO_COLTURE

	    writer.writeStartElement(TAG_NAME_SEZ_NULLA); // TAG_NAME_SEZ_NULLA
	    writeTag(writer, TAG_NAME_VISIBILITY_SEZ_NULLA, "false");
	    writer.writeEndElement(); // TAG_NAME_SEZ_NULLA
	}

	  writer.writeEndElement(); // TAG_NAME_SEZ_DATI_COLTURE
	  writer.writeEndElement(); // TAG_NAME_FRAGMENT_QUADRO_COLTURE_AZIENDALI
    }
    
}
