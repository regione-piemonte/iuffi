package it.csi.iuffi.iuffiweb.util.stampa;

import java.io.ByteArrayOutputStream;

import javax.naming.NamingException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class VerbaleIntegPagamento extends Stampa
{
  public static final String ROOT_TAG  = "Domanda";

  private String             cuNameRef = null;
  /* ATTENZIONE. ATTENZIONE. ATTENZIONE. ATTENZIONE */
  /*
   * NON USARE PROPRIETA' IN SCRITTURA PERCHE' QUESTA CLASSE VIENE ISTANZIATA
   * COME SINGLETON
   */

  public VerbaleIntegPagamento()
  {
    super();
  }

  public VerbaleIntegPagamento(String cuNameRef)
  {
    super();
    this.cuNameRef = cuNameRef;
  }

  @Override
  public byte[] genera(long idProcedimentoOggetto, String cuName)
      throws Exception
  {
    if (cuNameRef != null)
    {
      cuName = cuNameRef;
    }

    ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
    XMLStreamWriter writer = getXMLStreamWriter(xmlOutputStream);
    IQuadroEJB quadroEJB = IuffiUtils.APPLICATION.getEjbQuadro();
    generaXML(writer, idProcedimentoOggetto, quadroEJB, cuName);
    return callModol(xmlOutputStream.toByteArray());
  }

  protected void generaXML(XMLStreamWriter writer, long idProcedimentoOggetto,
      IQuadroEJB quadroEJB, String cuName) throws Exception
  {
    ProcedimentoOggetto procedimentoOggetto = quadroEJB
        .getProcedimentoOggetto(idProcedimentoOggetto);
    writer.writeStartDocument(DEFAULT_ENCODING, "1.0");
    writer.writeStartElement(ROOT_TAG);
    // Scrivo i blocchi di default (simulando come se fossero dei fragment)
    writeFragment("GLOBAL", writer, quadroEJB, procedimentoOggetto, cuName);
    writeFragment("HEADER_AMMISSIONE_FINANZIAMENTO", writer, quadroEJB,
        procedimentoOggetto, cuName);
    writeFragment("AMMISSIONE_FINANZIAMENTO_SEZIONI_TESTO", writer, quadroEJB,
        procedimentoOggetto, cuName);
    writeFragment(IuffiConstants.QUADRO.CODICE.ACCERTAMENTO_ACCONTO, writer,
        quadroEJB, procedimentoOggetto, cuName);
    writeFragment("QUADRO_INTEGRAZIONE", writer, quadroEJB, procedimentoOggetto,
        cuName);
    writeFragment("FIRMA_VERBALE_AMMISSIONE_FINANZIAMENTO", writer, quadroEJB,
        procedimentoOggetto, cuName);

    writer.writeEndElement();
    writer.writeEndDocument();
  }

  protected String getCodiceModulo()
  {
    return "IUF_VerbaleIntegPagamento";
  }

  protected String getCodiceModello()
  {
    return "IUF_VerbaleIntegPagamento";
  }

  protected String getRifAdobe()
  {
    return BASE_RIF_ADOBE + "VerbaleIntegPagamento.xdp";
  }

  @Override
  public String getDefaultFileName(long idProcedimentoOggetto)
      throws InternalUnexpectedException, NamingException
  {
    String cuaaBeneficiario = IuffiUtils.APPLICATION.getEjbQuadro()
        .getCuaaByIdProcedimentoOggetto(idProcedimentoOggetto);
    String identificativoProcedimento = IuffiUtils.APPLICATION.getEjbQuadro()
        .getIdentificativo(idProcedimentoOggetto);
    String ret = "";
    if (cuaaBeneficiario != null)
      ret += cuaaBeneficiario;

    if (identificativoProcedimento != null)
      ret += "_" + identificativoProcedimento;

    return ret + "_VerbaleIntegPagamento.pdf";
  }
}
