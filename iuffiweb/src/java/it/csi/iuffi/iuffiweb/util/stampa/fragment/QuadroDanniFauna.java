package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroIuffiEJB;
import it.csi.iuffi.iuffiweb.dto.danniFauna.DannoFaunaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class QuadroDanniFauna extends Fragment
{
  private static final String TAG_NAME_FRAGMENT_QUADRO_DANNI  = "QuadroDanniFauna";
  private static final String TAG_NAME_SEZIONE_TITOLO         = "TitoloQuandroDanniFauna";
  private static final String TAG_NAME_SEZIONE_DANNI          = "SezioneDannifauna";
  private static final String TAG_NAME_TAB_ELENCO             = "TabElencoDanni";
  private static final String TAG_NAME_RIGA_ELENCO            = "RigaElencoDanno";
  private static final String TAG_NAME_TAB_DANNO              = "TabDescDanno";
  private static final String TAG_NAME_RIGA_DANNO             = "RigaDanno";
  private static final String TAG_NAME_SEZIONE_LOCALIZZAZIONE = "SezLocalizDanno";
  private static final String TAG_NAME_TAB_LOCALIZZAZIONE     = "TabLocalizDanno";
  private static final String TAG_NAME_RIGA_LOCALIZZAZIONE    = "RigaLocalizDanno";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {

    final IQuadroIuffiEJB ejbQuadroIuffi = IuffiUtils.APPLICATION
        .getEjbQuadroIuffi();
    List<DannoFaunaDTO> danni = ejbQuadroIuffi
        .getListaDanniFauna(procedimentoOggetto.getIdProcedimentoOggetto());
    
    writer.writeStartElement(TAG_NAME_FRAGMENT_QUADRO_DANNI);

    if(danni!=null && !danni.isEmpty()) {
      writer.writeStartElement(TAG_NAME_SEZIONE_TITOLO);
      writeTag(writer, "TitoloDanniFauna", "Quadro - Elenco Danni Fauna");
      writeVisibility(writer, true);
      writer.writeEndElement(); // TAG_NAME_SEZIONE_TITOLO
      writeSezioneDanni(writer, procedimentoOggetto, danni, ejbQuadroIuffi);
    }else {
      writer.writeStartElement(TAG_NAME_SEZIONE_TITOLO);
      writeTag(writer, "TitoloDanniFauna", "Quadro - Elenco Danni Fauna");
      writeVisibility(writer, false);
      writer.writeEndElement(); // TAG_NAME_SEZIONE_TITOLO
    }
    writer.writeEndElement(); // TAG_NAME_FRAGMENT_QUADRO_DANNI
  }

  protected void writeSezioneDanni(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, List<DannoFaunaDTO> danni,
      IQuadroIuffiEJB ejb)
      throws XMLStreamException, InternalUnexpectedException
  {
    writer.writeStartElement(TAG_NAME_SEZIONE_DANNI);
    writer.writeStartElement(TAG_NAME_TAB_ELENCO);

    if (danni != null)
    {
      int progressivoDanno = 0;
      for (DannoFaunaDTO d : danni)
      {
        progressivoDanno++;
        writer.writeStartElement(TAG_NAME_RIGA_ELENCO);

        writer.writeStartElement(TAG_NAME_TAB_DANNO);
        writer.writeStartElement(TAG_NAME_RIGA_DANNO);
        writeTag(writer, "ProgressivoDanno", progressivoDanno + "");
        writeTag(writer, "Specie", d.getDescSpecieFauna());
        writeTag(writer, "TipoDanno", d.getDescTipoDannoFauna());
        writeTag(writer, "UlterioriInfo", d.getUlterioriInformazioni());
        writeTag(writer, "Quantita", d.getQuantitaStr());
        writer.writeEndElement(); // TAG_NAME_RIGA_DANNO
        writer.writeEndElement(); // TAG_NAME_TAB_DANNO

        long[] ids = new long[]
            { d.getIdDannoFauna() };
        List<DannoFaunaDTO> danniLocalizzati = ejb
            .getListaDanniFaunaDettaglio(procedimentoOggetto.getIdProcedimentoOggetto(), ids, true);
        if(danniLocalizzati!=null && !danniLocalizzati.isEmpty()) {
          writer.writeStartElement(TAG_NAME_SEZIONE_LOCALIZZAZIONE);
          writeTag(writer, "VisibilityLocDanno", "true");
          writer.writeStartElement(TAG_NAME_TAB_LOCALIZZAZIONE);
          
          BigDecimal totSupCatLocDanno = BigDecimal.ZERO;
          BigDecimal totSupUtilLocDanno = BigDecimal.ZERO;
          BigDecimal totSupCoinLocDanno = BigDecimal.ZERO;
          BigDecimal totSupUtilSecLocDanno = BigDecimal.ZERO;
          
          for (DannoFaunaDTO dl : danniLocalizzati)
          {
            
            totSupCatLocDanno = totSupCatLocDanno.add(IuffiUtils.NUMBERS.nvl(dl.getSupCatastale()));
            totSupUtilLocDanno = totSupUtilLocDanno.add(IuffiUtils.NUMBERS.nvl(dl.getSuperficieUtilizzata()));
            totSupCoinLocDanno = totSupCoinLocDanno.add(IuffiUtils.NUMBERS.nvl(dl.getSuperficieDanneggiata()));
            totSupUtilSecLocDanno = totSupUtilSecLocDanno.add(IuffiUtils.NUMBERS.nvl(dl.getSupUtilizzataSecondaria()));
            
            
            writer.writeStartElement(TAG_NAME_RIGA_LOCALIZZAZIONE);
            writeTag(writer, "ProvLocDanno", dl.getDescProvincia());
            writeTag(writer, "ComuneLocDanno", dl.getComune());
            writeTag(writer, "SezLocDanno", dl.getSezione());
            writeTag(writer, "FoglioLocDanno", String.valueOf(dl.getFoglio()));
            writeTag(writer, "PartLocDanno", dl.getParticella());
            writeTag(writer, "SubLocDanno", dl.getSubalterno());
            writeTag(writer, "SupCatLocDanno",
                IuffiUtils.FORMAT.formatDecimal4(dl.getSupCatastale()));
            writeTag(writer, "SupUtilLocDanno",
                IuffiUtils.FORMAT.formatDecimal4(dl.getSuperficieUtilizzata()));
            writeTag(writer, "SupCoinLocDanno",
                IuffiUtils.FORMAT.formatDecimal4(dl.getSuperficieDanneggiata()));
            writeTag(writer, "UtilizzoLocDanno", dl.getUtilizzo());

            writeTag(writer, "SupUtilSecLocDanno",
                IuffiUtils.FORMAT.formatDecimal4(dl.getSupUtilizzataSecondaria()));
            writeTag(writer, "UtilizzoSecLocDanno", dl.getUtilizzoSecondario());
            writeTag(writer, "FlagUtilizzoSecLocDanno", dl.getDecodeFlagUtilizzoSec());
            
            
            writer.writeEndElement(); // TAG_NAME_RIGA_LOCALIZZAZIONE
          }
          
          
          writer.writeStartElement("TotLocalizDanno");
          writeTag(writer, "TotSupCatLocDanno", IuffiUtils.FORMAT.formatDecimal4(totSupCatLocDanno) );
          writeTag(writer, "TotSupUtilLocDanno", IuffiUtils.FORMAT.formatDecimal4(totSupUtilLocDanno));
          writeTag(writer, "TotSupCoinLocDanno", IuffiUtils.FORMAT.formatDecimal4(totSupCoinLocDanno));
          writeTag(writer, "TotSupUtilSecLocDanno", IuffiUtils.FORMAT.formatDecimal4(totSupUtilSecLocDanno));
          writer.writeEndElement(); // TotLocalizDanno
          
          
          
          writer.writeEndElement(); // TAG_NAME_TAB_LOCALIZZAZIONE
          writer.writeEndElement(); // TAG_NAME_SEZIONE_LOCALIZZAZIONE
        }else {
          writer.writeStartElement(TAG_NAME_SEZIONE_LOCALIZZAZIONE);
          writeTag(writer, "VisibilityLocDanno", "false");
          writer.writeEndElement(); // TAG_NAME_SEZIONE_LOCALIZZAZIONE
        }

        writer.writeEndElement(); // TAG_NAME_RIGA_ELENCO
      }

    }
    writer.writeEndElement(); // TAG_NAME_TAB_ELENCO
    writer.writeEndElement(); // TAG_NAME_SEZIONE_DANNI
  }

}
