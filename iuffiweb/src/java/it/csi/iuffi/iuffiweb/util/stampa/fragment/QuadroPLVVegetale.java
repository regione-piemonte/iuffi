package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.superficicolture.SuperficiColturePlvVegetaleDTO;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroPLVVegetale extends Fragment
{
  private static final String TAG_VISIBILITY_PLV_VEGETALE = "VisibilityPLVVegetale";
  private static final String TAG_VISIBILITY_TESTO_NULLO = "VisibilityTestoNullo";
  private static final String TAG_SEZIONE_NULLA = "SezioneNulla";
  public static final String TAG_NAME_DOMANDA = "Domanda";
  public static final String TAG_NAME_FRAGMENT_PLV_VEGETALE = "QuadroPLVVegetale";
  public static final String TAG_NAME_PLV_VEGETALE = "PLVVegetale";
  public static final String TAG_NAME_TAB_PLV             = "TabPLV";
  public static final String TAG_NAME_RIGA_PLV            = "RigaPLV";

	@Override
	public void writeFragment(XMLStreamWriter writer, ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
			String cuName) throws Exception
	{
		final IQuadroIuffiEJB ejbQuadroIuffi = IuffiUtils.APPLICATION.getEjbQuadroIuffi();
		final long idProcedimentoOggetto = procedimentoOggetto.getIdProcedimentoOggetto();
		List<SuperficiColturePlvVegetaleDTO> listSuperficiColturePlv = ejbQuadroIuffi
				.getListSuperficiColturePlvVegetale(idProcedimentoOggetto);

		writer.writeStartElement(TAG_NAME_FRAGMENT_PLV_VEGETALE);
		//writeTag(writer, "SezioneVuota", "false");
		writeVisibility(writer, true);
		writeTag(writer, "TitoloSezionePLVVegetale", "Quadro - P.L.V. Vegetale");

		if (listSuperficiColturePlv != null && listSuperficiColturePlv.size() > 0)
		{
			writer.writeStartElement(TAG_NAME_PLV_VEGETALE);
			writeTag(writer, TAG_VISIBILITY_PLV_VEGETALE, "true");
			writer.writeStartElement(TAG_NAME_TAB_PLV);
			for (SuperficiColturePlvVegetaleDTO item : listSuperficiColturePlv)
			{
				writer.writeStartElement(TAG_NAME_RIGA_PLV);
					writeTag(writer, "Utilizzo", item.getTipoUtilizzoDescrizione());
					writeTag(writer, "Superficie", item.getSuperficieUtilizzataFormatted());
					writeTag(writer, "Produzione", item.getProduzioneQFormatted());
					writeTag(writer, "GGLavorative", item.getGiornateLavPerSupUtilFormatted());
					writeTag(writer, "UF", item.getUfFormatted());
					writeTag(writer, "ReimpieghiQ", item.getReimpieghiQFormatted());
					writeTag(writer, "ReimpieghiUF", item.getReimpieghiUfFormatted());
					writeTag(writer, "PLV", item.getPlvFormatted());
				writer.writeEndElement(); // TAG_NAME_RIGA_PLV
			}
			writer.writeEndElement(); // TAG_NAME_TAB_PLV
			writer.writeEndElement(); // TAG_NAME_PLV_VEGETALE

			writer.writeStartElement(TAG_SEZIONE_NULLA);
				writeTag(writer, TAG_VISIBILITY_TESTO_NULLO, "false");
			writer.writeEndElement();

		} else
		{
			writer.writeStartElement(TAG_NAME_PLV_VEGETALE);
			writeTag(writer, TAG_VISIBILITY_PLV_VEGETALE, "false");
				writer.writeStartElement(TAG_NAME_TAB_PLV);
					writer.writeStartElement(TAG_NAME_RIGA_PLV);
					
						writeTag(writer, "Utilizzo", "");
						writeTag(writer, "Superficie", "");
						writeTag(writer, "Produzione", "");
						writeTag(writer, "GGLavorative", "");
						writeTag(writer, "UF", "");
						writeTag(writer, "ReimpieghiQ", "");
						writeTag(writer, "ReimpieghiUF", "");
						writeTag(writer, "PLV", "");
					writer.writeEndElement(); // TAG_NAME_RIGA_PLV
				writer.writeEndElement(); // TAG_NAME_TAB_PLV
			writer.writeEndElement(); // TAG_NAME_FRAGMENT_PLV_VEGETALE

			writer.writeStartElement(TAG_SEZIONE_NULLA);
				writeTag(writer, TAG_VISIBILITY_TESTO_NULLO, "true");
			writer.writeEndElement();
		}
		writer.writeEndElement(); //TAG_NAME_FRAGMENT_PLV_VEGETALE
	}
}
