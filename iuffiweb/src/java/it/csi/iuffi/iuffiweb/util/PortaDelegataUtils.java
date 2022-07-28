package it.csi.iuffi.iuffiweb.util;

import java.util.Properties;

import javax.naming.Context;

import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.iuffi.iuffiweb.exception.InternalServiceException;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.smranags.gaaserv.interfacecsi.IGaaservHeavyLoadCSIInterface;
import it.csi.smrcomms.smrcomm.interfacecsi.IAgriWellCSIInterface;
import it.csi.solmr.dto.anag.sian.SianUtenteVO;
import it.csi.solmr.interfaceCSI.anag.services.AnagServiceCSIInterface;

public class PortaDelegataUtils
{
  public static final String FILE_PD_PAPUASERV     = "papuaserv-client-2.0.0.xml";
  public static final String FILE_PD_ANAGSERV      = "smrgaasv-client-5.4.0.xml";
  public static final String FILE_PD_AGRIWELL      = "agriwell-client-1.4.0.xml";
  public static final String FILE_PD_PDF_GENERATOR = "pdf-generator-client.xml";
  public static final String FILE_PD_GAASRV_HL     = "gaasrv-HL-client.xml";

  private InfoPortaDelegata  pdAnag;
  private InfoPortaDelegata  pdAgriwell;
  private InfoPortaDelegata  pdPdfGenerator;
  private InfoPortaDelegata  pdGaasrvHL;

  public AnagServiceCSIInterface getAnagServiceCSIInterface()
      throws InternalServiceException
  {
    try
    {
      if (pdAnag == null)
      {
        pdAnag = PDConfigReader.read(PortaDelegataUtils.class.getClassLoader()
            .getResourceAsStream(FILE_PD_ANAGSERV));
      }
      return (AnagServiceCSIInterface) PDProxy.newInstance(pdAnag);
    }
    catch (Exception e)
    {
      throw new InternalServiceException(
          "Errore di accesso al servizio ANAGSERV", e);
    }
  }

  public IGaaservHeavyLoadCSIInterface getGaaServiceHlCSIInterface()
      throws InternalServiceException
  {
    try
    {
      if (pdGaasrvHL == null)
      {
        pdGaasrvHL = PDConfigReader.read(PortaDelegataUtils.class
            .getClassLoader().getResourceAsStream(FILE_PD_GAASRV_HL));
      }
      return (IGaaservHeavyLoadCSIInterface) PDProxy.newInstance(pdGaasrvHL);
    }
    catch (Exception e)
    {
      throw new InternalServiceException("Errore di accesso al servizio GAASRV",
          e);
    }
  }

  public IAgriWellCSIInterface getAgriwellCSIInterface()
      throws InternalServiceException
  {
    try
    {
      if (pdAgriwell == null)
      {
        pdAgriwell = PDConfigReader.read(PortaDelegataUtils.class
            .getClassLoader().getResourceAsStream(FILE_PD_AGRIWELL));
      }
      return (IAgriWellCSIInterface) PDProxy.newInstance(pdAgriwell);
    }
    catch (Exception e)
    {
      throw new InternalServiceException(
          "Errore di accesso al servizio AGRIWELL", e);
    }
  }

  public String getProviderURLAgriwell()
  {
    try
    {
      if (pdAgriwell == null)
      {
        pdAgriwell = PDConfigReader.read(PortaDelegataUtils.class
            .getClassLoader().getResourceAsStream(FILE_PD_AGRIWELL));
      }
      return extractProviderURL(pdAgriwell);
    }
    catch (Exception e)
    {
      return "#ERROR# exception = " + e.getMessage();
    }
  }

  public String getProviderURLPDFGenerator()
  {

    try
    {
      if (pdPdfGenerator == null)
      {
        pdPdfGenerator = PDConfigReader.read(PortaDelegataUtils.class
            .getClassLoader().getResourceAsStream(FILE_PD_PDF_GENERATOR));
      }
      return extractProviderURL(pdPdfGenerator);
    }
    catch (Exception e)
    {
      return "#ERROR# exception = " + e.getMessage();
    }
  }

  public String extractProviderURL(InfoPortaDelegata pdInfo)
  {
    try
    {
      Properties propFirstPD = pdInfo.getPlugins()[0].getProperties();
      return (String) propFirstPD.get(Context.PROVIDER_URL);
    }
    catch (Exception e)
    {
      return "#ERROR# exception = " + e.getMessage();
    }
  }

  /**
   * Oggetto necessario per passare i dati relativi all'utente loggato
   * all'applicativo ai servizi di Anagrafe Tributaria
   * 
   * @param ruoloUtenza
   * @return SianUtenteVO
   */
  public SianUtenteVO getSianUtenteVO(UtenteAbilitazioni utenteAbilitazioni)
  {
    SianUtenteVO sianUtenteVO = new SianUtenteVO();
    sianUtenteVO
        .setCodiceEnte(IuffiUtils.PAPUASERV.extractInfoEnteBaseFromEnteLogin(
            utenteAbilitazioni.getEnteAppartenenza()).getCodiceEnte());
    sianUtenteVO.setCodiceFiscale(utenteAbilitazioni.getCodiceFiscale());
    sianUtenteVO.setIdProcedimento(utenteAbilitazioni.getIdProcedimento());
    sianUtenteVO.setRuolo(utenteAbilitazioni.getRuolo().getCodice());

    if (utenteAbilitazioni.getCodiceFiscale().startsWith("AAAAA"))
    {
      sianUtenteVO.setCodiceFiscale("DPLGNZ55A09F952X");
    }

    return sianUtenteVO;
  }

}
