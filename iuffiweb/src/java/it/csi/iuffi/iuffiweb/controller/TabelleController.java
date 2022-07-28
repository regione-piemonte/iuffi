package it.csi.iuffi.iuffiweb.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.RequestContextUtils;

import it.csi.iuffi.iuffiweb.business.IAnagraficaEJB;
import it.csi.iuffi.iuffiweb.business.IMenuItemEJB;
import it.csi.iuffi.iuffiweb.business.IMissioneEJB;
import it.csi.iuffi.iuffiweb.business.IUtilServiceEJB;
import it.csi.iuffi.iuffiweb.dto.Link;
import it.csi.iuffi.iuffiweb.dto.MenuItemDTO;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AnagraficaDTO;
import it.csi.iuffi.iuffiweb.model.DataFilter;
import it.csi.iuffi.iuffiweb.model.MissioneDTO;
import it.csi.iuffi.iuffiweb.model.RuoloDTO;
import it.csi.iuffi.iuffiweb.presentation.MessaggisticaBaseController;
import it.csi.iuffi.iuffiweb.propertyeditor.BigDecimalPropertyEditor;
import it.csi.iuffi.iuffiweb.propertyeditor.DatePropertyEditor;
import it.csi.iuffi.iuffiweb.propertyeditor.DoublePropertyEditor;
import it.csi.iuffi.iuffiweb.propertyeditor.IntegerPropertyEditor;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.papua.papuaserv.dto.gestioneutenti.Abilitazione;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
public class TabelleController extends MessaggisticaBaseController
{

  @Autowired
  private IMenuItemEJB menuItemEJB;

  @Autowired
  private IAnagraficaEJB anagraficaEJB;

  @Autowired
  private IMissioneEJB missioneEJB;
  
  @Autowired
  protected IUtilServiceEJB utilServiceEJB;
  
  protected static final Logger logger = Logger.getLogger(IuffiConstants.LOGGIN.LOGGER_NAME + ".integration");
  
  protected void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(Date.class, new DatePropertyEditor());
    binder.registerCustomEditor(Integer.class, new IntegerPropertyEditor());
    binder.registerCustomEditor(Integer.TYPE, new IntegerPropertyEditor());
    binder.registerCustomEditor(Double.class, new DoublePropertyEditor());
    binder.registerCustomEditor(Double.TYPE, new DoublePropertyEditor());
    binder.registerCustomEditor(BigDecimal.class, new BigDecimalPropertyEditor());
  }
  
  protected void setBreadcrumbs(Model model, HttpServletRequest request) throws InternalUnexpectedException {
    List<Link> breadcrumbs = new ArrayList<Link>();
    //Set<Link> breadcrumbs = new HashSet<Link>();
    String path = request.getServletPath().substring(1);
    //List<MenuItemDTO> list = menuItemEJB.getBreadcrumbs(path.substring(0,path.lastIndexOf("/"))+"/%");
    List<MenuItemDTO> list = menuItemEJB.getBreadcrumbs(path+"%");
    String nextPath = null;
    if (list != null) {
      for (MenuItemDTO menuItem : list) {
        nextPath = (menuItem.getPath().equals("home/index.do")) ? nextPath = menuItem.getPath() + "?idParent=" + menuItem.getIdMenuItem() : menuItem.getPath();
        breadcrumbs.add(new Link(nextPath, menuItem.getUseCase(), false, menuItem.getTitleMenuItem(), menuItem.getUseCase()));
      }
    }
    model.addAttribute("breadcrumbs", breadcrumbs);
  }

  protected void showFlashMessages(Model model, HttpServletRequest request) {
    String message = StringUtils.EMPTY;
    if (RequestContextUtils.getInputFlashMap(request) != null) {
      if (RequestContextUtils.getInputFlashMap(request).get(IuffiConstants.FLASH_ERROR_MESSAGE) != null) {
        message = (String) RequestContextUtils.getInputFlashMap(request).get(IuffiConstants.FLASH_ERROR_MESSAGE);
        if (StringUtils.isNotBlank(message)) {
          model.addAttribute(IuffiConstants.FLASH_ERROR_MESSAGE, message);
        }
      }
      if (RequestContextUtils.getInputFlashMap(request).get(IuffiConstants.FLASH_SUCCESS_MESSAGE) != null) {
        message = (String) RequestContextUtils.getInputFlashMap(request).get(IuffiConstants.FLASH_SUCCESS_MESSAGE);
        if (StringUtils.isNotBlank(message)) {
          model.addAttribute(IuffiConstants.FLASH_SUCCESS_MESSAGE, message);
        }
      }
    }
  }
