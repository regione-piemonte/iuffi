package it.csi.iuffi.iuffiweb.presentation.taglib.iuffiweb;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.ElencoCduDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.AzioneDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.exception.IuffiPermissionException;
import it.csi.iuffi.iuffiweb.util.IuffiFactory;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

public class AbilitazioneAzioneTag extends BaseTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 6582461845796172740L;
  private String            codiceQuadro;
  private String            codiceAzione;
  private Boolean           visible;

  @Override
  public int doEndTag() throws JspException
  {
    if (visible == null || visible.booleanValue())
    {
      final String KEY_CACHE_ABILITAZIONE = AbilitazioneAzioneTag.class
          .getName() + "_" + codiceQuadro + "_" + codiceAzione;
      final HttpServletRequest request = (HttpServletRequest) pageContext
          .getRequest();
      Boolean abilitazione = (Boolean) request
          .getAttribute(KEY_CACHE_ABILITAZIONE);
      if (abilitazione == null)
      {
        abilitazione = validate();
        request.setAttribute(KEY_CACHE_ABILITAZIONE, abilitazione);
      }
      if (abilitazione != null && abilitazione && this.bodyContent != null)
      {
        try
        {
          this.pageContext.getOut().write(this.bodyContent.getString());
        }
        catch (IOException e)
        {
          throw new JspException(e);
        }
      }
    }
    return super.doEndTag();
  }

  public boolean validateCdU(String nomeCdu, HttpServletRequest request)
      throws JspException
  {
    return validateCdU(nomeCdu, (UtenteAbilitazioni) pageContext.getSession()
        .getAttribute("utenteAbilitazioni"), request);
  }

  public static boolean validateCdU(String nomeCdu,
      UtenteAbilitazioni utenteAbilitazioni, HttpServletRequest request)
  {
    ElencoCduDTO cdu = IuffiUtils.APPLICATION.getCdu(nomeCdu);
    if (cdu == null)
    {
      return false;
    }
    String extCodMacroCdu = cdu.getExtCodMacroCdu();
    if (extCodMacroCdu != null)
    {
      if (!IuffiUtils.PAPUASERV.isMacroCUAbilitato(utenteAbilitazioni,
          extCodMacroCdu))
      {
        return false;
      }
    }
    String tipoAzione = cdu.getTipoAzione();
    if (ElencoCduDTO.TIPO_AZIONE_READ_WRITE.equals(tipoAzione))
    {
      if (IuffiUtils.PAPUASERV.isUtenteReadOnly(utenteAbilitazioni))
      {
        return false;
      }
      else
      {
        if (request != null)
        {
          Boolean canUpdatePO = (Boolean) request.getAttribute("canUpdatePO");
          if (canUpdatePO == null)
          {
            canUpdatePO = canUpdateProcedimentoOggetto(request);
            request.setAttribute("canUpdatePO", canUpdatePO);
          }
          return canUpdatePO.booleanValue();
        }
      }
    }
    return true;
  }

  private static Boolean canUpdateProcedimentoOggetto(
      HttpServletRequest request)
  {
    try
    {
      ApplicationContext appCtx = WebApplicationContextUtils
          .getWebApplicationContext(request.getServletContext());
      IQuadroEJB ejb = appCtx.getBean(IQuadroEJB.class);
      ProcedimentoOggetto procedimentoOggetto = IuffiFactory
          .getProcedimentoOggetto(request);
      ejb.canUpdateProcedimentoOggetto(
          procedimentoOggetto.getIdProcedimentoOggetto(), true);
      return Boolean.TRUE;
    }
    catch (IuffiPermissionException e)
    {
      return Boolean.FALSE;
    }
    catch (Exception e)
    {
      logger
          .error(
              "[AbilitazioneAzioneTag.canUpdateProcedimentoOggetto] Eccezione non gestita nel richiamo di canUpdateProcedimentoOggetto() ==> Abilitazione NEGATA perchè non verificabile!",
              e);
      return Boolean.FALSE;
    }
  }

  public boolean validate() throws JspException
  {
    ProcedimentoOggetto procedimentoOggetto = null;
    try
    {
      procedimentoOggetto = IuffiFactory.getProcedimentoOggetto(
          (HttpServletRequest) this.pageContext.getRequest());
    }
    catch (InternalUnexpectedException e)
    {
      throw new JspException(e);
    }
    final HttpSession session = pageContext.getSession();
    return validate(codiceQuadro, codiceAzione, procedimentoOggetto,
        (UtenteAbilitazioni) session.getAttribute("utenteAbilitazioni"),
        (HttpServletRequest) this.pageContext.getRequest());
  }

  public static boolean validate(String codiceQuadro, String codiceAzione,
      ProcedimentoOggetto procedimentoOggetto,
      UtenteAbilitazioni utenteAbilitazioni,
      HttpServletRequest request)
  {
    QuadroOggettoDTO quadro = null;
    if (codiceQuadro != null)
    {

      for (QuadroOggettoDTO qdr : procedimentoOggetto.getQuadri())
      {
        if (qdr.getCodQuadro().equalsIgnoreCase(codiceQuadro))
        {
          quadro = qdr;
          break;
        }
      }
      if (quadro == null)
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("[AbilitazioneAzioneTag:doEndTag] Quadro null ==> Esco");
        }
        return false;
      }
    }
    if (codiceAzione != null)
    {
      if (quadro == null)
      {
        logger.debug("[AbilitazioneAzioneTag:doEndTag] Quadro null ==> Esco");
        return false;
      }
      AzioneDTO azioneDTO = quadro.findAzioneByCodice(codiceAzione);
      if (azioneDTO == null)
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("[AbilitazioneAzioneTag:doEndTag] Azione null ==> Esco");
        }
        return false;
      }
      else
      {
        if (!validateCdU(azioneDTO.getCodiceCdu(), utenteAbilitazioni, request))
        {
          try
          {
            if (logger.isDebugEnabled())
            {
              logger.debug("[AbilitazioneAzioneTag:doEndTag] Azione "
                  + azioneDTO.getCodiceAzione() + " con CDU "
                  + azioneDTO.getCodiceCdu() + " del quadro "
                  + quadro.getCodQuadro() + " non atorizzata ==> Esco");
            }
          }
          catch (Exception e)
          {

          }
          return false;
        }
      }
    }
    return true;
  }

  public String getCodiceQuadro()
  {
    return codiceQuadro;
  }

  public void setCodiceQuadro(String codiceQuadro)
  {
    this.codiceQuadro = codiceQuadro;
  }

  public String getCodiceAzione()
  {
    return codiceAzione;
  }

  public void setCodiceAzione(String codiceAzione)
  {
    this.codiceAzione = codiceAzione;
  }

  public Boolean getVisible()
  {
    return visible;
  }

  public void setVisible(Boolean visible)
  {
    this.visible = visible;
  }
}
