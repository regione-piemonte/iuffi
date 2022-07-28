package it.csi.iuffi.iuffiweb.presentation.login;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.iride2.policy.entity.Identita;
import it.csi.iuffi.iuffiweb.business.IAutorizzazioniEJB;
import it.csi.iuffi.iuffiweb.business.IQuadroEJB;
import it.csi.iuffi.iuffiweb.dto.MapColonneNascosteVO;
import it.csi.iuffi.iuffiweb.dto.login.ProcedimentoAgricoloDTO;
import it.csi.iuffi.iuffiweb.dto.procedimentooggetto.allegati.ContenutoFileAllegatiDTO;
import it.csi.iuffi.iuffiweb.exception.InternalException;
import it.csi.iuffi.iuffiweb.exception.InternalServiceException;
import it.csi.iuffi.iuffiweb.exception.InternalUnexpectedException;
import it.csi.iuffi.iuffiweb.model.AutorizzazioniDTO;
import it.csi.iuffi.iuffiweb.util.IuffiConstants;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;
import it.csi.iuffi.iuffiweb.util.annotation.NoLoginRequired;
import it.csi.papua.papuaserv.dto.gestioneutenti.Abilitazione;
import it.csi.papua.papuaserv.dto.gestioneutenti.AmmCompetenza;
import it.csi.papua.papuaserv.dto.gestioneutenti.MacroCU;
import it.csi.papua.papuaserv.dto.gestioneutenti.Ruolo;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.papua.papuaserv.presentation.rest.profilazione.client.PapuaservProfilazioneServiceFactory;

@Controller
@RequestMapping(value = "/login")
@NoLoginRequired
public class CtrlLogin
{
  @Autowired
  IQuadroEJB quadroEJB;

  private static IAutorizzazioniEJB autorizzazioniEJB;
  
  @Autowired
  public CtrlLogin(IAutorizzazioniEJB autorizzazioniEJB) {
    CtrlLogin.autorizzazioniEJB = autorizzazioniEJB;
  }
  
  @RequestMapping(value = "wrup/seleziona_ruolo", method = RequestMethod.GET)
  public String selezionaRuoloWRUP(HttpServletResponse response,
      HttpSession session) throws InternalException
  {
    addPortalCookie(response,
        IuffiConstants.PORTAL.PUBBLICA_AMMINISTRAZIONE);
    session.setAttribute(IuffiConstants.PORTAL.IUFFIWEB_LOGIN_PORTAL,
        IuffiConstants.PORTAL.PUBBLICA_AMMINISTRAZIONE);
    Identita identita = (Identita) session.getAttribute("identita");
    /*
     * Imposto il 6 bit del livello di autenticazione a 0, come convenzione per
     * papua se questo bit ï¿½ a 1 significa che l'utente ha fatto login tramite
     * SPID
     */
    identita.setLivelloAutenticazione(
        identita.getLivelloAutenticazione()
            & (255 - IuffiConstants.SPID.BIT_SPID));
    return "redirect:../seleziona_procedimento_agricolo.do";
  }

  @RequestMapping(value = "sisp/seleziona_ruolo", method = RequestMethod.GET)
  public String selezionaRuoloSISP(HttpServletResponse response,
      HttpSession session) throws InternalException
  {
    addPortalCookie(response, IuffiConstants.PORTAL.PRIVATI_SISPIE);
    session.setAttribute(IuffiConstants.PORTAL.IUFFIWEB_LOGIN_PORTAL,
        IuffiConstants.PORTAL.PRIVATI_SISPIE);
    Identita identita = (Identita) session.getAttribute("identita");
    /*
     * Imposto il 6 bit del livello di autenticazione a 0, come convenzione per
     * papua se questo bit ï¿½ a 1 significa che l'utente ha fatto login tramite
     * SPID
     */
    identita.setLivelloAutenticazione(
        identita.getLivelloAutenticazione()
            & (255 - IuffiConstants.SPID.BIT_SPID));
    return "redirect:../seleziona_procedimento_agricolo.do";
  }

