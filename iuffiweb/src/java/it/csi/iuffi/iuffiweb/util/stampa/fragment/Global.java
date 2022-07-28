package it.csi.iuffi.iuffiweb.util.stampa.fragment;

import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;

public class Global extends Fragment
{
  public static final String TAG_NAME_GLOBAL = "Global";

  @Override
  public void writeFragment(XMLStreamWriter writer,
      ProcedimentoOggetto procedimentoOggetto, IQuadroEJB quadroEJB,
      String cuName) throws Exception
  {
    writer.writeStartElement(TAG_NAME_GLOBAL);
    writeTag(writer, "Bozza",
        String.valueOf(procedimentoOggetto.getDataFine() == null));

    if ("S".equals(procedimentoOggetto.getFlagValidazioneGrafica()))
      writeTag(writer, "IsValidazioneGrafica", "true");
    else
      writeTag(writer, "IsValidazioneGrafica", "false");

    // DA RIVEDERE ASSOLUTAMENTE!!!!!!!!!!!!!!!!
    if ("CU-IUFFI-242-N2".equals(cuName) ||
        "CU-IUFFI-242-P2".equals(cuName) ||
        "CU-IUFFI-243-N2".equals(cuName) ||
        "CU-IUFFI-243-P2".equals(cuName) ||
        "CU-IUFFI-244-N2".equals(cuName) ||
        "CU-IUFFI-244-P2".equals(cuName) ||
        "CU-IUFFI-245-N2".equals(cuName) ||
        "CU-IUFFI-245-P2".equals(cuName) ||
        "CU-IUFFI-246-N2".equals(cuName) ||
        "CU-IUFFI-246-P2".equals(cuName) ||
        "CU-IUFFI-247-N2".equals(cuName) ||
        "CU-IUFFI-247-P2".equals(cuName))
      writeTag(writer, "IsSecondaIstruttoria", "true");

    if (cuName
        .startsWith(IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VARIANTE)
        || cuName.startsWith(
            IuffiConstants.USECASE.STAMPE_OGGETTO.GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE))
      writeTag(writer, "IsVariante", "true");

    

    if (procedimentoOggetto != null
        && procedimentoOggetto.getCodOggetto() != null
        && (procedimentoOggetto.getCodOggetto().compareTo("ISABG") == 0
            || procedimentoOggetto.getCodOggetto().compareTo("ISAMB") == 0))
      writeTag(writer, "IsAmmib", "true");

    // writeTagSecondaIstruttoria(writer, cuName);

    writer.writeEndElement();
  }

  /*
   * private void writeTagSecondaIstruttoria(XMLStreamWriter writer, String
   * cuName) throws XMLStreamException {
   * 
   * if( cuName.equals(IuffiConstants.USECASE.STAMPE_OGGETTO.
   * GENERA_LETTERA_ISTRUTTORIA_ACCONTO_POSITIVO_2) ||
   * cuName.equals(IuffiConstants.USECASE.STAMPE_OGGETTO.
   * GENERA_LETTERA_ISTRUTTORIA_ACCONTO_NEGATIVO_2) ||
   * cuName.equals(IuffiConstants.USECASE.STAMPE_OGGETTO.
   * GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_POSITIVO_2) ||
   * cuName.equals(IuffiConstants.USECASE.STAMPE_OGGETTO.
   * GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_NEGATIVO_2) ||
   * cuName.equals(IuffiConstants.USECASE.STAMPE_OGGETTO.
   * GENERA_LETTERA_ISTRUTTORIA_SALDO_POSITIVO_2) ||
   * cuName.equals(IuffiConstants.USECASE.STAMPE_OGGETTO.
   * GENERA_LETTERA_ISTRUTTORIA_SALDO_NEGATIVO_2) ||
   * 
   * cuName.equals(IuffiConstants.USECASE.STAMPE_OGGETTO.
   * GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_POSITIVO_2) ||
   * cuName.equals(IuffiConstants.USECASE.STAMPE_OGGETTO.
   * GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_NEGATIVO_2) ||
   * cuName.equals(IuffiConstants.USECASE.STAMPE_OGGETTO.
   * GENERA_VERBALE_ISTRUTTORIA_ACCONTO_POSITIVO_2) ||
   * cuName.equals(IuffiConstants.USECASE.STAMPE_OGGETTO.
   * GENERA_VERBALE_ISTRUTTORIA_ACCONTO_NEGATIVO_2) ||
   * cuName.equals(IuffiConstants.USECASE.STAMPE_OGGETTO.
   * GENERA_VERBALE_ISTRUTTORIA_SALDO_POSITIVO_2) ||
   * cuName.equals(IuffiConstants.USECASE.STAMPE_OGGETTO.
   * GENERA_VERBALE_ISTRUTTORIA_SALDO_NEGATIVO_2)
   * 
   * ) writeTag(writer, "IsSecondaIstruttoria", "true"); }
   */

}
