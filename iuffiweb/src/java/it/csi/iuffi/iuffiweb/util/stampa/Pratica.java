package it.csi.iuffi.iuffiweb.util.stampa;

import java.io.ByteArrayOutputStream;

import javax.naming.NamingException;
import javax.xml.stream.XMLStreamWriter;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservProfilazioneServiceFactory;

public class Pratica extends Stampa
{
  public static final String ROOT_TAG = "Domanda";
  /* ATTENZIONE. ATTENZIONE. ATTENZIONE. ATTENZIONE */
  /*
   * NON USARE PROPRIETA' IN SCRITTURA PERCHE' QUESTA CLASSE VIENE ISTANZIATA
   * COME SINGLETON
   */

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
    writer.writeStartDocument(DEFAULT_ENCODING, "1.0");
    writer.writeStartElement(ROOT_TAG);
    // Scrivo i blocchi di default (simulando come se fossero dei fragment)
    MAP_FRAGMENTS.get("GLOBAL").writeFragment(writer, procedimentoOggetto,
        quadroEJB, cuName);
    MAP_FRAGMENTS.get("HEADER").writeFragment(writer, procedimentoOggetto,
        quadroEJB, cuName);
    generaStampaQuadri(writer, quadroEJB, procedimentoOggetto, cuName);

    if (quadroEJB
        .isQuadroAttestazioneVisible(procedimentoOggetto.getIdOggetto()))
    {
      if (PapuaservProfilazioneServiceFactory.getRestServiceClient()
          .getUtenteAbilitazioniByIdUtenteLogin(
              procedimentoOggetto.getIdUtenteAggiornamento())
          .getRuolo().isUtenteIntermediario())
      {
        MAP_FRAGMENTS.get("QUADRO_ATTESTAZIONE_CAA").writeFragment(writer,
            procedimentoOggetto, quadroEJB, cuName);
      }
    }

    writer.writeEndElement();
    writer.writeEndDocument();
  }

  protected String getCodiceModulo()
  {
    return "IUF_pratica";
  }

  protected String getCodiceModello()
  {
    return "IUF_pratica";
  }

  protected String getRifAdobe()
  {
    return BASE_RIF_ADOBE + "pratica.xdp";
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

    return ret + "_pratica.pdf";
  }
}