  @RequestMapping(value = "spid/seleziona_ruolo", method = RequestMethod.GET)
  public String selezionaRuoloSPID(HttpServletResponse response,
      HttpSession session) throws InternalException
  {
    addPortalCookie(response, IuffiConstants.PORTAL.SPID);
    session.setAttribute(IuffiConstants.PORTAL.IUFFIWEB_LOGIN_PORTAL,
        IuffiConstants.PORTAL.SPID);
    Identita identita = (Identita) session.getAttribute("identita");
    /*
     * Imposto il 6 bit del livello di autenticazione a 1, come convenzione per
     * papua se questo bit ï¿½ a 1 significa che l'utente ha fatto login tramite
     * SPID (Non crea problemi in quanto il livello di autenticazione di
     * shibboleth/iride2 usa solo i primi 5 bit.
     */
    identita.setLivelloAutenticazione(
        identita.getLivelloAutenticazione() | IuffiConstants.SPID.BIT_SPID);
    return "redirect:../seleziona_procedimento_agricolo.do";
  }

  @RequestMapping(value = "/seleziona_procedimento_agricolo", method = RequestMethod.GET)
  public String selezionaProcedimentoAgricolo(Model model) throws InternalUnexpectedException
  {
    List<ProcedimentoAgricoloDTO> procedimentiAgricoli = quadroEJB.getProcedimentiAgricoli(null);
    if (procedimentiAgricoli.size() == 1)
    {
      ProcedimentoAgricoloDTO procedimentoAgricolo = procedimentiAgricoli.get(0);
      return "redirect:/login/seleziona_ruolo_"+procedimentoAgricolo.getIdProcedimentoAgricolo()+".do";
    }
    else
    {
      model.addAttribute("procedimentiAgricoli", quadroEJB.getProcedimentiAgricoli(null));
      return "login/selezionaProcedimentoAgricolo";
    }
  }
  public void addPortalCookie(HttpServletResponse response, String portal)
  {
    Cookie cookie = new Cookie(IuffiConstants.PORTAL.IUFFIWEB_LOGIN_PORTAL,
        portal);
    cookie.setPath("/" + IuffiConstants.IUFFIWEB.WEB_CONTEXT);
    cookie.setMaxAge(8 * 60 * 60);
    response.addCookie(cookie);
  }

  @RequestMapping(value = "seleziona_procedimento_agricolo_{gruppoHomePage}", method = RequestMethod.GET)
  public String seleziona_procedimento_agricolo(
      @ModelAttribute("loginModel") ModelLogin loginModel, ModelMap model,
      HttpSession session, @PathVariable("gruppoHomePage") String gruppoHomePage) throws InternalException, InternalUnexpectedException
  {
    if (!quadroEJB.verificaEsistenzaGruppoHomePage(gruppoHomePage))
    {
      model.addAttribute("procedimentiAgricoli", quadroEJB.getProcedimentiAgricoli(null));
    }else {
      model.addAttribute("procedimentiAgricoli", quadroEJB.getProcedimentiAgricoli(gruppoHomePage));
      session.setAttribute(IuffiConstants.IUFFI.GRUPPO_HOME_PAGE, gruppoHomePage);
    }
    return "login/selezionaProcedimentoAgricolo";
  }
  
  @RequestMapping(value = "seleziona_procedimento_agricolo_{gruppoHomePage}", method = RequestMethod.POST)
  public String selezionaRuoloPost(ModelMap model, HttpSession session,
      @ModelAttribute("loginModel") @Valid ModelLogin loginModel,
      BindingResult bindingResult,@ModelAttribute("gruppoHomePage") @PathVariable("gruppoHomePage") String gruppoHomePage)
      throws InternalException, InternalUnexpectedException
  {
    if (bindingResult.hasErrors())
    {
      return seleziona_procedimento_agricolo(loginModel, model, session, gruppoHomePage);
    }
    if (!quadroEJB.verificaEsistenzaGruppoHomePage(gruppoHomePage))
    {
      model.addAttribute("procedimentiAgricoli", quadroEJB.getProcedimentiAgricoli(null));
    }else {
      model.addAttribute("procedimentiAgricoli", quadroEJB.getProcedimentiAgricoli(gruppoHomePage));
      session.setAttribute(IuffiConstants.IUFFI.GRUPPO_HOME_PAGE, gruppoHomePage);
    }
    return "login/selezionaProcedimentoAgricolo";
  }
  
