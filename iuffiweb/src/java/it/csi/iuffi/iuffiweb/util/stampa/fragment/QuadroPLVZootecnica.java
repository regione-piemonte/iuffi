package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.allevamenti.AllevamentiDettaglioPlvDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroPLVZootecnica extends Fragment
{
  private static final String TAG_NAME_FRAGMENT_PLV_ZOOTECNICA 	= "QuadroPLVZootecnica";
  private static final String SEZIONE_VUOTA 					= "SezioneVuota";
  private static final String TAG_NAME_TITOLO_SEZIONE_PLV_ZOOTECNICA 			= "TitoloSezionePLVZootecnica";
  private static final String TAG_NAME_PLV_ZOOTECNICA 			= "PLVZootecnica";
  private static final String TAG_NAME_TAB_PLV_ZOOTECNICA 		= "TabPLVZootecnica";
  private static final String TAG_NAME_RIGA_PLV_ZOOTECNICA		= "RigaPLVZootecnica";
  private static final String TAG_NAME_VISIBILITY_PLV_ZOOTECNICA		= "VisibilityPLVZootenica";
  private static final String TAG_NAME_SEZIONE_NULLA			= "SezioneNulla";
  private static final String TAG_NAME_VISIBILITY_TESTO_NULLO	= "VisibilityTestoNullo";
  

	@Override
	public void writeFragment(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
			String cuName) throws Exception
	{
		final IQuadroIuffiEJB ejbQuadroIuffi = IuffiUtils.APPLICATION.getEjbQuadroIuffi();
		final long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
		String visibilityTestoNullo;
		List<AllevamentiDettaglioPlvDTO> listPlvZootecnica = ejbQuadroIuffi.getListPlvZootecnicaDettaglioAllevamenti(idProcedimentoOggetto);

		writer.writeStartElement(TAG_NAME_FRAGMENT_PLV_ZOOTECNICA);
		writeTag(writer, SEZIONE_VUOTA, "false");
		writeVisibility(writer, true);
		writeTag(writer, TAG_NAME_TITOLO_SEZIONE_PLV_ZOOTECNICA, "Quadro - P.L.V. Zootecnica");
		writer.writeStartElement(TAG_NAME_PLV_ZOOTECNICA);
		if (listPlvZootecnica != null && listPlvZootecnica.size() > 0)
		{
			writeTag(writer,TAG_NAME_VISIBILITY_PLV_ZOOTECNICA,"true");
			writer.writeStartElement(TAG_NAME_TAB_PLV_ZOOTECNICA);
			for (AllevamentiDettaglioPlvDTO item : listPlvZootecnica)
			{
				writer.writeStartElement(TAG_NAME_RIGA_PLV_ZOOTECNICA);
				writeTag(writer, "CodAzZootecnica", item.getCodiceAziendaZootecnica());
				writeTag(writer, "Specie", item.getDescrizioneSpecieAnimale());
				writeTag(writer, "Categoria", item.getDescrizioneCategoriaAnimale());
				writeTag(writer, "TipoProd", item.getDescProduzione());
				writeTag(writer, "NCapi", item.getNumeroCapiFormattedPerStampa());
				writeTag(writer, "QProdAnnua", item.getQuantitaProdottaFormatted());
				writeTag(writer, "ProdLorda", item.getProdLordaFormatted());
				writeTag(writer, "Reimpieghi", item.getQuantitaReimpiegataFormatted());
				writeTag(writer, "ProdNetta", item.getProdNettaFormatted());
				writeTag(writer, "UDM", item.getDescUnitaMisura());
				writeTag(writer, "PrezzoUnitario", item.getPrezzoFormattedPerStampe());
				writeTag(writer, "PLVZoo", item.getPlvFormatted());
				writer.writeEndElement(); // TAG_NAME_RIGA_PLV
			}
			writer.writeEndElement(); 							// TAG_NAME_PLV_ZOOTECNICA
			visibilityTestoNullo = "false";
		} else
		{
			writeTag(writer,TAG_NAME_VISIBILITY_PLV_ZOOTECNICA,"false");
			writer.writeStartElement(TAG_NAME_TAB_PLV_ZOOTECNICA);
			writer.writeStartElement(TAG_NAME_RIGA_PLV_ZOOTECNICA);
			writeTag(writer, "CodAzZootecnica", "");
			writeTag(writer, "Specie", "");
			writeTag(writer, "Categoria", "");
			writeTag(writer, "TipoProd", "");
			writeTag(writer, "NCapi", "");
			writeTag(writer, "QProdAnnua", "");
			writeTag(writer, "ProdLorda", "");
			writeTag(writer, "Reimpieghi", "");
			writeTag(writer, "ProdNetta", "");
			writeTag(writer, "UDM", "");
			writeTag(writer, "PrezzoUnitario", "");
			writeTag(writer, "PLVZoo", "");
			writer.writeEndElement(); // TAG_NAME_RIGA_PLV
			writer.writeEndElement(); 							// TAG_NAME_PLV_ZOOTECNICA
			visibilityTestoNullo = "true";
		}
		writer.writeEndElement(); 							// TAG_NAME_TAB_PLV
		writer.writeStartElement(TAG_NAME_SEZIONE_NULLA);	//TAG_NAME_VISIBILITY_TESTO_NULLO
		writeTag(writer,TAG_NAME_VISIBILITY_TESTO_NULLO,visibilityTestoNullo);
		writer.writeEndElement(); 							// TAG_NAME_VISIBILITY_TESTO_NULLO
		writer.writeEndElement(); 							// TAG_NAME_FRAGMENT_PLV_ZOOTECNICA
	}
}
