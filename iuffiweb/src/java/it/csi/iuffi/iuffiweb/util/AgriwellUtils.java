package it.csi.iuffi.iuffiweb.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import it.csi.iuffi.iuffiweb.dto.procedimento.TestataProcedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiAziendaDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.datiidentificativi.DatiIdentificativi;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.papua.papuaserv.dto.gestioneutenti.Intermediario;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellDocumentoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellMetadatoVO;

public abstract class AgriwellUtils
{

  public AgriWellDocumentoVO getAgriWellDocumentoVO(long idTipoDocumento,
      byte[] fileDaAllegare,
      String originalFileName,
      Map<String, String> mapParametri, UtenteAbilitazioni utenteAbilitazioni,
      String flagFirmaGrafometrica,
      TestataProcedimento testataProcedimento,
      DatiIdentificativi datiIdentificativi, String identificativo)
      throws InternalUnexpectedException,
      IOException
  {
    return getAgriWellDocumentoVO(idTipoDocumento, fileDaAllegare,
        originalFileName, mapParametri, utenteAbilitazioni,
        flagFirmaGrafometrica,
        testataProcedimento,
        datiIdentificativi, identificativo, null);
  }

  public AgriWellDocumentoVO getAgriWellDocumentoVO(long idTipoDocumento,
      byte[] fileDaAllegare,
      String originalFileName,
      Map<String, String> mapParametri, UtenteAbilitazioni utenteAbilitazioni,
      String flagFirmaGrafometrica,
      TestataProcedimento testataProcedimento,
      DatiIdentificativi datiIdentificativi, String identificativo,
      List<AgriWellMetadatoVO> metadatiAggiuntivi)
      throws InternalUnexpectedException,
      IOException
  {
    AgriWellDocumentoVO agriWellDocumentoVO = new AgriWellDocumentoVO();
    Integer anno = testataProcedimento.getAnnoCampagna();
    if (anno == null)
    {
      anno = IuffiUtils.DATE
          .getYearFromDate(testataProcedimento.getDataInizioBando());
    }
    String fileName = getFileName(originalFileName);
    agriWellDocumentoVO.setAnno(anno);
    agriWellDocumentoVO.setContenutoFile(fileDaAllegare);
    agriWellDocumentoVO.setDataInserimento(new Date());
    agriWellDocumentoVO
        .setDimensioneFile(new BigDecimal(fileDaAllegare.length));
    agriWellDocumentoVO
        .setEstensione(IuffiUtils.FILE.getFileExtension(fileName, true));
    agriWellDocumentoVO.setDaFirmare(flagFirmaGrafometrica);
    if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
    {
      Intermediario intermediario = utenteAbilitazioni.getEnteAppartenenza()
          .getIntermediario();
      agriWellDocumentoVO
          .setExtIdIntermediario(intermediario.getIdIntermediario());
    }
    DatiAziendaDTO azienda = datiIdentificativi.getAzienda();
    agriWellDocumentoVO.setExtIdAzienda(azienda.getIdAzienda());
    agriWellDocumentoVO
        .setIdProcedimento((long) utenteAbilitazioni.getIdProcedimento());
    agriWellDocumentoVO.setIdTipoDocumento(idTipoDocumento);
    agriWellDocumentoVO
        .setIdUtenteAggiornamento(utenteAbilitazioni.getIdUtenteLogin());
    agriWellDocumentoVO.setNomeCartella(
        mapParametri.get(IuffiConstants.PARAMETRO.DOQUIAGRI_CARTELLA));
    agriWellDocumentoVO.setNomeFile(fileName);
    agriWellDocumentoVO.setIdentificativo(identificativo);
    agriWellDocumentoVO.setFlagCartellaDaCreare(IuffiConstants.FLAGS.SI);

    // Metadati
    List<AgriWellMetadatoVO> metadati = new ArrayList<AgriWellMetadatoVO>();
    // METADATI DA PASSARE SEMPRE - INIZIO
    AgriWellMetadatoVO metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta("cuaa");
    metadato.setValoreEtichetta(azienda.getCuaa());
    metadati.add(metadato);

    metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta("data_inizio");
    metadato.setValoreEtichetta(IuffiUtils.DATE.formatDateTime(new Date()));
    metadati.add(metadato);

    metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta("id_tipo_visibilita");
    metadato.setValoreEtichetta("1");
    metadati.add(metadato);

    metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta("codice_ente_utente");
    metadato.setValoreEtichetta(
        IuffiUtils.PAPUASERV.extractInfoEnteBaseFromEnteLogin(
            utenteAbilitazioni.getEnteAppartenenza()).getCodiceEnte());
    metadati.add(metadato);

    metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta("titolo_documento");
    metadato.setValoreEtichetta(fileName.replaceAll("\\s", "_"));
    metadati.add(metadato);

    metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta("classificazione_regione");
    metadato.setValoreEtichetta(
        mapParametri.get(IuffiConstants.PARAMETRO.DOQUIAGRI_CLASS_REG));
    metadati.add(metadato);

    metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta("fascicolazione");
    metadato.setValoreEtichetta(
        mapParametri.get(IuffiConstants.PARAMETRO.DOQUIAGRI_FASCICOLA));
    metadati.add(metadato);

    metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta("documento_firmato");
    metadato.setValoreEtichetta("FALSE");
    metadati.add(metadato);

    metadato = new AgriWellMetadatoVO();
    metadato.setNomeEtichetta("documento_verificato");
    metadato.setValoreEtichetta("FALSE");
    metadati.add(metadato);
    if (metadatiAggiuntivi != null && !metadatiAggiuntivi.isEmpty())
    {
      metadati.addAll(metadatiAggiuntivi);
    }
    agriWellDocumentoVO.setaMetadati(
        metadati.toArray(new AgriWellMetadatoVO[metadati.size()]));
    return agriWellDocumentoVO;
  }