  @RequestMapping(value = "seleziona_ruolo_{idProcedimentoAgricolo}", method = RequestMethod.GET)
  public String selezionaRuoloGet(
      @ModelAttribute("loginModel") ModelLogin loginModel, ModelMap model,
      HttpSession session, @ModelAttribute("idProcedimentoAgricolo") @PathVariable("idProcedimentoAgricolo") int idProcedimentoAgricolo) throws InternalException, InternalUnexpectedException
  {
    if (!quadroEJB.verificaEsistenzaProcedimentoAgricolo(idProcedimentoAgricolo))
    {
      return "redirect:../seleziona_procedimento_agricolo.do";
    }
    model.addAttribute("ruoli", loadRuoli(session,idProcedimentoAgricolo));
	session.setAttribute("headerProcedimento","header_"+idProcedimentoAgricolo+".html");
    return "login/selezionaRuolo";
  }

  @RequestMapping(value = "seleziona_ruolo_{idProcedimentoAgricolo}", method = RequestMethod.POST)
  public String selezionaRuoloPost(ModelMap model, HttpSession session,
      @ModelAttribute("loginModel") @Valid ModelLogin loginModel,
      BindingResult bindingResult,@ModelAttribute("idProcedimentoAgricolo") @PathVariable("idProcedimentoAgricolo") int idProcedimentoAgricolo)
      throws InternalException, InternalUnexpectedException
  {
    if (bindingResult.hasErrors())
    {
      return selezionaRuoloGet(loginModel, model, session, idProcedimentoAgricolo);
    }
    if (!quadroEJB.verificaEsistenzaProcedimentoAgricolo(idProcedimentoAgricolo))
    {
      return "redirect:../seleziona_procedimento_agricolo.do";
    }   
    return login(session, loginModel.getRuolo(), idProcedimentoAgricolo);
  }
  
  @RequestMapping(value = "wrup/seleziona_ruolo_gruppo_{gruppoHomePage}", method = RequestMethod.GET)
  public String selezionaRuoloWRUP(HttpServletResponse response,
      HttpSession session, @PathVariable("gruppoHomePage") String gruppoHomePage) throws InternalException
  {
    addPortalCookie(response,
        IuffiConstants.PORTAL.PUBBLICA_AMMINISTRAZIONE);
    session.setAttribute(IuffiConstants.PORTAL.IUFFIWEB_LOGIN_PORTAL,
        IuffiConstants.PORTAL.PUBBLICA_AMMINISTRAZIONE);
    Identita identita = (Identita) session.getAttribute("identita");
    /*
     * Imposto il 6 bit del livello di autenticazione a 0, come convenzione per
     * papua se questo bit ï¿½ a 1 significa che l'utente ha fatto login tramite
     * SPID
     */
    identita.setLivelloAutenticazione(
        identita.getLivelloAutenticazione()
        & (255 - IuffiConstants.SPID.BIT_SPID));
    return "redirect:../seleziona_procedimento_agricolo_"+gruppoHomePage+".do";
  }
  
  @RequestMapping(value = "spid/seleziona_ruolo_gruppo_{gruppoHomePage}", method = RequestMethod.GET)
  public String selezionaRuoloSPID(HttpServletResponse response,
      HttpSession session, @PathVariable("gruppoHomePage") String gruppoHomePage) throws InternalException
  {
    addPortalCookie(response, IuffiConstants.PORTAL.SPID);
    session.setAttribute(IuffiConstants.PORTAL.IUFFIWEB_LOGIN_PORTAL,
        IuffiConstants.PORTAL.SPID);
    Identita identita = (Identita) session.getAttribute("identita");
    /*
     * Imposto il 6 bit del livello di autenticazione a 1, come convenzione per
     * papua se questo bit ï¿½ a 1 significa che l'utente ha fatto login tramite
     * SPID (Non crea problemi in quanto il livello di autenticazione di
     * shibboleth/iride2 usa solo i primi 5 bit.
     */
    identita.setLivelloAutenticazione(
        identita.getLivelloAutenticazione() | IuffiConstants.SPID.BIT_SPID);
    return "redirect:../seleziona_procedimento_agricolo_"+gruppoHomePage+".do";
  }
  
