package it.csi.iuffi.iuffiweb.util.stampa;

import java.io.ByteArrayOutputStream;

import javax.naming.NamingException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class DomandaPSL extends Stampa
{
  public static final String ROOT_TAG = "Domanda";

  @Override
  public byte[] genera(long idProcedimentoOggetto, String cuName)
      throws Exception
  {
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
    writer.writeStartDocument("UTF-8", "1.0");
    writer.writeStartElement(ROOT_TAG);
    // Scrivo i blocchi di default (simulando come se fossero dei fragment)
    MAP_FRAGMENTS.get("GLOBAL").writeFragment(writer, procedimentoOggetto,
        quadroEJB, cuName);
    MAP_FRAGMENTS.get("HEADER").writeFragment(writer, procedimentoOggetto,
        quadroEJB, cuName);
    generaStampaQuadri(writer, quadroEJB, procedimentoOggetto, cuName);
    writer.writeEndElement();
    writer.writeEndDocument();
  }

  protected String getCodiceModulo()
  {
    return "IUF_domandaPSL";
  }

  protected String getCodiceModello()
  {
    return "IUF_domandaPSL";
  }

  protected String getRifAdobe()
  {
    return BASE_RIF_ADOBE + "domandaPSL.xdp";
  }

  @Override
  public String getDefaultFileName(long idProcedimentoOggetto)
      throws InternalUnexpectedException, NamingException
  {
    return "domanda_psl.pdf";
  }
}
