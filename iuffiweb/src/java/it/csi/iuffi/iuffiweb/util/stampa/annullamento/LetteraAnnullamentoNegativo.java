package it.csi.iuffi.iuffiweb.util.stampa.annullamento;

import java.io.ByteArrayOutputStream;

import javax.naming.NamingException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.stampa.Stampa;

public class LetteraAnnullamentoNegativo extends Stampa
{
  public static final String ROOT_TAG  = "Domanda";
  private String             cuNameRef = null;

  public LetteraAnnullamentoNegativo()
  {
    super();
  }

  public LetteraAnnullamentoNegativo(String cuNameRef)
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
    else
      if (!cuName.endsWith("-" + IuffiConstants.ESITO.TIPO.NEGATIVO))
      {
        cuName += "-" + IuffiConstants.ESITO.TIPO.NEGATIVO;
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
    writeFragment("HEADER_AMMISSIONE_FINANZIAMENTO", writer, quadroEJB,
        procedimentoOggetto, cuName);
    writeFragment("GLOBAL", writer, quadroEJB, procedimentoOggetto, cuName);
    writeFragment("AMMISSIONE_FINANZIAMENTO_SEZIONI_TESTO", writer, quadroEJB,
        procedimentoOggetto, cuName);
    writeFragment("AMMISSIONE_FINANZIAMENTO_INTERVENTI", writer, quadroEJB,
        procedimentoOggetto, cuName);
    writeFragment("FIRMA_AMMISSIONE_FINANZIAMENTO", writer, quadroEJB,
        procedimentoOggetto, cuName);
    writer.writeEndElement();
    writer.writeEndDocument();
  }

  protected String getCodiceModulo()
  {
    return "IUF_LetteraAnnullamento";
  }

  protected String getCodiceModello()
  {
    return "IUF_LetteraAnnullamento";
  }

  protected String getRifAdobe()
  {
    return BASE_RIF_ADOBE + "LetteraAnnullamento.xdp";
  }

  @Override
  public String getDefaultFileName(long idProcedimentoOggetto) throws InternalUnexpectedException, NamingException
  {
    return "LetteraAnnullamento.pdf";
  }
}