  @RequestMapping(value = "sisp/seleziona_ruolo_gruppo_{gruppoHomePage}", method = RequestMethod.GET)
  public String selezionaRuoloSISP(HttpServletResponse response,
      HttpSession session, @PathVariable("gruppoHomePage") String gruppoHomePage) throws InternalException
  {
    addPortalCookie(response, IuffiConstants.PORTAL.PRIVATI_SISPIE);
    session.setAttribute(IuffiConstants.PORTAL.IUFFIWEB_LOGIN_PORTAL,
        IuffiConstants.PORTAL.PRIVATI_SISPIE);
    Identita identita = (Identita) session.getAttribute("identita");
    /*
     * Imposto il 6 bit del livello di autenticazione a 0, come convenzione per
     * papua se questo bit ï¿½ a 1 significa che l'utente ha fatto login tramite
     * SPID
     */
    identita.setLivelloAutenticazione(
        identita.getLivelloAutenticazione()
        & (255 - IuffiConstants.SPID.BIT_SPID));
    return "redirect:../seleziona_procedimento_agricolo_"+gruppoHomePage+".do";
  }

  public static String login(HttpSession session, String ruolo, int idProcedimentoAgricolo)
      throws InternalServiceException
  {
    Identita identita = (Identita) session.getAttribute("identita");
    try
    {
      session.setAttribute("codiceRuoloLogin", ruolo);
      UtenteAbilitazioni utenteAbilitazioni = PapuaservProfilazioneServiceFactory
          .getRestServiceClient().login(
              identita.getCodFiscale(), identita.getCognome(),
              identita.getNome(),
              identita.getLivelloAutenticazione(),
              idProcedimentoAgricolo, ruolo);
              long idLivello = 0;
              Abilitazione[] abilitazioni = utenteAbilitazioni.getAbilitazioni(); //array di abilitazioni e ogni abilitazioni ha un getLivello
              long[] idLivelli = new long[abilitazioni.length];
//              long[] idLivelli = new long[1];
              //for(Abilitazione a : abilitazioni) {
              for(int l=0; l<abilitazioni.length; l++) { 
              //Con idLivello trovo 6001, 6002, ecc.
                if(abilitazioni[l].getLivello()!=null) {
                  idLivello = abilitazioni[l].getLivello().getIdLivello();  
                  idLivelli[l] = idLivello;
                }
              }
//              idLivelli[0] = 6003;
//              idLivelli[1] = 6003;
//              idLivelli[2] = 6004;
              List<AutorizzazioniDTO> autorizzazioniNegate1 = new ArrayList<AutorizzazioniDTO>();
              List<String> primoArray = new ArrayList<String>();
              List<String> secondoArray = new ArrayList<String>();
              List<String> elementiComuni = new ArrayList<String>();

              for(int n=0; n<idLivelli.length; n++) {
                if(!(primoArray.size()>0)) {
                  if (utenteAbilitazioni.isReadWrite())   // Modificato il 26/04/2021 (S.D.) - Gestione del flag sola lettura
                    autorizzazioniNegate1 = autorizzazioniEJB.findById(idLivelli[n]);
                  else
                    autorizzazioniNegate1 = autorizzazioniEJB.findByIdLivelloAndReadOnly(idLivelli[n]);   // Abilito solo i CU di tipo read only
                   
                    if(autorizzazioniNegate1 != null) {
                      for(int h=0; h<autorizzazioniNegate1.size(); h++) {
                        if(!autorizzazioniNegate1.get(h).getExtCodMacroCdu().equals("-")) {
                          primoArray.add(autorizzazioniNegate1.get(h).getExtCodMacroCdu());
                        }
                      }
                    }
                    else {
                      continue;
                    } 
                } 

                  if(idLivelli.length>1 && idLivelli.length>n+1) {

                    List<AutorizzazioniDTO> autorizzazioniNegate2 = null;
                    if (utenteAbilitazioni.isReadWrite())   // Modificato il 26/04/2021 (S.D.) - Gestione del flag sola lettura
                      autorizzazioniNegate2 = autorizzazioniEJB.findById(idLivelli[n+1]);
                    else
                      autorizzazioniNegate2 = autorizzazioniEJB.findByIdLivelloAndReadOnly(idLivelli[n+1]);   // Abilito solo i CU di tipo read only
                    
                  if(autorizzazioniNegate2 != null ) {
                      if(!(secondoArray.size()>0)) {
                        for(int c=0; c<autorizzazioniNegate2.size(); c++) {
                          secondoArray.add(autorizzazioniNegate2.get(c).getExtCodMacroCdu());
                        }
                        if(primoArray.retainAll(secondoArray)) {
                         continue; 
                        }
                        else {
                          break;
                        }
                      }           
                      
                      else {
                        secondoArray = new ArrayList<String>();
                        for(int c=0; c<autorizzazioniNegate2.size(); c++) {
                          
                          secondoArray.add(autorizzazioniNegate2.get(c).getExtCodMacroCdu());
                        }
                        if(primoArray.retainAll(secondoArray)) {
                         continue; 
                        }
                        else {
                          break;
                        }
                      }
                    }
                  }
                
              }
              
              elementiComuni = primoArray;
  
              if(elementiComuni != null) {
                //macroCU mi restituisce codice (ad. es GESTIONE_TRAPPOLE, VISUALIZZA_MISSIONI) e la descrizione
                MacroCU[] macroCU = utenteAbilitazioni.getMacroCU();
                /*int macroCUSize = macroCU.length;
                int autNegSize = autorizzazioniNegate.size();
                int macroCUNewSize = macroCUSize - autNegSize;*/
                MacroCU[] macroCUnew = new MacroCU[macroCU.length]; //non sappiamo a priori quanti elementi andranno rimossi, pertanno impostiamo inizialmente la lunghezza massima
                
              //Rimuovo dall'array i codici ottenuti dalla query e creo un nuovo array di tipo MacroCU
                String codiceCasoDB;
                String codiceMacroCU;
                boolean insert; 
                int count = 0;
                for(int i=0; i<macroCU.length; i++) {
                  codiceMacroCU = macroCU[i].getCodice();
                  insert = true;
                  for(int k=0; k<elementiComuni.size(); k++) {
                    codiceCasoDB = elementiComuni.get(k);//.getCodiceCasoDUso();                  
                    if(codiceMacroCU.equals(codiceCasoDB)) {
                      insert = false;
                      break;  
                    }
                  }
                  if(insert) {
                      macroCUnew[count] = macroCU[i];
                      count++;
                  }
                }
               
                //Rimuovo gli elementi null dall'array
                int size = 0;
                for(int m=0; m<macroCUnew.length; m++) {
                  if(macroCUnew[m] != null)
                    size++;
                }
                MacroCU[] macroCUToSet = new MacroCU[size];
                for(int t=0; t<macroCUnew.length; t++) {
                  if(macroCUnew[t] != null) {
                    macroCUToSet[t] = macroCUnew[t];
                  }
                }
/*
                //Rimuovo gli elementi null dall'array
                // Modificato il 22/04/2021 (S.D.) - Se l'utente è sola lettura non inserisco i macro CU che prevedono scrittura
                int size = 0;
                for(int m=0; m<macroCUnew.length; m++) {
                  if (macroCUnew[m] != null && (utenteAbilitazioni.isReadWrite() || macroCUnew[m].getCodice().startsWith("VISUALIZZA")))
                    size++;
                }
                MacroCU[] macroCUToSet = new MacroCU[size];
                int index = 0;
                for(int t=0; t<macroCUnew.length; t++) {
                  if(macroCUnew[t] != null && (utenteAbilitazioni.isReadWrite() || macroCUnew[t].getCodice().startsWith("VISUALIZZA"))) {
                    macroCUToSet[index] = macroCUnew[t];
                    index++;
                  }
                }
*/
                //Setto il nuovo oggetto ottenuto su utenteAbilitazioni
                utenteAbilitazioni.setMacroCU(macroCUToSet);
              }
                
      //FIX consentire l'accesso ai procedimenti agli ENTI LOCALI
      if(utenteAbilitazioni.isUtentePA() && utenteAbilitazioni.getEnteAppartenenza().getAmmCompetenza()==null){
        AmmCompetenza amm = new AmmCompetenza();
        amm.setIdAmmCompetenza(-1);
        utenteAbilitazioni.getEnteAppartenenza().setAmmCompetenza(amm);
      }
      
      session.setAttribute("utenteAbilitazioni", utenteAbilitazioni);
      session.setAttribute(
          IuffiConstants.GENERIC.SESSION_VAR_COLONNE_NASCOSTE,
          new MapColonneNascosteVO());
      session.setAttribute(IuffiConstants.GENERIC.SESSION_VAR_FILTER_AZIENDA,
          new HashMap<String, String>());

      /*Map<String, String> mapParametri = IuffiUtils.APPLICATION
          .getEjbQuadro().getParametri(new String[]
      { IuffiConstants.PARAMETRO.DOQUIAGRI_FLAG_PROT,
          IuffiConstants.PARAMETRO.HELP_DEFAULT_OPEN });
      session.setAttribute(IuffiConstants.PARAMETRO.DOQUIAGRI_FLAG_PROT,
          mapParametri.get(IuffiConstants.PARAMETRO.DOQUIAGRI_FLAG_PROT));
      session.setAttribute(IuffiConstants.PARAMETRO.HELP_DEFAULT_OPEN,
          mapParametri.get(IuffiConstants.PARAMETRO.HELP_DEFAULT_OPEN));*/
    }
    catch (Exception e)
    {
      throw new InternalServiceException(
          "Si è verificato un errore interno durante l'accesso ai servizi di autenticazione di PAPUA",
          InternalException.GENERIC_ERROR, e);
    }
    return "redirect:../cuiuffi201/index.do";
  }
  

