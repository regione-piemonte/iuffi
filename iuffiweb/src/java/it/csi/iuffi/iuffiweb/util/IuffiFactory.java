package it.csi.iuffi.iuffiweb.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import it.csi.iuffi.iuffiweb.business.IIuffiAbstractEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.ProcedimentoEProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.dto.procedimento.Procedimento;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;

public class IuffiFactory
{
  protected static IQuadroEJB   quadroEJB = null;
  protected static final Logger logger    = Logger
      .getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".util");
  static
  {
    try
    {
      quadroEJB = IuffiUtils.APPLICATION.getEjbQuadro();
    }
    catch (Exception e)
    {
      logger.fatal(
          "[IuffiFactory statick block] Errore nel reperimento dell'ejb Quadro",
          e);
    }
  }

  public static String getMotivoEsclusioneProcedimentoOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException
  {
    return IuffiUtils.STRING.nvl(quadroEJB
        .getProcedimentoOggEstrattoCampioneDescr(idProcedimentoOggetto));
  }

  public static String getMotivoEsclusioneProcedimento(long idProcedimento)
      throws InternalUnexpectedException
  {
    return IuffiUtils.STRING
        .nvl(quadroEJB.getProcedimentoEstrattoCampioneDescr(idProcedimento));
  }

  public static String getMotivoEsclusioneExPostProcedimento(
      long idProcedimento) throws InternalUnexpectedException
  {
    return IuffiUtils.STRING.nvl(
        quadroEJB.getProcedimentoEstrattoCampioneExPostDescr(idProcedimento));
  }

  public static final void setIdProcedimentoOggettoInSession(
      HttpSession session, long idProcedimentoOggetto,
      long idProcedimento)
  {
    SessionDatiProcedimentoEOggetto datiSession = new SessionDatiProcedimentoEOggetto();
    datiSession.idProcedimento = idProcedimento;
    datiSession.idProcedimentoOggetto = idProcedimentoOggetto;
    session.setAttribute(SessionDatiProcedimentoEOggetto.SESSION_NAME,
        datiSession);
    ((ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes()).getRequest()
            .removeAttribute(ProcedimentoEProcedimentoOggetto.REQUEST_NAME);
  }

  public static final void setIdProcedimentoInSession(HttpSession session,
      long idProcedimento)
  {
    SessionDatiProcedimentoEOggetto datiSession = new SessionDatiProcedimentoEOggetto();
    datiSession.idProcedimento = idProcedimento;
    session.setAttribute(SessionDatiProcedimentoEOggetto.SESSION_NAME,
        datiSession);
    ((ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes()).getRequest()
            .removeAttribute(ProcedimentoEProcedimentoOggetto.REQUEST_NAME);
  }

  public static final void removeIdProcedimentoInSession(HttpSession session)
  {
    // Se non rimuovo l'idProcedimento non serve più l'oggetto
    // SessionDatiProcedimentoEOggetto
    session.removeAttribute(SessionDatiProcedimentoEOggetto.SESSION_NAME);
  }

  public static final void removeIdProcedimentoOggettoFromSession(
      HttpSession session)
  {
    SessionDatiProcedimentoEOggetto datiSessione = (SessionDatiProcedimentoEOggetto) session
        .getAttribute(SessionDatiProcedimentoEOggetto.class.getName());
    datiSessione.idProcedimentoOggetto = null;
    // Cancellando idProcedimentoOggetto non ha senso lasciare i quadri ==> li
    // rimuovo
    datiSessione.quadri = null;
    session.setAttribute(SessionDatiProcedimentoEOggetto.SESSION_NAME,
        datiSessione);
  }

  public static final ProcedimentoOggetto getProcedimentoOggetto(
      HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return getProcedimentoOggetto(request, quadroEJB);
  }

  public static final ProcedimentoOggetto getProcedimentoOggetto(
      HttpServletRequest request,
      IIuffiAbstractEJB ejb) throws InternalUnexpectedException
  {
    ProcedimentoEProcedimentoOggetto ppo = findDatiProcedimentoEOggetto(request,
        ejb);
    if (ppo != null)
    {
      return ppo.getProcedimentoOggetto();
    }
    else
    {
      return null;
    }
  }

  public static final Procedimento getProcedimento(HttpServletRequest request)
      throws InternalUnexpectedException
  {
    return getProcedimento(request, quadroEJB);
  }

  public static final Procedimento getProcedimento(HttpServletRequest request,
      IIuffiAbstractEJB ejb)
      throws InternalUnexpectedException
  {
    ProcedimentoEProcedimentoOggetto ppo = findDatiProcedimentoEOggetto(request,
        ejb);
    if (ppo != null)
    {
      return ppo.getProcedimento();
    }
    else
    {
      return null;
    }
  }

  public static void setQuadri(HttpSession session,
      List<QuadroOggettoDTO> quadri)
      throws InternalUnexpectedException
  {
    SessionDatiProcedimentoEOggetto datiSessione = (SessionDatiProcedimentoEOggetto) session
        .getAttribute(SessionDatiProcedimentoEOggetto.SESSION_NAME);
    datiSessione.quadri = quadri;
    session.setAttribute(SessionDatiProcedimentoEOggetto.SESSION_NAME,
        datiSessione);
  }

  protected static ProcedimentoEProcedimentoOggetto findDatiProcedimentoEOggetto(
      HttpServletRequest request,
      IIuffiAbstractEJB ejb)
      throws InternalUnexpectedException
  {
    ProcedimentoEProcedimentoOggetto ppo = (ProcedimentoEProcedimentoOggetto) request
        .getAttribute(ProcedimentoEProcedimentoOggetto.REQUEST_NAME);
    if (ppo != null)
    {
      return ppo;
    }
    final HttpSession session = request.getSession();
    SessionDatiProcedimentoEOggetto datiSessione = (SessionDatiProcedimentoEOggetto) session
        .getAttribute(SessionDatiProcedimentoEOggetto.SESSION_NAME);
    if (datiSessione == null)
    {
      return null;
    }
    Long idProcedimentoOggetto = datiSessione.idProcedimentoOggetto;
    if (idProcedimentoOggetto == null)
    {
      ppo = new ProcedimentoEProcedimentoOggetto();
      ppo.setProcedimento(ejb.getProcedimento(datiSessione.idProcedimento));
    }
    else
    {
      boolean ricaricaQuadri = checkRicaricaQuadri(session,
          idProcedimentoOggetto);
      ppo = ejb.getProcedimentoEProcedimentoOggettoByIdProcedimentoOggetto(
          idProcedimentoOggetto, ricaricaQuadri);
      if (ppo != null)
      {
        ProcedimentoOggetto po = ppo.getProcedimentoOggetto();
        if (po != null)
        {

          if (ricaricaQuadri)
          {
            /*
             * Se ricaricaQuadri è true, vuol dire che non ho i dati dei quadri
             * in sessione, è la prima volta che carico i questo
             * ProcedimentoOggetto quindi ho caricato anche i dati dei quadri e
             * li devo mettere in sessione (dato che tanto non dovrebbero
             * cambiare visto che sono presi da tabelle di decodifica)
             */
            datiSessione.quadri = po.getQuadri();
            session.setAttribute(SessionDatiProcedimentoEOggetto.SESSION_NAME,
                datiSessione);
          }
          else
          {
            /*
             * Altrimenti vuol dire che i dati dei quadri di questo
             * ProcedimentoOggetto sono già in sessione e quindi basta che li
             * prenda e li metta nell'oggetto
             */
            po.setQuadri(datiSessione.quadri);
          }
        }
      }
    }
    request.setAttribute(ProcedimentoEProcedimentoOggetto.REQUEST_NAME, ppo);
    ProcedimentoOggetto po = ppo.getProcedimentoOggetto();
    if (po != null)
    {
      request.setAttribute("procedimentoOggettoCorrente", po);
    }
    return ppo;
  }

  protected static boolean checkRicaricaQuadri(HttpSession session,
      long idProcedimentoOggetto)
  {
    SessionDatiProcedimentoEOggetto datiSessione = (SessionDatiProcedimentoEOggetto) session
        .getAttribute(SessionDatiProcedimentoEOggetto.class.getName());
    if (datiSessione == null)
    {
      return true;
    }
    else
    {
      return datiSessione.quadri == null;
    }
  }

  public static class SessionDatiProcedimentoEOggetto implements ILoggable
  {
    /** serialVersionUID */
    private static final long      serialVersionUID = -4905551005664221990L;
    private static final String    SESSION_NAME     = SessionDatiProcedimentoEOggetto.class
        .getName();
    private long                   idProcedimento;
    private Long                   idProcedimentoOggetto;

    private List<QuadroOggettoDTO> quadri;

    public long getIdProcedimento()
    {
      return idProcedimento;
    }

    public Long getIdProcedimentoOggetto()
    {
      return idProcedimentoOggetto;
    }

    public List<QuadroOggettoDTO> getQuadri()
    {
      return quadri;
    }
  }
}