/*
  protected String verifyToken(String jwt) throws Exception {
    // verifica il token e ritorna il codice fiscale contenuto all'interno
    String codiceFiscale = null;
    //String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXUyJ9.eyJpc3MiOiJhdXRoMCJ9.AbIJTDMFc7yUa5MhvcP03nJPyCPzZtQcGEp-zWfOkEE";
    Context ctx = new InitialContext();
    String KEY = null;
    String token = jwt;
    Claims claims = null;
    try {
      KEY = (String) ctx.lookup(IuffiConstants.SECRETSTRING_JNDI_NAME);
    } catch (Exception e) {
        KEY = "Jjd2/73OK+41ZJLq0TxiDSieZQO2ri71NkD0+coBf4ygcUzlcfP54BLu43AYoe0YJCIXeeG0x5xSUoirRkpyJQ==";  // da eliminare quando si fa deploy con token (produzione)
    }
    try {
      if (token == null) {  // da eliminare in produzione
        token = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI2NzcyYzIzZC05NTBiLTRmODgtOTVmMy1hNThlZTBkM2QzZjUiLCJpYXQiOjE2MDcwNzg1NjEsImV4cCI6MTYwNzA4MDM2MSwiaXNzIjoiaXVmZmlhdXRoIC0gQ1NJIFBpZW1vbnRlIiwiYXVkIjoiSVVGRkkgbW9iaWxlIHN5c3RlbSIsInN1YiI6IkFBQUFBQTAwQjc3QjAwMEYiLCJub21lIjoiQ1NJIFBJRU1PTlRFIiwiY29nbm9tZSI6IkRFTU8gMjAifQ.geYrzIDcv4OytDkRZpR-332m5M7qqnIELhgT0sxC8zootreE51CTIdtlkIQ-2Nl6r4KzhBwqmT-iNdNxYKfcpw";
      } else {
        token = token.substring(jwt.indexOf("Bearer ")+7);
      }
      claims = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
      codiceFiscale = (String) claims.get("sub");
      //String nome = (String) claims.get("nome");
      //String cognome = (String) claims.get("cognome");
    } catch (ExpiredJwtException e) {
        codiceFiscale = (String) e.getClaims().get("sub");     // da togliere una volta che si hanno chiamate con token validi (produzione)
        //throw new JwtException("TOKEN EXPIRED");
    }
    /*
    if (claims.getExpiration().before(new Date())) {
      throw new JwtException("TOKEN EXPIRED");
    }
    */
    /* // creo un JWT (anzi, JWS) utilizzando solo ciò che mi serve dell'oggetto identita
        String jwtString = Jwts.builder().setId(UUID.randomUUID().toString()) // claim standard "jti"
            .setIssuedAt(cal_iat.getTime())  // claim standard "iat"
            .setExpiration(cal_exp.getTime()) // claim standard "exp"
            .setIssuer("iuffiauth - CSI Piemonte") // claim standard "iss"
            .setAudience("IUFFI mobile system") // claim standard "aud"
            .setSubject(identita.getCodFiscale()) // claim standard "sub"         
            .claim("nome", identita.getNome()) // claim custom "nome" 
            .claim("cognome", identita.getCognome()) // claim custom "cognome"
            .signWith(key, IuffiauthConstants.SIG_ALG) // firma
            .compact(); // conversione in stringa
     * /
    return codiceFiscale;
  }
*/
  
  protected DataFilter getFiltroDati(UtenteAbilitazioni utente) {
    // Metodo per fornire l'id anagrafica e l'id ente per filtrare i dati di missioni e relative attività
    Integer idAnagrafica = null;
    Integer idEnte = null;
    AnagraficaDTO anagFilter = new AnagraficaDTO(utente.getCodiceFiscale());
    anagFilter.setActive(true);

    try {

      List<AnagraficaDTO> listAnag = anagraficaEJB.findByFilter(anagFilter);
      AnagraficaDTO anagUtente = (listAnag != null && !listAnag.isEmpty()) ? listAnag.get(0) : null;

      for (Abilitazione a : utente.getAbilitazioni()) {
        if (a.getLivello() == null) {
          continue;
        }
        if (a.getLivello().getIdLivello() == IuffiConstants.LivelliPapuaEnum.AMMINISTRATORE.getIdLivello()) {
          idAnagrafica = null;
          idEnte = null;
          break;
        }
        if (a.getLivello().getIdLivello() == IuffiConstants.LivelliPapuaEnum.FUNZIONARIO_BACKOFFICE.getIdLivello()) {
          idAnagrafica = null;
          idEnte = null;
          break;
        }
        if (a.getLivello().getIdLivello() == IuffiConstants.LivelliPapuaEnum.FUNZIONARIO_LABORATORIO.getIdLivello()) {
          idAnagrafica = null;
          idEnte = null;
          break;
        }
        if (a.getLivello().getIdLivello() == IuffiConstants.LivelliPapuaEnum.SUPERVISORE.getIdLivello()) {
          idEnte = (anagUtente != null) ? anagUtente.getIdEnte() : 0;
          idAnagrafica = null;
          continue;
        }
        idAnagrafica = (anagUtente != null) ? anagUtente.getIdAnagrafica() : 0;
      }
    }
    catch (Exception e) {
      logger.error("Anagrafica non censita. Errore: " + e.getMessage());
      idAnagrafica = 0;
      idEnte = 0;
    }
    DataFilter dataFilter = new DataFilter(idAnagrafica, idEnte);
    return dataFilter;
  }

  protected boolean isUtenteAuthorizedByMissione(UtenteAbilitazioni utente, MissioneDTO missione) throws InternalUnexpectedException {
    
    DataFilter dataFilter = getFiltroDati(utente);
    List<AnagraficaDTO> ispettoriAggiunti = missioneEJB.getIspettoriAggiunti(missione.getIdMissione());
    AnagraficaDTO ispettoreAssegnato = anagraficaEJB.findById(missione.getIdIspettoreAssegnato().intValue());
    boolean authorized = true;
    
    if (dataFilter.getIdAnagrafica() != null) {
      // la missione deve essere dell'ispettore loggato (o come ispettore assegnato o come ispettore aggiunto)
      if (missione.getIdIspettoreAssegnato().intValue() != dataFilter.getIdAnagrafica()) {
        if (ispettoriAggiunti == null || !ispettoriAggiunti.contains(new AnagraficaDTO(dataFilter.getIdAnagrafica()))) {
          authorized = false;
        }
      }
    }
    else
    if (dataFilter.getIdEnte() != null && dataFilter.getIdEnte().intValue() != ispettoreAssegnato.getIdEnte().intValue()) {
      authorized = false;
    }
    return authorized;
  }
  
  
  protected RuoloDTO getRuolo(UtenteAbilitazioni utente) {
    RuoloDTO ruolo= new RuoloDTO();
    ruolo.setAmministratore(false);
    ruolo.setFunzionarioBO(false);
    ruolo.setFunzionarioLaboratorio(false);
    ruolo.setIncaricatoMonitoraggio(false);
    ruolo.setIspettoreMonitoraggio(false);
    ruolo.setSupervisore(false);    
    try {
      for (Abilitazione a : utente.getAbilitazioni()) {
        if (a.getLivello() == null) {
          continue;
        }
        if (a.getLivello().getIdLivello() == IuffiConstants.LivelliPapuaEnum.AMMINISTRATORE.getIdLivello()) {
          ruolo.setAmministratore(true);
          break;
        }
        if (a.getLivello().getIdLivello() == IuffiConstants.LivelliPapuaEnum.FUNZIONARIO_LABORATORIO.getIdLivello()) {
          ruolo.setFunzionarioLaboratorio(true);
          break;
        }

        if (a.getLivello().getIdLivello() == IuffiConstants.LivelliPapuaEnum.INCARICATO_MONITORAGGIO.getIdLivello()) {
          ruolo.setIncaricatoMonitoraggio(true);
          break;
        }

        if (a.getLivello().getIdLivello() == IuffiConstants.LivelliPapuaEnum.ISPETTORE_MONITORAGGIO.getIdLivello()) {
          ruolo.setIspettoreMonitoraggio(true);
          break;
        }

        if (a.getLivello().getIdLivello() == IuffiConstants.LivelliPapuaEnum.FUNZIONARIO_BACKOFFICE.getIdLivello()) {
         ruolo.setFunzionarioBO(true);
          break;
        }
        if (a.getLivello().getIdLivello() == IuffiConstants.LivelliPapuaEnum.SUPERVISORE.getIdLivello()) {
          ruolo.setSupervisore(true);
        }
      }
    }
    catch (Exception e) {
      logger.error("Anagrafica non censita. Errore: " + e.getMessage());
    }
    
    return ruolo;
  }
  
  @ExceptionHandler
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public void handle(Throwable e) {
    
    logger.error("ERRORE CHIAMATA HTTP REST");
    logger.error("Returning HTTP 400 Bad Request (e)", e);
    
  }

}