  private Ruolo[] loadRuoli(HttpSession session, int idProcedimentoAgricolo) throws InternalException
  {
    Identita identita = (Identita) session.getAttribute("identita");
    try
    {
      
      
      PapuaservProfilazioneServiceFactory.setLoggerName("iuffiweb");
      PapuaservProfilazioneServiceFactory.getRestServiceClient().setLogger(Logger.getLogger("iuffiweb"));
      Ruolo[] ruoli = PapuaservProfilazioneServiceFactory.getRestServiceClient()
          .findRuoliForPersonaInApplicazione(
              identita.getCodFiscale(),
              identita.getLivelloAutenticazione(),
              idProcedimentoAgricolo);
      if (ruoli != null)
      {
        for (Ruolo ruolo : ruoli)
        {
          ruolo.setDescrizione(" " + ruolo.getDescrizione());
        }
      }
      return ruoli;
    }
    catch (Exception e)
    {
      throw new InternalServiceException(
          "Si ï¿½ verificato un errore interno durante l'accesso ai servizi di autenticazione di PAPUA",
          InternalException.GENERIC_ERROR, e);
    }
  }
  
  @RequestMapping(value = "visualizza_immagine_{idProcedimentoAgricolo}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> visualizzaImmagine(HttpSession session,
      @PathVariable("idProcedimentoAgricolo") long idProcedimentoAgricolo)
      throws InternalUnexpectedException
  {
    ContenutoFileAllegatiDTO contenuto = quadroEJB.getImmagineDaVisualizzare(idProcedimentoAgricolo);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type",
        IuffiUtils.FILE.getMimeType(".jpg"));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"\"");

    if(contenuto.getContenuto()!=null){
    	 ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
    		        contenuto.getContenuto(), httpHeaders, HttpStatus.OK);
    		    return response;
    }
   return null;
  }

}