  private String getFileName(String originalFilename)
  {
    try
    {
      String fileName = originalFilename.replace("\\", "/");
      int pos = fileName.lastIndexOf('/');
      if (pos < 0)
      {
        return originalFilename;
      }
      return fileName.substring(pos);
    }
    catch (Exception e)
    {
      return originalFilename;
    }
  }

  public String getHtmlIconaAgriwellWeb(UtenteAbilitazioni utenteAbilitazioni,
      long idAzienda, long idProcedimento,
      String portal, String cssClass)
  {
    HttpSession session = ((ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes()).getRequest().getSession();

    if (!IuffiConstants.FLAGS.SI.equals(
        session.getAttribute(IuffiConstants.PARAMETRO.DOQUIAGRI_FLAG_PROT)))
    {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    if (cssClass == null)
    {
      cssClass = "ico32 ico_agriwell";
    }
    sb.append("\n<a");
    if (cssClass != null)
    {
      sb.append(" class=\"").append(cssClass).append("\"");
    }
    sb.append(
        " target=\"_blank\" style=\"vertical-align:middle\" href=\"#\" onclick=\"return openDocumentale('")
        .append(getAgriwellWebUrl(utenteAbilitazioni, idAzienda, idProcedimento,
            portal))
        .append("')\" title=\"Documentale\"></a>");
    return sb.toString();
  }

  protected String getAgriwellWebUrl(UtenteAbilitazioni utenteAbilitazioni,
      long idAzienda, long idProcedimento,
      String portal)
  {
    try
    {
      String baseUrl = IuffiConstants.URL.AGRIWELLWEB.BASE_URL_PRIVATI;
      if (IuffiConstants.PORTAL.PUBBLICA_AMMINISTRAZIONE.equals(portal))
      {
        baseUrl = IuffiConstants.URL.AGRIWELLWEB.BASE_URL_PUBBLICA_AMMINISTRAZIONE;
      }
      else
      {
        if (IuffiConstants.PORTAL.SPID.equals(portal))
        {
          baseUrl = IuffiConstants.URL.AGRIWELLWEB.BASE_URL_SPID;
        }
      }

      HttpSession session = ((ServletRequestAttributes) RequestContextHolder
          .getRequestAttributes()).getRequest().getSession();

      String codiceRuolo = (String) session.getAttribute("codiceRuoloLogin");
      StringBuilder sb = new StringBuilder(baseUrl)
          .append("?CF=").append(utenteAbilitazioni.getCodiceFiscale())
          .append("&ruolo=")
          .append(IuffiUtils.STRING.urlEncode(codiceRuolo));
      // if (!GenericValidator.isBlankOrNull(ruolo.getCodiceEnte()))
      // {
      // (sb.append("$$").append(ruolo.getCodiceEnte());
      // }
      sb.append("&idpr_a=").append(utenteAbilitazioni.getIdProcedimento())
          .append("&liv=").append(utenteAbilitazioni.getLivelloAutenticazione())
          .append("&extId=").append(idAzienda)
          .append("&idpr_u=").append(utenteAbilitazioni.getIdProcedimento())
          .append("&key=").append(idProcedimento);

      return sb.toString();

    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    return "";
  }

}
